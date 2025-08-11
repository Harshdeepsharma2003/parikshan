package com.yash.parikshan.service;

import com.yash.parikshan.model.Question;

import java.util.List;

public interface QuestionService {
    void addQuestions(List<Question> questions) throws Exception;
    List<Question> getQuestionsByTestId(String testId) throws Exception;

}
