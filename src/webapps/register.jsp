<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Register - Modern Portal</title>
    <link rel="stylesheet" href="css/register.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

</head>
<body>
    <div class="main-container">
        <div class="register-header">
            <h1>Create Account</h1>
            <p>Join our community today</p>
        </div>

        <form action="RegisterServlet" method="post" class="register-form" id="registerForm" autocomplete="off" onsubmit="return validateRegisterForm();">
            <div class="form-group">
                <label for="userid">User ID</label>
                <div class="input-container">
                    <i class="fas fa-id-badge input-icon"></i>
                    <input type="text" id="userid" name="userid" placeholder="Enter your user ID" required />
                </div>
            </div>

            <div class="form-group">
                <label for="username">Username</label>
                <div class="input-container">
                    <i class="fas fa-user input-icon"></i>
                    <input type="text" id="username" name="username" placeholder="Choose a username" required />
                </div>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <div class="input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="password" name="password" placeholder="Create a password" required />
                    <button type="button" class="toggle-password" id="togglePassword" aria-label="Toggle password visibility">
                        <i class="fas fa-eye"></i>
                    </button>
                </div>
            </div>

            <div class="form-group">
                <label for="email">Email Address</label>
                <div class="input-container">
                    <i class="fas fa-envelope input-icon"></i>
                    <input type="email" id="email" name="email" placeholder="Enter your email" required />
                </div>
            </div>

            <button type="submit" class="register-btn">
                <span class="btn-text">Create Account</span>
                <div class="btn-loader">
                    <div class="spinner"></div>
                </div>
            </button>
        </form>

        <div class="divider">
            <span>Already have an account?</span>
        </div>

        <form action="signin.jsp" method="get">
            <button type="submit" class="register-btn secondary">
                <i class="fas fa-sign-in-alt"></i>
                <span class="btn-text">Sign In</span>
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
    </div>

    <script src="js/register.js"></script>
</body>
</html>