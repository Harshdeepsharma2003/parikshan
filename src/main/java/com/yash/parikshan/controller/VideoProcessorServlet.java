package com.yash.parikshan.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yash.parikshan.dao.VideoProcessorDao;
import com.yash.parikshan.dao.TestResultDao;
import com.yash.parikshan.daoimpl.VideoProcessorDaoImpl;
import com.yash.parikshan.daoimpl.TestResultDaoImpl;
import com.yash.parikshan.model.Recording;
import com.yash.parikshan.model.VideoProcessorResult;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/VideoProcessorServlet")
public class VideoProcessorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String PYTHON_EXECUTABLE = "D:\\MediaPipeService\\.venv\\Scripts\\python.exe";
    private static final String PYTHON_SCRIPT_PATH = "D:\\MediaPipeService\\videoanalyzer.py";

    private final VideoProcessorDao videoProcessorDao = new VideoProcessorDaoImpl();
    private final TestResultDao testResultDao = new TestResultDaoImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/processrecording.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String recordingIdStr = request.getParameter("recordingId");

        if (recordingIdStr == null || recordingIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a valid recording ID");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/processrecording.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            int recordingId = Integer.parseInt(recordingIdStr.trim());

            Recording recording = videoProcessorDao.getRecordingById(recordingId);

            if (recording == null) {
                request.setAttribute("error", "Recording with ID " + recordingId + " not found");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/processrecording.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String tempVideoPath = createTempVideoFile(recording);
            try {
                VideoProcessorResult result = processVideo(tempVideoPath, recordingId);

                if (result.hasError()) {
                    request.setAttribute("error", "Processing failed: " + result.getError());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/processrecording.jsp");
                    dispatcher.forward(request, response);
                } else {
                    videoProcessorDao.saveOrUpdateResult(recordingId, result);
                    testResultDao.updateTestResults(recording, result);

                    HttpSession session = request.getSession();
                    session.setAttribute("successMessage",
                            "Recording " + recordingId + " processed successfully!");

                    response.sendRedirect("AdminResultsServlet");
                    return;
                }
            } finally {
                deleteTempFile(tempVideoPath);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/processrecording.jsp");
            dispatcher.forward(request, response);
        }

    }
    private String createTempVideoFile(Recording recording) throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "video_processing");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String fileName = "recording_" + recording.getId() + "_" + System.currentTimeMillis() + ".mp4";
        File tempFile = new File(tempDir, fileName);

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

    private VideoProcessorResult processVideo(String videoPath, int recordingId) throws IOException, InterruptedException {
        // Corrected ProcessBuilder - Python executable first, then script, then arguments
        ProcessBuilder processBuilder = new ProcessBuilder(
                PYTHON_EXECUTABLE,      // D:\\MediaPipeService\\.venv\\Scripts\\python.exe
                PYTHON_SCRIPT_PATH,     // D:\\MediaPipeService\\videoanalyzer.py
                videoPath,
                String.valueOf(recordingId)
        );

        // Set working directory to the script directory (optional but recommended)
        processBuilder.directory(new File("D:\\MediaPipeService"));

        // Redirect error stream to stdout for easier debugging
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        // Read the combined output and extract JSON
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        StringBuilder output = new StringBuilder();
        String jsonResponse = null;
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
            System.out.println("Python output: " + line); // For debugging

            // Look for JSON response line (starts with { and ends with })
            if (line.trim().startsWith("{") && line.trim().endsWith("}")) {
                System.out.println("Found potential JSON line: " + line.trim());
                try {
                    // Test if it's valid JSON by trying to parse it
                    Gson testGson = new Gson();
                    JsonObject testJson = testGson.fromJson(line.trim(), JsonObject.class);
                    if (testJson.has("status") || testJson.has("error")) {
                        jsonResponse = line.trim();
                        System.out.println("Confirmed valid JSON response: " + jsonResponse);
                    }
                } catch (Exception e) {
                    System.out.println("Line looks like JSON but failed to parse: " + e.getMessage());
                }
            }
        }

        int exitCode = process.waitFor();

        // If we found a valid JSON response, use it even if exit code is non-zero
        if (jsonResponse != null) {
            System.out.println("Using extracted JSON response: " + jsonResponse);

            try {
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
            } catch (Exception e) {
                throw new IOException("Failed to parse extracted JSON response: " + jsonResponse, e);
            }
        }

        // If no JSON found and exit code is non-zero, throw error
        if (exitCode != 0) {
            throw new IOException("Python script failed with exit code " + exitCode +
                    ". No valid JSON response found. Output: " + output.toString());
        }

        // If exit code is 0 but no JSON was extracted, this shouldn't happen
        throw new IOException("Python script completed successfully but no JSON response was found. Output: " + output.toString());
    }

}
