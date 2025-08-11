<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Verify Test</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/testloginstudent.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

    <!-- Test Verification Form Section -->
    <section class="test-verify-section">
        <div class="container">
            <h2 class="section-title">Verify Test</h2>
            <p class="section-subtitle">Enter your test ID to access your examination</p>

            <form id="verifyTestForm" class="verify-form" action="TestLogin" method="post" autocomplete="off">
                <div class="form-group with-icon">
                    <label for="testid">Test ID</label>
                    <input type="text" id="testid" name="testid" placeholder="Enter unique test ID" required>
                    <i class="fas fa-clipboard-list form-icon"></i>
                </div>

                <div class="submit-section">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check-circle"></i>
                        Verify Test
                    </button>
                </div>
            </form>

            <div class="info-section">
                <div class="info-card">
                    <i class="fas fa-info-circle"></i>
                    <p>Make sure you have the correct Test ID provided by your instructor</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Quick Access Section -->
    <section class="quick-access-section">
        <div class="container">
            <div class="quick-access-content">
                <h3>Need Help?</h3>
                <p>If you don't have a Test ID or facing issues</p>
                <div class="quick-buttons">

                    <a class="btn btn-secondary">
                        <i class="fas fa-question-circle"></i>
                        Mail us - sharmaharshdeep6@gmail.com
                    </a>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <div id="footer-container"></div>

    <script src="js/testloginstudent.js"></script>

</body>
</html>
