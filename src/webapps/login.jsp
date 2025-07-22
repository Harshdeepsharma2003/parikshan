<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Sign In - Modern Portal</title>
   <link rel="stylesheet" href="css/login.css">
   </head>
<body>
    <div class="main-container">
        <div class="signin-header">
            <div class="logo">
                <i class="fas fa-shield-alt"></i>
            </div>
            <h1>Welcome Back</h1>
            <p>Sign in to your account</p>
        </div>

        <form action="LoginServlet" method="post" class="signin-form" id="signinForm" autocomplete="off">
            <div class="form-group">
                <label for="userid">User ID</label>
                <div class="input-container">
                    <i class="fas fa-user input-icon"></i>
                    <input type="text" id="userid" name="userid" required />
                </div>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <div class="input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="password" name="password" required />
                    <button type="button" class="toggle-password" id="togglePassword" aria-label="Toggle password visibility">
                        <i class="fas fa-eye"></i>
                    </button>
                </div>
            </div>

            <div class="form-options">
                <label class="remember-me">
                    <input type="checkbox" id="remember" name="remember" />
                    Remember me
                </label>
                <a href="#" class="forgot-password">Forgot password?</a>
            </div>

            <button type="submit" class="signin-btn">
                <span class="btn-text">Sign In</span>
                <div class="btn-loader">
                    <div class="spinner"></div>
                </div>
            </button>

        </form>

        <div class="error-container" id="errorContainer">
            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i>
                <span><%= error %></span>
            </div>
            <% } %>
        </div>


    <script src="js/login.js"></script>
</body>
</html>
