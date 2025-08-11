<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String errorDetails = (String) request.getAttribute("errorDetails");
    Exception exception = (Exception) request.getAttribute("exception");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Question Addition Failed</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .error {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
        .btn {
            padding: 12px 24px;
            margin: 10px 5px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-weight: bold;
            color: white;
        }
        .btn-primary {
            background-color: #007bff;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn:hover {
            opacity: 0.9;
            transform: translateY(-1px);
            transition: all 0.2s;
        }
        h2 {
            color: #dc3545;
        }
        .error-icon {
            font-size: 48px;
            text-align: center;
            margin-bottom: 20px;
        }
        ul {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 5px;
            padding: 15px;
            margin: 15px 0;
        }
        li {
            margin: 5px 0;
        }
        .debug-info {
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 5px;
            padding: 10px;
            margin: 15px 0;
            font-family: monospace;
            font-size: 12px;
            max-height: 200px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-icon">❌</div>

        <h2><%= errorMessage != null ? errorMessage : "An Error Occurred" %></h2>

        <div class="error">
            <% if (errorDetails != null) { %>
                <% if (errorDetails.contains("Please make sure to:")) { %>
                    <p>No valid questions were found. Please ensure you:</p>
                    <ul>
                        <li>Enter question text</li>
                        <li>Fill all 4 options (A, B, C, D)</li>
                        <li>Select the correct answer</li>
                    </ul>
                <% } else { %>
                    <p><strong>Details:</strong> <%= errorDetails %></p>
                <% } %>
            <% } %>

            <% if (exception != null) { %>
                <div class="debug-info">
                    <strong>Technical Details:</strong><br>
                    <%= exception.getClass().getSimpleName() %>: <%= exception.getMessage() %>
                </div>
            <% } %>
        </div>

        <div style="text-align: center; margin-top: 30px;">
            <a href="addquestion.jsp" class="btn btn-primary">Try Again</a>
            <a href="managetest.jsp" class="btn btn-secondary">Back to Manage Tests</a>
        </div>

        <div style="text-align: center; margin-top: 20px;">
            <small style="color: #6c757d;">
                If this problem persists, please contact the system administrator.
            </small>
        </div>
    </div>
</body>
</html>