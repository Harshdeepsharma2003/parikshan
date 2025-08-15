<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Test – Parikshan</title>
<link rel="stylesheet" href="css/landingpage.css">
 <link rel="stylesheet" href="css/studenthome.css">
    <link rel="stylesheet" href="css/createtest.css">
      <!-- Optional icon font -->
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <!-- Basic page styles -->
</head>
<body>

<!-- Header / Navbar placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

<main class="wrapper">

    <!-- Flash messages -->
    <%
        String error   = (String) request.getAttribute("errorMessage");
        String success = (String) request.getAttribute("successMessage");
        if (error != null) {
    %>
        <div class="msg msg-error"><%= error %></div>
    <% } else if (success != null) { %>
        <div class="msg msg-success"><%= success %></div>
    <% } %>

    <section class="card">
        <h2 class="card-title">Test Details</h2>

        <form id="testForm"
              action="<%= request.getContextPath() %>/RegisterTest"
              method="post" autocomplete="off">

            <!-- Test ID -->
            <div class="form-group">
                <label for="testid">Test ID <span class="req">*</span></label>
                <input type="text" id="testid" name="testid"
                       placeholder="e.g. TEST_2025_001" required>
            </div>

            <!-- Test Title -->
            <div class="form-group">
                <label for="title">Test Title <span class="req">*</span></label>
                <input type="text" id="title" name="testTitle"
                       placeholder="Descriptive title" required>
            </div>

            <!-- Question count -->
            <div class="form-group">
                <label for="count">Number of Questions (1-5) <span class="req">*</span></label>
                <input type="number" id="count" name="noofquestions"
                       min="1" max="5" value="1" required>
            </div>

            <!-- Description -->
            <div class="form-group">
                <label for="desc">Description</label>
                <textarea id="desc" name="testDesc"
                          placeholder="Optional description…" rows="4"></textarea>
            </div>

            <button type="submit" class="btn-primary">Save &amp; Continue</button>
        </form>
    </section>

    <nav class="nav-links">
        <a href="adminhome.jsp" class="btn">Home</a>
    </nav>
    <!-- Footer placeholder -->
    <div id="footer-container"></div>
</main>


<script>
document.getElementById('testForm').addEventListener('submit', function (e) {
    const n = parseInt(document.getElementById('count').value, 10);
    if (n < 1 || n > 5) {
        alert('Please enter a number between 1 and 5.');
        e.preventDefault();
    }
});
</script>
<script src="js/createtest.js"></script>
</body>
</html>
