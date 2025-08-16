package com.yash.parikshan.controller;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.TestResultService;
import com.yash.parikshan.serviceimpl.TestResultServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/DisplayStudentResults")
public class DisplayStudentResults extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DisplayStudentResults.class.getName());
    private TestResultService testResultService;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("DisplayStudentResults servlet initialized");
        testResultService = new TestResultServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("DisplayStudentResults servlet called");

        try {
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null) {
                logger.warning("No session found, redirecting to login");
                response.sendRedirect("login.jsp?error=sessionExpired");
                return;
            }

            String userType = (String) session.getAttribute("userType");
            String sessionUserId = (String) session.getAttribute("studentid");

            // Handle backward compatibility
            if (sessionUserId == null) {
                sessionUserId = (String) session.getAttribute("studentid");
            }

            if (sessionUserId == null) {
                logger.warning("No user ID in session, redirecting to login");
                response.sendRedirect("login.jsp?error=sessionExpired");
                return;
            }

            logger.info("User logged in - Type: " + userType + ", ID: " + sessionUserId);

            // Get filter parameters
            String testId = request.getParameter("testId");
            String studentId = request.getParameter("studentId");
            logger.info("Filter parameters - testId: " + testId + ", studentId: " + studentId);

            // Get results based on user type and filters
            List<TestResult> results;

            if ("admin".equals(userType)) {
                logger.info("Processing admin request");
                // Admin can see all results or filter by criteria
                if (testId != null && !testId.trim().isEmpty() && !testId.equals("all")) {
                    logger.info("Getting results by test ID: " + testId);
                    results = testResultService.getResultsByTestId(testId.trim());
                    request.setAttribute("filterType", "test");
                    request.setAttribute("selectedTestId", testId.trim());
                } else if (studentId != null && !studentId.trim().isEmpty() && !studentId.equals("all")) {
                    logger.info("Getting results by student ID: " + studentId);
                    results = testResultService.getResultsByStudentId(studentId.trim());
                    request.setAttribute("filterType", "student");
                    request.setAttribute("selectedStudentId", studentId.trim());
                } else {
                    logger.info("Getting all results");
                    results = testResultService.getAllResults();
                    request.setAttribute("filterType", "all");
                }
            } else {
                logger.info("Processing student request for user: " + sessionUserId);
                // Student can only see their own results
                if (testId != null && !testId.trim().isEmpty() && !testId.equals("all")) {
                    logger.info("Getting student results filtered by test ID: " + testId);
                    List<TestResult> allStudentResults = testResultService.getResultsByStudentId(sessionUserId);
                    results = allStudentResults.stream()
                            .filter(result -> testId.trim().equals(result.getTestId()))
                            .collect(java.util.stream.Collectors.toList());
                    request.setAttribute("selectedTestId", testId.trim());
                } else {
                    logger.info("Getting all results for student: " + sessionUserId);
                    results = testResultService.getResultsByStudentId(sessionUserId);
                }
                request.setAttribute("filterType", "student");
            }

            // Get all tests for dropdown filter
            List<Test> tests = testResultService.getAllTests();
            logger.info("Retrieved " + results.size() + " results and " + tests.size() + " tests");

            // Set attributes for JSP
            request.setAttribute("testResults", results);
            request.setAttribute("tests", tests);
            request.setAttribute("totalResults", results.size());
            request.setAttribute("userType", userType);

            // Add info message if no results found
            if (results.isEmpty()) {
                if ("admin".equals(userType)) {
                    logger.info("No results found for admin filter criteria");
                    request.setAttribute("infoMessage", "No test results found for the selected criteria.");
                } else {
                    logger.info("No results found for student: " + sessionUserId);
                    request.setAttribute("infoMessage", "You haven't taken any tests yet.");
                }
            }

            // Forward to JSP
            logger.info("Forwarding to studentresults.jsp");
            request.getRequestDispatcher("/studentresults.jsp").forward(request, response);

        } catch (Exception e) {
            logger.severe("Error in DisplayStudentResults: " + e.getMessage());

            request.setAttribute("errorMessage", "Unable to load test results. Please try again later.");
            request.setAttribute("testResults", new ArrayList<TestResult>());
            request.setAttribute("tests", new ArrayList<Test>());
            request.getRequestDispatcher("/studentresults.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("POST request received, delegating to GET");
        doGet(request, response);
    }
}
