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

@WebServlet("/StudentRegisterTest")
public class StudentRegisterTest extends HttpServlet {

    private static final Logger logger = Logger.getLogger(StudentRegisterTest.class.getName());
    private TestService testService = new TestServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("StudentRegisterTest servlet called");

        String testId = request.getParameter("testid");
        logger.info("Registering for test with ID: " + testId);

        if (testId == null || testId.trim().isEmpty()) {
            logger.warning("Test ID is empty or null");
            request.setAttribute("error", "Test ID and Title are required.");
            request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
            return;
        }

        Test test = new Test();
        test.setTestId(testId);

        try {
            logger.info("Attempting to register test: " + testId);
            boolean success = testService.registerTest(test);

            if (success) {
                logger.info("Test registration successful for: " + testId);
                // Store the testId in HTTP session
                request.getSession().setAttribute("testid", testId);
                logger.info("Test ID stored in session, forwarding to test login page");
                request.getRequestDispatcher("testloginstudent.jsp").forward(request, response);

            } else {
                logger.warning("Test registration failed for: " + testId);
                request.setAttribute("error", "Failed to register test. Try again.");
                request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error registering test: " + e.getMessage());
            request.setAttribute("error", "Internal server error.");
            request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
        }
    }
}
