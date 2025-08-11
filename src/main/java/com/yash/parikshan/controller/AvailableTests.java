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
import java.util.List;

@WebServlet("/AvailableTests")
public class AvailableTests extends HttpServlet {

    private TestService testService;

    @Override
    public void init() {
        System.out.println("=== AvailableTests Servlet Initialized ===");
        // create the service once for the servlet's lifetime
        this.testService = new TestServiceImpl();
        System.out.println("TestService created: " + (testService != null ? "SUCCESS" : "FAILED"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("=== AvailableTests doGet() called ===");
        System.out.println("Request URI: " + req.getRequestURI());
        System.out.println("Context Path: " + req.getContextPath());
        System.out.println("Servlet Path: " + req.getServletPath());

        try {
            System.out.println("Attempting to fetch tests...");

            // 1. fetch list of tests
            List<Test> tests = testService.findAllActive();

            System.out.println("Tests fetched successfully!");
            System.out.println("Number of tests: " + (tests != null ? tests.size() : "NULL"));

            if (tests != null) {
                for (int i = 0; i < tests.size(); i++) {
                    Test t = tests.get(i);
                    System.out.println("Test " + i + ": ID=" + t.getTestId() +
                            ", Title=" + t.getTitle() +
                            ", Questions=" + t.getNoOfQuestions());
                }
            }

            // 2. expose list to JSP
            req.setAttribute("tests", tests);
            System.out.println("Tests attribute set on request");

        } catch (Exception ex) {
            System.err.println("=== ERROR in AvailableTests servlet ===");
            ex.printStackTrace();

            // 3. on failure send friendly error so the JSP can display it
            req.setAttribute("errorMessage",
                    "Unable to load available tests. Please try again later.");
            req.setAttribute("errorDetails", ex.getMessage());
            System.out.println("Error attributes set on request");
        }

        System.out.println("Forwarding to availabletest.jsp...");

        // 4. always forward to the same JSP
        req.getRequestDispatcher("availabletests.jsp").forward(req, resp);

        System.out.println("=== AvailableTests doGet() completed ===");
    }

    // delegate POST → GET (e.g., if a form submits here)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("=== AvailableTests doPost() called - delegating to doGet ===");
        doGet(req, resp);
    }
}