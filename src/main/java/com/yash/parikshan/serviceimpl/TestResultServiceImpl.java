package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.TestResultDao;
import com.yash.parikshan.daoimpl.TestResultDaoImpl;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.service.TestResultService;

import java.util.List;
import java.util.UUID;

public class TestResultServiceImpl implements TestResultService {

    private TestResultDao testResultDao;

    public TestResultServiceImpl() {
        this.testResultDao = new TestResultDaoImpl();
    }

    @Override
    public List<TestResult> getAllResults() {
        try {
            return testResultDao.getAllResults();
        } catch (Exception e) {
            System.err.println("Error in service getAllResults: " + e.getMessage());
            throw new RuntimeException("Failed to get all results", e);
        }
    }

    @Override
    public List<TestResult> getResultsByTestId(String testId) {
        if (testId == null || testId.trim().isEmpty()) {
            throw new IllegalArgumentException("Test ID cannot be null or empty");
        }

        try {
            return testResultDao.getResultsByTestId(testId);
        } catch (Exception e) {
            System.err.println("Error in service getResultsByTestId: " + e.getMessage());
            throw new RuntimeException("Failed to get results by test ID", e);
        }
    }

    @Override
    public List<TestResult> getResultsByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        try {
            return testResultDao.getResultsByStudentId(studentId);
        } catch (Exception e) {
            System.err.println("Error in service getResultsByStudentId: " + e.getMessage());
            throw new RuntimeException("Failed to get results by student ID", e);
        }
    }

    @Override
    public TestResult getResultById(String resultId) {
        if (resultId == null || resultId.trim().isEmpty()) {
            throw new IllegalArgumentException("Result ID cannot be null or empty");
        }

        try {
            return testResultDao.getResultById(resultId);
        } catch (Exception e) {
            System.err.println("Error in service getResultById: " + e.getMessage());
            throw new RuntimeException("Failed to get result by ID", e);
        }
    }

    @Override
    public List<Test> getAllTests() {
        try {
            return testResultDao.getAllTests();
        } catch (Exception e) {
            System.err.println("Error in service getAllTests: " + e.getMessage());
            throw new RuntimeException("Failed to get all tests", e);
        }
    }

    @Override
    public boolean saveTestResult(TestResult testResult) {
        if (testResult == null) {
            throw new IllegalArgumentException("TestResult cannot be null");
        }

        if (testResult.getStudentId() == null || testResult.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }

        if (testResult.getTestId() == null || testResult.getTestId().trim().isEmpty()) {
            throw new IllegalArgumentException("Test ID is required");
        }

        try {
            return testResultDao.saveTestResult(testResult);
        } catch (Exception e) {
            System.err.println("Error in service saveTestResult: " + e.getMessage());
            throw new RuntimeException("Failed to save test result", e);
        }
    }

    @Override
    public String generateResultId() {
        try {
            return "RESULT_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } catch (Exception e) {
            System.err.println("Error in service generateResultId: " + e.getMessage());
            // Fallback to timestamp-based ID
            return "RESULT_" + System.currentTimeMillis();
        }
    }

    @Override
    public boolean terminateTestForViolations(String testId, String studentId, int violationCount) {
        try {
            String reason = "Test terminated - " + violationCount + " violations detected";
            boolean terminated = testResultDao.terminateTest(testId, studentId, reason);

            if (terminated) {
                System.out.println("Test terminated for student: " + studentId + ", violations: " + violationCount);
            }

            return terminated;

        } catch (Exception e) {
            System.err.println("Error in service terminating test: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTestAlreadyTerminated(String testId, String studentId) {
        try {
            return testResultDao.isTestTerminated(testId, studentId);
        } catch (Exception e) {
            System.err.println("Error checking if test terminated: " + e.getMessage());
            return false;
        }
    }

    @Override
    public TestResult getTestResult(String testId, String studentId) {
        return null;
    }

    @Override
    public List<TestResult> findByStudentId(String studentId) throws Exception {
        return testResultDao.findByStudentId(studentId);
        }
    }

