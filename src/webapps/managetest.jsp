<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Tests - Parikshan</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/managetest.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

</head>
<body>
    <!-- Loading Overlay -->
    <div class="loading-overlay" id="loadingOverlay">
        <div class="loading-spinner">
            <div class="spinner"></div>
            <p>Processing your request...</p>
        </div>
    </div>

    <!-- Header -->
    <div id="header-container"></div>

    <!-- Navbar -->
    <div id="navbar-container"></div>

    <!-- Main Content -->
    <main>
        <section class="manage-section">
            <div class="container">
                <div class="manage-container">
                    <!-- Page Header -->
                    <div class="page-header">
                        <h1>Manage <span class="brand-highlight">Tests</span></h1>
                        <p>Edit existing tests or remove tests that are no longer needed</p>
                    </div>

                    <!-- Error/Success Messages -->
                    <%
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    String successMessage = (String) request.getAttribute("successMessage");

                    if (errorMessage != null) {
                    %>
                    <div class="error-container" id="errorContainer">
                        <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;">
                            <i class="fas fa-exclamation-triangle" style="color: #dc2626;"></i>
                            <strong style="color: #dc2626;">Error</strong>
                        </div>
                        <p style="margin: 0; color: #7f1d1d;"><%= errorMessage %></p>
                    </div>
                    <% } %>

                    <% if (successMessage != null) { %>
                    <div class="success-message" id="successMessage">
                        <i class="fas fa-check-circle"></i>
                        <span><%= successMessage %></span>
                    </div>
                    <% } %>

                    <!-- Forms Grid -->
                    <div class="forms-grid">

                        <!-- Edit Test Form -->
                        <div class="form-card edit-card">
                            <div class="form-header">
                                <div class="form-icon">
                                    <i class="fas fa-edit"></i>
                                </div>
                                <div>
                                    <h2>Edit Test</h2>
                                </div>
                            </div>

                            <p class="form-description">
                                Modify existing test details, questions, or configuration. Enter the test ID and number of questions to proceed to the edit interface.
                            </p>

                            <form id="verifyTestForm" action="ManageTest" method="post" autocomplete="off" novalidate>
                                <div class="form-group">
                                    <label for="editTestid">
                                        <i class="fas fa-hashtag"></i>
                                        Test ID
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-fingerprint"></i>
                                        <input
                                            type="text"
                                            id="editTestid"
                                            name="editTestid"
                                            class="form-input"
                                            placeholder="Enter unique test ID"
                                            required
                                            value="${param.editTestid}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="editTestid-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="noOfQuestions">
                                        <i class="fas fa-list-ol"></i>
                                        Number of Questions
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-calculator"></i>
                                        <input
                                            type="number"
                                            id="noOfQuestions"
                                            name="noOfQuestions"
                                            class="form-input"
                                            placeholder="Total number of questions"
                                            min="1"
                                            max="200"
                                            required
                                            value="${param.noOfQuestions}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="noOfQuestions-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>

                                <button type="submit" class="form-submit" id="editSubmitBtn">
                                    <i class="fas fa-search"></i>
                                    Verify & Edit
                                </button>
                            </form>
                        </div>

                        <!-- Delete Test Form -->
                        <div class="form-card delete-card">
                            <div class="form-header">
                                <div class="form-icon">
                                    <i class="fas fa-trash-alt"></i>
                                </div>
                                <div>
                                    <h2>Delete Test</h2>
                                </div>
                            </div>

                            <p class="form-description">
                                Permanently remove a test and all associated data. This action cannot be undone, so please verify the test ID carefully.
                            </p>

                            <!-- Warning Section -->
                            <div class="warning-section">
                                <div class="warning-header">
                                    <i class="fas fa-exclamation-triangle warning-icon"></i>
                                    <h4 class="warning-title">Warning</h4>
                                </div>
                                <p class="warning-text">
                                    Deleting a test will permanently remove all questions, student responses, and results. This action cannot be undone.
                                </p>
                            </div>

                            <form id="deleteTestForm" action="DeleteTest" method="post" autocomplete="off" novalidate>
                                <div class="form-group">
                                    <label for="deleteTestid">
                                        <i class="fas fa-hashtag"></i>
                                        Test ID
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-fingerprint"></i>
                                        <input
                                            type="text"
                                            id="deleteTestid"
                                            name="deleteTestid"
                                            class="form-input"
                                            placeholder="Enter test ID to delete"
                                            required
                                            value="${param.deleteTestid}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="deleteTestid-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>

                                <button type="submit" class="form-submit" id="deleteSubmitBtn">
                                    <i class="fas fa-trash"></i>
                                    Verify & Delete
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- Navigation Links -->
                    <div style="text-align: center; margin-top: var(--space-3xl);">
                        <div style="display: flex; justify-content: center; gap: var(--space-lg); flex-wrap: wrap;">
                            <a href="registerTest.jsp" style="color: var(--primary-color); text-decoration: none; font-weight: 500; display: flex; align-items: center; gap: var(--space-xs);">
                                <i class="fas fa-plus-circle"></i>
                                Create New Test
                            </a>
                            <a href="dashboard.jsp" style="color: var(--text-secondary); text-decoration: none; font-weight: 500; display: flex; align-items: center; gap: var(--space-xs);">
                                <i class="fas fa-home"></i>
                                Back to Dashboard
                            </a>
                            <a href="viewTests.jsp" style="color: var(--success-color); text-decoration: none; font-weight: 500; display: flex; align-items: center; gap: var(--space-xs);">
                                <i class="fas fa-list"></i>
                                View All Tests
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <div id="footer-container"></div>
     <script src="js/managetest.js"></script>
</body>
</html>
