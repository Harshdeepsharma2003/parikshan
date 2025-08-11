// Use variables from JSP (declared globally in HTML)
// These should be available from the inline script in JSP
let currentQuestion = 1;
let timeRemaining = 1800; // 30 minutes = 1800 seconds
let timerInterval = null;
let answers = {};

// Recording variables
let mediaRecorder = null;
let recordedChunks = [];
let stream = null;
let recordingStartTime = null;
let recordingTimer = null;
let isRecording = false;

// Initialization flag
let isInitialized = false;

console.log("External JS loaded");

// Function to start all test systems
window.startTestSystems = function() {
    if (isInitialized) {
        console.log("Already initialized, skipping");
        return;
    }

    console.log("Starting test systems with config:", window.MCQ_CONFIG);

    // Validate required variables
    if (typeof totalQuestions === 'undefined' || typeof userid === 'undefined' || typeof testId === 'undefined') {
        console.error("Required variables not found:", {
            totalQuestions: typeof totalQuestions,
            userid: typeof userid,
            testId: typeof testId
        });
        return;
    }

    isInitialized = true;

    console.log("Initializing test with", totalQuestions, "questions for user", userid);

    // Start timer IMMEDIATELY
    startTimer();

    // Start camera/mic IMMEDIATELY
    initializeRecording();

    // Show first question and update UI
    showQuestion(1);
    updateProgress();
    updateNavigationButtons();
    updateQuestionNavigationButtons();

    console.log("Test systems initialization complete");
};

// Force start functions for fallback
window.forceStartTimer = function() {
    console.log("Force starting timer");
    if (!timerInterval) {
        startTimer();
    }
};

window.forceStartRecording = function() {
    console.log("Force starting recording");
    initializeRecording();
};

// Initialize as soon as this script loads if DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', window.startTestSystems);
} else {
    // DOM already loaded
    setTimeout(window.startTestSystems, 50);
}

// TIMER FUNCTIONS
function startTimer() {
    console.log("Starting timer with", timeRemaining, "seconds");

    // Clear any existing timer
    if (timerInterval) {
        clearInterval(timerInterval);
        timerInterval = null;
    }

    // Update display immediately
    updateTimerDisplay();

    // Start countdown
    timerInterval = setInterval(function() {
        timeRemaining--;
        updateTimerDisplay();

        // Apply warning styles
        const timerElement = document.getElementById('timer');
        if (timerElement) {
            timerElement.classList.remove('warning', 'critical');
            if (timeRemaining <= 300) { // 5 minutes
                timerElement.classList.add('critical');
            } else if (timeRemaining <= 600) { // 10 minutes
                timerElement.classList.add('warning');
            }
        }

        // Auto-submit when time expires
        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            timerInterval = null;
            autoSubmitTest();
        }
    }, 1000);
}

function updateTimerDisplay() {
    const minutes = Math.floor(Math.max(0, timeRemaining) / 60);
    const seconds = Math.max(0, timeRemaining) % 60;
    const timerElement = document.getElementById('timer');

    if (timerElement) {
        const timeString = minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');
        timerElement.textContent = timeString;
        console.log("Timer updated:", timeString);
    }
}

function autoSubmitTest() {
    console.log("Time expired - auto submitting");

    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'flex';
    }

    // Stop recording before submitting
    if (isRecording && mediaRecorder && mediaRecorder.state === 'recording') {
        stopRecording();
    }

    // Give a moment for recording to stop, then submit
    setTimeout(function() {
        const form = document.getElementById('testForm');
        if (form) {
            form.submit();
        }
    }, 2000);
}

