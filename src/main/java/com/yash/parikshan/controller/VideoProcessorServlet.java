package com.yash.parikshan.controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yash.parikshan.model.RecordingData;
import com.yash.parikshan.model.VideoProcessorResult;
import com.yash.parikshan.util.DbUtil;

@WebServlet("/VideoProcessorServlet")
public class VideoProcessorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Path to your Python script and video analyzer project
    private static final String PYTHON_SCRIPT_PATH = "D:\\Intellijprojects\\MediaPipeService\\videoanalyzer.py";
    private static final String PYTHON_EXECUTABLE = "python"; // or "python3" depending on your setup

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("VideoProcessorServlet doGet called");
        // Forward to the JSP page for GET requests
        RequestDispatcher dispatcher = request.getRequestDispatcher("/processRecording.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("VideoProcessorServlet doPost called");
        String recordingIdStr = request.getParameter("recordingId");
        System.out.println("Received recordingId: " + recordingIdStr);

        if (recordingIdStr == null || recordingIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a valid recording ID");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/processRecording.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            int recordingId = Integer.parseInt(recordingIdStr.trim());

            // Get recording details from database
            RecordingData recording = getRecordingFromDB(recordingId);

            if (recording == null) {
                request.setAttribute("error", "Recording with ID " + recordingId + " not found");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/processRecording.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Create temporary video file from BLOB data
            String tempVideoPath = createTempVideoFile(recording);

            try {
                // Process video using Python script
                VideoProcessorResult result = processVideo(tempVideoPath);

                if (result.hasError()) {
                    request.setAttribute("error", "Processing failed: " + result.getError());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/processRecording.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // Save results to database
                    saveResultsToDB(recordingId, result);

                    System.out.println("Video processing completed successfully. Detection rate: " +
                            result.getDetectionPercentage() + "%");

                    // Set success message in session so it persists after redirect
                    HttpSession session = request.getSession();
                    session.setAttribute("successMessage",
                            "Recording " + recordingId + " processed successfully! Detection rate: " +
                                    String.format("%.1f", result.getDetectionPercentage()) + "%");

                    // Redirect to AdminResultsServlet to show all results
                    response.sendRedirect("AdminResultsServlet");
                    return;
                }
            } finally {
                // Clean up temporary file
                deleteTempFile(tempVideoPath);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid recording ID format");
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while processing: " + e.getMessage());
            e.printStackTrace();
        }

        // Only forward to processRecording.jsp if there was an error
        RequestDispatcher dispatcher = request.getRequestDispatcher("/processRecording.jsp");
        dispatcher.forward(request, response);
    }

    private RecordingData getRecordingFromDB(int recordingId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection(); // Your DB connection method
            String sql = "SELECT id, testid, studentid, recordingdata, filesize, createdat FROM recordings WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordingId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                RecordingData recording = new RecordingData();
                recording.setId(rs.getInt("id"));
                recording.setTestId(rs.getString("testid"));
                recording.setStudentId(rs.getString("studentid"));
                recording.setRecordingData(rs.getBytes("recordingdata"));
                recording.setFileSize(rs.getLong("filesize"));
                recording.setCreatedAt(rs.getTimestamp("createdat"));
                return recording;
            }
            return null;

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private String createTempVideoFile(RecordingData recording) throws IOException {
        // Create temp directory if it doesn't exist
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "video_processing");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        // Generate unique filename
        String fileName = "recording_" + recording.getId() + "_" + System.currentTimeMillis() + ".mp4";
        File tempFile = new File(tempDir, fileName);

        // Write BLOB data to temporary file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(recording.getRecordingData());
        }

        return tempFile.getAbsolutePath();
    }

    private void deleteTempFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // Log error but don't throw - cleanup failure shouldn't break the flow
            System.err.println("Failed to delete temp file: " + filePath + " - " + e.getMessage());
        }
    }

    private VideoProcessorResult processVideo(String videoPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                PYTHON_EXECUTABLE,
                PYTHON_SCRIPT_PATH,
                videoPath
        );

        // Set working directory if needed
        // processBuilder.directory(new File("/path/to/python/project"));

        Process process = processBuilder.start();

        // Read output
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        // Read error stream
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line);
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("Python script failed with exit code " + exitCode +
                    ". Error: " + errorOutput.toString());
        }

        // Parse JSON response
        String jsonResponse = output.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        VideoProcessorResult result = new VideoProcessorResult();

        if (jsonObject.has("error")) {
            result.setError(jsonObject.get("error").getAsString());
        } else {
            result.setStatus(jsonObject.get("status").getAsString());
            result.setTotalFrames(jsonObject.get("total_frames").getAsInt());
            result.setPoseDetectedFrames(jsonObject.get("pose_detected_frames").getAsInt());
            result.setDetectionPercentage(jsonObject.get("detection_percentage").getAsDouble());
        }

        return result;
    }

    private void saveResultsToDB(int recordingId, VideoProcessorResult result) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Create results table if it doesn't exist
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS video_analysis_results (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    recording_id BIGINT NOT NULL,
                    total_frames INT,
                    pose_detected_frames INT,
                    detection_percentage DECIMAL(5,2),
                    status VARCHAR(50),
                    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (recording_id) REFERENCES recordings(id)
                )
            """;

            PreparedStatement createStmt = conn.prepareStatement(createTableSQL);
            createStmt.executeUpdate();
            createStmt.close();

            // Check if result already exists for this recording
            String checkSQL = "SELECT COUNT(*) FROM video_analysis_results WHERE recording_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, recordingId);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            boolean exists = checkRs.getInt(1) > 0;
            checkRs.close();
            checkStmt.close();

            String sql;
            if (exists) {
                // Update existing record
                sql = """
                    UPDATE video_analysis_results 
                    SET total_frames = ?, pose_detected_frames = ?, detection_percentage = ?, 
                        status = ?, processed_at = CURRENT_TIMESTAMP 
                    WHERE recording_id = ?
                """;
            } else {
                // Insert new record
                sql = """
                    INSERT INTO video_analysis_results 
                    (recording_id, total_frames, pose_detected_frames, detection_percentage, status) 
                    VALUES (?, ?, ?, ?, ?)
                """;
            }

            pstmt = conn.prepareStatement(sql);
            if (exists) {
                pstmt.setInt(1, result.getTotalFrames());
                pstmt.setInt(2, result.getPoseDetectedFrames());
                pstmt.setDouble(3, result.getDetectionPercentage());
                pstmt.setString(4, result.getStatus());
                pstmt.setInt(5, recordingId);
            } else {
                pstmt.setInt(1, recordingId);
                pstmt.setInt(2, result.getTotalFrames());
                pstmt.setInt(3, result.getPoseDetectedFrames());
                pstmt.setDouble(4, result.getDetectionPercentage());
                pstmt.setString(5, result.getStatus());
            }

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
}