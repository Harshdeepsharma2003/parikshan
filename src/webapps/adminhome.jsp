<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan -Admin Dashboard</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/adminhome.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

    <!-- Teacher Dashboard Section -->
    <section class="teacher-dashboard">
        <div class="container">
            <h2 class="section-title">Admin Dashboard</h2>
            <p class="section-subtitle">Create tests, track student progress, and manage your courses</p>

            <div class="teacher-features-grid">
                <div class="teacher-feature-card">
                    <a href="createtest.jsp">
                        <div class="feature-icon">
                            <i class="fas fa-plus-circle"></i>
                        </div>
                        <h3>Create Test</h3>
                        <p>Design new tests with multiple question types, set time limits, and configure grading parameters.</p>
                        <div class="feature-benefits">
                            <span>MCQ Questions</span>
                            <span>Auto Grading</span>
                            <span>Time Control</span>
                        </div>
                        <div class="feature-action">
                            <span>Create New Test</span>
                            <i class="fas fa-arrow-right"></i>
                        </div>
                    </a>
                </div>

                <div class="teacher-feature-card">
                    <a href="managetest.jsp">
                        <div class="feature-icon">
                            <i class="fas fa-edit"></i>
                        </div>
                        <h3>Manage Tests</h3>
                        <p>Edit existing tests, update questions, modify schedules, and control test availability.</p>
                        <div class="feature-benefits">
                            <span>Edit Tests</span>
                            <span>Schedule Tests</span>
                            <span>Test Settings</span>
                        </div>
                        <div class="feature-action">
                            <span>Manage Tests</span>
                            <i class="fas fa-arrow-right"></i>
                        </div>
                    </a>
                </div>

                <div class="teacher-feature-card">
                    <a href="DisplayStudentResults">
                        <div class="feature-icon">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3>Student Results</h3>
                        <p>View detailed student performance, analyze test results, and generate progress reports.</p>
                        <div class="feature-benefits">
                            <span>Grade Analysis</span>
                            <span>Performance Tracking</span>
                            <span>Export Reports</span>
                        </div>
                        <div class="feature-action">
                            <span>View Results</span>
                            <i class="fas fa-arrow-right"></i>
                        </div>
                    </a>
                </div>

            </div>
        </div>
    </section>

    <!-- Footer -->
    <div id="footer-container"></div>

    <script src="js/adminhome.js"></script>
</body>
</html>