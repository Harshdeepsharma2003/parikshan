package com.yash.parikshan.controller;

import com.yash.parikshan.exceptions.GlobalException;
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

@WebServlet("/DeleteProfileStudent")
public class DeleteProfileStudent extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteProfileStudent.class.getName());
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("=== DeleteProfileStudent Servlet Initialization Started ===");
        try {
            studentService = new StudentServiceImpl();
            logger.info("StudentService initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize StudentService", e);
            throw new ServletException("Failed to initialize StudentService", e);
        }
        logger.info("=== DeleteProfileStudent Servlet Initialization Completed ===");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("=== DeleteProfileStudent doGet() method called ===");
        logger.info("Request URI: " + request.getRequestURI());
        logger.info("Context Path: " + request.getContextPath());

        try {
            // 1. Validate input parameters
            String studentId = request.getParameter("studentid");
            String password = request.getParameter("password");

            logger.info("Profile deletion request for student ID: " + studentId);

            if (studentId == null || studentId.trim().isEmpty()) {
                logger.warning("Profile deletion failed: Student ID is null or empty");
                throw GlobalException.validationError("Student ID is required for profile deletion");
            }

            if (password == null || password.trim().isEmpty()) {
                logger.warning("Profile deletion failed: Password is null or empty for student: " + studentId);
                throw GlobalException.validationError("Password is required for profile deletion verification");
            }

            logger.info("Input validation passed for student: " + studentId);

            // 2. Validate service availability
            if (studentService == null) {
                logger.severe("Student service is null - service not properly initialized");
                throw GlobalException.databaseError("Student service is not available");
            }

            logger.info("Service validation passed, proceeding with profile deletion");

            // 3. Additional security validation - check if user is authorized to delete this profile
            HttpSession session = request.getSession(false);
            if (session != null) {
                String sessionStudentId = (String) session.getAttribute("studentId");
                logger.info("Session student ID: " + sessionStudentId + ", Requested deletion for: " + studentId);

                // Only allow users to delete their own profile
                if (sessionStudentId != null && !sessionStudentId.equals(studentId.trim())) {
                    logger.warning("Unauthorized profile deletion attempt - Session user: " + sessionStudentId +
                            " trying to delete profile: " + studentId);
                    throw GlobalException.securityError("Unauthorized attempt to delete another user's profile");
                }
                logger.info("Authorization check passed for student: " + studentId);
            } else {
                logger.warning("No active session found for profile deletion request");
            }

            // 4. Perform profile deletion with proper error handling
            try {
                logger.info("Attempting to delete profile for student: " + studentId);
                boolean deleteResult = studentService.deleteProfile(studentId.trim(), password.trim());

                logger.info("Profile deletion service call completed for student: " + studentId +
                        ", Result: " + deleteResult);

                // Check if deletion was successful
                if (!deleteResult) {
                    logger.warning("Profile deletion failed for student: " + studentId + " - Service returned false");
                    throw GlobalException.validationError("Profile deletion failed. Please verify your credentials and try again.");
                }

                logger.info("Profile deletion successful for student: " + studentId);

                // 5. Clean up session after successful deletion
                if (session != null) {
                    logger.info("Invalidating session after successful profile deletion for student: " + studentId);
                    session.invalidate();
                }

                // 6. Set success message and redirect
                logger.info("Setting success message and redirecting after profile deletion");
                request.getSession().setAttribute("successMessage", "Your profile has been successfully deleted.");
                logger.info("Redirecting to testformats.jsp after successful profile deletion");
                response.sendRedirect("testformats.jsp");

            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception occurred during profile deletion service call for student: " + studentId, e);

                // Handle service layer exceptions
                String errorMessage = e.getMessage();
                if (errorMessage != null) {
                    if (errorMessage.contains("Invalid password") || errorMessage.contains("authentication")) {
                        logger.warning("Invalid password provided for profile deletion - Student: " + studentId);
                        throw GlobalException.securityError("Invalid password provided for profile deletion");
                    } else if (errorMessage.contains("Student not found") || errorMessage.contains("not exist")) {
                        logger.warning("Student profile not found for deletion - Student ID: " + studentId);
                        throw GlobalException.resourceNotFound("Student profile not found with ID: " + studentId);
                    } else if (errorMessage.contains("connection") || errorMessage.contains("timeout")) {
                        logger.severe("Database connection error during profile deletion for student: " + studentId);
                        throw GlobalException.databaseConnectionError("Unable to connect to database for profile deletion", e);
                    } else if (errorMessage.contains("constraint") || errorMessage.contains("reference")) {
                        logger.warning("Data integrity constraint violation during profile deletion for student: " + studentId);
                        throw GlobalException.dataIntegrityError("Cannot delete profile due to existing test records. Please contact administrator.", e);
                    } else if (errorMessage.contains("database") || errorMessage.contains("sql")) {
                        logger.severe("Database error during profile deletion for student: " + studentId);
                        throw GlobalException.databaseError("Database error occurred during profile deletion", e);
                    }
                }

                logger.severe("Unhandled exception during profile deletion for student: " + studentId);
                throw GlobalException.databaseError("Error occurred while deleting student profile", e);
            }

        } catch (GlobalException e) {
            logger.log(Level.WARNING, "GlobalException occurred in DeleteProfileStudent: " + e.getMessage(), e);
            handleGlobalException(e, request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception occurred in DeleteProfileStudent", e);
            // Handle any other unexpected exceptions
            GlobalException globalEx = new GlobalException("Unexpected error occurred during profile deletion", e);
            handleGlobalException(globalEx, request, response);
        }

        logger.info("=== DeleteProfileStudent doGet() method completed ===");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("=== DeleteProfileStudent doPost() called - delegating to doGet ===");
        logger.info("POST request parameters: " + request.getParameterMap().keySet());
        // Handle POST requests the same way as GET for profile deletion
        doGet(request, response);
    }

    /**
     * Handle GlobalException and redirect with appropriate error messages
     */
    private void handleGlobalException(GlobalException e, HttpServletRequest request,
                                       HttpServletResponse response) throws ServletException, IOException {

        logger.info("=== Handling GlobalException in DeleteProfileStudent servlet ===");
        logger.warning("Error Type: " + e.getErrorType());
        logger.warning("Error Code: " + e.getErrorCode());
        logger.warning("Error Message: " + e.getMessage());

        // Log the error for debugging
        getServletContext().log("Error in DeleteProfileStudent servlet: " + e.getErrorType() +
                " - " + e.getErrorCode() + " - " + e.getMessage(), e);

        // Set error attributes for JSP display
        HttpSession session = request.getSession();
        session.setAttribute("hasError", true);
        session.setAttribute("errorCode", e.getErrorCode());
        session.setAttribute("errorType", e.getErrorType().toString());

        logger.info("Set common error attributes on session");

        // Set specific error messages based on error type
        if (e.isValidationError()) {
            logger.warning("Handling validation error");
            session.setAttribute("errorTitle", "Validation Error");
            session.setAttribute("errorMessage", e.getMessage());
            session.setAttribute("errorDetails", "Please check your input and try again.");
            response.setStatus(400); // Bad Request

        } else if (e.isSecurityError()) {
            logger.warning("Handling security error");
            session.setAttribute("errorTitle", "Security Error");
            session.setAttribute("errorMessage", "Authentication failed");
            session.setAttribute("errorDetails", "Invalid credentials or unauthorized access attempt.");
            response.setStatus(403); // Forbidden

        } else if (e.isResourceNotFound()) {
            logger.warning("Handling resource not found error");
            session.setAttribute("errorTitle", "Profile Not Found");
            session.setAttribute("errorMessage", e.getMessage());
            session.setAttribute("errorDetails", "The student profile you're trying to delete was not found.");
            response.setStatus(404); // Not Found

        } else if (e.isDatabaseError()) {
            logger.severe("Handling database error");
            session.setAttribute("errorTitle", "System Error");
            session.setAttribute("errorMessage", "Unable to delete profile");
            session.setAttribute("errorDetails", "There was a problem processing your request. Please try again later or contact support.");
            response.setStatus(500); // Internal Server Error

        } else {
            logger.severe("Handling unexpected error");
            session.setAttribute("errorTitle", "Unexpected Error");
            session.setAttribute("errorMessage", "An error occurred while deleting your profile");
            session.setAttribute("errorDetails", "Please try again later or contact support if the problem persists.");
            response.setStatus(500); // Internal Server Error
        }

        logger.info("Error attributes set on session with HTTP status: " + response.getStatus());

        // Redirect to error page or back to the form
        logger.info("Redirecting to studenthome.jsp with error state");
        response.sendRedirect("studenthome.jsp");

        logger.info("=== DeleteProfileStudent error handling completed ===");
    }
}
