package com.yash.parikshan.controller;

import com.yash.parikshan.service.TestService;
import com.yash.parikshan.serviceimpl.TestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ManageTest")
public class ManageTest extends HttpServlet {

    private TestService testService;

    @Override
    public void init() throws ServletException {
        super.init();
        testService = new TestServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String testId = request.getParameter("editTestid");
        System.out.println("Received testId: '" + testId + "'");

        if (testId == null || testId.isEmpty()) {
            System.out.println("TestId is null or empty, forwarding to createtest.jsp");
            request.setAttribute("error", "Test ID is required.");
            request.getRequestDispatcher("createtest.jsp").forward(request, response);
            return;
        }

        String noOfQuestions = request.getParameter("noOfQuestions");
        System.out.println("Received questions: '" + noOfQuestions + "'");

        if (noOfQuestions == null || noOfQuestions.isEmpty()) {
            System.out.println("no questions found is null or empty, forwarding to createtest.jsp");
            request.setAttribute("error", "Test ID is required.");
            request.getRequestDispatcher("createtest.jsp").forward(request, response);
            return;
        }

        try {
            boolean authenticated = testService.authenticate(testId);
            System.out.println("Authentication result: " + authenticated);

            if (authenticated) {
                System.out.println("Authentication successful, setting session and redirecting...");
                HttpSession session = request.getSession();
                session.setAttribute("testid", testId);
                session.setAttribute("noOfQuestions", noOfQuestions);

                String redirectUrl = "addquestion.jsp?testid=" + java.net.URLEncoder.encode(testId, "UTF-8");
                System.out.println("Redirecting to: " + redirectUrl);

                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                response.sendRedirect(redirectUrl);
                return;
            }

             else {
                System.out.println("Authentication failed, forwarding to managetest.jsp");
                request.setAttribute("error", "Invalid id.");
                request.getRequestDispatcher("managetest.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("managetest.jsp").forward(request, response);
        }
    }
}


