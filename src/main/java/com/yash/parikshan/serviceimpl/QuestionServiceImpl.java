package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.QuestionDao;
import com.yash.parikshan.daoimpl.QuestionDaoImpl;
import com.yash.parikshan.model.Question;
import com.yash.parikshan.service.QuestionService;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private QuestionDao questionDao = new QuestionDaoImpl();


    @Override
    public void addQuestions(List<Question> questions) throws Exception {
        questionDao.addQuestions(questions);
    }

    @Override
    public List<Question> getQuestionsByTestId(String testId) throws Exception {
        return questionDao.getQuestionsByTestId(testId);
    }
}
