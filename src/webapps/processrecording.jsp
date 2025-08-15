<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Process Recording - Parikshan</title>
    <link rel="stylesheet" href="css/processrecording.css">

</head>
<body>
    <div class="container">
        <h1>Process Recording</h1>

        <!-- Error Message -->
        <% if(request.getAttribute("error") != null) { %>
            <div class="error-message">
                <strong>Error:</strong> <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- Success Message -->
        <% if(request.getAttribute("success") != null) { %>
            <div class="success-message">
                <strong>Success:</strong> <%= request.getAttribute("success") %>
            </div>
        <% } %>

        <!-- Process Form -->
        <form id="processForm" action="VideoProcessorServlet" method="post">
            <div class="form-group">
                <label for="recordingId">Recording ID:</label>
                <input type="number" id="recordingId" name="recordingId"
                       placeholder="Enter recording ID" min="1" required>
            </div>

            <button type="submit" id="processBtn" class="btn-primary">
                Process Recording
            </button>
        </form>

        <!-- Loading Section -->
        <div class="loading" id="loadingDiv">
            <div class="spinner"></div>
            <p>Processing recording... Please wait.</p>
        </div>

        <!-- Navigation Links -->
        <div class="nav-links">

            <a href="adminhome.jsp" class="btn">Back to Dashboard</a>
        </div>
    </div>

    <script src="js/processrecording.js"></script>
</body>
</html>
