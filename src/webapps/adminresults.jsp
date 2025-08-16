<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.parikshan.model.VideoProcessorResult" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Video Analysis Results</title>
    <link rel="stylesheet" href="css/studentresults.css">
   <link rel="stylesheet" href="css/adminresults.css">

</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📊 Admin - Video Analysis Results</h1>
        </div>

        <!-- Success Messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message" style="background: #d4edda; color: #155724; padding: 15px; border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 20px;">
                <strong>✅ Success:</strong> ${successMessage}
            </div>
        </c:if>

        <!-- Error Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message" style="background: #f8d7da; color: #721c24; padding: 15px; border: 1px solid #f5c6cb; border-radius: 5px; margin-bottom: 20px;">
                <strong>⚠️ Error:</strong> ${errorMessage}
            </div>
        </c:if>

        <!-- Summary Cards -->
        <c:if test="${not empty videoResults or not empty testResults}">
            <div class="summary-cards">
                <div class="summary-card">
                    <h3>${totalVideoResults}</h3>
                    <p>Videos Analyzed</p>
                </div>
                <div class="summary-card">
                    <h3>${totalTestResults}</h3>
                    <p>Test Results</p>
                </div>
                <div class="summary-card">
                    <h3>${suspiciousTests}</h3>
                    <p>Suspicious Tests</p>
                </div>
                <div class="summary-card">
                    <h3>${terminatedTests}</h3>
                    <p>Terminated Tests</p>
                </div>
            </div>
        </c:if>

        <!-- Test Results with Cheating Detection -->
        <c:if test="${not empty testResults}">
            <div class="section">
                <h2>🚨 Test Results & Cheating Detection</h2>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Student</th>
                                <th>Test</th>
                                <th>Score</th>
                                <th>Status</th>
                                <th>Violations</th>
                                <th>Risk Level</th>
                                <th>Invalidation Reason</th>
                                <th>Test Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="result" items="${testResults}">
                                <tr class="${result.violationCount >= 5 ? 'high-risk-row' : result.violationCount > 0 ? 'medium-risk-row' : ''}">
                                    <td>

                                        <small style="color: #666;">${result.studentId}</small>
                                    </td>
                                    <td>
                                        <small style="color: #666;">${result.testId}</small>
                                    </td>
                                    <td>
                                        <strong>${result.score}/${result.totalMarks}</strong>
                                        <br><span style="color: #28a745; font-size: 0.9em;">${result.percentage}%</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.status == 'COMPLETED'}">
                                                <span class="status-badge status-success">Completed</span>
                                            </c:when>
                                            <c:when test="${result.status == 'TERMINATED'}">
                                                <span class="status-badge status-error">Terminated</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge" style="background: #ffc107; color: black;">${result.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.violationCount == 0}">
                                                <span style="color: #28a745; font-weight: bold;">0</span>
                                            </c:when>
                                            <c:when test="${result.violationCount < 3}">
                                                <span style="color: #ffc107; font-weight: bold;">${result.violationCount}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #dc3545; font-weight: bold;">${result.violationCount}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.violationCount == 0}">
                                                <span class="detection-high">✓ Clean</span>
                                            </c:when>
                                            <c:when test="${result.violationCount < 3}">
                                                <span class="detection-medium">⚠ Low Risk</span>
                                            </c:when>
                                            <c:when test="${result.violationCount < 5}">
                                                <span class="detection-low">⚠ Medium Risk</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="detection-low">🚨 High Risk</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${not empty result.invalidationReason}">
                                            <span style="color: #dc3545; font-size: 0.8em;">${result.invalidationReason}</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${result.testDate}" pattern="MMM dd, yyyy HH:mm"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <!-- Video Analysis Results Table -->
        <c:if test="${not empty videoResults}">
            <div class="section">
                <h2>📹 Video Analysis Results</h2>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Recording ID</th>
                                <th>Student ID</th>
                                <th>Test ID</th>
                                <th>Status</th>
                                <th>Total Frames</th>
                                <th>Pose Detected Frames</th>
                                <th>Detection Percentage</th>
                                <th>Detection Quality</th>
                                <th>Error (if any)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="result" items="${videoResults}">
                                <tr>
                                    <td>
                                        <strong style="color: #667eea;">#${result.recordingId}</strong>
                                    </td>
                                    <td>
                                        <strong>${result.studentId}</strong>
                                    </td>
                                    <td>
                                        <strong>${result.testId}</strong>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.status == 'success'}">
                                                <span class="status-badge status-success">Success</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-error">${result.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <strong>${result.totalFrames}</strong>
                                    </td>
                                    <td>
                                        <strong>${result.poseDetectedFrames}</strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <fmt:formatNumber value="${result.detectionPercentage}" pattern="##.#"/>%
                                        </strong>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.detectionPercentage >= 80}">
                                                <span class="detection-high">
                                                    ✓ Excellent
                                                </span>
                                            </c:when>
                                            <c:when test="${result.detectionPercentage >= 60}">
                                                <span class="detection-medium">
                                                    ⚠ Moderate
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="detection-low">
                                                    ✗ Poor
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${not empty result.error}">
                                            <span style="color: #dc3545; font-size: 0.8em;">${result.error}</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <!-- No Results Message -->
        <c:if test="${empty videoResults and empty testResults}">
            <div class="no-results" style="text-align: center; padding: 40px; color: #666;">
                <h3>📋 No results found</h3>
                <p>No video analysis or test results available yet.</p>
                <a href="VideoProcessorServlet" class="btn">🎥 Process Video</a>
            </div>
        </c:if>

        <!-- Action Buttons -->
        <div class="actions">
            <a href="adminhome.jsp" class="btn">🏠 Admin Dashboard</a>
            <a href="VideoProcessorServlet" class="btn">🎥 Process Video</a>
            <a href="javascript:location.reload();" class="btn">🔄 Refresh Results</a>
        </div>
    </div>
</body>
</html>