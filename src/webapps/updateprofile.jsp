<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Update Profile</title>
    <link rel="stylesheet" href="css/studenthome.css">
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/updateprofilestudent.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

    <!-- Update Profile Section -->
    <section class="simple-profile-section">
        <div class="container">
            <h2 class="section-title">Update Profile</h2>
            <p class="section-subtitle">Manage your personal information</p>

            <!-- Fetch Profile Form -->
            <div class="form-card fetch-card">
                <h3><i class="fas fa-search"></i> Load Profile</h3>
                <form class="simple-form" action="UpdateProfileStudent" method="get">
                    <div class="form-group with-icon">
                        <label for="studentId"> ID</label>
                        <input type="text" name="studentId" id="studentid" required placeholder="Enter your Student ID">
                        <i class="fas fa-id-card form-icon"></i>
                    </div>
                    <button type="submit" class="btn btn-primary">
                       <i class="fas fa-save"></i>
                       Fetch Profile
                    </button>
                </form>
            </div>

            <!-- Update Profile Form -->
            <div class="form-card update-card">
                <h3><i class="fas fa-user-edit"></i> Update Information</h3>
                <form id="updateProfileStudentForm" class="simple-form" action="UpdateProfileStudent" method="post" autocomplete="off" onsubmit="return validateForm();">

                    <div class="form-grid">
                        <!-- Row 1 -->
                        <div class="form-group with-icon readonly">
                            <label for="studentId">ID</label>
                            <input type="text" id="studentid" name="studentId" value="${Student.studentId}" readonly>
                            <i class="fas fa-id-card form-icon"></i>
                        </div>
                        <div class="form-group with-icon">
                            <label for="username">Username</label>
                            <input type="text" id="name" name="name" value="${Student.name}" required placeholder="Enter your name">
                            <i class="fas fa-user form-icon"></i>
                        </div>

                        <!-- Row 2 -->
                        <div class="form-group with-icon">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="${Student.email}" required placeholder="Enter your email">
                            <i class="fas fa-envelope form-icon"></i>
                        </div>
                        <div class="form-group with-icon">
                            <label for="phone">Phone</label>
                            <input type="tel" id="phone" name="phone" value="${Student.phone}" required placeholder="Enter your phone">
                            <i class="fas fa-phone form-icon"></i>
                        </div>

                        <!-- Row 3 - Full Width -->
                        <div class="form-group with-icon full-width">
                            <label for="password">New Password</label>
                            <input type="password" id="password" name="password" placeholder="Leave blank to keep current password">
                            <i class="fas fa-lock form-icon"></i>
                            <small>Leave blank to keep your current password</small>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            Update Profile
                        </button>
                    </div>
                    <div id="formMessage" class="form-message"></div>
                </form>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <div id="footer-container"></div>

    <script>
        // Load external components
        fetch('html/headerhomestudent.html').then(response => response.text()).then(data => {
            document.getElementById('header-container').innerHTML = data;
        }).catch(error => console.error('Error loading header:', error));

        fetch('html/navbarhomestudent.html').then(response => response.text()).then(data => {
            document.getElementById('navbar-container').innerHTML = data;
        }).catch(error => console.error('Error loading navbar:', error));

        fetch('html/footerstudenthome.html').then(response => response.text()).then(data => {
            document.getElementById('footer-container').innerHTML = data;
        }).catch(error => console.error('Error loading footer:', error));

        // Form validation
        function validateForm() {
            try {
                var name = document.getElementById('name').value.trim();
                var email = document.getElementById('email').value.trim();
                var phone = document.getElementById('phone').value.trim();
                var password = document.getElementById('password').value;
                var messageDiv = document.getElementById('formMessage');

                // Clear previous messages
                messageDiv.innerHTML = '';
                messageDiv.className = 'form-message';

                // Validation
                if (!name) {
                    showMessage('Please enter your name.', 'error');
                    return false;
                }

                if (!email || !isValidEmail(email)) {
                    showMessage('Please enter a valid email address.', 'error');
                    return false;
                }

                if (!phone) {
                    showMessage('Please enter your phone number.', 'error');
                    return false;
                }

                if (password && password.length < 6) {
                    showMessage('Password must be at least 6 characters long.', 'error');
                    return false;
                }

                // Add loading state
                var submitBtn = document.querySelector('#updateProfileStudentForm button[type="submit"]');
                submitBtn.classList.add('loading');
                submitBtn.disabled = true;

                showMessage('Updating profile...', 'info');
                return true;
            } catch (error) {
                console.error('Validation error:', error);
                showMessage('An error occurred. Please try again.', 'error');
                return false;
            }
        }

        function isValidEmail(email) {
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        }

        function showMessage(message, type) {
            var messageDiv = document.getElementById('formMessage');
            var iconMap = {
                'success': 'check-circle',
                'error': 'exclamation-circle',
                'info': 'info-circle'
            };

            messageDiv.innerHTML = '<i class="fas fa-' + iconMap[type] + '"></i> ' + message;
            messageDiv.className = 'form-message ' + type;
            messageDiv.style.display = 'block';
        }

        // Global error handling
        window.addEventListener('error', function(e) {
            console.error('Error:', e.error);
            showMessage('Something went wrong. Please refresh the page.', 'error');
        });
    </script>
</body>
</html>