// RECORDING INITIALIZATION
async function initializeRecording() {
    const recordingStatus = document.getElementById('recordingStatus');
    const recordingDot = document.getElementById('recordingDot');
    const videoPreview = document.getElementById('videoPreview');

    console.log("Starting recording initialization...");

    // Update status immediately
    if (recordingStatus) recordingStatus.textContent = "Requesting camera/microphone access...";

    try {
        // Check if getUserMedia is supported
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
            throw new Error('Camera/microphone not supported in this browser');
        }

        // Enhanced constraints for better compatibility
        const constraints = {
            video: {
                width: { ideal: 640, max: 1280 },
                height: { ideal: 480, max: 720 },
                facingMode: 'user',
                frameRate: { ideal: 15, max: 30 }
            },
            audio: {
                echoCancellation: true,
                noiseSuppression: true,
                autoGainControl: true
            }
        };

        console.log("Requesting media permissions...");
        const localStream = await navigator.mediaDevices.getUserMedia(constraints);

        if (!localStream) {
            throw new Error('No media stream received');
        }

        stream = localStream;
        console.log("✅ Media stream obtained successfully");
        console.log("Video tracks:", stream.getVideoTracks().length);
        console.log("Audio tracks:", stream.getAudioTracks().length);

        // Attach preview with error handling
        if (videoPreview) {
            videoPreview.srcObject = stream;

            // Handle video loading
            videoPreview.onloadedmetadata = function() {
                console.log("Video metadata loaded");
                videoPreview.play().then(() => {
                    console.log("✅ Video preview started successfully");
                    if (recordingStatus) recordingStatus.textContent = "Camera preview active";
                }).catch(playErr => {
                    console.warn("Video autoplay failed:", playErr);
                    if (recordingStatus) recordingStatus.textContent = "Camera ready (click to activate preview)";
                    // Try to play on first user interaction
                    videoPreview.addEventListener('click', function() {
                        videoPreview.play();
                    }, { once: true });
                });
            };

            videoPreview.onerror = function(error) {
                console.error("Video preview error:", error);
            };
        }

        // Setup and start recording
        await setupMediaRecorder();

        // Start recording if recorder is ready
        if (mediaRecorder) {
            startRecording();
            if (recordingStatus) recordingStatus.textContent = "Recording active";
            if (recordingDot) {
                recordingDot.classList.remove('inactive');
                recordingDot.classList.add('active');
            }
        } else {
            if (recordingStatus) recordingStatus.textContent = "Camera active (recording not supported)";
            if (recordingDot) recordingDot.classList.add('inactive');
        }

        // Hide retry button if it was shown
        const retryBtn = document.getElementById('retryRecordingBtn');
        if (retryBtn) retryBtn.style.display = 'none';

    } catch (error) {
        console.error("❌ Error initializing recording:", error);

        let errorMessage = "Camera/microphone access failed: ";
        if (error.name === 'NotAllowedError') {
            errorMessage = "Permission denied. Please allow camera and microphone access and refresh the page.";
        } else if (error.name === 'NotFoundError') {
            errorMessage = "No camera or microphone found on this device.";
        } else if (error.name === 'NotSupportedError') {
            errorMessage = "Camera/microphone not supported in this browser.";
        } else {
            errorMessage += error.message || "Unknown error";
        }

        if (recordingStatus) recordingStatus.textContent = errorMessage;
        if (recordingDot) recordingDot.classList.add('inactive');

        // Show retry button
        const retryBtn = document.getElementById('retryRecordingBtn');
        if (retryBtn) retryBtn.style.display = 'inline-block';

        console.log("Showing user-friendly error message");
    }
}

// IMPROVED MEDIA RECORDER SETUP
async function setupMediaRecorder() {
    try {
        if (typeof MediaRecorder === 'undefined') {
            console.warn("MediaRecorder not supported in this browser");
            mediaRecorder = null;
            return;
        }

        if (!stream) {
            console.error("No stream available for MediaRecorder");
            return;
        }

        // Test different MIME types for better compatibility
        let options = {};
        const mimeTypes = [
            'video/webm;codecs=vp9,opus',
            'video/webm;codecs=vp8,opus',
            'video/webm;codecs=vp9',
            'video/webm;codecs=vp8',
            'video/webm',
            'video/mp4'
        ];

        for (const mimeType of mimeTypes) {
            if (MediaRecorder.isTypeSupported(mimeType)) {
                options.mimeType = mimeType;
                console.log("Using MIME type:", mimeType);
                break;
            }
        }

        mediaRecorder = new MediaRecorder(stream, options);

        mediaRecorder.onstart = function() {
            console.log("✅ MediaRecorder started");
            isRecording = true;
        };

        mediaRecorder.ondataavailable = function(event) {
            if (event.data && event.data.size > 0) {
                recordedChunks.push(event.data);
                console.log("Recording chunk received:", event.data.size, "bytes");
            }
        };

        mediaRecorder.onerror = function(event) {
            console.error("MediaRecorder error:", event);
            isRecording = false;
        };

        mediaRecorder.onstop = function() {
            console.log("MediaRecorder stopped, total chunks:", recordedChunks.length);
            isRecording = false;
            // Save recording asynchronously
            if (recordedChunks.length > 0) {
                setTimeout(() => saveRecordingToDatabase(), 500);
            }
        };

        console.log("✅ MediaRecorder setup complete");
    } catch (error) {
        console.error("Error setting up MediaRecorder:", error);
        mediaRecorder = null;
    }
}

