<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Submission Error - Parikshan</title>
     <link rel="stylesheet" href="css/testsumissionerror.css">

</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>

        <div class="error-title">
            ${errorTitle != null ? errorTitle : "Test Submission Error"}
        </div>

        <div class="error-message">
            ${errorMessage != null ? errorMessage : "There was an error processing your test submission. Please try again."}
        </div>

        <div class="support-info">
            <strong>What you can do:</strong>
            <ul style="margin: 10px 0; padding-left: 20px;">
                <li>Try taking the test again</li>
                <li>Check your internet connection</li>
                <li>Contact technical support if the problem persists</li>
            </ul>
        </div>

        <div class="button-group">
            <% if (request.getAttribute("testId") != null) { %>
                <a href="TakeTest?testId=${testId}" class="btn btn-primary">Retake Test</a>
            <% } %>
            <a href="StudentDashboard" class="btn btn-secondary">Dashboard</a>
            <a href="landingpage.jsp" class="btn btn-secondary">Home</a>
        </div>

        <% if (request.getAttribute("technicalDetails") != null) { %>
        <div class="technical-details">
            <strong>Technical Details:</strong><br>
            ${technicalDetails}
            <% if (request.getAttribute("testId") != null) { %>
                <br><strong>Test ID:</strong> ${testId}
            <% } %>
            <% if (request.getAttribute("studentId") != null) { %>
                <br><strong>Student ID:</strong> ${studentId}
            <% } %>
        </div>
        <% } %>
    </div>
</body>
</html>
