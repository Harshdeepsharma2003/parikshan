<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Instructions - Parikshan</title>

    <!-- Shared styles / icons -->
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/instructions.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">


</head>
<body>
    <!-- Header / Navbar placeholders -->
    <div id="header-container"></div>
    <div id="navbar-container"></div>

    <main>
        <section class="instruction-section">
            <div class="container">
                <div class="instruction-container">
                    <!-- Header Section -->
                    <div class="instruction-header">
                        <h1><i class="fas fa-clipboard-list"></i> Test Instructions</h1>
                        <div class="test-details">
                            <div class="test-details-grid">
                                <div class="detail-item">
                                    <i class="fas fa-file-alt detail-icon"></i>
                                    <div>
                                        <strong>Test: ${testTitle != null ? testTitle : 'Online Assessment'}</strong>
                                    </div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-question-circle detail-icon"></i>
                                    <div>
                                        <strong>${totalQuestions != null ? totalQuestions : '25'} Questions</strong>
                                    </div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-clock detail-icon"></i>
                                    <div>
                                        <strong>${timeLimit != null ? timeLimit : '45'} Minutes</strong>
                                    </div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-video detail-icon"></i>
                                    <div>
                                        <strong>Proctored Test</strong>
                                    </div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-robot detail-icon"></i>
                                    <div>
                                        <strong>Auto-Submit</strong>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Permissions Section -->
                    <div class="instruction-content">
                        <div class="permissions-box">
                            <div class="permissions-header">
                                <i class="fas fa-shield-alt permissions-icon"></i>
                                <strong>Required Permissions</strong>
                            </div>
                            <p style="margin-bottom: var(--space-md); color: #1565c0;">
                                This test requires camera and microphone access for proctoring purposes. Please grant permissions to proceed.
                            </p>

                            <div class="permission-item">
                                <i class="fas fa-video" style="margin-right: var(--space-sm); color: #1976d2;"></i>
                                <span>Camera Access</span>
                                <span class="permission-status status-pending" id="cameraStatus">Pending</span>
                            </div>

                            <div class="permission-item">
                                <i class="fas fa-microphone" style="margin-right: var(--space-sm); color: #1976d2;"></i>
                                <span>Microphone Access</span>
                                <span class="permission-status status-pending" id="micStatus">Pending</span>
                            </div>

                            <button class="btn-permissions" onclick="requestPermissions()">
                                <i class="fas fa-unlock-alt"></i> Grant Permissions
                            </button>
                        </div>

                        <!-- Timer Information -->
                        <div class="timer-info">
                            <i class="fas fa-stopwatch timer-icon"></i>
                            <h3 style="margin: 0 0 var(--space-sm) 0; color: #f57c00;">Timer & Auto-Submit</h3>
                            <p style="margin: 0;">
                                The test will automatically submit when the timer reaches 00:00.
                                You will receive warnings at 10 minutes and 2 minutes remaining.
                            </p>
                        </div>

                        <!-- Instructions Content -->
                        <h2 style="margin-bottom: var(--space-lg); color: var(--primary-color);">
                            <i class="fas fa-info-circle"></i> Please Read Carefully
                        </h2>

                        <ul class="instruction-list">
                            <li class="instruction-item">
                                <div class="instruction-number">1</div>
                                <div class="instruction-text">
                                    <strong>Test Format:</strong> This is a Multiple Choice Question (MCQ) test. Each question has multiple options with only one correct answer.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">2</div>
                                <div class="instruction-text">
                                    <strong>Time Management:</strong> You have ${timeLimit != null ? timeLimit : '45'} minutes to complete all questions. A countdown timer will be visible throughout the test.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">3</div>
                                <div class="instruction-text">
                                    <strong>Auto-Submit:</strong> The test will automatically submit when time expires. Ensure you answer all questions before the timer reaches zero.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">4</div>
                                <div class="instruction-text">
                                    <strong>Proctoring:</strong> Your camera and microphone will monitor the test session. Ensure proper lighting and remain visible throughout the test.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">5</div>
                                <div class="instruction-text">
                                    <strong>Navigation:</strong> Use "Previous" and "Next" buttons to navigate between questions. Click question numbers for direct navigation.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">6</div>
                                <div class="instruction-text">
                                    <strong>Answering:</strong> Select one option per question. You can change answers anytime before submission or time expiry.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">7</div>
                                <div class="instruction-text">
                                    <strong>Browser Guidelines:</strong> Stay on the test page. Do not switch tabs, minimize browser, or open other applications during the test.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">8</div>
                                <div class="instruction-text">
                                    <strong>Technical Requirements:</strong> Ensure stable internet connection. Test will pause if connection is lost and resume when reconnected.
                                </div>
                            </li>

                            <li class="instruction-item">
                                <div class="instruction-number">9</div>
                                <div class="instruction-text">
                                    <strong>Final Submission:</strong> Review all answers before manual submission. Auto-submission occurs at time expiry with no further modifications possible.
                                </div>
                            </li>
                        </ul>

                        <!-- Warning Box -->
                        <div class="warning-box">
                            <div class="warning-header">
                                <i class="fas fa-exclamation-triangle warning-icon"></i>
                                <strong>Important Guidelines:</strong>
                            </div>
                            <ul style="margin: 0; padding-left: var(--space-lg);">
                                <li>Maintain proper posture and stay within camera frame</li>
                                <li>No external help, books, or electronic devices allowed</li>
                                <li>Speaking during the test may be flagged for review</li>
                                <li>Any suspicious activity will be recorded and reported</li>
                                <li>Ensure quiet environment with minimal interruptions</li>
                                <li>Keep your ID document ready for verification if required</li>
                            </ul>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <a href="availableTests.jsp" class="btn-back">
                            <i class="fas fa-arrow-left"></i> Back to Tests
                        </a>
                        <form action="startTest.jsp" method="post" style="margin: 0;">
                            <input type="hidden" name="testId" value="${testId}">
                            <button type="submit" class="btn-start" id="startTestBtn" disabled>
                                <i class="fas fa-play"></i> Start Test Now
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <div id="footer-container"></div>
 <script src="js/instructions.js"></script>
</body>
</html>
