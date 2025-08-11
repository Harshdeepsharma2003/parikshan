package com.yash.parikshan.model;


public class Question {
    private String questionId;
    private String testId;
    private String content;
    private String questionType;  // e.g., "MCQ", "TrueFalse", "FillBlank"
    private String imageUrl;// optional
    private String answerText;
    private boolean isCorrect;

    public Question(){}


    public Question(String questionId, String testId, String content, String questionType, String imageUrl, String answerText, boolean isCorrect) {
        this.questionId = questionId;
        this.testId = testId;
        this.content = content;
        this.questionType = questionType;
        this.imageUrl = imageUrl;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}