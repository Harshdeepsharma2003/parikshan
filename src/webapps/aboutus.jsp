<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us – Parikshan</title>

    <!-- Shared styles / icons -->
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/aboutus.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- ===== Header ===== -->
    <header class="main-header">
        <div class="container">
            <div class="header-content">
                <div class="logo">
                    <a href="landingpage.jsp">
                        <div class="logo-icon"><i class="fas fa-graduation-cap"></i></div>
                        <span class="logo-text">Parikshan</span>
                    </a>
                </div>
                <div class="header-actions">
                    <div class="auth-buttons">
                        <a href="login.jsp"  class="btn btn-outline-small"><i class="fas fa-sign-in-alt"></i> Login</a>
                        <a href="register.jsp" class="btn btn-primary-small"><i class="fas fa-user-plus"></i> Sign Up</a>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- ===== Navbar ===== -->
    <nav class="main-navbar">
        <div class="container">
            <div class="nav-content">
                <ul class="nav-links">
                    <li><a href="landingpage.jsp">Home</a></li>
                    <li><a href="aboutus.jsp" class="active">About Us</a></li>
                    <li><a href="AvailableTests">Tests</a></li>

                </ul>
            </div>
        </div>
    </nav>

    <!-- ===== Main ===== -->
    <main>
        <!-- Hero / Intro -->
        <section class="about-hero">
            <div class="container">
                <h1>Empowering Assessments with <span class="brand-highlight">Integrity</span> & Innovation</h1>
                <p class="subtitle">Parikshan is an advanced online testing platform that blends cutting-edge proctoring technology with a seamless user experience.</p>
            </div>
        </section>

        <!-- Mission / Vision -->
        <section class="mission-section">
            <div class="container grid-2">
                <div>
                    <h2 class="section-heading">Our Mission</h2>
                    <p>To make high-stakes online examinations <strong>secure</strong>, <strong>accessible</strong>, and <strong>reliable</strong> for every learner and institution worldwide.</p>
                </div>
                <div>
                    <h2 class="section-heading">Our Vision</h2>
                    <p>To become the global benchmark for integrity-driven, AI-powered assessment solutions that foster genuine learning outcomes.</p>
                </div>
            </div>
        </section>

        <!-- Core Values -->
        <section class="values-section">
            <div class="container">
                <h2 class="section-title">Core Values</h2>
                <div class="values-grid">
                    <div class="value-card">
                        <i class="fas fa-shield-alt"></i>
                        <h3>Integrity</h3>
                        <p>Ensuring every test session is fair and free from malpractice.</p>
                    </div>
                    <div class="value-card">
                        <i class="fas fa-lightbulb"></i>
                        <h3>Innovation</h3>
                        <p>Constantly evolving with AI and analytics to enhance assessments.</p>
                    </div>
                    <div class="value-card">
                        <i class="fas fa-users"></i>
                        <h3>User-First</h3>
                        <p>Designing intuitive experiences for students, educators, and admins alike.</p>
                    </div>
                    <div class="value-card">
                        <i class="fas fa-lock"></i>
                        <h3>Security</h3>
                        <p>Protecting data privacy and upholding rigorous security standards.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Team -->
        <section class="team-section">
            <div class="container">
                <h2 class="section-title">Developed By</h2>
                <div class="team-grid">
                    <div class="team-card">
                        <div class="avatar"><i class="fas fa-user-tie"></i></div>
                        <h4>Harshdeep Sharma</h4>
                        <span class="role">Intern</span>
                    </div>

                </div>
            </div>
        </section>
    </main>

    <!-- ===== Footer ===== -->
    <footer class="main-footer">
        <div class="container">
            <div class="footer-bottom">
                <div class="footer-bottom-content">
                    <p>&copy; 2025 Parikshan – Online Testing Platform</p>
                </div>
            </div>
        </div>
    </footer>
</body>
</html>