function startRecording() {
    if (!mediaRecorder) {
        console.warn("MediaRecorder not available");
        return;
    }

    if (mediaRecorder.state === 'inactive') {
        try {
            recordedChunks = [];
            mediaRecorder.start(1000); // Record in 1-second chunks
            recordingStartTime = Date.now();

            // Start duration timer
            if (recordingTimer) {
                clearInterval(recordingTimer);
            }
            recordingTimer = setInterval(updateRecordingDuration, 1000);

            console.log("✅ Recording started successfully");
        } catch (error) {
            console.error("Error starting recording:", error);
            isRecording = false;
        }
    } else {
        console.log("MediaRecorder not in inactive state:", mediaRecorder.state);
    }
}

function stopRecording() {
    if (mediaRecorder && mediaRecorder.state === 'recording') {
        try {
            mediaRecorder.stop();
            console.log("Recording stop initiated");
        } catch (error) {
            console.error("Error stopping recording:", error);
        }
    }

    if (recordingTimer) {
        clearInterval(recordingTimer);
        recordingTimer = null;
    }

    isRecording = false;
}

function updateRecordingDuration() {
    if (recordingStartTime) {
        const elapsed = Math.floor((Date.now() - recordingStartTime) / 1000);
        const minutes = Math.floor(elapsed / 60);
        const seconds = elapsed % 60;

        const durationElement = document.getElementById('recordingDuration');
        if (durationElement) {
            durationElement.textContent = minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');
        }
    }
}

function testCameraMic() {
    const videoPreview = document.getElementById('videoPreview');
    const recordingStatus = document.getElementById('recordingStatus');

    if (stream && stream.active) {
        const videoTracks = stream.getVideoTracks();
        const audioTracks = stream.getAudioTracks();

        let message = "✅ Camera/Microphone Test Results:\n\n";
        message += "Video tracks: " + videoTracks.length + (videoTracks.length > 0 ? " (Active)" : " (None)") + "\n";
        message += "Audio tracks: " + audioTracks.length + (audioTracks.length > 0 ? " (Active)" : " (None)") + "\n";
        message += "Recording support: " + (mediaRecorder ? "Yes" : "No") + "\n";
        message += "Current status: " + (recordingStatus ? recordingStatus.textContent : "Unknown");

        alert(message);
    } else {
        alert("❌ Camera/microphone not accessible. Retrying initialization...");
        initializeRecording();
    }
}

async function saveRecordingToDatabase() {
    if (recordedChunks.length === 0) {
        console.log("No recording data to save");
        return;
    }

    try {
        const recordingBlob = new Blob(recordedChunks, {
            type: mediaRecorder && mediaRecorder.mimeType ? mediaRecorder.mimeType : 'video/webm'
        });

        console.log("Saving recording blob size:", recordingBlob.size, "bytes");

        const formData = new FormData();
        formData.append('recording', recordingBlob, 'test-recording.webm');
        formData.append('testid', testId);
        formData.append('userid', userid);

        const response = await fetch('recordingUpload', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const result = await response.text();
            console.log("✅ Recording saved successfully:", result);
        } else {
            console.error("Failed to save recording, status:", response.status);
        }

    } catch (error) {
        console.error("Error saving recording:", error);
    }
}

// QUESTION NAVIGATION FUNCTIONS
function showQuestion(questionNumber) {
    console.log("Showing question:", questionNumber);

    const allQuestions = document.querySelectorAll('.question-card');
    allQuestions.forEach(q => {
        q.classList.remove('active');
        q.style.display = 'none';
    });

    const summary = document.getElementById('testSummary');
    if (summary) {
        summary.classList.remove('active');
        summary.style.display = 'none';
    }

    // Show current question
    const currentQuestionElement = document.querySelector(`.question-card[data-question="${questionNumber}"]`);
    if (currentQuestionElement) {
        currentQuestionElement.classList.add('active');
        currentQuestionElement.style.display = 'block';
    }

    currentQuestion = questionNumber;
    updateProgress();
    updateNavigationButtons();
    updateQuestionNavigationButtons();
}

