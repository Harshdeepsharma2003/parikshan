package com.yash.parikshan.model;

import java.sql.Timestamp;

public class TestResult {

    private String resultId;
    private String studentId;
    private String testId;
    private String score;
    private String totalMarks;
    private String timeTaken;
    private Timestamp testDate;

    // Additional fields for display purposes
    private String studentName;
    private String testTitle;
    private String percentage;
    private String status = "COMPLETED"; // COMPLETED, TERMINATED
    private String invalidationReason;
    private int violationCount = 0;

    public TestResult(){}

    public TestResult(String resultId, String studentId, String testId, String score, String totalMarks, String timeTaken, Timestamp testDate, String studentName, String testTitle, String percentage, String status, String invalidationReason, int violationCount) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.testId = testId;
        this.score = score;
        this.totalMarks = totalMarks;
        this.timeTaken = timeTaken;
        this.testDate = testDate;
        this.studentName = studentName;
        this.testTitle = testTitle;
        this.percentage = percentage;
        this.status = status;
        this.invalidationReason = invalidationReason;
        this.violationCount = violationCount;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Timestamp getTestDate() {
        return testDate;
    }

    public void setTestDate(Timestamp testDate) {
        this.testDate = testDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvalidationReason() {
        return invalidationReason;
    }

    public void setInvalidationReason(String invalidationReason) {
        this.invalidationReason = invalidationReason;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "resultId='" + resultId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", testId='" + testId + '\'' +
                ", score='" + score + '\'' +
                ", totalMarks='" + totalMarks + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                ", testDate=" + testDate +
                ", studentName='" + studentName + '\'' +
                ", testTitle='" + testTitle + '\'' +
                ", percentage='" + percentage + '\'' +
                ", status='" + status + '\'' +
                ", invalidationReason='" + invalidationReason + '\'' +
                ", violationCount=" + violationCount +
                '}';
    }
}
