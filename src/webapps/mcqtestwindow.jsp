<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.yash.parikshan.model.Question" %>
<%@ page import="com.yash.parikshan.service.QuestionService" %>
<%@ page import="com.yash.parikshan.serviceimpl.QuestionServiceImpl" %>

<%
  String testId = request.getParameter("testId");
  if (testId == null)
  {
      testId = (String) session.getAttribute("testid");
  }

  if (testId == null) {
      response.sendRedirect("selecttest.jsp");
      return;
  }

  // Get questions for this test
  QuestionService questionService = new QuestionServiceImpl();
  List<Question> questions = new ArrayList<>();

  try {
      questions = questionService.getQuestionsByTestId(testId);
      System.out.println("JSP DEBUG: Retrieved " + questions.size() + " questions for testId: " + testId);
  } catch (Exception e) {
      e.printStackTrace();
  }

  if (questions.isEmpty()) {
      out.println("<script>alert('No questions found for this test!'); window.location.href='selecttest.jsp';</script>");
      return;
  }

  // Get user ID (student or temp user)
  String userid = "";
  if (session.getAttribute("studentid") != null) {
      userid = (String) session.getAttribute("studentid");
  } else if (session.getAttribute("tempuserid") != null) {
      userid = (String) session.getAttribute("tempuserid");
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MCQ Test - Test ID: <%= testId %></title>
<link rel="stylesheet" href="css/mcqtestwindow.css">


</head>
<body>

<!-- Loading Overlay -->
<div class="loading-overlay" id="loadingOverlay">
  <div class="loading-content">
    <div class="spinner"></div>
    <h3>Time's Up!</h3>
    <p>Auto-submitting your test...</p>
  </div>
</div>

<div class="test-container">
  <!-- Test Header -->
  <div class="test-header">
    <h1>MCQ Test</h1>
    <div class="test-info">Test ID: <%= testId %> | Total Questions: <%= questions.size() %></div>
  </div>

  <!-- Recording Section -->
  <div class="recording-section">
    <div class="recording-status">
      <div class="recording-indicator">
        <div class="recording-dot" id="recordingDot"></div>
        <span id="recordingStatus">Initializing camera...</span>
      </div>
      <video id="videoPreview" class="video-preview" autoplay muted playsinline></video>
    </div>
    <div class="recording-controls">
      <button type="button" class="btn btn-warning" id="testCameraBtn" onclick="testCameraMic()">🎥 Test Camera</button>
      <span id="recordingDuration">00:00</span>
    </div>
  </div>

  <!-- Timer and Progress -->
  <div class="timer-section">
    <div class="timer" id="timer">30:00</div>
    <div class="progress-bar">
      <div class="progress-fill" id="progressFill"></div>
    </div>
    <div class="question-counter" id="questionCounter">Question 1 of <%= questions.size() %></div>
  </div>

  <!-- Question Navigation Panel -->
  <div class="question-navigation">
      <div class="nav-header">Questions</div>
      <div class="question-numbers" id="questionNumbers">
          <% for (int i = 1; i <= questions.size(); i++) { %>
              <button type="button" class="question-num-btn" data-question="<%= i %>" onclick="goToQuestion(<%= i %>)">
                  <%= i %>
              </button>
          <% } %>
      </div>
  </div>


  <!-- Questions Container -->
  <form id="testForm" action="SubmitTest" method="POST">
    <input type="hidden" name="testId" value="<%= testId %>" />
    <input type="hidden" name="totalQuestions" value="<%= questions.size() %>" />
    <input type="hidden" name="userid" value="<%= userid %>" />

    <div class="questions-container">
      <%
      for (int i = 0; i < questions.size(); i++) {
          Question q = questions.get(i);
          String answerText = q.getAnswerText();
          String[] parts = answerText.split("\\|");

          // Parse options
          String optionA = "", optionB = "", optionC = "", optionD = "";
          for (String part : parts) {
              if (part.startsWith("A:")) optionA = part.substring(2);
              else if (part.startsWith("B:")) optionB = part.substring(2);
              else if (part.startsWith("C:")) optionC = part.substring(2);
              else if (part.startsWith("D:")) optionD = part.substring(2);
          }
      %>

      <div class="question-card <%= i == 0 ? "active" : "" %>" data-question="<%= i + 1 %>">
        <div class="question-number">Question <%= i + 1 %> of <%= questions.size() %></div>
        <div class="question-text"><%= q.getContent() %></div>

        <div class="options-grid">
          <div class="option" onclick="selectOption(this, '<%= i + 1 %>', 'A')">
            <input type="radio" name="question_<%= i + 1 %>" value="A" class="option-radio">
            <div class="option-text">A. <%= optionA %></div>
          </div>

          <div class="option" onclick="selectOption(this, '<%= i + 1 %>', 'B')">
            <input type="radio" name="question_<%= i + 1 %>" value="B" class="option-radio">
            <div class="option-text">B. <%= optionB %></div>
          </div>

          <div class="option" onclick="selectOption(this, '<%= i + 1 %>', 'C')">
            <input type="radio" name="question_<%= i + 1 %>" value="C" class="option-radio">
            <div class="option-text">C. <%= optionC %></div>
          </div>

          <div class="option" onclick="selectOption(this, '<%= i + 1 %>', 'D')">
            <input type="radio" name="question_<%= i + 1 %>" value="D" class="option-radio">
            <div class="option-text">D. <%= optionD %></div>
          </div>
        </div>
      </div>

      <% } %>

      <!-- Test Summary -->
      <div class="test-summary" id="testSummary">
        <div class="summary-title">Test Summary</div>
        <div class="summary-stats">
          <div class="stat-card">
            <div class="stat-number" id="answeredCount">0</div>
            <div class="stat-label">Answered</div>
          </div>
          <div class="stat-card">
            <div class="stat-number" id="unansweredCount"><%= questions.size() %></div>
            <div class="stat-label">Unanswered</div>
          </div>
          <div class="stat-card">
            <div class="stat-number" id="totalTime">00:00</div>
            <div class="stat-label">Time Taken</div>
          </div>
        </div>
        <p style="margin: 30px 0; color: #6c757d; font-size: 16px;">
          Review your answers before submitting. Once submitted, you cannot change your answers.
        </p>
      </div>
    </div>

    <!-- Navigation -->
    <div class="navigation-section">
      <div class="question-nav">
        <button type="button" class="btn btn-secondary" id="prevBtn" onclick="previousQuestion()">← Previous</button>
        <button type="button" class="btn btn-primary" id="nextBtn" onclick="nextQuestion()">Next →</button>
      </div>

      <div class="question-nav">
        <button type="button" class="btn btn-success" id="reviewBtn" onclick="showSummary()" style="display: none;">📋 Review Answers</button>
        <button type="submit" class="btn btn-success" id="submitBtn" style="display: none;">✓ Submit Test</button>
      </div>
    </div>
  </form>
</div>
 <script src="js/mcqtestwindow.js"></script>
</body>
</html>
