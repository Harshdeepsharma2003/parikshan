package com.yash.parikshan.dao;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;

import java.util.List;

public interface TestResultDao {

    /**
     * Get all test results
     * @return List of all test results
     */
    List<TestResult> getAllResults();

    /**
     * Get test results by test ID
     * @param testId Test ID to filter by
     * @return List of test results for the specified test
     */
    List<TestResult> getResultsByTestId(String testId);

    /**
     * Get test results by student ID
     * @param studentId Student ID to filter by
     * @return List of test results for the specified student
     */
    List<TestResult> getResultsByStudentId(String studentId);

    /**
     * Get a specific test result by result ID
     * @param resultId Result ID
     * @return TestResult object or null if not found
     */
    TestResult getResultById(String resultId);

    /**
     * Get all available tests for dropdown
     * @return List of Test objects
     */
    List<Test> getAllTests();

    /**
     * Save a test result to database
     * @param testResult TestResult object to save
     * @return boolean indicating success
     */
    boolean saveTestResult(TestResult testResult);
}
