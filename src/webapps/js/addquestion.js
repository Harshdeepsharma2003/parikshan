
let currentQuestionCount = 1;
const maxQuestions = 5;

function showNextQuestion() {
    if (currentQuestionCount < maxQuestions) {
        currentQuestionCount++;
        const nextQuestion = document.getElementById('question_' + currentQuestionCount);
        if (nextQuestion) {
            nextQuestion.classList.remove('hidden');

            // Make the new question's fields required
            const textarea = nextQuestion.querySelector('textarea');
            const textInputs = nextQuestion.querySelectorAll('input[type="text"]');
            const radioInputs = nextQuestion.querySelectorAll('input[type="radio"]');

            textarea.required = true;
            textInputs.forEach(input => input.required = true);
            radioInputs.forEach(input => input.required = true);

            updateCounter();
        }
    }

    if (currentQuestionCount >= maxQuestions) {
        document.querySelector('button[onclick="showNextQuestion()"]').style.display = 'none';
    }
}

function updateCounter() {
    document.getElementById('questionCounter').textContent = 'Showing: ' + currentQuestionCount + ' questions';
}

// Form validation
document.getElementById('questionsForm').addEventListener('submit', function(e) {
    let isValid = true;

    for (let i = 1; i <= currentQuestionCount; i++) {
        const questionBlock = document.getElementById('question_' + i);
        if (!questionBlock || questionBlock.classList.contains('hidden')) continue;

        const textarea = questionBlock.querySelector('textarea');
        const optionA = questionBlock.querySelector(`input[name="optionA_${i}"]`);
        const optionB = questionBlock.querySelector(`input[name="optionB_${i}"]`);
        const optionC = questionBlock.querySelector(`input[name="optionC_${i}"]`);
        const optionD = questionBlock.querySelector(`input[name="optionD_${i}"]`);
        const correct = questionBlock.querySelector(`input[name="correct_${i}"]:checked`);

        if (!textarea.value.trim()) {
            alert(`Please enter text for Question ${i}`);
            isValid = false;
            break;
        }

        if (!optionA.value.trim() || !optionB.value.trim() ||
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
    } else {
        // Show saving message
        const submitBtn = document.querySelector('button[type="submit"]');
        submitBtn.innerHTML = 'Saving to Database...';
        submitBtn.disabled = true;
    }
});