function goToQuestion(questionNumber) {
    showQuestion(questionNumber);
}

function nextQuestion() {
    if (currentQuestion < totalQuestions) {
        showQuestion(currentQuestion + 1);
    } else {
        showSummary();
    }
}

function previousQuestion() {
    if (currentQuestion > 1) {
        showQuestion(currentQuestion - 1);
    }
}

function showSummary() {
    console.log("Showing test summary");

    // Hide all questions
    const allQuestions = document.querySelectorAll('.question-card');
    allQuestions.forEach(q => {
        q.classList.remove('active');
        q.style.display = 'none';
    });

    // Show summary
    const summary = document.getElementById('testSummary');
    if (summary) {
        summary.classList.add('active');
        summary.style.display = 'block';
    }

    // Update summary stats
    updateSummaryStats();
    updateNavigationButtons();
    updateQuestionNavigationButtons();
}

// OPTION SELECTION
function selectOption(optionElement, questionNum, optionValue) {
    console.log(`Selected option ${optionValue} for question ${questionNum}`);

    // Update answers object
    answers[`question_${questionNum}`] = optionValue;

    // Clear previous selections for this question
    const questionCard = optionElement.closest('.question-card');
    const allOptions = questionCard.querySelectorAll('.option');
    allOptions.forEach(opt => opt.classList.remove('selected'));

    // Mark current option as selected
    optionElement.classList.add('selected');

    // Check the radio button
    const radioButton = optionElement.querySelector('.option-radio');
    if (radioButton) {
        radioButton.checked = true;
    }

    // Update question navigation button
    updateQuestionNavigationButtons();
}

// UPDATE FUNCTIONS
function updateProgress() {
    const progressFill = document.getElementById('progressFill');
    const questionCounter = document.getElementById('questionCounter');

    if (progressFill) {
        const progress = (currentQuestion / totalQuestions) * 100;
        progressFill.style.width = progress + '%';
    }

    if (questionCounter) {
        if (document.getElementById('testSummary').style.display === 'block') {
            questionCounter.textContent = "Review Summary";
        } else {
            questionCounter.textContent = `Question ${currentQuestion} of ${totalQuestions}`;
        }
    }
}

function updateNavigationButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const reviewBtn = document.getElementById('reviewBtn');
    const submitBtn = document.getElementById('submitBtn');
    const summaryVisible = document.getElementById('testSummary').style.display === 'block';

    if (summaryVisible) {
        // On summary page
        if (prevBtn) {
            prevBtn.disabled = false;
            prevBtn.textContent = "← Back to Questions";
            prevBtn.onclick = () => showQuestion(totalQuestions);
        }
        if (nextBtn) nextBtn.style.display = 'none';
        if (reviewBtn) reviewBtn.style.display = 'none';
        if (submitBtn) submitBtn.style.display = 'inline-block';
    } else {
        // On question pages
        if (prevBtn) {
            prevBtn.disabled = currentQuestion === 1;
            prevBtn.textContent = "← Previous";
            prevBtn.onclick = previousQuestion;
        }

        if (nextBtn) {
            nextBtn.style.display = 'inline-block';
            if (currentQuestion < totalQuestions) {
                nextBtn.textContent = "Next →";
                nextBtn.onclick = nextQuestion;
            } else {
                nextBtn.textContent = "Review →";
                nextBtn.onclick = showSummary;
            }
        }

        if (reviewBtn) reviewBtn.style.display = currentQuestion === totalQuestions ? 'inline-block' : 'none';
        if (submitBtn) submitBtn.style.display = 'none';
    }
}

function updateQuestionNavigationButtons() {
    const questionButtons = document.querySelectorAll('.question-num-btn');

    questionButtons.forEach((btn, index) => {
        const questionNum = index + 1;
        const isAnswered = answers[`question_${questionNum}`];
        const isCurrent = questionNum === currentQuestion;

        btn.classList.remove('answered', 'current', 'unanswered');

        if (isCurrent && document.getElementById('testSummary').style.display !== 'block') {
            btn.classList.add('current');
        } else if (isAnswered) {
            btn.classList.add('answered');
        } else {
            btn.classList.add('unanswered');
        }
    });
}

