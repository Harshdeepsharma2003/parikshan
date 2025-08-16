package com.yash.parikshan.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.parikshan.exceptions.GlobalException;
import com.yash.parikshan.model.Question;
import com.yash.parikshan.service.QuestionService;
import com.yash.parikshan.serviceimpl.QuestionServiceImpl;

@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddQuestion.class.getName());
    private QuestionService questionService = new QuestionServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("AddQuestion servlet initialized successfully");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Starting question addition process");

        try {
            String testId = request.getParameter("testId");
            String expectedQuestionsStr = request.getParameter("expectedQuestions");
            logger.info("Processing questions for test ID: " + testId);


            if (testId == null || testId.trim().isEmpty()) {
                logger.warning("Test ID validation failed: Test ID is null or empty");
                throw GlobalException.validationError("Test ID is required and cannot be empty");
            }

            // Get expected questions count for validation
            int expectedQuestions = 0;
            if (expectedQuestionsStr != null) {
                try {
                    expectedQuestions = Integer.parseInt(expectedQuestionsStr);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid expected questions format: " + expectedQuestionsStr);
                }
            }

            List<Question> questions = new ArrayList<>();
            int successfullyProcessed = 0;

            // Dynamically determine maximum question number to check
            // Fixed limit of 5 questions maximum
            int maxToCheck = questionService.getMaxQuestionsAllowed();


            logger.info("Starting to process questions (maximum 5 questions allowed)");
            for (int i = 1; i <= maxToCheck; i++) {
                String questionContent = request.getParameter("question_" + i);

                if (questionContent == null || questionContent.trim().isEmpty()) {
                    continue; // Skip this number and check next
                }

                logger.info("Processing question " + i + " for test ID: " + testId);

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
                    logger.warning("Question " + i + " has incomplete data, skipping");
                    continue; // Skip incomplete questions
                }

                // Additional validation for correct answer format
                if (!correctAnswer.matches("[ABCD]")) {
                    logger.warning("Invalid correct answer format for question " + i + ": " + correctAnswer);
                    throw GlobalException.validationError(
                            "Invalid correct answer format for question " + i + ". Must be A, B, C, or D");
                }


                Question question = new Question();
                String questionId = "Q_" + testId + "_" + String.format("%03d", i) + "_" + System.currentTimeMillis();
                question.setQuestionId(questionId);
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
                logger.info("Successfully processed question " + i + " with ID: " + questionId);
            }

            // Validate that we have questions to save
            if (questions.isEmpty()) {
                logger.warning("No valid questions found for test ID: " + testId);
                throw GlobalException.validationError(
                        "No valid questions found. Please ensure each question has: " +
                                "question text, all 4 options (A, B, C, D), and a correct answer selection");
            }

            logger.info("Total questions to be saved: " + questions.size() + " for test ID: " + testId);

            //  Validate against 5 question limit
            if (questions.size() > 5) {
                logger.warning("Too many questions submitted: " + questions.size() + ". Maximum allowed is 5.");
                throw GlobalException.validationError("Maximum 5 questions allowed per test. You submitted " + questions.size() + " questions.");
            }

            // Save to database
            try {
                logger.info("Attempting to save questions to database");
                questionService.addQuestionsWithValidation(questions);
                logger.info("Successfully saved " + questions.size() + " questions to database");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "SQL exception occurred while saving questions", e);
                // Handle specific SQL exceptions
                if (e.getSQLState().startsWith("08")) {
                    throw GlobalException.databaseConnectionError("Unable to connect to database", e);
                } else if (e.getErrorCode() == 1062 || e.getErrorCode() == 1169) {
                    throw GlobalException.dataIntegrityError("Duplicate question ID detected", e);
                } else if (e.getSQLState().startsWith("42")) {
                    throw GlobalException.sqlExecutionError("SQL syntax error in question insertion", e);
                } else {
                    throw GlobalException.databaseError("Failed to save questions to database", e);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected exception occurred while saving questions", e);
                // Handle other database-related exceptions
                if (e.getMessage().contains("connection") || e.getMessage().contains("timeout")) {
                    throw GlobalException.databaseConnectionError("Database connection issue", e);
                } else {
                    throw GlobalException.databaseError("Unexpected error while saving questions", e);
                }
            }

            // Set success attributes and forward to success page
            request.setAttribute("testId", testId);
            request.setAttribute("questionsAdded", successfullyProcessed);
            request.setAttribute("totalRecords", questions.size());
            request.setAttribute("questions", questions);
            request.setAttribute("expectedQuestions", expectedQuestions);
            request.setAttribute("successMessage",
            String.format("Successfully added %d questions to test %s!", successfullyProcessed, testId));

            logger.info("Question addition completed successfully. Test ID: " + testId +
                    ", Questions added: " + successfullyProcessed);

            // Forward to success JSP
            request.getRequestDispatcher("adminhome.jsp").forward(request, response);

        } catch (GlobalException e) {
            logger.log(Level.WARNING, "GlobalException occurred: " + e.getMessage(), e);
            // Handle our custom GlobalException with proper error categorization
            handleGlobalException(e, request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception occurred in AddQuestion servlet", e);
            // Handle any other unexpected exceptions
            GlobalException globalEx = new GlobalException("Unexpected error occurred while processing questions", e);
            handleGlobalException(globalEx, request, response);
        }
    }

    /**
     * Handle GlobalException and forward to appropriate error page
     */
    private void handleGlobalException(GlobalException e, HttpServletRequest request,
                                       HttpServletResponse response) throws ServletException, IOException {

        logger.info("Handling GlobalException: " + e.getErrorType() + " - " + e.getMessage());

        // Set common error attributes
        request.setAttribute("exception", e);
        request.setAttribute("errorCode", e.getErrorCode());
        request.setAttribute("errorType", e.getErrorType().toString());

        // Set specific attributes based on error type
        if (e.isValidationError()) {
            logger.warning("Validation error occurred: " + e.getMessage());
            request.setAttribute("errorMessage", "Validation Error");
            request.setAttribute("errorDetails", e.getMessage());
            response.setStatus(400); // Bad Request

        } else if (e.isDatabaseError()) {
            logger.severe("Database error occurred: " + e.getMessage());
            request.setAttribute("errorMessage", "Database Error");
            request.setAttribute("errorDetails", "There was a problem saving the questions. Please try again.");

            // Log the full exception details for debugging
            getServletContext().log("Database error in AddQuestion servlet", e);
            response.setStatus(500); // Internal Server Error

        } else {
            logger.severe("System error occurred: " + e.getMessage());
            request.setAttribute("errorMessage", "System Error");
            request.setAttribute("errorDetails", "An unexpected error occurred. Please contact support.");

            // Log the full exception details
            getServletContext().log("Unexpected error in AddQuestion servlet", e);
            response.setStatus(500); // Internal Server Error
        }

        logger.info("Forwarding to error page: question-error.jsp");
        // Forward to error page
        request.getRequestDispatcher("question-error.jsp").forward(request, response);
    }
}