package com.yash.parikshan.controller;

import com.yash.parikshan.exceptions.GlobalException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Central error handler servlet to process all application errors
 * before forwarding to appropriate JSP views.
 */
public class ErrorHandler extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Standard servlet error attributes
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

        // Basic normalization
        if (requestUri == null) requestUri = "Unknown";
        if (servletName == null) servletName = "Unknown";
        if (statusCode == null) statusCode = 500;

        String errorType = "GENERAL_ERROR";

        // Handle GlobalException with built-in error types
        if (throwable instanceof GlobalException) {
            GlobalException globalEx = (GlobalException) throwable;

            switch (globalEx.getErrorType()) {
                case RESOURCE_NOT_FOUND:
                    statusCode = 404;
                    errorType = "NOT_FOUND";
                    break;
                case VALIDATION:
                    statusCode = 400;
                    errorType = "VALIDATION_ERROR";
                    break;
                case SECURITY:
                    statusCode = 403;
                    errorType = "SECURITY_ERROR";
                    break;
                case NETWORK:
                    if ("CONNECTION_TIMEOUT".equals(globalEx.getErrorCode())) {
                        statusCode = 504;
                    } else if ("EXTERNAL_SERVICE_ERROR".equals(globalEx.getErrorCode())) {
                        statusCode = 502;
                    } else {
                        statusCode = 503;
                    }
                    errorType = "NETWORK_ERROR";
                    break;
                case DATABASE:
                    if ("DB_CONNECTION_ERROR".equals(globalEx.getErrorCode())) {
                        statusCode = 503;
                    } else if ("DATA_INTEGRITY_VIOLATION".equals(globalEx.getErrorCode())) {
                        statusCode = 409;
                    } else {
                        statusCode = 500;
                    }
                    errorType = "DATABASE_ERROR";
                    break;
                default:
                    statusCode = 500;
                    errorType = "GENERAL_ERROR";
            }
        }

        // Log the error
        logError(throwable, errorType, requestUri);

        // Set response status
        response.setStatus(statusCode);

        // Set attributes for JSP
        request.setAttribute("app.statusCode", statusCode);
        request.setAttribute("app.errorType", errorType);
        request.setAttribute("app.requestUri", requestUri);
        request.setAttribute("app.servletName", servletName);
        request.setAttribute("app.exception", throwable);

        // Forward to appropriate view
        String view = determineView(statusCode);
        request.getRequestDispatcher(view).forward(request, response);
    }

    private String determineView(int statusCode) {
        if (statusCode == 404) {
            return "/404.jsp";
        } else if (statusCode >= 400 && statusCode < 500) {
            return "/client-error.jsp";
        } else {
            return "/error.jsp";
        }
    }

    private void logError(Throwable throwable, String errorType, String requestUri) {
        if (throwable instanceof GlobalException) {
            GlobalException globalEx = (GlobalException) throwable;
            if (globalEx.isNetworkError() || globalEx.isDatabaseError()) {
                getServletContext().log("ERROR [" + errorType + "] at " + requestUri, throwable);
            } else if (globalEx.isValidationError() || globalEx.isSecurityError()) {
                getServletContext().log("WARN [" + errorType + "] at " + requestUri + ": " + throwable.getMessage());
            } else {
                getServletContext().log("ERROR [" + errorType + "] at " + requestUri, throwable);
            }
        } else {
            getServletContext().log("ERROR [" + errorType + "] at " + requestUri, throwable);
        }
    }
}
