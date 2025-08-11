<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Online Testing Platform</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/login.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

 <!-- Header -->
    <div id="header-container"></div>

    <!-- Student Login Form Section -->
    <section class="student-login-section">
      <div class="container">
        <h2 class="section-title">Login</h2>
        <form class="login-form" action="StudentLogin" method="post" autocomplete="off">
          <div class="form-group with-icon">
            <label for="studentid">User ID</label>
            <input type="text" id="studentid" name="studentid" maxlength="100" required placeholder="Enter your User ID">
            <i class="fas fa-user form-icon"></i>
          </div>
          <div class="form-group with-icon">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" minlength="6" maxlength="60" required placeholder="Enter your password">
            <i class="fas fa-lock form-icon"></i>
          </div>
          <button type="submit" class="btn btn-primary">Login</button>
        </form>
        <div class="register-link">
          Don't have an account? <a href="register.jsp">Register here</a>
        </div>
      </div>
    </section>

    </main>

    <!-- Footer -->
    <div id="footer-container"></div>

    <script src="js/login.js"></script>
</body>
</html>