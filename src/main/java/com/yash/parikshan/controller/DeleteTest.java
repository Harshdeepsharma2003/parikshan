package com.yash.parikshan.controller;

import com.yash.parikshan.service.TestService;
import com.yash.parikshan.serviceimpl.TestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/DeleteTest")
public class DeleteTest extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteTest.class.getName());
    private TestService testService = new TestServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("DeleteTest servlet called");

        try {
            String testId = request.getParameter("deleteTestid");
            logger.info("Deleting test with ID: " + testId);

            if (testId == null || testId.trim().isEmpty()) {
                logger.warning("Test ID is empty");
                request.getSession().setAttribute("errorMessage", "Test ID is required");
                response.sendRedirect("adminhome.jsp");
                return;
            }

            testService.deleteTest(testId);
            logger.info("Test deleted successfully: " + testId);

            request.getSession().setAttribute("successMessage", "Test deleted successfully!");

        } catch (Exception e) {
            logger.severe("Error deleting test: " + e.getMessage());
            request.getSession().setAttribute("errorMessage", "Failed to delete test");
        }

        response.sendRedirect("adminhome.jsp");
    }
}
