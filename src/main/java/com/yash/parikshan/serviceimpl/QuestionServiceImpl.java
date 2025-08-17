package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.QuestionDao;
import com.yash.parikshan.daoimpl.QuestionDaoImpl;
import com.yash.parikshan.exceptions.GlobalException;
import com.yash.parikshan.model.Question;
import com.yash.parikshan.service.QuestionService;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private QuestionDao questionDao = new QuestionDaoImpl();

    private static final int MAX_QUESTIONS_PER_TEST = 5;


    @Override
    public void addQuestions(List<Question> questions) throws Exception {
        questionDao.addQuestions(questions);
    }

    @Override
    public List<Question> getQuestionsByTestId(String testId) throws Exception {
        return questionDao.getQuestionsByTestId(testId);
    }

    @Override
    public int getMaxQuestionsAllowed() {
        return MAX_QUESTIONS_PER_TEST;
    }

    @Override
    public void validateQuestionCount(List<Question> questions) throws Exception {
        if (questions == null || questions.isEmpty()) {
            throw GlobalException.validationError(
                    "No valid questions found. Please ensure each question has: " +
                            "question text, all 4 options (A, B, C, D), and a correct answer selection");
        }

        if (questions.size() > MAX_QUESTIONS_PER_TEST) {
            throw GlobalException.validationError(
                    "Maximum " + MAX_QUESTIONS_PER_TEST + " questions allowed per test. " +
                            "You submitted " + questions.size() + " questions.");
        }
    }

    @Override
    public void addQuestionsWithValidation(List<Question> questions) throws Exception {
        validateQuestionCount(questions);
        addQuestions(questions);
    }
}
