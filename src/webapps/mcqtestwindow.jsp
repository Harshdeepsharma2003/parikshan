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
      response.sendRedirect("selecttest.jsp");
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
      out.println("<script>alert('No questions found for this test!'); window.location.href='selecttest.jsp';</script>");
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
<style>
/* Essential CSS for immediate functionality */
.recording-dot {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    display: inline-block;
    margin-right: 8px;
}

.recording-dot.active {
    background-color: #dc3545;
    animation: pulse 1s infinite;
}

.recording-dot.inactive {
    background-color: #6c757d;
}

.timer.warning {
    color: #ffc107;
}

.timer.critical {
    color: #dc3545;
    animation: blink 1s infinite;
}

@keyframes pulse {
    0% { opacity: 1; }
    50% { opacity: 0.5; }
    100% { opacity: 1; }
}

@keyframes blink {
    0%, 50% { opacity: 1; }
    51%, 100% { opacity: 0.3; }
}

.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
}

.loading-content {
    background: white;
    padding: 30px;
    border-radius: 10px;
    text-align: center;
}

.spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #3498db;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    animation: spin 2s linear infinite;
    margin: 0 auto 20px;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>
</head>
<body>

<!-- Loading Overlay -->
<div class="loading-overlay" id="loadingOverlay" style="display: none;">
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
        <div class="recording-dot inactive" id="recordingDot"></div>
        <span id="recordingStatus">Starting camera...</span>
      </div>
      <video id="videoPreview" class="video-preview" autoplay muted playsinline style="width: 320px; height: 240px; border: 1px solid #ddd; border-radius: 8px;"></video>
    </div>
    <div class="recording-controls">
      <button type="button" class="btn btn-warning" id="testCameraBtn" onclick="testCameraMic()">🎥 Test Camera</button>
      <button type="button" class="btn btn-info" id="retryRecordingBtn" onclick="initializeRecording()" style="display: none;">🔄 Retry Recording</button>
      <span id="recordingDuration">00:00</span>
    </div>
  </div>

  <!-- Timer and Progress -->
  <div class="timer-section">
    <div class="timer" id="timer">30:00</div>
    <div class="progress-bar">
      <div class="progress-fill" id="progressFill" style="width: 0%; height: 100%; background: #007bff; transition: width 0.3s;"></div>
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

          String optionA = "", optionB = "", optionC = "", optionD = "";
          for (String part : parts) {
              if (part.startsWith("A:")) optionA = part.substring(2);
              else if (part.startsWith("B:")) optionB = part.substring(2);
              else if (part.startsWith("C:")) optionC = part.substring(2);
              else if (part.startsWith("D:")) optionD = part.substring(2);
          }
      %>

      <div class="question-card <%= i == 0 ? "active" : "" %>" data-question="<%= i + 1 %>" style="<%= i == 0 ? "display: block;" : "display: none;" %>">
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
      <div class="test-summary" id="testSummary" style="display: none;">
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
        <button type="button" class="btn btn-secondary" id="prevBtn" onclick="previousQuestion()" disabled>← Previous</button>
        <button type="button" class="btn btn-primary" id="nextBtn" onclick="nextQuestion()">Next →</button>
      </div>
      <div class="question-nav">
        <button type="button" class="btn btn-success" id="reviewBtn" onclick="showSummary()" style="display: none;">📋 Review Answers</button>
        <button type="submit" class="btn btn-success" id="submitBtn" style="display: none;">✓ Submit Test</button>
      </div>
    </div>
  </form>
</div>

<!-- CRITICAL: Embed JSP variables into JavaScript BEFORE loading external script -->
<script type="text/javascript">
    // Global variables - MUST be declared before external script loads
    var totalQuestions = <%= questions.size() %>;
    var userid = '<%= userid %>';
    var testId = '<%= testId %>';

    // Ensure immediate initialization
    console.log("JSP Variables initialized:", {
        totalQuestions: totalQuestions,
        userid: userid,
        testId: testId
    });

    // IMMEDIATE initialization - don't wait for DOMContentLoaded
    window.MCQ_CONFIG = {
        totalQuestions: totalQuestions,
        userid: userid,
        testId: testId,
        autoStart: true
    };

    // Force start as soon as possible
    document.addEventListener('DOMContentLoaded', function() {
        console.log("DOM Ready - Force starting all systems");

        // Update status immediately
        const recordingStatus = document.getElementById('recordingStatus');
        if (recordingStatus) {
            recordingStatus.textContent = "Requesting camera access...";
        }

        // Force immediate start
        if (window.startTestSystems) {
            window.startTestSystems();
        }
    });

    // Also try to start immediately if DOM is already ready
    if (document.readyState === 'loading') {
        // Document still loading
        console.log("Document still loading, will wait for DOMContentLoaded");
    } else {
        // Document already loaded
        console.log("Document already loaded, starting immediately");
        setTimeout(function() {
            if (window.startTestSystems) {
                window.startTestSystems();
            }
        }, 100);
    }
</script>

<!-- Load external JavaScript file AFTER variables are set -->
<script src="js/mcqtestwindow.js"></script>

<!-- Fallback initialization script -->
<script type="text/javascript">
    // Fallback - ensure everything starts even if external script fails
    setTimeout(function() {
        console.log("Fallback initialization check");

        // Check if timer is running
        const timerElement = document.getElementById('timer');
        if (timerElement && timerElement.textContent === '30:00') {
            console.log("Timer not started - forcing manual start");
            if (window.forceStartTimer) {
                window.forceStartTimer();
            }
        }

        // Check if camera is initializing
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

    // Additional safety net
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