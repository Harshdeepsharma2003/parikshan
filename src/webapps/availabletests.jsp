<%
// If no data exists, redirect to servlet
if (request.getAttribute("tests") == null && request.getAttribute("errorMessage") == null) {
    response.sendRedirect(request.getContextPath() + "/AvailableTests");
    return;
}
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Available Tests - Parikshan</title>

    <!-- Shared styles / icons -->
    <link rel="stylesheet" href="css/common.css">
     <link rel="stylesheet" href="css/availabletests.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

</head>
<body>
<!-- Loading Overlay -->
<div class="loading-overlay" id="loadingOverlay">
    <div class="loading-box">
        <div class="spinner"></div>
        <p>Please wait…</p>
    </div>
</div>

<!-- Header / Navbar / Footer placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

<main>
    <section class="list-section">
        <div class="container">
            <div class="form-header" style="text-align:center; margin-bottom:var(--space-2xl);">
                <h2>Available <span class="brand-highlight">Tests</span></h2>
                <p>Select a test to begin</p>
            </div>

            <!-- ---------- SERVER-SIDE ERROR / SUCCESS MESSAGES ---------- -->
            <c:if test="${not empty errorMessage}">
                <div class="error-container" id="errorContainer">
                    <div class="error-header">
                        <i class="fas fa-exclamation-triangle error-icon"></i>
                        <h3 class="error-title">Something went wrong</h3>
                    </div>
                    <p class="error-message">${errorMessage}</p>
                    <c:if test="${not empty errorDetails}">
                        <details class="error-details">
                            <summary>Error Details</summary>
                            <div class="error-code">${errorDetails}</div>
                        </details>
                    </c:if>
                    <div class="error-actions">
                        <a href="<c:url value='/landingpage.jsp'/>" class="btn-error">
                            <i class="fas fa-home"></i> Back to Home
                        </a>
                        <button type="button" class="btn-secondary-error" onclick="location.reload()">
                            <i class="fas fa-redo"></i> Try Again
                        </button>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="success-message" id="successMessage">
                    <i class="fas fa-check-circle"></i> <span>${successMessage}</span>
                </div>
            </c:if>

            <!-- ---------- TEST LIST TABLE ---------- -->
            <div class="test-table-wrapper">
                <table class="test-table">
                   <thead>
                   <tr>
                       <th>Test&nbsp;ID</th>
                       <th>Title</th>
                       <th>Total&nbsp;Questions</th>
                       <th>Description</th>
                       <th>Action</th>
                   </tr>
                   </thead>
                   <tbody>
                   <c:choose>
                       <c:when test="${not empty tests}">
                           <c:forEach items="${tests}" var="t" varStatus="status">
                               <tr>
                                   <td>${t.testId}</td>
                                   <td>${t.title}</td>
                                   <td>${t.noOfQuestions}</td>
                                   <td>
                                       <c:choose>
                                           <c:when test="${not empty t.description}">
                                               ${fn:length(t.description) > 80 ? fn:substring(t.description,0,77) += '…' : t.description}
                                           </c:when>
                                           <c:otherwise>
                                               <span style="color: #666;">[No Description]</span>
                                           </c:otherwise>
                                       </c:choose>
                                   </td>
                                   <td>
                                       <form action="testloginstudent.jsp" method="get" onsubmit="return startTest(this);">
                                           <input type="hidden" name="testId" value="${t.testId}">
                                           <button type="submit" class="btn-start">
                                               <i class="fas fa-play"></i>&nbsp;Start&nbsp;Test
                                           </button>
                                       </form>
                                   </td>
                               </tr>
                           </c:forEach>
                       </c:when>
                       <c:otherwise>
                           <tr>
                               <td colspan="5" style="text-align:center; padding:var(--space-xl);">
                                   <c:choose>
                                       <c:when test="${not empty errorMessage}">
                                           <i class="fas fa-exclamation-triangle" style="color: #dc3545;"></i><br>
                                           Unable to load tests. Please check the error message above.
                                       </c:when>
                                       <c:otherwise>
                                           <i class="fas fa-inbox" style="color: #6c757d; font-size: 2em; margin-bottom: 10px;"></i><br>
                                           No tests available at the moment.
                                       </c:otherwise>
                                   </c:choose>
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

<div id="footer-container"></div>
 <script src="js/availabletests.js"></script>
</body>
</html>
