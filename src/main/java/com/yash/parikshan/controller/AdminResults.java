package com.yash.parikshan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.TestResultService;
import com.yash.parikshan.serviceimpl.TestResultServiceImpl;

@WebServlet("/AdminResults")
public class AdminResults extends HttpServlet {

    private TestResultService testResultService;

    @Override
    public void init() throws ServletException {
        super.init();
        testResultService = new TestResultServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String action = request.getParameter("action");
            String page = "adminResults.jsp"; // default page

            if ("viewByTest".equals(action)) {
                handleViewByTest(request, response);
                return;

            } else if ("viewByStudent".equals(action)) {
                handleViewByStudent(request, response);
                return;

            } else if ("viewResult".equals(action)) {
                handleViewResult(request, response);
                return;

            } else {
                // Default: show all results with filtering options
                handleViewAll(request, response);
                return;
            }

        } catch (IllegalArgumentException e) {
            handleError(request, response, "Invalid Parameters", e.getMessage(), e);
        } catch (RuntimeException e) {
            handleError(request, response, "Service Error",
                    "Unable to retrieve test results. Please try again later.", e);
        } catch (Exception e) {
            handleError(request, response, "Unexpected Error",
                    "An unexpected error occurred while loading test results.", e);
        }
    }

    /**
     * Handle viewing results by test ID
     */
    private void handleViewByTest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String testId = request.getParameter("testId");

        if (testId == null || testId.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Test ID is required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        List<TestResult> results = testResultService.getResultsByTestId(testId.trim());
        List<Test> allTests = testResultService.getAllTests();

        // Find test title for display
        String testTitle = allTests.stream()
                .filter(test -> testId.equals(test.getTestId()))
                .map(Test::getTitle)
                .findFirst()
                .orElse("Unknown Test");

        request.setAttribute("results", results);
        request.setAttribute("allTests", allTests);
        request.setAttribute("viewType", "byTest");
        request.setAttribute("selectedTestId", testId);
        request.setAttribute("pageTitle", "Results for Test: " + testTitle);
        request.setAttribute("totalResults", results.size());

        // Calculate statistics for this test
        calculateTestStatistics(request, results);

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handle viewing results by student ID
     */
    private void handleViewByStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentId = request.getParameter("studentId");

        if (studentId == null || studentId.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Student ID is required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        List<TestResult> results = testResultService.getResultsByStudentId(studentId.trim());
        List<Test> allTests = testResultService.getAllTests();

        // Get student name from first result if available
        String studentName = results.isEmpty() ? "Unknown Student" :
                results.get(0).getStudentName();

        request.setAttribute("results", results);
        request.setAttribute("allTests", allTests);
        request.setAttribute("viewType", "byStudent");
        request.setAttribute("selectedStudentId", studentId);
        request.setAttribute("pageTitle", "Results for Student: " + studentName);
        request.setAttribute("totalResults", results.size());

        // Calculate statistics for this student
        calculateStudentStatistics(request, results);

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handle viewing a specific result
     */
    private void handleViewResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String resultId = request.getParameter("resultId");

        if (resultId == null || resultId.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Result ID is required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        TestResult result = testResultService.getResultById(resultId.trim());

        if (result == null) {
            request.setAttribute("errorMessage", "Test result not found");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        request.setAttribute("result", result);
        request.setAttribute("viewType", "single");
        request.setAttribute("pageTitle", "Test Result Details");

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminResultDetail.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handle viewing all results with optional filtering
     */
    private void handleViewAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filterTestId = request.getParameter("filterTestId");
        List<TestResult> results;

        // Apply filter if specified
        if (filterTestId != null && !filterTestId.trim().isEmpty() && !"all".equals(filterTestId)) {
            results = testResultService.getResultsByTestId(filterTestId.trim());
            request.setAttribute("selectedFilterTestId", filterTestId.trim());
        } else {
            results = testResultService.getAllResults();
        }

        // Get all tests for filter dropdown
        List<Test> allTests = testResultService.getAllTests();

        request.setAttribute("results", results);
        request.setAttribute("allTests", allTests);
        request.setAttribute("viewType", "all");
        request.setAttribute("pageTitle", "All Test Results");
        request.setAttribute("totalResults", results.size());

        // Calculate overall statistics
        calculateOverallStatistics(request, results);

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Calculate statistics for a specific test
     */
    private void calculateTestStatistics(HttpServletRequest request, List<TestResult> results) {
        if (results.isEmpty()) {
            request.setAttribute("noDataMessage", "No results found for this test.");
            return;
        }

        int totalAttempts = results.size();

        // Calculate average score
        double averageScore = results.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);

        // Find highest and lowest scores
        double highestScore = results.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .max()
                .orElse(0.0);

        double lowestScore = results.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .min()
                .orElse(0.0);

        request.setAttribute("totalAttempts", totalAttempts);
        request.setAttribute("averageScore", String.format("%.1f", averageScore));
        request.setAttribute("highestScore", String.format("%.1f", highestScore));
        request.setAttribute("lowestScore", String.format("%.1f", lowestScore));
    }

    /**
     * Calculate statistics for a specific student
     */
    private void calculateStudentStatistics(HttpServletRequest request, List<TestResult> results) {
        if (results.isEmpty()) {
            request.setAttribute("noDataMessage", "No results found for this student.");
            return;
        }

        int totalTestsTaken = results.size();

        // Calculate average performance
        double averagePerformance = results.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);

        request.setAttribute("totalTestsTaken", totalTestsTaken);
        request.setAttribute("averagePerformance", String.format("%.1f", averagePerformance));
    }

    /**
     * Calculate overall statistics for all results
     */
    private void calculateOverallStatistics(HttpServletRequest request, List<TestResult> results) {
        if (results.isEmpty()) {
            request.setAttribute("noDataMessage", "No test results found.");
            return;
        }

        int totalResults = results.size();

        // Count unique students and tests
        long uniqueStudents = results.stream()
                .map(TestResult::getStudentId)
                .distinct()
                .count();

        long uniqueTests = results.stream()
                .map(TestResult::getTestId)
                .distinct()
                .count();

        // Calculate overall average score
        double overallAverageScore = results.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);

        request.setAttribute("totalStudents", uniqueStudents);
        request.setAttribute("totalTests", uniqueTests);
        request.setAttribute("overallAverageScore", String.format("%.1f", overallAverageScore));
    }

    /**
     * Helper method to handle errors consistently
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorTitle, String errorMessage, Exception e)
            throws ServletException, IOException {

        // Log the full stack trace for debugging
        System.err.println("Error in AdminResults: " + errorTitle);
        e.printStackTrace();

        // Set error attributes for JSP
        request.setAttribute("hasError", true);
        request.setAttribute("errorTitle", errorTitle);
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("errorDetails", e.getMessage());

        // Set empty lists to prevent JSP errors
        request.setAttribute("results", new ArrayList<TestResult>());
        request.setAttribute("allTests", new ArrayList<Test>());

        // Forward to error page
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests the same way as GET
        doGet(request, response);
    }
}
