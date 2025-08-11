<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan - Delete Profile</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/deleteprofilestudent.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

            <!-- Delete Profile Form -->
            <div class="form-card delete-card">
                <h3><i class="fas fa-user-times"></i> Confirm Account Deletion</h3>
                <form id="deleteProfileForm" class="delete-form" action="DeleteProfileStudent" method="get" onsubmit="return confirmDeletion();">

                    <div class="form-grid">
                        <div class="form-group with-icon">
                            <label for="studentId"> ID</label>
                            <input type="text" name="studentId" id="studentid" required placeholder="Enter your  ID">
                            <i class="fas fa-id-card form-icon"></i>
                        </div>

                        <div class="form-group with-icon">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required placeholder="Confirm your password">
                            <i class="fas fa-lock form-icon"></i>
                        </div>
                    </div>

                    <!-- Confirmation Checkbox -->
                    <div class="confirmation-section">
                        <div class="checkbox-group">
                            <input type="checkbox" id="confirmDelete" required>
                            <label for="confirmDelete">
                                <span class="checkmark"></span>
                                I understand that this action is permanent and cannot be undone
                            </label>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="button" class="btn btn-secondary" onclick="goBack()">
                            <i class="fas fa-arrow-left"></i>
                            Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt"></i>
                            Delete Profile
                        </button>
                    </div>
                    <div id="formMessage" class="form-message"></div>
                </form>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <div id="footer-container"></div>

     <script src="js/deleteprofilestudent.js"></script>
</body>
</html>
