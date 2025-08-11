package com.yash.parikshan.model;

public class Test {
    private String testId;
    private String title;
    private String description;
    private String noOfQuestions;

    public Test() {}

    public Test(String testId, String title, String description, String noOfQuestions) {
        this.testId = testId;
        this.title = title;
        this.description = description;
        this.noOfQuestions = noOfQuestions;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    @Override
    public String toString() {
        return "Test{" +
                "testId='" + testId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", noOfQuestions='" + noOfQuestions + '\'' +
                '}';
    }
}
