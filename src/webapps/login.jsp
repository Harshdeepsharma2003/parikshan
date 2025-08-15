<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Login - Parikshan</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/landingpage.css"> <!-- Your main theme CSS -->
    <link rel="stylesheet" href="css/login.css"> <!-- Simple login styles -->
</head>
<body>

<!-- Header -->
    <div id="header-container"></div>

   <!-- Navbar -->
        <div id="navbar-container"></div>


    <!-- Main Content -->
    <main>
        <section class="login-section">
            <div class="container">
                <div class="login-container">
                    <h2 class="login-title">Student Login</h2>
                    <form class="login-form" action="StudentLogin" method="post">
                        <div class="form-group">
                            <label for="studentid">
                                <i class="fas fa-user"></i>
                                User ID
                            </label>
                            <input type="text" id="studentid" name="studentid"
                                   placeholder="Enter your User ID" required>
                        </div>

                        <div class="form-group">
                            <label for="password">
                                <i class="fas fa-lock"></i>
                                Password
                            </label>
                            <input type="password" id="password" name="password"
                                   placeholder="Enter your password" required>
                        </div>

                        <button type="submit" class="btn btn-primary login-btn">
                            <i class="fas fa-sign-in-alt"></i>
                            Login
                        </button>
                    </form>

                    <p class="register-link">
                        Don't have an account?
                        <a href="register.jsp">Register here</a>
                    </p>
                </div>
            </div>
        </section>
    </main>

<!-- Footer -->
    <div id="footer-container"></div>

    <script src="js/login.js"></script>
</body>
</html>
