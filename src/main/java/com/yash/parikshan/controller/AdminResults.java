package com.yash.parikshan.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.parikshan.exceptions.GlobalException;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.TestResultService;
import com.yash.parikshan.serviceimpl.TestResultServiceImpl;

@WebServlet("/AdminResults")
public class AdminResults extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AdminResults.class.getName());
    private TestResultService testResultService;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing AdminResults servlet");
        try {
            testResultService = new TestResultServiceImpl();
            logger.info("TestResultService initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize TestResultService", e);
            throw new ServletException("Failed to initialize TestResultService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("AdminResults doGet method called");

        try {
            String action = request.getParameter("action");
            logger.info("Processing admin results request with action: " + action);

            if ("viewByTest".equals(action)) {
                logger.info("Handling viewByTest action");
                handleViewByTest(request, response);
                return;

            } else if ("viewByStudent".equals(action)) {
                logger.info("Handling viewByStudent action");
                handleViewByStudent(request, response);
                return;

            } else if ("viewResult".equals(action)) {
                logger.info("Handling viewResult action");
                handleViewResult(request, response);
                return;

            } else {
                logger.info("Handling default viewAll action");
                // Default: show all results with filtering options
                handleViewAll(request, response);
                return;
            }

        } catch (GlobalException e) {
            logger.log(Level.WARNING, "GlobalException occurred in AdminResults: " + e.getMessage(), e);
            handleGlobalException(e, request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception occurred in AdminResults", e);
            GlobalException globalEx = new GlobalException("Unexpected error occurred while processing admin results", e);
            handleGlobalException(globalEx, request, response);
        }
    }


    private void handleViewByTest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, GlobalException {

        String testId = request.getParameter("testId");
        logger.info("Handling viewByTest for test ID: " + testId);

        if (testId == null || testId.trim().isEmpty()) {
            logger.warning("Test ID is null or empty in viewByTest request");
            throw GlobalException.validationError("Test ID is required to view test results");
        }

        try {
            logger.info("Fetching results for test ID: " + testId.trim());
            List<TestResult> results = testResultService.getResultsByTestId(testId.trim());
            List<Test> allTests = testResultService.getAllTests();
            logger.info("Retrieved " + results.size() + " results for test ID: " + testId.trim());

            // Find test title for display
            String testTitle = allTests.stream()
                    .filter(test -> testId.equals(test.getTestId()))
                    .map(Test::getTitle)
                    .findFirst()
                    .orElse("Unknown Test");

            // Check if test exists
            boolean testExists = allTests.stream()
                    .anyMatch(test -> testId.equals(test.getTestId()));

            if (!testExists) {
                logger.warning("Test with ID '" + testId + "' not found");
                throw GlobalException.resourceNotFound("Test with ID '" + testId + "' not found");
            }

            logger.info("Test found: " + testTitle + " with " + results.size() + " results");

            request.setAttribute("results", results);
            request.setAttribute("allTests", allTests);
            request.setAttribute("viewType", "byTest");
            request.setAttribute("selectedTestId", testId);
            request.setAttribute("pageTitle", "Results for Test: " + testTitle);
            request.setAttribute("totalResults", results.size());

            // Calculate statistics for this test
            logger.info("Calculating test statistics for test ID: " + testId);
            calculateTestStatistics(request, results);

            logger.info("Forwarding to adminResults.jsp for viewByTest");
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleViewByTest for test ID: " + testId, e);
            // Handle all exceptions generically with context-aware error messages
            if (e.getMessage().contains("connection") || e.getMessage().contains("timeout")) {
                throw GlobalException.databaseConnectionError("Unable to connect to database while retrieving test results", e);
            } else if (e.getMessage().contains("table") || e.getMessage().contains("sql")) {
                throw GlobalException.databaseError("Database error while retrieving test results", e);
            } else {
                throw GlobalException.databaseError("Error retrieving results for test ID: " + testId, e);
            }
        }
    }

    /**
     * Handle viewing results by student ID
     */
    private void handleViewByStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, GlobalException {

        String studentId = request.getParameter("studentId");
        logger.info("Handling viewByStudent for student ID: " + studentId);

        if (studentId == null || studentId.trim().isEmpty()) {
            logger.warning("Student ID is null or empty in viewByStudent request");
            throw GlobalException.validationError("Student ID is required to view student results");
        }

        try {
            logger.info("Fetching results for student ID: " + studentId.trim());
            List<TestResult> results = testResultService.getResultsByStudentId(studentId.trim());
            List<Test> allTests = testResultService.getAllTests();
            logger.info("Retrieved " + results.size() + " results for student ID: " + studentId.trim());

            // Get student name from first result if available
            String studentName = results.isEmpty() ? "Unknown Student" :
                    results.get(0).getStudentName();

            logger.info("Student found: " + studentName + " with " + results.size() + " test attempts");

            request.setAttribute("results", results);
            request.setAttribute("allTests", allTests);
            request.setAttribute("viewType", "byStudent");
            request.setAttribute("selectedStudentId", studentId);
            request.setAttribute("pageTitle", "Results for Student: " + studentName);
            request.setAttribute("totalResults", results.size());

            // Calculate statistics for this student
            logger.info("Calculating student statistics for student ID: " + studentId);
            calculateStudentStatistics(request, results);

            logger.info("Forwarding to adminResults.jsp for viewByStudent");
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleViewByStudent for student ID: " + studentId, e);
            // Handle all exceptions generically with context-aware error messages
            if (e.getMessage().contains("connection") || e.getMessage().contains("timeout")) {
                throw GlobalException.databaseConnectionError("Unable to connect to database while retrieving student results", e);
            } else if (e.getMessage().contains("table") || e.getMessage().contains("sql")) {
                throw GlobalException.databaseError("Database error while retrieving student results", e);
            } else {
                throw GlobalException.databaseError("Error retrieving results for student ID: " + studentId, e);
            }
        }
    }

    /**
     * Handle viewing a specific result
     */
    private void handleViewResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, GlobalException {

        String resultId = request.getParameter("resultId");
        logger.info("Handling viewResult for result ID: " + resultId);

        if (resultId == null || resultId.trim().isEmpty()) {
            logger.warning("Result ID is null or empty in viewResult request");
            throw GlobalException.validationError("Result ID is required to view result details");
        }

        try {
            logger.info("Fetching result details for result ID: " + resultId.trim());
            TestResult result = testResultService.getResultById(resultId.trim());

            if (result == null) {
                logger.warning("Test result with ID '" + resultId + "' not found");
                throw GlobalException.resourceNotFound("Test result with ID '" + resultId + "' not found");
            }

            logger.info("Successfully retrieved result details for result ID: " + resultId);

            request.setAttribute("result", result);
            request.setAttribute("viewType", "single");
            request.setAttribute("pageTitle", "Test Result Details");

            logger.info("Forwarding to adminResultDetail.jsp for viewResult");
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminResultDetail.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleViewResult for result ID: " + resultId, e);
            // Handle all exceptions generically with context-aware error messages
            if (e.getMessage().contains("connection") || e.getMessage().contains("timeout")) {
                throw GlobalException.databaseConnectionError("Unable to connect to database while retrieving result details", e);
            } else if (e.getMessage().contains("table") || e.getMessage().contains("sql")) {
                throw GlobalException.databaseError("Database error while retrieving result details", e);
            } else {
                throw GlobalException.databaseError("Error retrieving result with ID: " + resultId, e);
            }
        }
    }

    /**
     * Handle viewing all results with optional filtering
     */
    private void handleViewAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, GlobalException {

        String filterTestId = request.getParameter("filterTestId");
        logger.info("Handling viewAll with filter test ID: " + filterTestId);

        try {
            List<TestResult> results;

            // Apply filter if specified
            if (filterTestId != null && !filterTestId.trim().isEmpty() && !"all".equals(filterTestId)) {
                logger.info("Applying filter for test ID: " + filterTestId.trim());
                results = testResultService.getResultsByTestId(filterTestId.trim());
                request.setAttribute("selectedFilterTestId", filterTestId.trim());
            } else {
                logger.info("Fetching all test results without filter");
                results = testResultService.getAllResults();
            }

            // Get all tests for filter dropdown
            List<Test> allTests = testResultService.getAllTests();
            logger.info("Retrieved " + results.size() + " total results and " + allTests.size() + " tests");

            request.setAttribute("results", results);
            request.setAttribute("allTests", allTests);
            request.setAttribute("viewType", "all");
            request.setAttribute("pageTitle", "All Test Results");
            request.setAttribute("totalResults", results.size());

            // Calculate overall statistics
            logger.info("Calculating overall statistics for all results");
            calculateOverallStatistics(request, results);

            logger.info("Forwarding to adminResults.jsp for viewAll");
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminResults.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleViewAll", e);
            // Handle all exceptions generically with context-aware error messages
            if (e.getMessage().contains("connection") || e.getMessage().contains("timeout")) {
                throw GlobalException.databaseConnectionError("Unable to connect to database while retrieving all results", e);
            } else if (e.getMessage().contains("table") || e.getMessage().contains("sql")) {
                throw GlobalException.databaseError("Database error while retrieving all results", e);
            } else {
                throw GlobalException.databaseError("Error retrieving test results", e);
            }
        }
    }

    /**
     * Calculate statistics for a specific test
     */
    private void calculateTestStatistics(HttpServletRequest request, List<TestResult> results) {
        logger.info("Calculating test statistics for " + results.size() + " results");

        if (results.isEmpty()) {
            logger.info("No results found for statistics calculation");
            request.setAttribute("noDataMessage", "No results found for this test.");
            return;
        }

        try {
            int totalAttempts = results.size();

            // Calculate average score
            double averageScore = results.stream()
                    .mapToDouble(r -> {
                        try {
                            return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                        } catch (NumberFormatException | ArithmeticException e) {
                            logger.warning("Error parsing score for result: " + r.getResultId() + " - " + e.getMessage());
                            getServletContext().log("Error parsing score for result: " + r.getResultId(), e);
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
                        } catch (NumberFormatException | ArithmeticException e) {
                            return 0.0;
                        }
                    })
                    .max()
                    .orElse(0.0);

            double lowestScore = results.stream()
                    .mapToDouble(r -> {
                        try {
                            return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                        } catch (NumberFormatException | ArithmeticException e) {
                            return 0.0;
                        }
                    })
                    .min()
                    .orElse(0.0);

            logger.info("Test statistics calculated - Attempts: " + totalAttempts +
                    ", Average: " + String.format("%.1f", averageScore) +
                    "%, Highest: " + String.format("%.1f", highestScore) +
                    "%, Lowest: " + String.format("%.1f", lowestScore) + "%");

            request.setAttribute("totalAttempts", totalAttempts);
            request.setAttribute("averageScore", String.format("%.1f", averageScore));
            request.setAttribute("highestScore", String.format("%.1f", highestScore));
            request.setAttribute("lowestScore", String.format("%.1f", lowestScore));

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error calculating test statistics", e);
            getServletContext().log("Error calculating test statistics", e);
            request.setAttribute("noDataMessage", "Unable to calculate statistics for this test.");
        }
    }

    /**
     * Calculate statistics for a specific student
     */
    private void calculateStudentStatistics(HttpServletRequest request, List<TestResult> results) {
        logger.info("Calculating student statistics for " + results.size() + " test attempts");

        if (results.isEmpty()) {
            logger.info("No results found for student statistics calculation");
            request.setAttribute("noDataMessage", "No results found for this student.");
            return;
        }

        try {
            int totalTestsTaken = results.size();

            // Calculate average performance
            double averagePerformance = results.stream()
                    .mapToDouble(r -> {
                        try {
                            return Double.parseDouble(r.getScore()) / Double.parseDouble(r.getTotalMarks()) * 100;
                        } catch (NumberFormatException | ArithmeticException e) {
                            logger.warning("Error parsing score for result: " + r.getResultId() + " - " + e.getMessage());
                            getServletContext().log("Error parsing score for result: " + r.getResultId(), e);
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0.0);

            logger.info("Student statistics calculated - Tests taken: " + totalTestsTaken +
                    ", Average performance: " + String.format("%.1f", averagePerformance) + "%");

            request.setAttribute("totalTestsTaken", totalTestsTaken);
            request.setAttribute("averagePerformance", String.format("%.1f", averagePerformance));

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error calculating student statistics", e);
            getServletContext().log("Error calculating student statistics", e);
            request.setAttribute("noDataMessage", "Unable to calculate statistics for this student.");
        }
    }

    /**
     * Calculate overall statistics for all results
     */
    private void calculateOverallStatistics(HttpServletRequest request, List<TestResult> results) {
        logger.info("Calculating overall statistics for " + results.size() + " total results");

        if (results.isEmpty()) {
            logger.info("No results found for overall statistics calculation");
            request.setAttribute("noDataMessage", "No test results found.");
            return;
        }

        try {
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
                        } catch (NumberFormatException | ArithmeticException e) {
                            logger.warning("Error parsing score for result: " + r.getResultId() + " - " + e.getMessage());
                            getServletContext().log("Error parsing score for result: " + r.getResultId(), e);
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0.0);

            logger.info("Overall statistics calculated - Students: " + uniqueStudents +
                    ", Tests: " + uniqueTests +
                    ", Overall average: " + String.format("%.1f", overallAverageScore) + "%");

            request.setAttribute("totalStudents", uniqueStudents);
            request.setAttribute("totalTests", uniqueTests);
            request.setAttribute("overallAverageScore", String.format("%.1f", overallAverageScore));

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error calculating overall statistics", e);
            getServletContext().log("Error calculating overall statistics", e);
            request.setAttribute("noDataMessage", "Unable to calculate overall statistics.");
        }
    }

    /**
     * Handle GlobalException and forward to appropriate error page
     */
    private void handleGlobalException(GlobalException e, HttpServletRequest request,
                                       HttpServletResponse response) throws ServletException, IOException {

        logger.info("Handling GlobalException: " + e.getErrorType() + " - " + e.getMessage());

        // Set common error attributes
        request.setAttribute("exception", e);
        request.setAttribute("errorCode", e.getErrorCode());
        request.setAttribute("errorType", e.getErrorType().toString());

        // Set empty lists to prevent JSP errors
        request.setAttribute("results", new ArrayList<TestResult>());
        request.setAttribute("allTests", new ArrayList<Test>());

        // Set specific attributes based on error type
        if (e.isValidationError()) {
            logger.warning("Validation error: " + e.getMessage());
            request.setAttribute("hasError", true);
            request.setAttribute("errorTitle", "Validation Error");
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("errorDetails", "Please check your input and try again.");
            response.setStatus(400); // Bad Request

        } else if (e.isResourceNotFound()) {
            logger.warning("Resource not found error: " + e.getMessage());
            request.setAttribute("hasError", true);
            request.setAttribute("errorTitle", "Resource Not Found");
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("errorDetails", "The requested resource could not be found.");
            response.setStatus(404); // Not Found

        } else if (e.isDatabaseError()) {
            logger.severe("Database error: " + e.getMessage());
            request.setAttribute("hasError", true);
            request.setAttribute("errorTitle", "Database Error");
            request.setAttribute("errorMessage", "Unable to retrieve test results");
            request.setAttribute("errorDetails", "There was a problem accessing the database. Please try again later.");

            // Log the full exception details for debugging
            getServletContext().log("Database error in AdminResults servlet", e);
            response.setStatus(500); // Internal Server Error

        } else {
            logger.severe("System error: " + e.getMessage());
            request.setAttribute("hasError", true);
            request.setAttribute("errorTitle", "System Error");
            request.setAttribute("errorMessage", "An unexpected error occurred");
            request.setAttribute("errorDetails", "Please contact support if the problem persists.");

            // Log the full exception details
            getServletContext().log("Unexpected error in AdminResults servlet", e);
            response.setStatus(500); // Internal Server Error
        }

        logger.info("Forwarding to error page: error.jsp");
        // Forward to error page
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("AdminResults doPost method called, delegating to doGet");
        // Handle POST requests the same way as GET
        doGet(request, response);
    }
}
