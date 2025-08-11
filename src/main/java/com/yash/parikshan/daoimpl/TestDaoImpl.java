package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.TestDao;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestDaoImpl implements TestDao {
    @Override
    public boolean insertTest(Test test) throws Exception {
        String sql = "INSERT INTO tests (testid, title, description,noofquestions) VALUES (?, ?, ?,?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, test.getTestId());
            stmt.setString(2, test.getTitle());
            stmt.setString(3, test.getDescription());
            stmt.setString(4, test.getNoOfQuestions());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Test getTestById(String testId) throws Exception {
        String sql = "SELECT testid,title,description,noofquestions FROM tests WHERE testid = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String noOfQuestions = rs.getString("noofquestions");

                    return new Test(testId, title, description, noOfQuestions);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteTest(String testId) {
        String sql = "DELETE FROM tests WHERE testid = ? ";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, testId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Test getLoginTestId(String testId) throws Exception {
        String sql = "SELECT testid,title,description,noofquestions FROM tests WHERE testid = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String noOfQuestions = rs.getString("noofquestions");

                    return new Test(testId, title, description, noOfQuestions);
                }
            }
        }
        return null;
    }

    @Override
    public List<Test> findAllActive() throws SQLException {
        System.out.println("=== TestDAO.findAllActive() called ===");

        List<Test> list = new ArrayList<>();
        String sql = "SELECT * FROM tests";  // Get ALL tests for now

        System.out.println("SQL Query: " + sql);

        try (Connection conn = DbUtil.getConnection()) {
            System.out.println("Database connection obtained: " + (conn != null ? "SUCCESS" : "FAILED"));

            if (conn != null) {
                System.out.println("Connection URL: " + conn.getMetaData().getURL());
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                System.out.println("PreparedStatement created");

                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("Query executed");

                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("Processing row " + count);

                        Test test = new Test();

                        String testId = rs.getString("testid");
                        String title = rs.getString("title");
                        String description = rs.getString("description");
                        String noOfQuestions = rs.getString("noofquestions");

                        System.out.println("Row " + count + " data:");
                        System.out.println("  testid: '" + testId + "'");
                        System.out.println("  title: '" + title + "'");
                        System.out.println("  description: '" + description + "'");
                        System.out.println("  noofquestions: '" + noOfQuestions + "'");

                        test.setTestId(testId);
                        test.setTitle(title);
                        test.setDescription(description);
                        test.setNoOfQuestions(noOfQuestions);

                        list.add(test);
                        System.out.println("Test added to list");
                    }
                    System.out.println("Total rows processed: " + count);
                }
            }
        } catch (SQLException e) {
            System.err.println("=== SQL ERROR in TestDAO.findAllActive() ===");
            e.printStackTrace();
            throw e;
        }

        System.out.println("Returning list with " + list.size() + " tests");
        return list;
    }
}



