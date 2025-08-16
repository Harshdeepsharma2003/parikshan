<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String testId = (String) session.getAttribute("testid");
  String noOfQuestionsStr = (String) session.getAttribute("noOfQuestions");

  if (testId == null) {
      response.sendRedirect("managetest.jsp");
      return;
  }

  int noOfQuestions = 0;
  if (noOfQuestionsStr != null) {
      try {
          noOfQuestions = Integer.parseInt(noOfQuestionsStr);
      } catch (NumberFormatException e) {
          noOfQuestions = 5;
      }
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Add MCQ Questions</title>
<link rel="stylesheet" href="css/addquestion.css">
</head>
<body>

<!-- Header / Navbar placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

<div class="container">
  <div class="header">
    <h2>Add MCQ Questions</h2>
    <p><strong>Test ID:</strong> <%= testId %> | <strong>Expected Questions:</strong> <%= noOfQuestions %></p>
  </div>

  <% if (noOfQuestions > 5) { %>
  <div class="alert alert-info">
    <strong>Note:</strong> You specified <%= noOfQuestions %> questions, but the current limit is 5 questions per test.
    You can add up to 5 questions now.

    Questions will be numbered automatically.
  </div>
  <% } %>

  <form action="AddQuestion" method="POST" id="questionsForm">
    <input type="hidden" name="testId" value="<%= testId %>" />
    <input type="hidden" name="expectedQuestions" value="<%= noOfQuestions %>" />

    <!-- Questions will be dynamically generated here -->
    <div id="questionsContainer">
      <!-- Initial question will be added by JavaScript -->
    </div>

    <div class="button-group">
      <button type="button" class="btn btn-primary" id="addQuestionBtn">
        + Add Another Question
      </button>
      <button type="submit" class="btn btn-success" id="submitBtn">
        Save All Questions to Database
      </button>
    </div>

    <div class="progress-info">
      <span id="questionCounter">Showing: 0 questions</span> |
      <span id="maxQuestions">Maximum: 5 questions allowed</span>
    </div>
  </form>
</div>

<!-- Footer placeholder -->
<div id="footer-container"></div>
<script src="js/addquestion.js"></script>
</body>
</html>