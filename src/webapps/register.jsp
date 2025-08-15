<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Registration - Parikshan</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/landingpage.css"> <!-- Your main theme CSS -->
    <link rel="stylesheet" href="css/register.css"> <!-- Simplified register styles -->
</head>
<body>
    <!-- Header (using your theme) -->
    <header class="main-header">
        <div class="container">
            <div class="header-content">
                <div class="logo">
                    <a href="landingpage.jsp">
                        <div class="logo-icon">
                            <i class="fas fa-graduation-cap"></i>
                        </div>
                        <span class="logo-text">Parikshan</span>
                    </a>
                </div>
                <div class="header-actions">
                    <div class="auth-buttons">
                        <a href="login.jsp" class="btn btn-outline-small">
                            <i class="fas fa-sign-in-alt"></i>
                            Login
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Navigation (using your theme) -->
    <nav class="main-navbar">
        <div class="container">
            <div class="nav-content">
                <ul class="nav-links">
                    <li><a href="landingpage.jsp">Home</a></li>
                    <li><a href="aboutus.jsp">About Us</a></li>
                    <li><a href="login.jsp">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main>
        <!-- Registration Section -->
        <section class="register-section">
            <div class="container">
                <div class="register-container">
                    <h2 class="register-title">Student Registration</h2>
                    <form class="register-form" action="RegisterStudent" method="post">
                        <div class="form-grid">
                            <!-- Row 1: Student ID and Full Name -->
                            <div class="form-group">
                                <label for="studentid">
                                    <i class="fas fa-id-card"></i>
                                    Student ID *
                                </label>
                                <input type="text" id="studentid" name="studentid"
                                       placeholder="Enter Student ID" required>
                            </div>

                            <div class="form-group">
                                <label for="name">
                                    <i class="fas fa-user"></i>
                                    Full Name *
                                </label>
                                <input type="text" id="name" name="name"
                                       placeholder="Enter Full Name" required>
                            </div>

                            <!-- Row 2: Email and Phone -->
                            <div class="form-group">
                                <label for="email">
                                    <i class="fas fa-envelope"></i>
                                    Email Address *
                                </label>
                                <input type="email" id="email" name="email"
                                       placeholder="Enter Email Address" required>
                            </div>

                            <div class="form-group">
                                <label for="phone">
                                    <i class="fas fa-phone"></i>
                                    Phone Number
                                </label>
                                <input type="tel" id="phone" name="phone"
                                       placeholder="+91 9876543210">
                            </div>

                            <!-- Row 3: Password (Full Width) -->
                            <div class="form-group full-width">
                                <label for="password">
                                    <i class="fas fa-lock"></i>
                                    Password *
                                </label>
                                <input type="password" id="password" name="password"
                                       placeholder="Create Password (min. 6 characters)" required>
                                <small>Minimum 6 characters required</small>
                            </div>
                        </div>

                        <div class="submit-section">
                            <button type="submit" class="btn btn-primary register-btn">
                                <i class="fas fa-user-plus"></i>
                                Register
                            </button>
                            <p class="login-link">
                                Already registered?
                                <a href="login.jsp">Login here</a>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </section>

        <!-- Testimonials Section (using your theme) -->
        <section class="testimonials">
            <div class="container">
                <h2 class="section-title">What Our Users Say</h2>
                <p class="section-subtitle">Trusted by students and institutions worldwide</p>

                <div class="testimonials-grid">
                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <i class="fas fa-quote-left"></i>
                            <p>"Parikshan transformed how we conduct online exams. The proctoring system is reliable and user-friendly."</p>
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
                            <p>"The variety of question formats helped me prepare better. The instant results feature is fantastic!"</p>
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
                            <p>"Secure, efficient, and comprehensive. Parikshan has made online testing seamless for our institution."</p>
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

    <!-- Footer (using your theme) -->
    <footer class="main-footer">
        <div class="container">
            <div class="footer-bottom">
                <div class="footer-bottom-content">
                    <div class="copyright">
                        <p>&copy; 2025 Parikshan - Online Testing Platform</p>
                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!-- Footer -->
        <div id="footer-container"></div>
        <script src="js/register.js"></script>
</body>
</html>