function updateSummaryStats() {
    const answeredCount = Object.keys(answers).length;
    const unansweredCount = totalQuestions - answeredCount;
    const totalTimeUsed = 1800 - timeRemaining; // Total seconds used

    const answeredElement = document.getElementById('answeredCount');
    const unansweredElement = document.getElementById('unansweredCount');
    const totalTimeElement = document.getElementById('totalTime');

    if (answeredElement) answeredElement.textContent = answeredCount;
    if (unansweredElement) unansweredElement.textContent = unansweredCount;
    if (totalTimeElement) {
        const minutes = Math.floor(totalTimeUsed / 60);
        const seconds = totalTimeUsed % 60;
        totalTimeElement.textContent = minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');
    }
}

// FORM SUBMISSION
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('testForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            // Stop recording before submitting
            if (isRecording && mediaRecorder && mediaRecorder.state === 'recording') {
                stopRecording();
            }

            // Show loading overlay
            const loadingOverlay = document.getElementById('loadingOverlay');
            if (loadingOverlay) {
                loadingOverlay.style.display = 'flex';
            }

            // Let form submit normally
            return true;
        });
    }
});

// CLEANUP ON PAGE UNLOAD
window.addEventListener('beforeunload', function(e) {
    // Stop recording
    if (isRecording && mediaRecorder && mediaRecorder.state === 'recording') {
        stopRecording();
    }

    // Stop camera/microphone
    if (stream) {
        stream.getTracks().forEach(track => track.stop());
    }

    // Clear timers
    if (timerInterval) clearInterval(timerInterval);
    if (recordingTimer) clearInterval(recordingTimer);
});

// Add this function to calculate time taken
function getTimeTaken() {
    const timeUsed = 1800 - timeRemaining; // Total seconds used (30 minutes = 1800 seconds)
    return Math.max(0, timeUsed); // Ensure it's never negative
}

// Update your form submission to include time taken
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('testForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            // Add time taken to form before submission
            const timeTakenInput = document.createElement('input');
            timeTakenInput.type = 'hidden';
            timeTakenInput.name = 'timeTaken';
            timeTakenInput.value = getTimeTaken();
            form.appendChild(timeTakenInput);

            // Also add it in minutes format if needed
            const timeTakenMinutesInput = document.createElement('input');
            timeTakenMinutesInput.type = 'hidden';
            timeTakenMinutesInput.name = 'timeTakenMinutes';
            timeTakenMinutesInput.value = Math.round(getTimeTaken() / 60);
            form.appendChild(timeTakenMinutesInput);

            // Stop recording before submitting
            if (isRecording && mediaRecorder && mediaRecorder.state === 'recording') {
                stopRecording();
            }

            // Show loading overlay
            const loadingOverlay = document.getElementById('loadingOverlay');
            if (loadingOverlay) {
                loadingOverlay.style.display = 'flex';
            }

            return true;
        });
    }
});

// Also update the autoSubmitTest function
function autoSubmitTest() {
    console.log("Time expired - auto submitting");

    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'flex';
    }

    // Stop recording before submitting
    if (isRecording && mediaRecorder && mediaRecorder.state === 'recording') {
        stopRecording();
    }

    // Add time taken to form
    const form = document.getElementById('testForm');
    if (form) {
        const timeTakenInput = document.createElement('input');
        timeTakenInput.type = 'hidden';
        timeTakenInput.name = 'timeTaken';
        timeTakenInput.value = 1800; // Full 30 minutes when auto-submitted
        form.appendChild(timeTakenInput);

        const timeTakenMinutesInput = document.createElement('input');
        timeTakenMinutesInput.type = 'hidden';
        timeTakenMinutesInput.name = 'timeTakenMinutes';
        timeTakenMinutesInput.value = 30;
        form.appendChild(timeTakenMinutesInput);
    }

    // Give a moment for recording to stop, then submit
    setTimeout(function() {
        if (form) {
            form.submit();
        }
    }, 2000);
}
