<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - Parikshan</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/landingpage.css"> <!-- Your main theme CSS -->
    <link rel="stylesheet" href="css/studenthome.css"> <!-- Simple student home styles -->
</head>
<body>

<!-- Header -->
    <div id="header-container"></div>

     <!-- Navbar -->
          <div id="navbar-container"></div>


    <!-- Main Content -->
    <main>

        <!-- Test Format Section -->
        <section class="test-formats">
            <div class="container">
                <h2 class="section-title">Available Test Format</h2>
                <p class="section-subtitle">Multiple Choice Questions to assess your knowledge</p>

                <div class="format-card-container">
                    <div class="format-card">
                        <a href="AvailableTests" class="card-link">
                            <div class="format-icon">
                                <i class="fas fa-list-ul"></i>
                            </div>
                            <h3>Multiple Choice Questions</h3>
                            <p>Traditional MCQs with single correct answers. Perfect for knowledge assessment.</p>
                            <div class="format-features">
                                <span><i class="fas fa-check"></i> Single Answer</span>
                                <span><i class="fas fa-clock"></i> Timed Response</span>
                            </div>
                            <div class="card-action">
                                <i class="fas fa-arrow-right"></i>
                                Start Test
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </section>

        <!-- How It Works Section -->
        <section class="how-it-works">
            <div class="container">
                <h2 class="section-title">How It Works</h2>
                <p class="section-subtitle">Simple steps to get started</p>

                <div class="steps-container">
                    <div class="step-card">
                        <div class="step-number">1</div>
                        <div class="step-icon">
                            <i class="fas fa-clipboard-check"></i>
                        </div>
                        <h3>Choose Test Format</h3>
                        <p>Select from Multiple Choice Questions format</p>
                        <div class="time-info">
                            <i class="fas fa-clock"></i>
                            <span>Browse instantly</span>
                        </div>
                    </div>

                    <div class="step-card">
                        <div class="step-number">2</div>
                        <div class="step-icon">
                            <i class="fas fa-trophy"></i>
                        </div>
                        <h3>Take Test & Get Results</h3>
                        <p>Complete your test under secure proctoring and receive instant detailed results</p>
                        <div class="time-info">
                            <i class="fas fa-clock"></i>
                            <span>Results in seconds</span>
                        </div>
                    </div>
                </div>

                <!-- Proctoring Info -->
                <div class="proctoring-section">
                    <h3 class="proctoring-title">Secure Testing Environment</h3>
                    <div class="proctoring-grid">
                        <div class="info-card">
                            <i class="fas fa-video"></i>
                            <h4>Camera Monitoring</h4>
                            <p>Continuous video surveillance ensures test integrity</p>
                        </div>
                        <div class="info-card">
                            <i class="fas fa-microphone"></i>
                            <h4>Audio Recording</h4>
                            <p>Audio monitoring detects unauthorized communication</p>
                        </div>
                        <div class="info-card">
                            <i class="fas fa-shield-alt"></i>
                            <h4>Secure Environment</h4>
                            <p>Advanced algorithms prevent cheating and ensure fairness</p>
                        </div>
                    </div>
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

<!-- Footer -->
    <div id="footer-container"></div>

<script src="js/homestudent.js"></script>

</body>
</html>
