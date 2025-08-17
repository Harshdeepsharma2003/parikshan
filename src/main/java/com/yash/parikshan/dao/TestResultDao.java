package com.yash.parikshan.dao;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;

import java.util.List;

public interface TestResultDao {

    //list of all test results
    List<TestResult> getAllResults();

    //for specific test
    List<TestResult> getResultsByTestId(String testId);

    //for specific student
    List<TestResult> getResultsByStudentId(String studentId);

    TestResult getResultById(String resultId);

    List<Test> getAllTests();


   // boolean isTestTerminated(String testId, String studentId);

    TestResult getTestResult(String testId, String studentId);

    List<TestResult> findByStudentId(String studentId) throws Exception;
    boolean saveTestResult(TestResult testResult);

    //List<TestResult> getResultsWithViolations();

    // boolean terminateTest(String testId, String studentId, String reason);


}
