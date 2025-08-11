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

@WebServlet("/DisplayStudentResults")
public class DisplayStudentResults extends HttpServlet {
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
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp?error=sessionExpired");
                return;
            }

            String userType = (String) session.getAttribute("userType");
            String sessionUserId = (String) session.getAttribute("studentId");

            // Handle backward compatibility
            if (sessionUserId == null) {
                sessionUserId = (String) session.getAttribute("studentid");
            }

            if (sessionUserId == null) {
                response.sendRedirect("login.jsp?error=sessionExpired");
                return;
            }

            // Get filter parameters
            String testId = request.getParameter("testId");
            String studentId = request.getParameter("studentId");

            // Get results based on user type and filters
            List<TestResult> results;

            if ("admin".equals(userType)) {
                // Admin can see all results or filter by criteria
                if (testId != null && !testId.trim().isEmpty() && !testId.equals("all")) {
                    results = testResultService.getResultsByTestId(testId.trim());
                    request.setAttribute("filterType", "test");
                    request.setAttribute("selectedTestId", testId.trim());
                } else if (studentId != null && !studentId.trim().isEmpty() && !studentId.equals("all")) {
                    results = testResultService.getResultsByStudentId(studentId.trim());
                    request.setAttribute("filterType", "student");
                    request.setAttribute("selectedStudentId", studentId.trim());
                } else {
                    results = testResultService.getAllResults();
                    request.setAttribute("filterType", "all");
                }
            } else {
                // Student can only see their own results
                if (testId != null && !testId.trim().isEmpty() && !testId.equals("all")) {
                    List<TestResult> allStudentResults = testResultService.getResultsByStudentId(sessionUserId);
                    results = allStudentResults.stream()
                            .filter(result -> testId.trim().equals(result.getTestId()))
                            .collect(java.util.stream.Collectors.toList());
                    request.setAttribute("selectedTestId", testId.trim());
                } else {
                    results = testResultService.getResultsByStudentId(sessionUserId);
                }
                request.setAttribute("filterType", "student");
            }

            // Get all tests for dropdown filter
            List<Test> tests = testResultService.getAllTests();

            // Set attributes for JSP
            request.setAttribute("testResults", results);
            request.setAttribute("tests", tests);
            request.setAttribute("totalResults", results.size());
            request.setAttribute("userType", userType);

            // Add info message if no results found
            if (results.isEmpty()) {
                if ("admin".equals(userType)) {
                    request.setAttribute("infoMessage", "No test results found for the selected criteria.");
                } else {
                    request.setAttribute("infoMessage", "You haven't taken any tests yet.");
                }
            }

            // Forward to JSP
            request.getRequestDispatcher("/studentresults.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in DisplayStudentResults: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("errorMessage", "Unable to load test results. Please try again later.");
            request.setAttribute("testResults", new ArrayList<TestResult>());
            request.setAttribute("tests", new ArrayList<Test>());
            request.getRequestDispatcher("/studentresults.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
