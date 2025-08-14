package com.yash.parikshan.daoimpl;

import com.yash.parikshan.controller.RecordingServlet;
import com.yash.parikshan.dao.TestResultDao;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.logging.Level;

public class TestResultDaoImpl implements TestResultDao {

    private static final Logger logger = Logger.getLogger(RecordingServlet.class.getName());

    // Updated mapping method to include status fields
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

        // Status fields
        String status = resultSet.getString("status");
        result.setStatus(status != null ? status : "COMPLETED");
        result.setInvalidationReason(resultSet.getString("invalidationreason"));
        result.setViolationCount(resultSet.getInt("violationcount"));

        // Calculate percentage
        try {
            double score = Double.parseDouble(result.getScore());
            double total = Double.parseDouble(result.getTotalMarks());
            double percentage = (score / total) * 100;
            result.setPercentage(String.format("%.1f", percentage));
        } catch (Exception e) {
            result.setPercentage("0.0");
        }

        return result;
    }

    @Override
    public List<TestResult> getAllResults() {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, tr.status, " +
                    "tr.invalidationreason, tr.violationcount, " +
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
            System.err.println("Error getting all results with status: " + e.getMessage());
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
                    "ORDER BY tr.score DESC, tr.timetaken ASC";

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

    @Override
    public boolean terminateTest(String testId, String studentId, String reason) {
        String sql = "INSERT INTO testresults (testid, studentid, status, invalidationreason, testdate, score, totalmarks, timetaken) " +
                "VALUES (?, ?, 'TERMINATED', ?, NOW(), '0', '0', '0') " +
                "ON DUPLICATE KEY UPDATE status = 'TERMINATED', invalidation_reason = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, testId);
            stmt.setString(2, studentId);
            stmt.setString(3, reason);
            stmt.setString(4, reason);

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error terminating test: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTestTerminated(String testId, String studentId) {
        String sql = "SELECT status FROM testresults WHERE testid = ? AND studentid = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, testId);
            stmt.setString(2, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return "TERMINATED".equals(rs.getString("status"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error checking test termination: " + e.getMessage());
        }

        return false;
    }

    @Override
    public TestResult getTestResult(String testId, String studentId) {
        String sql = "SELECT * FROM testresults WHERE testid = ? AND studentid = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, testId);
            stmt.setString(2, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TestResult result = new TestResult();
                    result.setResultId(rs.getString("resultid"));
                    result.setTestId(rs.getString("testid"));
                    result.setStudentId(rs.getString("studentid"));
                    result.setScore(rs.getString("score"));
                    result.setTotalMarks(rs.getString("totalmarks"));
                    result.setTimeTaken(rs.getString("timetaken"));
                    result.setTestDate(rs.getTimestamp("testdate"));

                    // Handle new fields that might be null
                    String status = rs.getString("status");
                    result.setStatus(status != null ? status : "COMPLETED");

                    result.setInvalidationReason(rs.getString("invalidationreason"));

                    int violationCount = rs.getInt("violationcount");
                    result.setViolationCount(violationCount);

                    return result;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting test result for testId: " + testId + ", studentId: " + studentId);
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public List<TestResult> getResultsWithViolations() {
        List<TestResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();
            String sql = "SELECT tr.resultid, tr.studentid, tr.testid, tr.score, " +
                    "tr.totalmarks, tr.timetaken, tr.testdate, tr.status, " +
                    "tr.invalidationreason, tr.violationcount, " +
                    "s.name as studentname, t.title as testtitle " +
                    "FROM testresults tr " +
                    "JOIN students s ON tr.studentid = s.studentid " +
                    "JOIN tests t ON tr.testid = t.testid " +
                    "WHERE tr.violationcount > 0 OR tr.status = 'TERMINATED' OR tr.status = 'INVALIDATED' " +
                    "ORDER BY tr.violationcount DESC, tr.testdate DESC";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TestResult result = mapResultSetToTestResult(resultSet);
                results.add(result);
            }

        } catch (SQLException e) {
            System.err.println("Error getting results with violations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return results;
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

    // Add this method to your existing TestResultDAOImpl class
    private void invalidateTest(String testId, String studentId) {
        String sql = "UPDATE testresults SET status = 'INVALIDATED', invalidationreason = 'Cheating detected' WHERE testid = ? AND studentid = ?";

        try (Connection connection = DbUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, testId);
            stmt.setString(2, studentId);
            int updated = stmt.executeUpdate();

            if (updated > 0) {
                logger.info("Test invalidated successfully for student: " + studentId + ", test: " + testId);
            } else {
                logger.warning("No test record found to invalidate for student: " + studentId + ", test: " + testId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to invalidate test", e);
        }
    }


}