package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.QuestionDao;
import com.yash.parikshan.model.Question;
import com.yash.parikshan.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {
    @Override
    public void addQuestions(List<Question> questions) throws Exception {
        String sql = "INSERT INTO questions (questionid, content, questiontype, imageurl,testid, answertext, iscorrect) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (Question q : questions) {
                ps.setString(1, q.getQuestionId());
                ps.setString(2, q.getContent());
                ps.setString(3, q.getQuestionType());
                ps.setString(4, q.getImageUrl());
                ps.setString(5, q.getTestId());
                ps.setString(6, q.getAnswerText());
                ps.setBoolean(7, q.isCorrect());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public List<Question> getQuestionsByTestId(String testId) throws Exception {
        List<Question> questions = new ArrayList<>();

        // Get ALL options for each question, not just correct ones
        String sql = "SELECT questionid, content, questiontype, imageurl, testid, answertext, iscorrect " +
                "FROM questions WHERE testid = ? AND questiontype = 'MCQ' " +
                "ORDER BY questionid, iscorrect DESC"; // Correct answers first

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, testId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setQuestionId(rs.getString("questionid"));
                question.setTestId(rs.getString("testid"));
                question.setContent(rs.getString("content"));
                question.setQuestionType(rs.getString("questiontype"));
                question.setImageUrl(rs.getString("imageurl"));
                question.setAnswerText(rs.getString("answertext"));
                question.setCorrect(rs.getBoolean("iscorrect"));
                questions.add(question);
            }

            System.out.println("DEBUG: Found " + questions.size() + " questions for testId: " + testId);
            for (Question q : questions) {
                System.out.println("Question ID: " + q.getQuestionId() + ", Content: " + q.getContent());
            }
        }
        return questions;
    }
}

