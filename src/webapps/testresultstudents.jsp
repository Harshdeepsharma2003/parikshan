<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.parikshan.model.Question" %>
<%@ page import="com.yash.parikshan.model.TestResult" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Results - Parikshan</title>
     <link rel="stylesheet" href="css/testresultstudents.css">

</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <h1>🎉 Test Results</h1>
        </div>

        <!-- Warning message if save failed -->
        <c:if test="${not empty saveWarning}">
            <div class="warning-message">
                <strong>⚠️ Warning:</strong> ${saveWarning}
            </div>
        </c:if>

        <!-- Score Card -->
        <div class="score-card">
            <h2>🏆 Your Score</h2>
            <div class="score-number">${formattedPercentage}%</div>
            <p style="font-size: 18px; margin: 10px 0;">
                ${correctAnswers} out of ${totalQuestions} questions correct
            </p>
            <div class="grade">Grade: ${grade}</div>
        </div>

        <!-- Statistics -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">${totalQuestions}</div>
                <div class="stat-label">Total Questions</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${attemptedQuestions}</div>
                <div class="stat-label">Attempted</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${correctAnswers}</div>
                <div class="stat-label">✅ Correct</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${incorrectAnswers}</div>
                <div class="stat-label">❌ Incorrect</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${unattemptedQuestions}</div>
                <div class="stat-label">⚠️ Unattempted</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${formattedTime}</div>
                <div class="stat-label">⏱️ Time Taken</div>
            </div>
        </div>

        <!-- Answer Review -->
        <div class="answer-review">
            <h3>📝 Answer Review</h3>

            <c:forEach var="question" items="${questions}" varStatus="questionStatus">
                <%
                    // Get the question number correctly
                    javax.servlet.jsp.jstl.core.LoopTagStatus status =
                        (javax.servlet.jsp.jstl.core.LoopTagStatus) pageContext.getAttribute("questionStatus");
                    int questionNumber = status.getIndex() + 1;
                    String studentAnswer = request.getParameter("question_" + questionNumber);

                    // Get the current question
                    Question currentQuestion = (Question) pageContext.getAttribute("question");
                    String answerText = currentQuestion.getAnswerText();

                    if (answerText == null) answerText = "";

                    String[] parts = answerText.split("\\|");
                    String correctAnswer = "";
                    String optionA = "", optionB = "", optionC = "", optionD = "";

                    // Parse answer options
                    for (String part : parts) {
                        if (part.startsWith("A:")) optionA = part.substring(2).trim();
                        else if (part.startsWith("B:")) optionB = part.substring(2).trim();
                        else if (part.startsWith("C:")) optionC = part.substring(2).trim();
                        else if (part.startsWith("D:")) optionD = part.substring(2).trim();
                        else if (part.startsWith("CORRECT:")) correctAnswer = part.substring(8).trim();
                    }

                    // Determine status
                    String cssClass = "";
                    String statusIcon = "";
                    String statusText = "";

                    if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
                        cssClass = "unattempted";
                        statusIcon = "⚠️";
                        statusText = "Not attempted";
                    } else {
                        boolean isCorrect = correctAnswer.equalsIgnoreCase(studentAnswer.trim());
                        cssClass = isCorrect ? "correct" : "incorrect";
                        statusIcon = isCorrect ? "✅" : "❌";
                        statusText = isCorrect ? "Correct" : "Incorrect";
                    }

                    // Set attributes for use in JSP
                    pageContext.setAttribute("studentAnswer", studentAnswer);
                    pageContext.setAttribute("correctAnswer", correctAnswer);
                    pageContext.setAttribute("optionA", optionA);
                    pageContext.setAttribute("optionB", optionB);
                    pageContext.setAttribute("optionC", optionC);
                    pageContext.setAttribute("optionD", optionD);
                    pageContext.setAttribute("cssClass", cssClass);
                    pageContext.setAttribute("statusIcon", statusIcon);
                    pageContext.setAttribute("statusText", statusText);
                    pageContext.setAttribute("questionNumber", questionNumber);
                %>

                <div class="question-item ${cssClass}">
                    <div class="question-header">
                        <span class="question-number">Q${questionNumber}</span>
                        <span class="status-icon">${statusIcon}</span>
                    </div>

                    <div class="question-content">
                        ${question.content}
                    </div>

                    <c:if test="${not empty optionA or not empty optionB or not empty optionC or not empty optionD}">
                        <div class="options">
                            <c:if test="${not empty optionA}">
                                <p><strong>A.</strong> ${optionA}</p>
                            </c:if>
                            <c:if test="${not empty optionB}">
                                <p><strong>B.</strong> ${optionB}</p>
                            </c:if>
                            <c:if test="${not empty optionC}">
                                <p><strong>C.</strong> ${optionC}</p>
                            </c:if>
                            <c:if test="${not empty optionD}">
                                <p><strong>D.</strong> ${optionD}</p>
                            </c:if>
                        </div>
                    </c:if>

                    <div class="answer-section">
                        <p class="your-answer">
                            <strong>Your Answer:</strong>
                            <c:choose>
                                <c:when test="${not empty studentAnswer}">
                                    ${studentAnswer} (${statusText})
                                </c:when>
                                <c:otherwise>
                                    ${statusText}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p class="correct-answer">
                            <strong>Correct Answer:</strong>
                            <c:choose>
                                <c:when test="${not empty correctAnswer}">
                                    ${correctAnswer}
                                </c:when>
                                <c:otherwise>
                                    Not specified
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Action Buttons -->
        <div class="actions">
            <a href="testformats.jsp" class="btn">📋 Take Another Test</a>
            <a href="homestudent.jsp" class="btn btn-secondary">🏠 Back to Dashboard</a>
            </div>
    </div>
  <script src="js/testresultstudent.js"></script>
</body>
</html>