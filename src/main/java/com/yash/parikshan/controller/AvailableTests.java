package com.yash.parikshan.controller;

import com.yash.parikshan.exceptions.GlobalException;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.service.TestService;
import com.yash.parikshan.serviceimpl.TestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/AvailableTests")
public class AvailableTests extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AvailableTests.class.getName());
    private TestService testService;

    @Override
    public void init() throws ServletException {
        logger.info("=== AvailableTests Servlet Initialization Started ===");
        try {
            // Create the service once for the servlet's lifetime
            this.testService = new TestServiceImpl();
            logger.info("TestService created successfully: " + (testService != null ? "SUCCESS" : "FAILED"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize TestService: " + e.getMessage(), e);
            throw new ServletException("Failed to initialize TestService", e);
        }
        logger.info("=== AvailableTests Servlet Initialization Completed ===");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("=== AvailableTests doGet() method called ===");
        logger.info("Request URI: " + req.getRequestURI());
        logger.info("Context Path: " + req.getContextPath());
        logger.info("Servlet Path: " + req.getServletPath());

        try {
            logger.info("Starting to fetch available tests...");

            // 1. Validate service availability
            if (testService == null) {
                logger.severe("Test service is null - service not properly initialized");
                throw GlobalException.databaseError("Test service is not available");
            }

            logger.info("Test service validation passed, proceeding to fetch tests");

            // 2. Fetch list of tests
            List<Test> tests = testService.findAllActive();

            logger.info("Tests fetched from service successfully!");
            logger.info("Number of tests retrieved: " + (tests != null ? tests.size() : "NULL"));

            // 3. Handle null response from service
            if (tests == null) {
                tests = new ArrayList<>();
                logger.warning("Service returned null, using empty list as fallback");
            }

            // 4. Log test details for debugging
            if (!tests.isEmpty()) {
                logger.info("Logging test details for debugging:");
                for (int i = 0; i < tests.size(); i++) {
                    Test t = tests.get(i);
                    logger.info("Test " + (i + 1) + ": ID=" + t.getTestId() +
                            ", Title='" + t.getTitle() + "'" +
                            ", Questions=" + t.getNoOfQuestions() +
                            ", Duration=" + t.getDescription() + " minutes");
                }
            } else {
                logger.warning("No active tests found in the system");
            }

            // 5. Expose list to JSP
            req.setAttribute("tests", tests);
            req.setAttribute("totalTests", tests.size());
            logger.info("Tests attribute set on request - Total tests: " + tests.size());

        } catch (GlobalException e) {
            logger.log(Level.WARNING, "GlobalException occurred while fetching tests: " + e.getMessage(), e);
            handleGlobalException(e, req, resp);
            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception occurred while fetching tests", e);
            // Handle unexpected exceptions by wrapping in GlobalException
            GlobalException globalEx = handleUnexpectedException(e);
            handleGlobalException(globalEx, req, resp);
            return;
        }

        logger.info("Successfully processed test data, forwarding to JSP");
        logger.info("Forwarding to availabletests.jsp...");

        // Forward to JSP
        req.getRequestDispatcher("availabletests.jsp").forward(req, resp);

        logger.info("=== AvailableTests doGet() completed successfully ===");
    }

    /**
     * Convert unexpected exceptions to appropriate GlobalExceptions
     */
    private GlobalException handleUnexpectedException(Exception e) {
        logger.severe("=== Handling unexpected exception in AvailableTests servlet ===");
        logger.log(Level.SEVERE, "Exception details:", e);

        // Analyze exception to determine type
        String message = e.getMessage();
        if (message != null) {
            message = message.toLowerCase();
            logger.info("Analyzing exception message for categorization: " + message);

            // Database-related exceptions
            if (message.contains("connection") || message.contains("timeout")) {
                logger.warning("Categorized as database connection error");
                return GlobalException.databaseConnectionError("Unable to connect to database while fetching tests", e);
            } else if (message.contains("table") || message.contains("sql") || message.contains("database")) {
                logger.warning("Categorized as general database error");
                return GlobalException.databaseError("Database error while retrieving available tests", e);
            }

            // Network-related exceptions
            else if (message.contains("network") || message.contains("host") || message.contains("socket")) {
                logger.warning("Categorized as network error");
                return GlobalException.networkError("Network error while fetching test data", e);
            }
        }

        // Check exception type
        if (e instanceof java.sql.SQLException) {
            logger.warning("Categorized as SQL exception");
            return GlobalException.databaseError("SQL error while retrieving tests", e);
        } else if (e instanceof java.net.SocketTimeoutException) {
            logger.warning("Categorized as socket timeout exception");
            return GlobalException.connectionTimeout("Request timeout while fetching tests", e);
        } else if (e instanceof java.net.ConnectException) {
            logger.warning("Categorized as connection exception");
            return GlobalException.networkError("Connection error while fetching tests", e);
        } else if (e instanceof NullPointerException) {
            logger.warning("Categorized as null pointer exception (data integrity issue)");
            return GlobalException.databaseError("Data integrity issue while fetching tests", e);
        }

        // Default to general error
        logger.warning("Could not categorize exception, using general error");
        return new GlobalException("Unexpected error occurred while loading available tests", e);
    }

    /**
     * Handle GlobalException and set appropriate request attributes
     */
    private void handleGlobalException(GlobalException e, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("=== Handling GlobalException in AvailableTests servlet ===");
        logger.warning("Error Type: " + e.getErrorType());
        logger.warning("Error Code: " + e.getErrorCode());
        logger.warning("Error Message: " + e.getMessage());

        // Set common error attributes
        req.setAttribute("exception", e);
        req.setAttribute("errorCode", e.getErrorCode());
        req.setAttribute("errorType", e.getErrorType().toString());
        req.setAttribute("hasError", true);

        // Set empty tests list to prevent JSP errors
        req.setAttribute("tests", new ArrayList<Test>());
        req.setAttribute("totalTests", 0);
        logger.info("Set empty tests list and error flags on request");

        // Set specific attributes based on error type
        if (e.isDatabaseError()) {
            logger.warning("Handling database error");
            req.setAttribute("errorTitle", "Database Error");
            req.setAttribute("errorMessage", "Unable to load available tests");
            req.setAttribute("errorDetails", "There was a problem accessing the test database. Please try again later.");

            // Log full exception for debugging
            getServletContext().log("Database error in AvailableTests servlet", e);
            resp.setStatus(500); // Internal Server Error

        } else if (e.isNetworkError()) {
            logger.warning("Handling network error");
            req.setAttribute("errorTitle", "Network Error");
            req.setAttribute("errorMessage", "Unable to retrieve test data");
            req.setAttribute("errorDetails", "There was a network problem while fetching tests. Please check your connection and try again.");

            getServletContext().log("Network error in AvailableTests servlet", e);
            resp.setStatus(503); // Service Unavailable

        } else {
            logger.severe("Handling system/unknown error");
            req.setAttribute("errorTitle", "System Error");
            req.setAttribute("errorMessage", "Unable to load available tests");
            req.setAttribute("errorDetails", "An unexpected error occurred. Please try again later or contact support.");

            getServletContext().log("Unexpected error in AvailableTests servlet", e);
            resp.setStatus(500); // Internal Server Error
        }

        logger.info("Error attributes set on request with status: " + resp.getStatus());

        // Forward to the same JSP - it should handle error display
        logger.info("Forwarding to availabletests.jsp with error state...");
        req.getRequestDispatcher("availabletests.jsp").forward(req, resp);

        logger.info("=== AvailableTests error handling completed ===");
    }

    // Delegate POST → GET (e.g., if a form submits here)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("=== AvailableTests doPost() called - delegating to doGet ===");
        logger.info("POST request parameters: " + req.getParameterMap().keySet());
        doGet(req, resp);
    }
}
