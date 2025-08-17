package com.yash.parikshan.controller;


import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.TestResultService;
import com.yash.parikshan.serviceimpl.TestResultServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/MyResults")
public class MyResultsServlet extends HttpServlet {

    private final TestResultService resultService = new TestResultServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Prevents browser caching
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);
        String studentId = null;

        if (session != null) {
            // Refresh session
            session.setAttribute("lastAccessed", System.currentTimeMillis());
            studentId = (String) session.getAttribute("studentid");
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            resp.sendRedirect("login.jsp?error=Please login to view your results");
            return;
        }

        try {
            List<TestResult> results = resultService.findByStudentId(studentId.trim());
            req.setAttribute("results", results);

        } catch (Exception ex) {
            req.setAttribute("errorMessage", "Unable to load results: " + ex.getMessage());
        }

        req.getRequestDispatcher("myresults.jsp").forward(req, resp);
    }
}
