<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register New Test - Parikshan</title>
    <link rel="stylesheet" href="css/common.css">
     <link rel="stylesheet" href="css/createtest.css">
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
        <section class="form-section">
            <div class="container">
                <div class="form-container">

                    <!-- Server-side Error Display -->
                    <%
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    String errorDetails = (String) request.getAttribute("errorDetails");
                    String successMessage = (String) request.getAttribute("successMessage");

                    if (errorMessage != null) {
                    %>
                    <div class="error-container" id="errorContainer">
                        <div class="error-header">
                            <i class="fas fa-exclamation-triangle error-icon"></i>
                            <h3 class="error-title">Something went wrong</h3>
                        </div>
                        <p class="error-message"><%= errorMessage %></p>

                        <% if (errorDetails != null) { %>
                        <details class="error-details">
                            <summary>Error Details</summary>
                            <div class="error-code"><%= errorDetails %></div>
                        </details>
                        <% } %>

                        <div class="error-actions">
                            <a href="<%= request.getContextPath() %>/landingpage.jsp" class="btn-error">
                                <i class="fas fa-home"></i>
                                Back to Home
                            </a>
                            <button type="button" class="btn-secondary-error" onclick="location.reload()">
                                <i class="fas fa-redo"></i>
                                Try Again
                            </button>
                            <button type="button" class="btn-secondary-error" onclick="reportError()">
                                <i class="fas fa-bug"></i>
                                Report Issue
                            </button>
                        </div>
                    </div>
                    <% } %>

                    <!-- Success Message -->
                    <% if (successMessage != null) { %>
                    <div class="success-message" id="successMessage">
                        <i class="fas fa-check-circle"></i>
                        <span><%= successMessage %></span>
                    </div>
                    <% } %>

                    <div class="form-header">
                        <h2>Create New <span class="brand-highlight">Test</span></h2>
                        <p>Set up your comprehensive test configuration</p>
                    </div>

                    <div class="test-form-card">
                        <form id="registerTestForm" action="RegisterTest" method="post" autocomplete="off" novalidate>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="testid">
                                        <i class="fas fa-hashtag"></i>
                                        Test ID
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-fingerprint"></i>
                                        <input
                                            type="text"
                                            id="testid"
                                            name="testid"
                                            class="form-input"
                                            placeholder="e.g., TEST_2025_001"
                                            required
                                            value="${param.testid}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="testid-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="testTitle">
                                        <i class="fas fa-heading"></i>
                                        Test Title
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-file-alt"></i>
                                        <input
                                            type="text"
                                            id="testTitle"
                                            name="testTitle"
                                            class="form-input"
                                            placeholder="Enter descriptive test title"
                                            required
                                            value="${param.testTitle}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="testTitle-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="noofquestions">
                                        <i class="fas fa-list-ol"></i>
                                        Total Questions
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <div class="input-icon">
                                        <i class="fas fa-calculator"></i>
                                        <input
                                            type="number"
                                            id="noofquestions"
                                            name="noofquestions"
                                            class="form-input"
                                            placeholder="Enter number"
                                            min="1"
                                            max="200"
                                            required
                                            value="${param.noofquestions}"
                                        />
                                    </div>
                                    <div class="field-error-message" id="noofquestions-error" style="display: none;">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <span></span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="testDesc">
                                        <i class="fas fa-align-left"></i>
                                        Description
                                    </label>
                                    <textarea
                                        id="testDesc"
                                        name="testDesc"
                                        class="form-input"
                                        placeholder="Test description..."
                                        style="min-height: 100px;"
                                    >${param.testDesc}</textarea>
                                </div>
                            </div>

                            <div class="form-actions" style="display: flex; justify-content: space-between; margin-top: 2rem;">

                                <button type="submit" class="form-submit" id="submitBtn">
                                    Next: Add Questions
                                    <i class="fas fa-arrow-right"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <div id="footer-container"></div>
 <script src="js/createtest.js"></script>
</body>
</html>
