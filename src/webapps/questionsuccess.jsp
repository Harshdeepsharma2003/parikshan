<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.parikshan.model.Question" %>
<%
    String testId = (String) request.getAttribute("testId");
    Integer questionsAdded = (Integer) request.getAttribute("questionsAdded");
    Integer totalRecords = (Integer) request.getAttribute("totalRecords");
    String successMessage = (String) request.getAttribute("successMessage");
    @SuppressWarnings("unchecked")
    List<Question> questions = (List<Question>) request.getAttribute("questions");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Questions Added Successfully</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
        .question-summary {
            border: 1px solid #ddd;
            margin: 10px 0;
            padding: 15px;
            border-radius: 5px;
            background-color: #f9f9f9;
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
        .btn-success {
            background-color: #28a745;
        }
        .btn:hover {
            opacity: 0.9;
            transform: translateY(-1px);
            transition: all 0.2s;
        }
        h2 {
            color: #28a745;
        }
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin: 20px 0;
        }
        .stat-item {
            background: #e8f5e8;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            border-left: 4px solid #28a745;
        }
        .correct-answer {
            color: #28a745;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>✅ <%= successMessage %></h2>

        <div class="success">
            <div class="stats">
                <div class="stat-item">
                    <strong>Test ID</strong><br>
                    <%= testId %>
                </div>
                <div class="stat-item">
                    <strong>Questions Added</strong><br>
                    <%= questionsAdded %>
                </div>
                <div class="stat-item">
                    <strong>Database Records</strong><br>
                    <%= totalRecords %>
                </div>
            </div>
        </div>

        <h3>Questions Summary:</h3>
        <%
            if (questions != null && !questions.isEmpty()) {
                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);
                    String answerText = q.getAnswerText();
                    String[] parts = answerText.split("\\|");
        %>
        <div class="question-summary">
            <h4>Question <%= (i + 1) %></h4>
            <p><strong>ID:</strong> <%= q.getQuestionId() %></p>
            <p><strong>Content:</strong> <%= q.getContent() %></p>

            <p><strong>Options & Answer:</strong></p>
            <ul>
                <%
                    for (String part : parts) {
                        if (part.startsWith("CORRECT:")) {
                %>
                <li class="correct-answer">✓ Correct Answer: <%= part.substring(8) %></li>
                <%
                        } else {
                %>
                <li><%= part %></li>
                <%
                        }
                    }
                %>
            </ul>
        </div>
        <%
                }
            }
        %>

        <div style="text-align: center; margin-top: 30px;">
            <a href="addquestion.jsp" class="btn btn-primary">Add More Questions</a>
            <a href="managetest.jsp" class="btn btn-success">Back to Manage Tests</a>
        </div>
    </div>
</body>
</html>