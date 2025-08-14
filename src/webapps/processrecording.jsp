<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Process Recording</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        input[type="number"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #0056b3;
        }
        button:disabled {
            background-color: #6c757d;
            cursor: not-allowed;
        }
        .loading {
            display: none;
            text-align: center;
            margin-top: 20px;
            color: #007bff;
        }
        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #007bff;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            animation: spin 1s linear infinite;
            margin: 0 auto 10px;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        .error {
            color: #dc3545;
            background-color: #f8d7da;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .success {
            color: #155724;
            background-color: #d4edda;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .nav-links {
            text-align: center;
            margin-top: 20px;
        }
        .nav-links a {
            color: #007bff;
            text-decoration: none;
            margin: 0 15px;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Process Recording</h2>

        <% if(request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if(request.getAttribute("success") != null) { %>
            <div class="success">
                <%= request.getAttribute("success") %>
            </div>
        <% } %>

        <form id="processForm" action="VideoProcessorServlet" method="post">
            <div class="form-group">
                <label for="recordingId">Recording ID:</label>
                <input type="number" id="recordingId" name="recordingId" required
                       placeholder="Enter the recording ID to process" min="1">
            </div>

            <button type="submit" id="processBtn">Process Recording</button>
        </form>

        <div class="loading" id="loadingDiv">
            <div class="spinner"></div>
            <p>Processing recording... This may take a few minutes.</p>
        </div>

        <div class="nav-links">
            <a href="resultpage.jsp">View Results</a>
            <a href="displayresults.jsp">Admin Results</a>
        </div>
    </div>

    <script>
        document.getElementById('processForm').addEventListener('submit', function(e) {
            const recordingId = document.getElementById('recordingId').value;
            if (!recordingId || recordingId <= 0) {
                alert('Please enter a valid recording ID');
                e.preventDefault();
                return;
            }

            // Show loading state
            document.getElementById('processBtn').disabled = true;
            document.getElementById('processBtn').textContent = 'Processing...';
            document.getElementById('loadingDiv').style.display = 'block';
        });
    </script>
</body>
</html>