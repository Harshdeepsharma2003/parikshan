<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.yash.parikshan.model.Question" %>
<%@ page import="com.yash.parikshan.service.QuestionService" %>
<%@ page import="com.yash.parikshan.serviceimpl.QuestionServiceImpl" %>

<%
  String testId = request.getParameter("testId");
  if (testId == null) {
      testId = (String) session.getAttribute("testid");
  }

  if (testId == null) {
      response.sendRedirect("homestudent.jsp");
      return;
  }

  QuestionService questionService = new QuestionServiceImpl();
  List<Question> questions = new ArrayList<>();

  try {
      questions = questionService.getQuestionsByTestId(testId);
      System.out.println("JSP DEBUG: Retrieved " + questions.size() + " questions for testId: " + testId);
  } catch (Exception e) {
      e.printStackTrace();
  }

  if (questions.isEmpty()) {
      out.println("<script>alert('No questions found for this test!'); window.location.href='homestudent.jsp';</script>");
      return;
  }

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

<!-- Main Container -->
<div class="container">

  <!-- Header -->
  <header class="header">
    <h1>MCQ Test</h1>
    <p>Test ID: <%= testId %> | Total Questions: <%= questions.size() %></p>
  </header>

  <!-- Top Controls -->
  <div class="top-controls">

    <!-- Timer Section -->
    <div class="timer-section">
      <div class="timer" id="timer">10:00</div>
      <div class="progress-bar">
        <div class="progress-fill" id="progressFill"></div>
      </div>
    </div>

    <!-- Recording Section -->
    <div class="recording-section">
      <div class="recording-status">
        <div class="recording-indicator">
          <span class="recording-dot inactive" id="recordingDot"></span>
          <span id="recordingStatus">Starting camera...</span>
        </div>
        <span class="recording-duration" id="recordingDuration">00:00</span>
      </div>
      <video id="videoPreview" autoplay muted playsinline></video>
      <div class="recording-controls">
        <button type="button" class="btn btn-test" id="testCameraBtn" onclick="testCameraMic()">Test Camera</button>
        <button type="button" class="btn btn-retry" id="retryRecordingBtn" onclick="initializeRecording()">Retry Recording</button>
      </div>
    </div>
  </div>

  <!-- Question Navigation -->
  <div class="question-nav-panel">
    <h4>Questions</h4>
    <div class="question-numbers" id="questionNumbers">
      <% for (int i = 1; i <= questions.size(); i++) { %>
        <button type="button" class="question-num-btn" data-question="<%= i %>" onclick="goToQuestion(<%= i %>)">
          <%= i %>
        </button>
      <% } %>
    </div>
  </div>

  <!-- Test Form -->
  <form id="testForm" action="SubmitTest" method="POST">
    <input type="hidden" name="testId" value="<%= testId %>" />
    <input type="hidden" name="totalQuestions" value="<%= questions.size() %>" />
    <input type="hidden" name="userid" value="<%= userid %>" />

    <!-- Question Counter -->
    <div class="question-counter" id="questionCounter">Question 1 of <%= questions.size() %></div>

    <!-- Questions Container -->
    <div class="questions-container">
      <%
      for (int i = 0; i < questions.size(); i++) {
          Question q = questions.get(i);
          String answerText = q.getAnswerText();
          String[] parts = answerText.split("\\|");

          String optionA = "", optionB = "", optionC = "", optionD = "";
          for (String part : parts) {
              if (part.startsWith("A:")) optionA = part.substring(2);
              else if (part.startsWith("B:")) optionB = part.substring(2);
              else if (part.startsWith("C:")) optionC = part.substring(2);
              else if (part.startsWith("D:")) optionD = part.substring(2);
          }
      %>

      <div class="question-card <%= i == 0 ? "active" : "" %>" data-question="<%= i + 1 %>">
        <div class="question-text">
          <strong>Q<%= i + 1 %>.</strong> <%= q.getContent() %>
        </div>

        <div class="options">
          <label class="option" onclick="selectOption(this, '<%= i + 1 %>', 'A')">
            <input type="radio" name="question_<%= i + 1 %>" value="A">
            <span class="option-text">A. <%= optionA %></span>
          </label>
          <label class="option" onclick="selectOption(this, '<%= i + 1 %>', 'B')">
            <input type="radio" name="question_<%= i + 1 %>" value="B">
            <span class="option-text">B. <%= optionB %></span>
          </label>
          <label class="option" onclick="selectOption(this, '<%= i + 1 %>', 'C')">
            <input type="radio" name="question_<%= i + 1 %>" value="C">
            <span class="option-text">C. <%= optionC %></span>
          </label>
          <label class="option" onclick="selectOption(this, '<%= i + 1 %>', 'D')">
            <input type="radio" name="question_<%= i + 1 %>" value="D">
            <span class="option-text">D. <%= optionD %></span>
          </label>
        </div>
      </div>
      <% } %>

      <!-- Test Summary -->
      <div class="test-summary" id="testSummary">
        <h3>Test Summary</h3>
        <div class="summary-stats">
          <div class="stat">
            <span class="stat-number" id="answeredCount">0</span>
            <span class="stat-label">Answered</span>
          </div>
          <div class="stat">
            <span class="stat-number" id="unansweredCount"><%= questions.size() %></span>
            <span class="stat-label">Unanswered</span>
          </div>
          <div class="stat">
            <span class="stat-number" id="totalTime">00:00</span>
            <span class="stat-label">Time Taken</span>
          </div>
        </div>
        <p>Review your answers before submitting. Once submitted, you cannot change your answers.</p>
      </div>
    </div>

    <!-- Navigation Buttons -->
    <div class="navigation">
      <div class="nav-buttons">
        <button type="button" class="btn btn-secondary" id="prevBtn" onclick="previousQuestion()" disabled>← Previous</button>
        <button type="button" class="btn btn-primary" id="nextBtn" onclick="nextQuestion()">Next →</button>
      </div>
      <div class="nav-buttons">
        <button type="button" class="btn btn-review" id="reviewBtn" onclick="showSummary()">Review Answers</button>
        <button type="submit" class="btn btn-submit" id="submitBtn">Submit Test</button>
      </div>
    </div>
  </form>
