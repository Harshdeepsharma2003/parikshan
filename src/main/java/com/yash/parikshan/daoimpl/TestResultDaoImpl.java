package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.TestResultDao;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestResultDaoImpl implements TestResultDao {

    @Override
    public List<TestResult> getAllResults() {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, " +
                    "s.name as studentname, t.title as testtitle " +
                    "FROM testresults tr " +
                    "JOIN students s ON tr.studentid = s.studentid " +
                    "JOIN tests t ON tr.testid = t.testid " +
                    "ORDER BY tr.testdate DESC";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TestResult result = mapResultSetToTestResult(resultSet);
                results.add(result);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all results: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return results;
    }

    @Override
    public List<TestResult> getResultsByTestId(String testId) {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, " +
                    "s.name as studentname, t.title as testtitle " +
                    "FROM testresults tr " +
                    "JOIN students s ON tr.studentid = s.studentid " +
                    "JOIN tests t ON tr.testid = t.testid " +
                    "WHERE tr.testid = ? " +
                    "ORDER BY tr.score DESC, tr.time_taken ASC";

            statement = connection.prepareStatement(sql);
            statement.setString(1, testId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TestResult result = mapResultSetToTestResult(resultSet);
                results.add(result);
            }

        } catch (SQLException e) {
            System.err.println("Error getting results by test ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return results;
    }

    @Override
    public List<TestResult> getResultsByStudentId(String studentId) {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, " +
                    "s.name as studentname, t.title as testtitle " +
                    "FROM testresults tr " +
                    "JOIN students s ON tr.studentid = s.studentid " +
                    "JOIN tests t ON tr.testid = t.testid " +
                    "WHERE tr.studentid = ? " +
                    "ORDER BY tr.testdate DESC";

            statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TestResult result = mapResultSetToTestResult(resultSet);
                results.add(result);
            }

        } catch (SQLException e) {
            System.err.println("Error getting results by student ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return results;
    }

    @Override
    public TestResult getResultById(String resultId) {
        TestResult result = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, " +
                    "s.name as studentname, t.title as testtitle " +
                    "FROM testresults tr " +
                    "JOIN students s ON tr.studentid = s.studentid " +
                    "JOIN tests t ON tr.testid = t.testid " +
                    "WHERE tr.result_id = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, resultId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapResultSetToTestResult(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error getting result by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return result;
    }

    @Override
    public List<Test> getAllTests() {
        List<Test> tests = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT testid, title, description, noofquestions FROM tests ORDER BY title";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Test test = new Test();
                test.setTestId(resultSet.getString("testid"));
                test.setTitle(resultSet.getString("title"));
                test.setDescription(resultSet.getString("description"));
                test.setNoOfQuestions(resultSet.getString("noofquestions"));
                tests.add(test);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all tests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return tests;
    }

    @Override
    public boolean saveTestResult(TestResult testResult) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "INSERT INTO testresults (resultid, studentid, testid, score, totalmarks, timetaken, testdate) VALUES (?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, testResult.getResultId());
            statement.setString(2, testResult.getStudentId());
            statement.setString(3, testResult.getTestId());
            statement.setString(4, testResult.getScore());
            statement.setString(5, testResult.getTotalMarks());
            statement.setString(6, testResult.getTimeTaken());
            statement.setTimestamp(7, testResult.getTestDate());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving test result: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Helper method to map ResultSet to TestResult object
     * Fixed to match the actual column aliases in SQL queries
     */
    private TestResult mapResultSetToTestResult(ResultSet resultSet) throws SQLException {
        TestResult result = new TestResult();
        result.setResultId(resultSet.getString("resultid"));
        result.setStudentId(resultSet.getString("studentid"));
        result.setTestId(resultSet.getString("testid"));
        result.setScore(resultSet.getString("score"));
        result.setTotalMarks(resultSet.getString("totalmarks"));
        result.setTimeTaken(resultSet.getString("timetaken"));
        result.setTestDate(resultSet.getTimestamp("testdate"));
        result.setStudentName(resultSet.getString("studentname"));
        result.setTestTitle(resultSet.getString("testtitle"));

        return result;
    }

    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
