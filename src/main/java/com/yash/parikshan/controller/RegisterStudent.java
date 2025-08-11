package com.yash.parikshan.controller;

import com.yash.parikshan.model.Student;
import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.serviceimpl.StudentServiceImpl;
import com.yash.parikshan.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/RegisterStudent")
public class RegisterStudent extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterStudent.class.getName());

    // Use the service layer
    private StudentService studentService = new StudentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extract parameters from request
        String studentId = request.getParameter("studentid");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        LOGGER.info("Registration attempt for student ID: " + studentId);

        if (studentId == null || studentId.trim().isEmpty()) {
            LOGGER.warning("Registration failed: Student ID is missing");
            request.setAttribute("error", "Student ID is required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            // Hash the password before creating student object
            String hashedPassword = PasswordUtil.hashPassword(password);
            LOGGER.info("Password hashed successfully for student: " + studentId);

            // Create student model object with hashed password
            Student student = new Student(studentId, name, email, hashedPassword, phone);

            // Register user via service layer (validations will be handled here)
            studentService.insertStudent(student);
            LOGGER.info("Student registered successfully: " + studentId);

            // On success, redirect to login page
            response.sendRedirect("login.jsp");

        } catch (IllegalArgumentException e) {
            // Validation or business logic error from service layer
            LOGGER.log(Level.WARNING, "Registration validation failed for student " + studentId + ": " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } catch (Exception e) {
            // Unexpected errors (e.g. DB errors, hashing errors)
            LOGGER.log(Level.SEVERE, "Unexpected error during registration for student " + studentId, e);
            request.setAttribute("error", "Internal server error. Please try again later.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}