</div>

<!-- JavaScript Configuration -->
<script type="text/javascript">
    // Global variables
    var totalQuestions = <%= questions.size() %>;
    var userid = '<%= userid %>';
    var testId = '<%= testId %>';

    console.log("JSP Variables initialized:", {
        totalQuestions: totalQuestions,
        userid: userid,
        testId: testId
    });

    // Configuration object
    window.MCQ_CONFIG = {
        totalQuestions: totalQuestions,
        userid: userid,
        testId: testId,
        autoStart: true
    };

    // Initialize when DOM is ready
    document.addEventListener('DOMContentLoaded', function() {
        console.log("DOM Ready - Starting test systems");

        const recordingStatus = document.getElementById('recordingStatus');
        if (recordingStatus) {
            recordingStatus.textContent = "Requesting camera access...";
        }

        if (window.startTestSystems) {
            window.startTestSystems();
        }
    });

    // Fallback initialization
    if (document.readyState === 'loading') {
        console.log("Document still loading, waiting for DOMContentLoaded");
    } else {
        console.log("Document already loaded, starting immediately");
        setTimeout(function() {
            if (window.startTestSystems) {
                window.startTestSystems();
            }
        }, 100);
    }
</script>

<!-- External JavaScript -->
<script src="js/mcqtestwindow.js"></script>

<!-- Fallback Scripts -->
<script type="text/javascript">
    // Fallback initialization check
    setTimeout(function() {
        console.log("Fallback initialization check");

        const timerElement = document.getElementById('timer');
        if (timerElement && timerElement.textContent === '30:00') {
            console.log("Timer not started - forcing manual start");
            if (window.forceStartTimer) {
                window.forceStartTimer();
            }
        }

        const recordingStatus = document.getElementById('recordingStatus');
        const videoPreview = document.getElementById('videoPreview');
        if (recordingStatus && recordingStatus.textContent.includes('Starting camera') &&
            (!videoPreview.srcObject || !videoPreview.srcObject.active)) {
            console.log("Camera not started - forcing manual start");
            if (window.forceStartRecording) {
                window.forceStartRecording();
            }
        }
    }, 2000);

    // Safety net
    setTimeout(function() {
        console.log("Final safety check");
        const timer = document.getElementById('timer');
        const status = document.getElementById('recordingStatus');

        if (timer && timer.textContent === '30:00') {
            alert("Timer failed to start automatically. Please refresh the page or contact support.");
        }

        if (status && status.textContent.includes('Starting camera')) {
            console.log("Camera initialization taking too long - showing retry button");
            const retryBtn = document.getElementById('retryRecordingBtn');
            if (retryBtn) {
                retryBtn.style.display = 'inline-block';
            }
        }
    }, 10000);
</script>

</body>
</html>