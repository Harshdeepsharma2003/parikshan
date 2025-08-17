package com.yash.parikshan.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yash.parikshan.model.Question;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.QuestionService;
import com.yash.parikshan.service.TestResultService;
import com.yash.parikshan.serviceimpl.QuestionServiceImpl;
import com.yash.parikshan.serviceimpl.TestResultServiceImpl;

@WebServlet("/SubmitTest")
public class SubmitTest extends HttpServlet {

    private QuestionService questionService;
    private TestResultService testResultService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            questionService = new QuestionServiceImpl();
            testResultService = new TestResultServiceImpl();
        } catch (Exception e) {
            System.err.println("Failed to initialize services in SubmitTest servlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Service initialization failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String testId = null;
        String studentId = null;

        try {

            testId = request.getParameter("testId");
            String totalQuestionsStr = request.getParameter("totalQuestions");
            String timeTaken = request.getParameter("timeTaken");


            HttpSession session = request.getSession(false);
            if (session != null) {
                studentId = (String) session.getAttribute("studentid");
            }

            if (studentId == null) {
                studentId = request.getParameter("studentid");
            }


            System.out.println("SubmitTest - Received parameters:");
            System.out.println("testId: " + testId);
            System.out.println("studentId: " + studentId);
            System.out.println("totalQuestions: " + totalQuestionsStr);
            System.out.println("timeTaken: " + timeTaken);

            // Validating required parameters
            validateRequiredParameters(testId, totalQuestionsStr, studentId);

            int totalQuestions = parseIntegerParameter(totalQuestionsStr, "totalQuestions");


            List<Question> questions = getQuestionsForTest(testId);
            ScoreCalculationResult scoreResult = calculateScore(request, questions, totalQuestions);


            TestResult testResult = createTestResult(studentId, testId, scoreResult, timeTaken, totalQuestions);


            boolean resultSaved = saveTestResult(testResult);

            if (!resultSaved) {
                System.err.println("Warning: Failed to save test result to database for student: " + studentId);
                request.setAttribute("saveWarning", "Test completed successfully, but there was an issue saving to database. Please contact administrator.");
            }

            // Setting attributes for JSP display
            setResultAttributes(request, testResult, scoreResult, totalQuestions, questions);


            RequestDispatcher dispatcher = request.getRequestDispatcher("testresultstudents.jsp");
            dispatcher.forward(request, response);

        } catch (ParameterValidationException e) {
            handleError(request, response, "Invalid Input", e.getMessage(), e, testId, studentId);
        } catch (ServiceException e) {
            handleError(request, response, "Service Error",
                    "There was an issue processing your test. Please try again or contact support.", e, testId, studentId);
        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid Data Format",
                    "Some of the test data appears to be corrupted. Please retake the test.", e, testId, studentId);
        } catch (Exception e) {
            handleError(request, response, "System Error",
                    "An unexpected error occurred while processing your test. Your answers may have been saved. Please contact support.", e, testId, studentId);
        }
    }

    private void validateRequiredParameters(String testId, String totalQuestionsStr, String studentId)
            throws ParameterValidationException {

        if (testId == null || testId.trim().isEmpty()) {
            throw new ParameterValidationException("Test ID is missing. Please restart the test.");
        }

        if (totalQuestionsStr == null || totalQuestionsStr.trim().isEmpty()) {
            throw new ParameterValidationException("Test configuration is invalid. Please restart the test.");
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ParameterValidationException("Student session has expired. Please log in again.");
        }
    }

     // Parsing integer parameter with proper error handling
    private int parseIntegerParameter(String value, String paramName) throws NumberFormatException {
        try {
            int result = Integer.parseInt(value.trim());
            if (result <= 0) {
                throw new NumberFormatException(paramName + " must be a positive number");
            }
            return result;
        } catch (NumberFormatException e) {
            System.err.println("Invalid " + paramName + " value: " + value);
            throw new NumberFormatException("Invalid " + paramName + ": " + e.getMessage());
        }
    }

    private List<Question> getQuestionsForTest(String testId) throws ServiceException {
        try {
            List<Question> questions = questionService.getQuestionsByTestId(testId);
            if (questions == null || questions.isEmpty()) {
                throw new ServiceException("No questions found for test ID: " + testId);
            }
            return questions;
        } catch (Exception e) {
            System.err.println("Error retrieving questions for test " + testId + ": " + e.getMessage());
            throw new ServiceException("Unable to retrieve test questions: " + e.getMessage(), e);
        }
    }

     // Calculate score with detailed result
    private ScoreCalculationResult calculateScore(HttpServletRequest request, List<Question> questions, int totalQuestions) {
        int correctAnswers = 0;
        int attemptedQuestions = 0;

        for (int i = 1; i <= totalQuestions; i++) {
            String studentAnswer = request.getParameter("question_" + i);

            if (studentAnswer != null && !studentAnswer.trim().isEmpty()) {
                attemptedQuestions++;

                if (i <= questions.size()) {
                    Question question = questions.get(i - 1);
                    String answerText = question.getAnswerText();

                    // Extracting correct answer
                    String correctAnswer = extractCorrectAnswer(answerText);

                    if (studentAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
                        correctAnswers++;
                    }
                }
            }
        }

        double percentage = totalQuestions > 0 ? (double) correctAnswers * 100 / totalQuestions : 0;

        return new ScoreCalculationResult(correctAnswers, attemptedQuestions, percentage);
    }

    private String extractCorrectAnswer(String answerText) {
        if (answerText == null) return "";

        String[] parts = answerText.split("\\|");
        for (String part : parts) {
            if (part.startsWith("CORRECT:")) {
                return part.substring(8).trim();
            }
        }
        return "";
    }

    private TestResult createTestResult(String studentId, String testId, ScoreCalculationResult scoreResult,
                                        String timeTaken, int totalQuestions) {
        TestResult testResult = new TestResult();
        testResult.setResultId(generateResultId());
        testResult.setStudentId(studentId);
        testResult.setTestId(testId);
        testResult.setScore(String.valueOf(scoreResult.correctAnswers));
        testResult.setTotalMarks(String.valueOf(totalQuestions));
        testResult.setTimeTaken(timeTaken);
        testResult.setTestDate(new java.sql.Timestamp(System.currentTimeMillis()));

        return testResult;
    }

    private boolean saveTestResult(TestResult testResult) {
        try {
            return testResultService.saveTestResult(testResult);
        } catch (Exception e) {
            System.err.println("Failed to save test result: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

 // for jsp display
    private void setResultAttributes(HttpServletRequest request, TestResult testResult,
                                     ScoreCalculationResult scoreResult, int totalQuestions, List<Question> questions) {

        request.setAttribute("testResult", testResult);
        request.setAttribute("testId", testResult.getTestId());
        request.setAttribute("totalQuestions", totalQuestions);
        request.setAttribute("attemptedQuestions", scoreResult.attemptedQuestions);
        request.setAttribute("correctAnswers", scoreResult.correctAnswers);
        request.setAttribute("incorrectAnswers", scoreResult.attemptedQuestions - scoreResult.correctAnswers);
        request.setAttribute("unattemptedQuestions", totalQuestions - scoreResult.attemptedQuestions);
        request.setAttribute("percentage", scoreResult.percentage);
        request.setAttribute("formattedPercentage", String.format("%.1f", scoreResult.percentage));
        request.setAttribute("timeTaken", testResult.getTimeTaken());
        request.setAttribute("formattedTime", formatTime(testResult.getTimeTaken()));
        request.setAttribute("grade", getGrade(scoreResult.percentage));
        request.setAttribute("questions", questions);
        request.setAttribute("currentDate", new java.util.Date());
    }

  // error handling
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorTitle, String userMessage, Exception e,
                             String testId, String studentId) throws ServletException, IOException {

        // Logged detailed error information
        System.err.println("=== SubmitTest Error ===");
        System.err.println("Error Title: " + errorTitle);
        System.err.println("Test ID: " + testId);
        System.err.println("Student ID: " + studentId);
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("Exception Type: " + e.getClass().getSimpleName());
        e.printStackTrace();
        System.err.println("========================");

        // Setting user-friendly error attributes
        request.setAttribute("hasError", true);
        request.setAttribute("errorTitle", errorTitle);
        request.setAttribute("errorMessage", userMessage);
        request.setAttribute("testId", testId);
        request.setAttribute("studentId", studentId);

        //debugging
        request.setAttribute("technicalDetails", e.getClass().getSimpleName() + ": " + e.getMessage());

        // Forward to error page
        RequestDispatcher dispatcher = request.getRequestDispatcher("testsubmissionerror.jsp");
        dispatcher.forward(request, response);
    }

    private String generateResultId() {
        return "RESULT_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String formatTime(String timeTakenStr) {
        try {
            int totalSeconds = Integer.parseInt(timeTakenStr);
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            return timeTakenStr;
        }
    }

    private String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B+";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C";
        else if (percentage >= 40) return "D";
        else return "F";
    }

    // Helper classes
    private static class ScoreCalculationResult {
        final int correctAnswers;
        final int attemptedQuestions;
        final double percentage;

        ScoreCalculationResult(int correctAnswers, int attemptedQuestions, double percentage) {
            this.correctAnswers = correctAnswers;
            this.attemptedQuestions = attemptedQuestions;
            this.percentage = percentage;
        }
    }

    // Custom exceptions
    private static class ParameterValidationException extends Exception {
        public ParameterValidationException(String message) {
            super(message);
        }
    }

    private static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }

        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}