
// Global variables - declare only once
const totalQuestions = <%= questions.size() %>;
const userid = '<%= userid %>';
const testId = '<%= testId %>';

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

console.log("Initializing test with", totalQuestions, "questions");

// Initialize when page loads
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM loaded, starting initialization...");

    // Start timer immediately
    startTimer();

    // Initialize camera/mic
    initializeRecording();

    // Show first question and update UI
    showQuestion(1);
    updateProgress();
    updateNavigationButtons();
    updateQuestionNavigationButtons();

    console.log("Initialization complete");
});

// FIXED TIMER FUNCTIONS
function startTimer() {
    console.log("Starting timer with", timeRemaining, "seconds");

    // Clear any existing timer
    if (timerInterval) {
        clearInterval(timerInterval);
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
            autoSubmitTest();
        }
    }, 1000);
}

function updateTimerDisplay() {
    const minutes = Math.floor(timeRemaining / 60);
    const seconds = timeRemaining % 60;
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

    if (isRecording) {
        stopRecording();
    }

    setTimeout(function() {
        document.getElementById('testForm').submit();
    }, 2000);
}

async function initializeRecording() {
    const recordingStatus = document.getElementById('recordingStatus');
    const recordingDot = document.getElementById('recordingDot');
    const videoPreview = document.getElementById('videoPreview');

    try {
        console.log("Requesting camera and microphone access...");

        // Secure context check (helps avoid silent failures)
        if (location.protocol !== 'https:' && location.hostname !== 'localhost') {
            throw new Error('Camera/mic require HTTPS or localhost.');
        }

        const constraints = {
            video: { width: { ideal: 640 }, height: { ideal: 480 }, facingMode: 'user' },
            audio: true
        };

        const localStream = await navigator.mediaDevices.getUserMedia(constraints);
        stream = localStream;
        console.log("Media stream obtained successfully");

        // Attach preview
        if (videoPreview) {
            videoPreview.srcObject = stream;
            try {
                await videoPreview.play();
                console.log("Video preview started");
            } catch (playErr) {
                console.warn("Autoplay failed, waiting for user gesture:", playErr);
            }
        }

        // Setup recorder only if supported
        setupMediaRecorder();

        // Start recording if possible
        if (mediaRecorder) {
            startRecording();
            if (recordingStatus) recordingStatus.textContent = "Recording active";
            if (recordingDot) recordingDot.classList.remove('inactive');
        } else {
            if (recordingStatus) recordingStatus.textContent = "Preview active (recording unsupported)";
            if (recordingDot) recordingDot.classList.add('inactive');
        }

    } catch (error) {
        console.error("Error accessing camera/microphone:", error);
        if (recordingStatus) recordingStatus.textContent = "Access denied or unavailable: " + (error && error.message ? error.message : '');
        if (recordingDot) recordingDot.classList.add('inactive');
        alert("Please allow camera and microphone access and ensure the site is served over HTTPS (or localhost).");
    }
}

function setupMediaRecorder() {
    try {
        if (typeof MediaRecorder === 'undefined') {
            console.warn("MediaRecorder not supported in this browser");
            mediaRecorder = null;
            return;
        }

        let options = {};
        if (MediaRecorder.isTypeSupported('video/webm;codecs=vp9')) {
            options.mimeType = 'video/webm;codecs=vp9';
        } else if (MediaRecorder.isTypeSupported('video/webm;codecs=vp8')) {
            options.mimeType = 'video/webm;codecs=vp8';
        } else if (MediaRecorder.isTypeSupported('video/webm')) {
            options.mimeType = 'video/webm';
        }

        mediaRecorder = new MediaRecorder(stream, options);

        mediaRecorder.onstart = function() {
            console.log("MediaRecorder started");
        };

        mediaRecorder.ondataavailable = function(event) {
            if (event.data && event.data.size > 0) {
                recordedChunks.push(event.data);
            }
        };

        mediaRecorder.onerror = function(e) {
            console.error("MediaRecorder error:", e);
        };

        mediaRecorder.onstop = function() {
            console.log("Recording stopped");
            saveRecordingToDatabase();
        };

        console.log("MediaRecorder setup complete");
    } catch (error) {
        console.error("Error setting up MediaRecorder:", error);
        mediaRecorder = null;
    }
}

