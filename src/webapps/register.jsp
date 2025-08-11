<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Online Testing Platform</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/register.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

 <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

<!-- Student Registration Form Section -->
<section class="student-register-section">
  <div class="container">
    <h2 class="section-title">User Registration</h2>
    <form class="register-form" action="RegisterStudent" method="post" autocomplete="off">

      <div class="form-grid">
        <!-- Row 1: Student ID and Full Name -->
        <div class="form-group with-icon">
          <label for="studentid">Student ID *</label>
          <input type="text" id="studentid" name="studentid" maxlength="100" required placeholder="Enter ID">
          <i class="fas fa-id-card form-icon"></i>
        </div>
        <div class="form-group with-icon">
          <label for="name">Full Name *</label>
          <input type="text" id="name" name="name" maxlength="100" required placeholder="Enter name">
          <i class="fas fa-user form-icon"></i>
        </div>

        <!-- Row 2: Email and Phone -->
        <div class="form-group with-icon">
          <label for="email">Email Address *</label>
          <input type="email" id="email" name="email" maxlength="150" required placeholder="Enter email">
          <i class="fas fa-envelope form-icon"></i>
        </div>
        <div class="form-group with-icon">
          <label for="phone">Phone Number</label>
          <input type="tel" id="phone" name="phone" maxlength="20" pattern="[\d\s\-+]*" placeholder="+91 9876543210">
          <i class="fas fa-phone form-icon"></i>
        </div>

        <!-- Row 3: Password (Full Width) -->
        <div class="form-group with-icon ">
          <label for="password">Password *</label>
          <input type="password" id="password" name="password" minlength="6" maxlength="60" required placeholder="Create password (min. 6 characters)">
          <i class="fas fa-lock form-icon"></i>
          <small>Minimum 6 characters required</small>
        </div>
      </div>

      <div class="submit-section">
        <button type="submit" class="btn btn-primary">Register</button>
        <div class="login-link">
          Already registered? <a href="login.jsp">Login here</a>
        </div>
      </div>
    </form>
  </div>
</section>

        <!-- Testimonials Section -->
        <section class="testimonials">
            <div class="container">
                <h2 class="section-title">What Our Users Say</h2>
                <p class="section-subtitle">Trusted by students and institutions worldwide</p>

                <div class="testimonials-grid">
                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <i class="fas fa-quote-left"></i>
                            <p>"Parikshan transformed how we conduct online exams. The proctoring system is reliable and the interface is incredibly user-friendly."</p>
                        </div>
                        <div class="testimonial-author">
                            <div class="author-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <div class="author-info">
                                <h4>Dr. Priya Sharma</h4>
                                <span>Professor, Delhi University</span>
                            </div>
                        </div>
                        <div class="rating">
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                        </div>
                    </div>

                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <i class="fas fa-quote-left"></i>
                            <p>"The variety of question formats helped me prepare better for my exams. The instant results feature is fantastic!"</p>
                        </div>
                        <div class="testimonial-author">
                            <div class="author-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <div class="author-info">
                                <h4>Rahul Kumar</h4>
                                <span>Engineering Student</span>
                            </div>
                        </div>
                        <div class="rating">
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                        </div>
                    </div>

                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <i class="fas fa-quote-left"></i>
                            <p>"Secure, efficient, and comprehensive. Parikshan has made online testing seamless for our entire institution."</p>
                        </div>
                        <div class="testimonial-author">
                            <div class="author-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <div class="author-info">
                                <h4>Admin Team</h4>
                                <span>Mumbai Institute</span>
                            </div>
                        </div>
                        <div class="rating">
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                        </div>
                    </div>
                </div>
            </div>
        </section>


    </main>

    <!-- Footer -->
    <div id="footer-container"></div>

   <script src="js/register.js"></script>
</body>
</html>