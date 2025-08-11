package com.yash.parikshan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.parikshan.model.Question;
import com.yash.parikshan.service.QuestionService;
import com.yash.parikshan.serviceimpl.QuestionServiceImpl;

@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {

    private QuestionService questionService = new QuestionServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String testId = request.getParameter("testId");

            if (testId == null || testId.trim().isEmpty()) {
                // Set error attributes and forward to error page
                request.setAttribute("errorMessage", "Test ID is missing!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            List<Question> questions = new ArrayList<>();
            int successfullyProcessed = 0;

            // Process questions - check up to 20 possible questions
            for (int i = 1; i <= 20; i++) {
                String questionContent = request.getParameter("question_" + i);

                if (questionContent == null || questionContent.trim().isEmpty()) {
                    continue; // Skip this number and check next
                }

                String optionA = request.getParameter("optionA_" + i);
                String optionB = request.getParameter("optionB_" + i);
                String optionC = request.getParameter("optionC_" + i);
                String optionD = request.getParameter("optionD_" + i);
                String correctAnswer = request.getParameter("correct_" + i);

                // Validate all required fields are present and not empty
                if (optionA == null || optionB == null || optionC == null ||
                        optionD == null || correctAnswer == null ||
                        optionA.trim().isEmpty() || optionB.trim().isEmpty() ||
                        optionC.trim().isEmpty() || optionD.trim().isEmpty()) {
                    continue; // Skip incomplete questions
                }

                // Create Question object
                Question question = new Question();
                question.setQuestionId("Q_" + testId + "_" + i + "_" + System.currentTimeMillis());
                question.setTestId(testId);
                question.setContent(questionContent.trim());
                question.setQuestionType("MCQ");
                question.setImageUrl(""); // Empty for now

                // Store all options and correct answer in structured format
                String answerText = String.format("A:%s|B:%s|C:%s|D:%s|CORRECT:%s",
                        optionA.trim(), optionB.trim(), optionC.trim(), optionD.trim(), correctAnswer);
                question.setAnswerText(answerText);
                question.setCorrect(true); // Mark as valid question entry

                questions.add(question);
                successfullyProcessed++;
            }

            // Check if we have questions to save
            if (!questions.isEmpty()) {
                // Save to database
                questionService.addQuestions(questions);

                // Set success attributes and forward to success page
                request.setAttribute("testId", testId);
                request.setAttribute("questionsAdded", successfullyProcessed);
                request.setAttribute("totalRecords", questions.size());
                request.setAttribute("questions", questions);
                request.setAttribute("successMessage", "Questions Successfully Added to Database!");

                // Forward to success JSP
                request.getRequestDispatcher("question-success.jsp").forward(request, response);

            } else {
                // Set error attributes and forward to error page
                request.setAttribute("errorMessage", "No Valid Questions Found");
                request.setAttribute("errorDetails",
                        "Please make sure to: Enter question text, Fill all 4 options (A, B, C, D), Select the correct answer");
                request.getRequestDispatcher("question-error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // Set error attributes and forward to error page
            request.setAttribute("errorMessage", "Database Error!");
            request.setAttribute("errorDetails", e.getMessage());
            request.setAttribute("exception", e);
            request.getRequestDispatcher("question-error.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
}