package com.yash.parikshan.dao;

import com.yash.parikshan.model.Question;

import java.util.List;

public interface QuestionDao {
    void addQuestions(List<Question> questions) throws Exception;
    List<Question> getQuestionsByTestId(String testId) throws Exception;
}
