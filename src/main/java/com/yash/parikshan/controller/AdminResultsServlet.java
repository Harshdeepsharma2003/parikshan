package com.yash.parikshan.controller;

import com.yash.parikshan.model.VideoProcessorResult;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.util.DbUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AdminResultsServlet")
public class AdminResultsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("AdminResultsServlet doGet called");

        try {
             HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("successMessage") != null) {
                request.setAttribute("successMessage", session.getAttribute("successMessage"));
                session.removeAttribute("successMessage"); // Remove after displaying
            }

            // Fetch video analysis results
            List<VideoProcessorResult> videoResults = getVideoProcessorResults();
            request.setAttribute("videoResults", videoResults);
            request.setAttribute("totalVideoResults", videoResults.size());

            // Fetch test results with cheating information
            List<TestResult> testResults = getTestResultsWithViolations();
            request.setAttribute("testResults", testResults);
            request.setAttribute("totalTestResults", testResults.size());

            // Calculatin summary statistics
            calculateSummaryStats(request, videoResults, testResults);

            System.out.println("Fetched " + videoResults.size() + " video analysis results");
            System.out.println("Fetched " + testResults.size() + " test results");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching results: " + e.getMessage());
        }

        request.getRequestDispatcher("/adminresults.jsp").forward(request, response);
    }

    private List<VideoProcessorResult> getVideoProcessorResults() throws SQLException {
        List<VideoProcessorResult> results = new ArrayList<>();
        String sql = """
                SELECT r.id as recording_id, r.studentid, r.testid, 
                       var.status, var.total_frames, var.pose_detected_frames, 
                       var.detection_percentage, var.processed_at
                FROM recordings r 
                JOIN video_analysis_results var ON r.id = var.recording_id 
                ORDER BY var.processed_at DESC
            """;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                VideoProcessorResult result = new VideoProcessorResult();
                result.setRecordingId(resultSet.getLong("recording_id"));
                result.setStatus(resultSet.getString("status"));
                result.setTotalFrames(resultSet.getInt("total_frames"));
                result.setPoseDetectedFrames(resultSet.getInt("pose_detected_frames"));
                result.setDetectionPercentage(resultSet.getDouble("detection_percentage"));
                result.setStudentId(resultSet.getString("studentid"));
                result.setTestId(resultSet.getString("testid"));

                results.add(result);
            }
        }
        return results;
    }

    private List<TestResult> getTestResultsWithViolations() throws SQLException {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();

              String sql = """
                SELECT resultid, studentid, testid, score, totalmarks, 
                       timetaken, testdate, status, invalidationreason, violationcount
                FROM testresults 
                ORDER BY testdate DESC, violationcount DESC
            """;

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TestResult result = new TestResult();
                result.setResultId(resultSet.getString("resultid"));
                result.setStudentId(resultSet.getString("studentid"));
                result.setTestId(resultSet.getString("testid"));
                result.setScore(resultSet.getString("score"));
                result.setTotalMarks(resultSet.getString("totalmarks"));
                result.setTimeTaken(resultSet.getString("timetaken"));
                result.setTestDate(resultSet.getTimestamp("testdate"));
                result.setStatus(resultSet.getString("status"));
                result.setInvalidationReason(resultSet.getString("invalidationreason"));
                result.setViolationCount(resultSet.getInt("violationcount"));

                // Calculate percentage if not stored
                try {
                    if (result.getScore() != null && result.getTotalMarks() != null) {
                        double scoreVal = Double.parseDouble(result.getScore());
                        double totalVal = Double.parseDouble(result.getTotalMarks());
                        if (totalVal > 0) {
                            double percentage = (scoreVal / totalVal) * 100;
                            result.setPercentage(String.format("%.1f", percentage));
                        }
                    }
                } catch (NumberFormatException e) {
                    // Keep percentage as null if calculation fails
                }

                // Set display names - you might want to fetch these from other tables
                result.setStudentName("Student " + result.getStudentId()); // Placeholder
                result.setTestTitle("Test " + result.getTestId()); // Placeholder

                results.add(result);
            }

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return results;
    }

    private void calculateSummaryStats(HttpServletRequest request,
                                       List<VideoProcessorResult> videoResults,
                                       List<TestResult> testResults) {

        // Video analysis stats
        int highDetectionCount = 0;
        int lowDetectionCount = 0;
        for (VideoProcessorResult result : videoResults) {
            if (result.getDetectionPercentage() >= 80) {
                highDetectionCount++;
            } else if (result.getDetectionPercentage() < 60) {
                lowDetectionCount++;
            }
        }

        // Test violation stats
        int suspiciousTests = 0;
        int terminatedTests = 0;
        int highViolationTests = 0;

        for (TestResult result : testResults) {
            if (result.getViolationCount() > 0) {
                suspiciousTests++;
            }
            if ("TERMINATED".equals(result.getStatus())) {
                terminatedTests++;
            }
            if (result.getViolationCount() >= 5) {
                highViolationTests++;
            }
        }

        request.setAttribute("highDetectionCount", highDetectionCount);
        request.setAttribute("lowDetectionCount", lowDetectionCount);
        request.setAttribute("suspiciousTests", suspiciousTests);
        request.setAttribute("terminatedTests", terminatedTests);
        request.setAttribute("highViolationTests", highViolationTests);
    }
}