function startRecording() {
    if (!mediaRecorder) {
        console.warn("Recording not supported or not initialized");
        isRecording = false;
        return;
    }
    if (mediaRecorder.state === 'inactive') {
        try {
            recordedChunks = [];
            mediaRecorder.start(5000); // 5s timeslice
            recordingStartTime = Date.now();
            isRecording = true;
            recordingTimer = setInterval(updateRecordingDuration, 1000);
            console.log("Recording started");
        } catch (error) {
            console.error("Error starting recording:", error);
            isRecording = false;
        }
    }
}


function stopRecording() {
    if (mediaRecorder && mediaRecorder.state === 'recording') {
        mediaRecorder.stop();
        isRecording = false;

        if (recordingTimer) {
            clearInterval(recordingTimer);
        }
    }
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
    if (videoPreview && videoPreview.srcObject) {
        alert("Camera preview is active. " + (mediaRecorder ? "Recording is supported." : "Recording not supported on this browser."));
    } else {
        alert("Camera/microphone not accessible. Ensure HTTPS or localhost and allow permissions, then try again.");
        initializeRecording();
    }
}


async function saveRecordingToDatabase() {
    if (recordedChunks.length === 0) {
        console.log("No recording data to save");
        return;
    }

    try {
        const recordingBlob = new Blob(recordedChunks, { type: 'video/webm' });
        console.log("Recording blob size:", recordingBlob.size);

        const formData = new FormData();
        formData.append('recording', recordingBlob);
        formData.append('testid', testId);
        formData.append('userid', userid);

        const response = await fetch('recordingUpload', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            console.log("Recording saved successfully");
        } else {
            console.error("Failed to save recording");
        }

    } catch (error) {
        console.error("Error saving recording:", error);
    }
}

// FIXED QUESTION NAVIGATION FUNCTIONS
function showQuestion(questionNumber) {
    console.log("Showing question:", questionNumber);

    // Hide all questions
    const allQuestions = document.querySelectorAll('.question-card');
    allQuestions.forEach(q => {
        q.classList.remove('active');
        q.style.display = 'none';
    });

    // Hide summary
    const summary = document.getElementById('testSummary');
    if (summary) {
        summary.classList.remove('active');
        summary.style.display = 'none';
    }

    // Show current question
    const targetQuestion = document.querySelector('.question-card[data-question="' + questionNumber + '"]');
    if (targetQuestion) {
        targetQuestion.style.display = 'block';
        targetQuestion.classList.add('active');
        targetQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });
        console.log("Successfully showed question", questionNumber);
    } else {
        // Fallback - use index
        const questionByIndex = allQuestions[questionNumber - 1];
        if (questionByIndex) {
            questionByIndex.style.display = 'block';
            questionByIndex.classList.add('active');
            console.log("Showed question using index fallback");
        } else {
            console.error("Could not find question", questionNumber);
        }
    }
}

function nextQuestion() {
    console.log("Next button clicked - current:", currentQuestion, "total:", totalQuestions);

    if (currentQuestion < totalQuestions) {
        currentQuestion++;
        showQuestion(currentQuestion);
        updateProgress();
        updateNavigationButtons();
        updateQuestionNavigationButtons();
        console.log("Moved to question:", currentQuestion);
    } else {
        console.log("Last question reached, showing summary");
        showSummary();
    }
}

function previousQuestion() {
    console.log("Previous button clicked");

    if (currentQuestion > 1) {
        currentQuestion--;
        showQuestion(currentQuestion);
        updateProgress();
        updateNavigationButtons();
        updateQuestionNavigationButtons();
        console.log("Moved to question:", currentQuestion);
    }
}

function goToQuestion(questionNum) {
    console.log("Going directly to question:", questionNum);

    currentQuestion = questionNum;
    showQuestion(currentQuestion);
    updateProgress();
    updateNavigationButtons();
    updateQuestionNavigationButtons();
}

// FIXED PROGRESS AND UI FUNCTIONS
function updateProgress() {
    const progressPercent = ((currentQuestion - 1) / totalQuestions) * 100;
    const progressFill = document.getElementById('progressFill');
    const questionCounter = document.getElementById('questionCounter');

    if (progressFill) {
        progressFill.style.width = progressPercent + '%';
    }

    if (questionCounter) {
        questionCounter.textContent = 'Question ' + currentQuestion + ' of ' + totalQuestions;
        console.log("Updated counter:", questionCounter.textContent);
    }
}

function updateNavigationButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const reviewBtn = document.getElementById('reviewBtn');

    if (prevBtn) {
        prevBtn.disabled = (currentQuestion === 1);
        prevBtn.style.display = 'inline-block';
    }

    if (currentQuestion === totalQuestions) {
        if (nextBtn) nextBtn.style.display = 'none';
        if (reviewBtn) reviewBtn.style.display = 'inline-block';
    } else {
        if (nextBtn) nextBtn.style.display = 'inline-block';
        if (reviewBtn) reviewBtn.style.display = 'none';
    }
}

function updateQuestionNavigationButtons() {
    const questionBtns = document.querySelectorAll('.question-num-btn');

    questionBtns.forEach((btn, index) => {
        const questionNum = index + 1;
        btn.classList.remove('current', 'answered');

        if (questionNum === currentQuestion) {
            btn.classList.add('current');
        } else if (answers[questionNum]) {
            btn.classList.add('answered');
        }
    });
}

function selectOption(optionElement, questionNum, optionValue) {
    console.log("Option selected:", questionNum, optionValue);

    // Remove selection from other options in this question
    const questionCard = optionElement.closest('.question-card');
    if (questionCard) {
        questionCard.querySelectorAll('.option').forEach(opt => {
            opt.classList.remove('selected');
        });
    }

    // Select this option
    optionElement.classList.add('selected');
    const radioInput = optionElement.querySelector('input[type="radio"]');
    if (radioInput) {
        radioInput.checked = true;
    }

    // Store answer
    answers[questionNum] = optionValue;

    // Update UI
    updateSummaryStats();
    updateQuestionNavigationButtons();
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

    // Update buttons
    const submitBtn = document.getElementById('submitBtn');
    const reviewBtn = document.getElementById('reviewBtn');

    if (submitBtn) submitBtn.style.display = 'inline-block';
    if (reviewBtn) reviewBtn.style.display = 'none';

    // Update progress bar and counter
    const progressFill = document.getElementById('progressFill');
    const questionCounter = document.getElementById('questionCounter');

    if (progressFill) progressFill.style.width = '100%';
    if (questionCounter) questionCounter.textContent = 'Test Summary';

    updateSummaryStats();
}

function updateSummaryStats() {
    const answeredCount = Object.keys(answers).length;
    const unansweredCount = totalQuestions - answeredCount;
    const timeTaken = (1800 - timeRemaining); // 30 minutes - remaining
    const minutes = Math.floor(timeTaken / 60);
    const seconds = timeTaken % 60;

    const answeredCountEl = document.getElementById('answeredCount');
    const unansweredCountEl = document.getElementById('unansweredCount');
    const totalTimeEl = document.getElementById('totalTime');

    if (answeredCountEl) answeredCountEl.textContent = answeredCount;
    if (unansweredCountEl) unansweredCountEl.textContent = unansweredCount;
    if (totalTimeEl) {
        totalTimeEl.textContent = minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');
    }
}

// Form submission handling
document.getElementById('testForm').addEventListener('submit', function(e) {
    console.log("Form submitted");

    if (timerInterval) {
        clearInterval(timerInterval);
    }

    if (isRecording) {
        stopRecording();
    }

    const answeredCount = Object.keys(answers).length;
    if (answeredCount < totalQuestions && timeRemaining > 0) {
        const confirmed = confirm('You have answered ' + answeredCount + ' out of ' + totalQuestions + ' questions. Do you want to submit?');
        if (!confirmed) {
            e.preventDefault();
            startTimer();
            if (!isRecording && stream) {
                startRecording();
            }
            return;
        }
    }

    // Add time taken to form
    const timeTaken = (1800 - timeRemaining);
    const hiddenTime = document.createElement('input');
    hiddenTime.type = 'hidden';
    hiddenTime.name = 'timeTaken';
    hiddenTime.value = timeTaken;
    this.appendChild(hiddenTime);

    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.innerHTML = 'Submitting...';
        submitBtn.disabled = true;
    }
});

// Prevent page navigation during test
window.addEventListener('beforeunload', function(e) {
    if (timeRemaining > 0) {
        if (isRecording) stopRecording();
        e.preventDefault();
        e.returnValue = 'Are you sure you want to leave the test?';
        return 'Are you sure you want to leave the test?';
    }
});

// Cleanup on page unload
window.addEventListener('unload', function() {
    if (stream) {
        stream.getTracks().forEach(track => track.stop());
    }
    if (isRecording) {
        stopRecording();
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    if (e.key === 'ArrowLeft' && currentQuestion > 1) {
        previousQuestion();
    } else if (e.key === 'ArrowRight' && currentQuestion < totalQuestions) {
        nextQuestion();
    }
});
