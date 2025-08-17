package com.yash.parikshan.controller;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.service.TestService;
import com.yash.parikshan.serviceimpl.TestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/RegisterTest")
public class RegisterTest extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterTest.class.getName());
    private TestService testService = new TestServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("RegisterTest servlet initialized successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("RegisterTest servlet: Processing new test registration");

        try {

            String testId = request.getParameter("testid");
            String title = request.getParameter("testTitle");
            String description = request.getParameter("testDesc");
            String noOfQuestions = request.getParameter("noofquestions");

            logger.info("Test registration attempt - ID: " + testId + ", Title: " + title + ", Questions: " + noOfQuestions);


            if (testId == null || testId.trim().isEmpty()) {
                logger.warning("Test ID validation failed: empty or null");
                request.setAttribute("errorMessage", "Validation Error");
                request.setAttribute("errorDetails", "Test ID is required and cannot be empty.");
                request.getRequestDispatcher("createtest.jsp").forward(request, response);
                return;
            }

            if (title == null || title.trim().isEmpty()) {
                logger.warning("Test title validation failed: empty or null");
                request.setAttribute("errorMessage", "Validation Error");
                request.setAttribute("errorDetails", "Test title is required and cannot be empty.");
                request.getRequestDispatcher("createtest.jsp").forward(request, response);
                return;
            }

            // Validating number of questions
            int questionCount = 0;
            if (noOfQuestions != null && !noOfQuestions.trim().isEmpty()) {
                try {
                    questionCount = Integer.parseInt(noOfQuestions.trim());
                    if (questionCount < 1 || questionCount > 5) {
                        logger.warning("Invalid question count: " + questionCount);
                        request.setAttribute("errorMessage", "Validation Error");
                        request.setAttribute("errorDetails", "Number of questions must be between 1 and 5.");
                        request.getRequestDispatcher("createtest.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Invalid number format for questions: " + noOfQuestions);
                    request.setAttribute("errorMessage", "Validation Error");
                    request.setAttribute("errorDetails", "Please enter a valid number for questions (1-5).");
                    request.getRequestDispatcher("createtest.jsp").forward(request, response);
                    return;
                }
            } else {
                logger.warning("Number of questions not provided");
                request.setAttribute("errorMessage", "Validation Error");
                request.setAttribute("errorDetails", "Number of questions is required.");
                request.getRequestDispatcher("createtest.jsp").forward(request, response);
                return;
            }

            Test test = new Test();
            test.setTestId(testId.trim());
            test.setTitle(title.trim());
            test.setDescription(description != null ? description.trim() : "");
            test.setNoOfQuestions(String.valueOf(questionCount));

            logger.info("Attempting to register test: " + testId);

            boolean success = testService.registerTest(test);

            if (success) {
                logger.info("Test registered successfully: " + testId);

                request.getSession().setAttribute("testid", testId);
                request.getSession().setAttribute("noOfQuestions", noOfQuestions);
                request.getSession().setAttribute("testTitle", title);
                request.setAttribute("successMessage", "Test '" + title.trim() + "' created successfully! Now add your questions.");


                request.getRequestDispatcher("addquestion.jsp").forward(request, response);

            } else {
                logger.warning("Test registration failed for unknown reason: " + testId);
                request.setAttribute("errorMessage", "Registration Failed");
                request.setAttribute("errorDetails", "Failed to register test. This could be due to a duplicate Test ID or database issue. Please try with a different Test ID.");
                request.getRequestDispatcher("createtest.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in RegisterTest servlet", e);

            // Handling different types of exceptions
            String errorDetails;
            if (e.getMessage() != null && e.getMessage().contains("duplicate")) {
                errorDetails = "A test with this ID already exists. Please choose a different Test ID.";
            } else if (e.getMessage() != null && e.getMessage().contains("connection")) {
                errorDetails = "Database connection error. Please try again later.";
            } else {
                errorDetails = "An unexpected error occurred. Please contact support if the problem persists.";
            }

            request.setAttribute("errorMessage", "System Error");
            request.setAttribute("errorDetails", errorDetails);
            request.getRequestDispatcher("createtest.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("GET request to RegisterTest - redirecting to create test page");
        response.sendRedirect(request.getContextPath() + "/createtest.jsp");
    }
}