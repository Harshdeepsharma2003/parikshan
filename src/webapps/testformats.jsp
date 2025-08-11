<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Online Testing Platform</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/landingpage.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>


<!-- Test Formats Section -->
<section class="test-formats">
    <div class="container">
        <h2 class="section-title">Test Formats</h2>
        <p class="section-subtitle">Diverse question types to assess different skills</p>

        <div class="formats-grid">

            <a href="availabletests.jsp" class="format-card" tabindex="0" style="text-decoration:none; color:inherit;">
                <div class="format-icon">
                    <i class="fas fa-list-ul"></i>
                </div>
                <h3>Multiple Choice Questions</h3>
                <p>Traditional MCQs with single or multiple correct answers. Perfect for knowledge assessment.</p>
                 <div class="format-details" style="display: none;">
                    <span>Single Answer</span>
                    <span>Multiple Answer</span>
                    <span>Timed Response</span>
                </div>
            </a>


        </div>
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

        <!-- Registration/Login Section -->
        <section class="auth-section">
            <div class="container">
                <div class="auth-content">
                    <h2>Ready to Get Started?</h2>
                    <p>Join thousands of students and educators already using Parikshan</p>
                    <div class="auth-buttons">
                        <a href="student-register.jsp" class="btn btn-primary">
                            <i class="fas fa-graduation-cap"></i>
                            Student Registration
                        </a>
                        <a href="admin-register.jsp" class="btn btn-outline">
                            <i class="fas fa-user-shield"></i>
                            Admin Registration
                        </a>
                    </div>
                    <div class="login-link">
                        Already have an account?
                        <a href="student-login.jsp">Student Login</a> |
                        <a href="admin-login.jsp">Admin Login</a>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <div id="footer-container"></div>


    <script src="js/testformats.js"></script>
</body>
</html>