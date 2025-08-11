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

@WebServlet("/StudentRegisterTest")
public class StudentRegisterTest extends HttpServlet {

    private TestService testService = new TestServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String testId = request.getParameter("testid");


        if (testId == null || testId.trim().isEmpty()) {
            request.setAttribute("error", "Test ID and Title are required.");
            request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
            return;
        }

        Test test = new Test();
        test.setTestId(testId);

        try {
            boolean success = testService.registerTest(test);
            if (success) {
                // Store the testId in HTTP session
                request.getSession().setAttribute("testid", testId);
                request.getRequestDispatcher("testloginstudent.jsp").forward(request, response);


            } else {

                request.setAttribute("error", "Failed to register test. Try again.");
                request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Internal server error.");
            request.getRequestDispatcher("studenttestcreation.jsp").forward(request, response);
        }
    }

}
