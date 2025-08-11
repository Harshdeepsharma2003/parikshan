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
</head>
<body>

<div class="container">
  <div class="header">
    <h2>Add MCQ Questions</h2>
    <p><strong>Test ID:</strong> <%= testId %> | <strong>Expected Questions:</strong> <%= noOfQuestions %></p>
  </div>

  <form action="AddQuestion" method="POST" id="questionsForm">
    <input type="hidden" name="testId" value="<%= testId %>" />

    <!-- Pre-built question blocks (initially hidden except first one) -->

    <!-- Question 1 -->
    <div class="question-block" id="question_1">
      <div class="form-group">
        <label>Question 1</label>
        <textarea name="question_1" placeholder="Enter your question here..." required></textarea>
      </div>

      <div class="form-group">
        <label>Select the correct answer by clicking the radio button:</label>
        <div class="option-group">
          <input type="radio" name="correct_1" value="A" required>
          <label>A:</label>
          <input type="text" name="optionA_1" placeholder="Option A" required>
        </div>
        <div class="option-group">
          <input type="radio" name="correct_1" value="B" required>
          <label>B:</label>
          <input type="text" name="optionB_1" placeholder="Option B" required>
        </div>
        <div class="option-group">
          <input type="radio" name="correct_1" value="C" required>
          <label>C:</label>
          <input type="text" name="optionC_1" placeholder="Option C" required>
        </div>
        <div class="option-group">
          <input type="radio" name="correct_1" value="D" required>
          <label>D:</label>
          <input type="text" name="optionD_1" placeholder="Option D" required>
        </div>
      </div>
    </div>

    <!-- Question 2 (initially hidden) -->
    <div class="question-block hidden" id="question_2">
      <div class="form-group">
        <label>Question 2</label>
        <textarea name="question_2" placeholder="Enter your second question here..."></textarea>
      </div>

      <div class="form-group">
        <label>Select the correct answer by clicking the radio button:</label>
        <div class="option-group">
          <input type="radio" name="correct_2" value="A">
          <label>A:</label>
          <input type="text" name="optionA_2" placeholder="Option A">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_2" value="B">
          <label>B:</label>
          <input type="text" name="optionB_2" placeholder="Option B">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_2" value="C">
          <label>C:</label>
          <input type="text" name="optionC_2" placeholder="Option C">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_2" value="D">
          <label>D:</label>
          <input type="text" name="optionD_2" placeholder="Option D">
        </div>
      </div>
    </div>

    <!-- Question 3 (initially hidden) -->
    <div class="question-block hidden" id="question_3">
      <div class="form-group">
        <label>Question 3</label>
        <textarea name="question_3" placeholder="Enter your third question here..."></textarea>
      </div>

      <div class="form-group">
        <label>Select the correct answer by clicking the radio button:</label>
        <div class="option-group">
          <input type="radio" name="correct_3" value="A">
          <label>A:</label>
          <input type="text" name="optionA_3" placeholder="Option A">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_3" value="B">
          <label>B:</label>
          <input type="text" name="optionB_3" placeholder="Option B">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_3" value="C">
          <label>C:</label>
          <input type="text" name="optionC_3" placeholder="Option C">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_3" value="D">
          <label>D:</label>
          <input type="text" name="optionD_3" placeholder="Option D">
        </div>
      </div>
    </div>

    <!-- Question 4 (initially hidden) -->
    <div class="question-block hidden" id="question_4">
      <div class="form-group">
        <label>Question 4</label>
        <textarea name="question_4" placeholder="Enter your fourth question here..."></textarea>
      </div>

      <div class="form-group">
        <label>Select the correct answer by clicking the radio button:</label>
        <div class="option-group">
          <input type="radio" name="correct_4" value="A">
          <label>A:</label>
          <input type="text" name="optionA_4" placeholder="Option A">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_4" value="B">
          <label>B:</label>
          <input type="text" name="optionB_4" placeholder="Option B">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_4" value="C">
          <label>C:</label>
          <input type="text" name="optionC_4" placeholder="Option C">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_4" value="D">
          <label>D:</label>
          <input type="text" name="optionD_4" placeholder="Option D">
        </div>
      </div>
    </div>

    <!-- Question 5 (initially hidden) -->
    <div class="question-block hidden" id="question_5">
      <div class="form-group">
        <label>Question 5</label>
        <textarea name="question_5" placeholder="Enter your fifth question here..."></textarea>
      </div>

      <div class="form-group">
        <label>Select the correct answer by clicking the radio button:</label>
        <div class="option-group">
          <input type="radio" name="correct_5" value="A">
          <label>A:</label>
          <input type="text" name="optionA_5" placeholder="Option A">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_5" value="B">
          <label>B:</label>
          <input type="text" name="optionB_5" placeholder="Option B">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_5" value="C">
          <label>C:</label>
          <input type="text" name="optionC_5" placeholder="Option C">
        </div>
        <div class="option-group">
          <input type="radio" name="correct_5" value="D">
          <label>D:</label>
          <input type="text" name="optionD_5" placeholder="Option D">
        </div>
      </div>
    </div>

    <div class="button-group">
      <button type="button" class="btn btn-primary" onclick="showNextQuestion()">+ Add Another Question</button>
      <button type="submit" class="btn btn-success">Save All Questions to Database</button>
    </div>

    <p style="text-align: center; margin-top: 15px; color: #666;">
      <span id="questionCounter">Showing: 1 question</span> | <span id="maxQuestions">Maximum: 5 questions</span>
    </p>
  </form>
</div>
 <script src="js/addquestion.js"></script>
</body>
</html>
