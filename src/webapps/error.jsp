catch all unhandled exceptions and display a user-friendly error message.

<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="css/error.css">
</head>
<body>
    <div class="error-container">
        <h1>Something went wrong</h1>
        <p>
            <strong>Error Details:</strong><br>
            <%= exception != null ? exception.getMessage() : "Unknown error" %>
        </p>
        <p>
            <a href="signin.jsp">Back to Home</a>
        </p>
    </div>
</body>
</html>
