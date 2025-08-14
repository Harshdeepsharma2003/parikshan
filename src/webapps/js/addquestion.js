// Initialize variables
let currentQuestionCount = 0;
const maxQuestions = 5;

// Function to create a new question block
function createQuestionBlock(questionNumber) {
    return `
        <div class="question-block" id="question_${questionNumber}">
            <div class="form-group">
                <label>Question ${questionNumber}</label>
                <textarea name="question_${questionNumber}" placeholder="Enter your question here..." required></textarea>
            </div>

            <div class="form-group">
                <label>Select the correct answer by clicking the radio button:</label>
                <div class="option-group">
                    <input type="radio" name="correct_${questionNumber}" value="A" required>
                    <label>A:</label>
                    <input type="text" name="optionA_${questionNumber}" placeholder="Option A" required>
                </div>
                <div class="option-group">
                    <input type="radio" name="correct_${questionNumber}" value="B" required>
                    <label>B:</label>
                    <input type="text" name="optionB_${questionNumber}" placeholder="Option B" required>
                </div>
                <div class="option-group">
                    <input type="radio" name="correct_${questionNumber}" value="C" required>
                    <label>C:</label>
                    <input type="text" name="optionC_${questionNumber}" placeholder="Option C" required>
                </div>
                <div class="option-group">
                    <input type="radio" name="correct_${questionNumber}" value="D" required>
                    <label>D:</label>
                    <input type="text" name="optionD_${questionNumber}" placeholder="Option D" required>
                </div>
            </div>
        </div>
    `;
}

// Function to add a new question
function showNextQuestion() {
    if (currentQuestionCount < maxQuestions) {
        currentQuestionCount++;
        const container = document.getElementById('questionsContainer');
        if (container) {
            container.insertAdjacentHTML('beforeend', createQuestionBlock(currentQuestionCount));
            updateCounter();

            // Scroll to the new question
            const newQuestion = document.getElementById(`question_${currentQuestionCount}`);
            if (newQuestion) {
                newQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });

                // Focus on the new question textarea
                const textarea = newQuestion.querySelector('textarea');
                if (textarea) {
                    setTimeout(() => textarea.focus(), 300);
                }
            }
        }
    }

    // Hide add button if we've reached the maximum
    if (currentQuestionCount >= maxQuestions) {
        const addBtn = document.getElementById('addQuestionBtn');
        if (addBtn) {
            addBtn.style.display = 'none';
        }
    }
}

// Function to update the counter
function updateCounter() {
    const counterElement = document.getElementById('questionCounter');
    if (counterElement) {
        counterElement.textContent = `Showing: ${currentQuestionCount} questions`;
    }

    // Update submit button text
    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        if (currentQuestionCount === 0) {
            submitBtn.textContent = 'Add at least one question first';
            submitBtn.disabled = true;
        } else {
            submitBtn.textContent = `Save ${currentQuestionCount} Question${currentQuestionCount > 1 ? 's' : ''} to Database`;
            submitBtn.disabled = false;
        }
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('AddQuestion page loaded');

    // Set up add question button
    const addQuestionBtn = document.getElementById('addQuestionBtn');
    if (addQuestionBtn) {
        addQuestionBtn.addEventListener('click', showNextQuestion);
    }

    // Set up form validation
    const form = document.getElementById('questionsForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            console.log('Questions form submission started');

            if (currentQuestionCount === 0) {
                e.preventDefault();
                alert('Please add at least one question before submitting.');
                return false;
            }

            let isValid = true;

            for (let i = 1; i <= currentQuestionCount; i++) {
                const questionBlock = document.getElementById(`question_${i}`);
                if (!questionBlock) continue;

                const textarea = questionBlock.querySelector('textarea');
                const optionA = questionBlock.querySelector(`input[name="optionA_${i}"]`);
                const optionB = questionBlock.querySelector(`input[name="optionB_${i}"]`);
                const optionC = questionBlock.querySelector(`input[name="optionC_${i}"]`);
                const optionD = questionBlock.querySelector(`input[name="optionD_${i}"]`);
                const correct = questionBlock.querySelector(`input[name="correct_${i}"]:checked`);

                if (!textarea || !textarea.value.trim()) {
                    alert(`Please enter text for Question ${i}`);
                    if (textarea) textarea.focus();
                    isValid = false;
                    break;
                }

                if (!optionA || !optionB || !optionC || !optionD ||
                !optionA.value.trim() || !optionB.value.trim() ||
                !optionC.value.trim() || !optionD.value.trim()) {
                    alert(`Please fill all options for Question ${i}`);
                    isValid = false;
                    break;
                }

                if (!correct) {
                    alert(`Please select the correct answer for Question ${i}`);
                    isValid = false;
                    break;
                }
            }

            if (!isValid) {
                e.preventDefault();
                return false;
            } else {
                // Show saving message
                const submitBtn = document.getElementById('submitBtn');
                if (submitBtn) {
                    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Saving to Database...';
                    submitBtn.disabled = true;
                }

                console.log('Form validation passed, submitting questions...');
                return true;
            }
        });
    }

    // Initialize with first question
    showNextQuestion();

    console.log('AddQuestion JavaScript initialized successfully');
});