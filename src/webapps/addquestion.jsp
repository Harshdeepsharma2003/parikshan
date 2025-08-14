<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String testId = (String) session.getAttribute("testid");
  String noOfQuestionsStr = (String) session.getAttribute("noOfQuestions");

  if (testId == null) {
      response.sendRedirect("managetest.jsp");
      return;
  }

  int noOfQuestions = 0;
  if (noOfQuestionsStr != null) {
      try {
          noOfQuestions = Integer.parseInt(noOfQuestionsStr);
      } catch (NumberFormatException e) {
          noOfQuestions = 5;
      }
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Add MCQ Questions</title>
<link rel="stylesheet" href="css/addquestion.css">
<style>
.container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
}

.header {
    text-align: center;
    margin-bottom: 30px;
    padding: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 10px;
}

.header h2 {
    margin: 0 0 10px 0;
    font-size: 28px;
}

.header p {
    margin: 0;
    font-size: 16px;
    opacity: 0.9;
}

.question-block {
    background: white;
    border: 2px solid #e0e0e0;
    border-radius: 10px;
    padding: 25px;
    margin-bottom: 25px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
}

.question-block:hover {
    border-color: #667eea;
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
}

.question-block.hidden {
    display: none;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
    font-size: 16px;
}

.form-group textarea {
    width: 100%;
    min-height: 100px;
    padding: 12px;
    border: 2px solid #ddd;
    border-radius: 6px;
    font-size: 14px;
    font-family: Arial, sans-serif;
    resize: vertical;
    transition: border-color 0.3s ease;
}

.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.option-group {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    padding: 10px;
    background: #f8f9fa;
    border-radius: 6px;
    transition: background-color 0.3s ease;
}

.option-group:hover {
    background: #e9ecef;
}

.option-group input[type="radio"] {
    margin-right: 10px;
    transform: scale(1.2);
}

.option-group > label {
    font-weight: bold;
    color: #495057;
    margin: 0 10px 0 0;
    min-width: 25px;
}

.option-group input[type="text"] {
    flex: 1;
    padding: 8px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    font-size: 14px;
    transition: border-color 0.3s ease;
}

.option-group input[type="text"]:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.button-group {
    text-align: center;
    margin-top: 30px;
    padding: 25px;
    background: #f8f9fa;
    border-radius: 10px;
}

.btn {
    padding: 12px 25px;
    margin: 0 10px;
    border: none;
    border-radius: 6px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-block;
}

.btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(102, 126, 234, 0.3);
}

.btn-success {
    background: linear-gradient(135deg, #56ab2f 0%, #a8e6cf 100%);
    color: white;
}

.btn-success:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(86, 171, 47, 0.3);
}

.btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none !important;
}

.progress-info {
    text-align: center;
    margin-top: 15px;
    color: #666;
    font-size: 14px;
}

.progress-info span {
    font-weight: bold;
    color: #495057;
}

.alert {
    padding: 15px;
    margin-bottom: 20px;
    border-radius: 6px;
    font-weight: bold;
}

.alert-info {
    background-color: #d1ecf1;
    border-color: #b8daff;
    color: #0c5460;
}
</style>
</head>
<body>

<div class="container">
  <div class="header">
    <h2>Add MCQ Questions</h2>
    <p><strong>Test ID:</strong> <%= testId %> | <strong>Expected Questions:</strong> <%= noOfQuestions %></p>
  </div>

  <% if (noOfQuestions > 5) { %>
  <div class="alert alert-info">
    <strong>Note:</strong> You specified <%= noOfQuestions %> questions, but the current limit is 5 questions per test.
    You can add up to 5 questions now.

    Questions will be numbered automatically.
  </div>
  <% } %>

  <form action="AddQuestion" method="POST" id="questionsForm">
    <input type="hidden" name="testId" value="<%= testId %>" />
    <input type="hidden" name="expectedQuestions" value="<%= noOfQuestions %>" />

    <!-- Questions will be dynamically generated here -->
    <div id="questionsContainer">
      <!-- Initial question will be added by JavaScript -->
    </div>

    <div class="button-group">
      <button type="button" class="btn btn-primary" id="addQuestionBtn">
        + Add Another Question
      </button>
      <button type="submit" class="btn btn-success" id="submitBtn">
        Save All Questions to Database
      </button>
    </div>

    <div class="progress-info">
      <span id="questionCounter">Showing: 0 questions</span> |
      <span id="maxQuestions">Maximum: 5 questions allowed</span>
    </div>
  </form>
</div>

<script>
let currentQuestionCount = 0;
const maxQuestions = 5; // Fixed maximum of 5 questions
const testId = '<%= testId %>';

// Function to create a new question block
function createQuestionBlock(questionNumber) {
    const questionHtml = `
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
    return questionHtml;
}

// Function to add a new question
function addNewQuestion() {
    if (currentQuestionCount < maxQuestions) {
        currentQuestionCount++;
        const container = document.getElementById('questionsContainer');
        container.insertAdjacentHTML('beforeend', createQuestionBlock(currentQuestionCount));
        updateCounter();

        // Scroll to the new question
        const newQuestion = document.getElementById(`question_${currentQuestionCount}`);
        newQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // Focus on the new question textarea
        const textarea = newQuestion.querySelector('textarea');
        setTimeout(() => textarea.focus(), 300);
    }

    // Hide add button if we've reached the maximum
    if (currentQuestionCount >= maxQuestions) {
        document.getElementById('addQuestionBtn').style.display = 'none';
    }
}

// Function to update the counter
function updateCounter() {
    document.getElementById('questionCounter').textContent = `Showing: ${currentQuestionCount} questions`;

    // Update submit button text
    const submitBtn = document.getElementById('submitBtn');
    if (currentQuestionCount === 0) {
        submitBtn.textContent = 'Add at least one question first';
        submitBtn.disabled = true;
    } else {
        submitBtn.textContent = `Save ${currentQuestionCount} Question${currentQuestionCount > 1 ? 's' : ''} to Database`;
        submitBtn.disabled = false;
    }
}

// Event listener for add question button
document.getElementById('addQuestionBtn').addEventListener('click', addNewQuestion);

// Form validation
document.getElementById('questionsForm').addEventListener('submit', function(e) {
    if (currentQuestionCount === 0) {
        alert('Please add at least one question before submitting.');
        e.preventDefault();
        return;
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

        if (!textarea.value.trim()) {
            alert(`Please enter text for Question ${i}`);
            textarea.focus();
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
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Saving to Database...';
        submitBtn.disabled = true;
    }
});

// Initialize with first question
document.addEventListener('DOMContentLoaded', function() {
    addNewQuestion();
});
</script>

</body>
</html>