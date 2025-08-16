<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Results – Parikshan</title>

    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/availabletests.css"> <!-- Reuse existing table styles -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<!-- Header -->
<header class="main-header">
    <div class="container">
        <div class="header-content">
            <div class="logo">
                <a href="landingpage.jsp">
                    <div class="logo-icon"><i class="fas fa-graduation-cap"></i></div>
                    <span class="logo-text">Parikshan</span>
                </a>
            </div>
            <div class="header-actions">
                <div class="auth-buttons">
                    <a href="landingpage.jsp" class="btn btn-primary-small">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Navbar -->
<nav class="main-navbar">
    <div class="container">
        <div class="nav-content">
            <ul class="nav-links">
                <li><a href="homestudent.jsp">Home</a></li>
                <li><a href="AvailableTests">Available Tests</a></li>
                <li><a href="MyResults" class="active">My Results</a></li>
                <li class="dropdown">
                    <a href="profile.jsp" class="dropdown-toggle">
                        Profile <i class="fas fa-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="updateProfile.jsp">Update Profile</a></li>
                        <li><a href="deleteProfile.jsp">Delete Profile</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main>
    <section class="list-section">
        <div class="container">
            <div class="form-header" style="text-align:center;margin-bottom:var(--space-2xl);">
                <h2>Your <span class="brand-highlight">Test Results</span></h2>
                <p>Review your completed tests and performance</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty errorMessage}">
                <div class="error-container">
                    <div class="error-header">
                        <i class="fas fa-exclamation-triangle error-icon"></i>
                        <h3 class="error-title">Error Loading Results</h3>
                    </div>
                    <p class="error-message">${errorMessage}</p>
                </div>
            </c:if>

            <!-- Results Table -->
            <div class="test-table-wrapper">
                <table class="test-table">
                    <thead>
                    <tr>
                        <th>Test&nbsp;Title</th>
                        <th>Date&nbsp;Taken</th>
                        <th>Score</th>
                        <th>Time&nbsp;Taken(in seconds)</th>
                        <th>Status</th>
                        <th>Violations</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty results}">
                            <c:forEach items="${results}" var="result">
                                <tr>
                                    <td>
                                        <strong>${not empty result.testTitle ? result.testTitle : 'Unknown Test'}</strong>
                                        <br><small style="color: #666;">ID: ${result.testId}</small>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${result.testDate}" pattern="dd MMM yyyy" /><br>
                                        <small style="color: #666;">
                                            <fmt:formatDate value="${result.testDate}" pattern="HH:mm" />
                                        </small>
                                    </td>
                                    <td>
                                        <strong>${result.score}/${result.totalMarks}</strong>
                                        <c:if test="${not empty result.score and not empty result.totalMarks}">
                                            <c:set var="percentage" value="${(result.score * 100) / result.totalMarks}" />
                                            <br><small style="color: #666;">
                                                <fmt:formatNumber value="${percentage}" maxFractionDigits="1" />%
                                            </small>
                                        </c:if>
                                    </td>
                                    <td>${result.timeTaken}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.status eq 'PASSED' or result.status eq 'COMPLETED'}">
                                                <span class="status-passed">
                                                    <i class="fas fa-check-circle"></i> ${result.status}
                                                </span>
                                            </c:when>
                                            <c:when test="${result.status eq 'FAILED'}">
                                                <span class="status-failed">
                                                    <i class="fas fa-times-circle"></i> ${result.status}
                                                </span>
                                            </c:when>
                                            <c:when test="${result.status eq 'INVALIDATED'}">
                                                <span class="status-invalidated">
                                                    <i class="fas fa-ban"></i> ${result.status}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-other">${result.status}</span>
                                            </c:otherwise>
                                        </c:choose>

                                        <c:if test="${not empty result.invalidationReason}">
                                            <br><small style="color: #dc3545;" title="${result.invalidationReason}">
                                                <i class="fas fa-info-circle"></i> Reason available
                                            </small>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.violationCount > 0}">
                                                <span class="violation-count">
                                                    <i class="fas fa-exclamation-triangle"></i> ${result.violationCount}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #28a745;">
                                                    <i class="fas fa-check"></i> None
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" style="text-align:center;padding:var(--space-xl);">
                                    <i class="fas fa-info-circle" style="color:#6c757d;font-size:2em;margin-bottom:10px;"></i><br>
                                    No test results found. Take your first test to see results here!
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</main>

<footer class="main-footer">
    <div class="container">
        <div class="footer-bottom">
            <div class="footer-bottom-content">
                <p>&copy; 2025 Parikshan – Online Testing Platform</p>
            </div>
        </div>
    </div>
</footer>

 <link rel="stylesheet" href="css/myresults.css">
</body>
</html>
