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
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/StudentLogin")
public class StudentLogin extends HttpServlet {

    private static final Logger logger = Logger.getLogger(StudentLogin.class.getName());
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing StudentLogin servlet");
        studentService = new StudentServiceImpl();
        logger.info("StudentService initialized successfully");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("=== Student login attempt started ===");

        String studentId = request.getParameter("studentid");
        String password = request.getParameter("password");

        // Log the attempt (without password for security)
        logger.info("Login attempt for student ID: '" + studentId + "'");
        System.out.println("DEBUG LOGIN: Student ID from form: '" + studentId + "'");

        if (studentId == null || studentId.isEmpty() || password == null || password.isEmpty()) {
            logger.warning("Login attempt failed: Missing student ID or password");
            request.setAttribute("error", "Student ID and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            logger.info("Authenticating student: " + studentId);
            boolean authenticated = studentService.authenticate(studentId, password);

            if (authenticated) {
                logger.info("Authentication successful for student: " + studentId);

                // IMPORTANT: Destroy any existing session first
                HttpSession existingSession = request.getSession(false);
                if (existingSession != null) {
                    System.out.println("DEBUG LOGIN: Invalidating existing session: " + existingSession.getId());
                    existingSession.invalidate();
                }

                // Create fresh session
                HttpSession session = request.getSession(true);
                System.out.println("DEBUG LOGIN: Created new session: " + session.getId());

                // Set session attributes
                session.setAttribute("studentid", studentId);
                session.setAttribute("userType", studentId.endsWith("@yash") ? "admin" : "student");
                session.setAttribute("loginTime", System.currentTimeMillis());

                // VERIFY session was set correctly
                String verifyStudentId = (String) session.getAttribute("studentid");
                String verifyUserType = (String) session.getAttribute("userType");

                System.out.println("DEBUG LOGIN: Session attributes set:");
                System.out.println("  studentid = '" + verifyStudentId + "'");
                System.out.println("  userType = '" + verifyUserType + "'");
                System.out.println("  loginTime = " + session.getAttribute("loginTime"));

                // Check if it's an admin (ends with @yash)
                if (studentId.endsWith("@yash")) {
                    logger.info("Admin user logged in: " + studentId);
                    System.out.println("DEBUG LOGIN: Redirecting to adminhome.jsp");
                    response.sendRedirect("adminhome.jsp");
                } else {
                    logger.info("Student user logged in: " + studentId);
                    System.out.println("DEBUG LOGIN: Redirecting to homestudent.jsp");
                    response.sendRedirect("homestudent.jsp");
                }

            } else {
                logger.warning("Authentication failed for student: " + studentId);
                request.setAttribute("error", "Invalid credentials.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database error during login for student: " + studentId, e);
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to StudentLogin - redirecting to login.jsp");
        response.sendRedirect("login.jsp");
    }
}
