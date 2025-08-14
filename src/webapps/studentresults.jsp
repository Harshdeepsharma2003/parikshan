<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.parikshan.model.TestResult" %>
<%@ page import="com.yash.parikshan.model.Test" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Results - Parikshan</title>
     <link rel="stylesheet" href="css/studentresults.css">

</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📊 Test Results</h1>

            <!-- User Info -->
            <div class="user-info">
                <c:choose>
                    <c:when test="${userType eq 'admin'}">
                        <p><strong>👨‍💼 Admin View:</strong> Viewing all test results</p>
                    </c:when>
                    <c:otherwise>
                        <p><strong>👨‍🎓 Student View:</strong> Your test results</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Error/Info Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <strong>⚠️ Error:</strong> ${errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty infoMessage}">
            <div class="info-message">
                <strong>ℹ️ Info:</strong> ${infoMessage}
            </div>
        </c:if>

        <!-- Statistics Summary -->
        <c:if test="${not empty testResults}">
            <div class="stats-summary">
                <div class="stat-card">
                    <div class="stat-number">${totalResults}</div>
                    <div class="stat-label">Total Results</div>
                </div>
            </div>
        </c:if>

        <!-- Filter Section -->
        <div class="filter-section">
            <form method="get" action="DisplayStudentResults">
                <div class="filter-row">
                    <div class="filter-group">
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
                        <div class="filter-group">
                            <label for="studentId">Filter by Student ID:</label>
                            <input type="text" name="studentId" id="studentId"
                                   value="${selectedStudentId}" placeholder="Enter Student ID">
                        </div>
                    </c:if>

                    <div class="filter-group">
                        <input type="submit" value="🔍 Filter Results">
                    </div>

                    <div class="filter-group">
                        <a href="DisplayStudentResults" class="btn">🔄 Clear Filters</a>
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
                        <th>Time Taken (in seconds)</th>
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
                                            // Calculate percentage
                                            TestResult currentResult = (TestResult) pageContext.getAttribute("result");
                                            double percentage = 0;
                                            try {
                                                double score = Double.parseDouble(currentResult.getScore());
                                                double total = Double.parseDouble(currentResult.getTotalMarks());
                                                percentage = (score / total) * 100;
                                            } catch (Exception e) {
                                                percentage = 0;
                                            }

                                            String cssClass = "";
                                            if (percentage >= 80) cssClass = "score-high";
                                            else if (percentage >= 60) cssClass = "score-medium";
                                            else cssClass = "score-low";

                                            pageContext.setAttribute("percentage", percentage);
                                            pageContext.setAttribute("cssClass", cssClass);
                                        %>
                                        <span class="${cssClass}">
                                            <fmt:formatNumber value="${percentage}" pattern="##.#"/>%
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty result.timeTaken}">
                                                ${result.timeTaken}
                                                <c:choose>
                                                    <c:when test="${fn:contains(result.timeTaken, ':')}">
                                                        <!-- Already formatted as MM:SS -->
                                                    </c:when>
                                                    <c:otherwise>
                                                        sec
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${result.testDate}" pattern="dd-MM-yyyy"/>
                                        <br><small><fmt:formatDate value="${result.testDate}" pattern="HH:mm"/></small>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="${userType eq 'admin' ? '7' : '6'}" class="no-results">
                                    📋 No test results found for the selected criteria.
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
                    <a href="adminhome.jsp" class="btn">🏠 Admin Dashboard</a>
                    <a href="processrecording.jsp" class="btn">📊 Detailed Analytics</a>
                </c:when>
                <c:otherwise>
                    <a href="homestudent.jsp" class="btn">🏠 Student Dashboard</a>
                    <a href="testformats.jsp" class="btn">📝 Take Another Test</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
