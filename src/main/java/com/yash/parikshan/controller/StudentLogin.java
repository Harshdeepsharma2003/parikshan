package com.yash.parikshan.controller;

import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.serviceimpl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/StudentLogin")
public class StudentLogin extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        super.init();
        studentService = new StudentServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentId = request.getParameter("studentid");
        String password = request.getParameter("password");

        if (studentId == null || studentId.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "student ID and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            boolean authenticated = studentService.authenticate(studentId, password);

            if (authenticated) {
                HttpSession session = request.getSession();
                session.setAttribute("studentid",studentId);

                // Check if it's an admin (ends with @yash.com)
                if (studentId.endsWith("@yash")) {
                    session.setAttribute("userType", "admin");
                    response.sendRedirect("adminhome.jsp");
                }
                else {
                    session.setAttribute("userType", "student");
                    response.sendRedirect("homestudent.jsp");
                }

            } else {
                request.setAttribute("error", "Invalid credentials.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    }


