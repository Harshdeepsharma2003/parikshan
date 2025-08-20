<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.parikshan.model.TestResult" %>
<%@ page import="com.yash.parikshan.model.Test" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
// Debug session information at the top of JSP
System.out.println("=== JSP SESSION DEBUG ===");
HttpSession currentSession = request.getSession(false);
if (currentSession != null) {
    System.out.println("JSP: Session exists: " + currentSession.getId());
    System.out.println("JSP: Session studentid: " + currentSession.getAttribute("studentid"));
    System.out.println("JSP: Session userType: " + currentSession.getAttribute("userType"));
} else {
    System.out.println("JSP: No session found!");
}

// Check if user is logged in - redirect if not
if (currentSession == null || currentSession.getAttribute("studentid") == null) {
    System.out.println("JSP: Redirecting to login due to missing session");
    response.sendRedirect("login.jsp?error=sessionExpired");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Results - Parikshan</title>

    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/studenthome.css">
    <link rel="stylesheet" href="css/adminhome.css">
    <link rel="stylesheet" href="css/studentresults.css">
</head>
<body>


<!-- Header / Navbar placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

    <div class="container">
        <h1>Test Results</h1>

        <!-- Debug info (remove in production) -->
        <%
        if ("admin".equals(session.getAttribute("userType"))) {
        %>
        <div style="background: #f0f0f0; padding: 10px; margin: 10px 0; border-radius: 5px;">
            <strong>Debug Info:</strong><br>
            Session ID: <%= session.getAttribute("studentid") %><br>
            User Type: <%= session.getAttribute("userType") %><br>
            Total Results: ${totalResults}
        </div>
        <% } %>

        <!-- Simple Stats -->
        <c:if test="${not empty testResults}">
            <div class="stats">
                <p><strong>Total Results: ${totalResults}</strong></p>
            </div>
        </c:if>

        <!-- Error/Info Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message" style="color: red; background: #ffe6e6; padding: 10px; border-radius: 5px; margin: 10px 0;">
                ${errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty infoMessage}">
            <div class="info-message" style="color: blue; background: #e6f3ff; padding: 10px; border-radius: 5px; margin: 10px 0;">
                ${infoMessage}
            </div>
        </c:if>

        <!-- Filter Section -->
        <div class="filter-section">
            <form method="get" action="DisplayStudentResults">
                <div class="filter-row">
                    <div>
                        <label for="testId">Filter by Test:</label>
                        <select name="testId" id="testId">
                            <option value="">All Tests</option>
                            <c:forEach var="test" items="${tests}">
                                <option value="${test.testId}"
                                        <c:if test="${test.testId eq selectedTestId}">selected</c:if>>
                                    ${test.title}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Admin can filter by student ID -->
                    <c:if test="${userType eq 'admin'}">
                        <div>
                            <label for="studentId">Filter by Student ID:</label>
                            <input type="text" name="studentId" id="studentId"
                                   value="${selectedStudentId}" placeholder="Enter Student ID">
                        </div>
                    </c:if>

                    <div>
                        <input type="submit" value="Filter Results" class="btn-primary">
                        <a href="DisplayStudentResults" class="btn">Clear Filters</a>
                    </div>
                </div>
            </form>
        </div>

        <!-- Results Table -->
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <c:if test="${userType eq 'admin'}">
                            <th>Student ID</th>
                        </c:if>
                        <th>Student Name</th>
                        <th>Test Title</th>
                        <th>Score</th>
                        <th>Percentage</th>
                        <th>Time Taken</th>
                        <th>Test Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty testResults}">
                            <c:forEach var="result" items="${testResults}">
                                <tr>
                                    <c:if test="${userType eq 'admin'}">
                                        <td><strong>${result.studentId}</strong></td>
                                    </c:if>
                                    <td>${result.studentName}</td>
                                    <td>
                                        <strong>${result.testTitle}</strong>
                                        <br><small>Test ID: ${result.testId}</small>
                                    </td>
                                    <td><strong>${result.score}/${result.totalMarks}</strong></td>
                                    <td>
                                        <%
                                            // Fixed percentage calculation
                                            TestResult currentResult = (TestResult) pageContext.getAttribute("result");
                                            double percentage = 0;
                                            String cssClass = "score-low";

                                            try {
                                                if (currentResult != null) {
                                                    double score = Double.parseDouble(currentResult.getScore());
                                                    double total = Double.parseDouble(currentResult.getTotalMarks());
                                                    percentage = (score / total) * 100;
                                                }
                                            } catch (Exception e) {
                                                percentage = 0;
                                            }

                                            if (percentage >= 80) cssClass = "score-high";
                                            else if (percentage >= 60) cssClass = "score-medium";
                                            else cssClass = "score-low";

                                            pageContext.setAttribute("percentage", percentage);
                                            pageContext.setAttribute("cssClass", cssClass);
                                        %>
                                        <span class="${cssClass}">
                                            <c:choose>
                                                <c:when test="${percentage > 0}">
                                                    <fmt:formatNumber value="${percentage}" pattern="##.#"/>%
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Fallback calculation using EL -->
                                                    <c:set var="calcPercentage" value="${(result.score / result.totalMarks) * 100}" />
                                                    <fmt:formatNumber value="${calcPercentage}" pattern="##.#"/>%
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty result.timeTaken}">
                                                ${result.timeTaken}
                                                <c:if test="${not fn:contains(result.timeTaken, ':')}">
                                                    sec
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty result.testDate}">
                                                <fmt:formatDate value="${result.testDate}" pattern="dd-MM-yyyy"/>
                                                <br><small><fmt:formatDate value="${result.testDate}" pattern="HH:mm"/></small>
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="${userType eq 'admin' ? '7' : '6'}" class="no-results">
                                    <c:choose>
                                        <c:when test="${not empty infoMessage}">
                                            ${infoMessage}
                                        </c:when>
                                        <c:otherwise>
                                            No test results found.
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${userType ne 'admin'}">
                                        <br><a href="testformats.jsp" class="btn">Take a Test</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:otherwise>
                                        </c:choose>
                </tbody>
            </table>
        </div>

        <!-- Action Buttons -->
        <div class="actions">
            <c:choose>
                <c:when test="${userType eq 'admin'}">
                    <a href="adminhome.jsp" class="btn">Home</a>
                    <a href="processrecording.jsp" class="btn">Detailed Analytics</a>
                </c:when>
                <c:otherwise>
                    <a href="homestudent.jsp" class="btn">Student Dashboard</a>
                    <a href="testformats.jsp" class="btn">Take Another Test</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <!-- Footer placeholder -->
    <div id="footer-container"></div>
<script src="js/studentresults.js"></script>
</body>
</html>