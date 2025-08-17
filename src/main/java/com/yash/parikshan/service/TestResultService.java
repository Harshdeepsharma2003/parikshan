package com.yash.parikshan.service;

import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;

import java.util.List;

public interface TestResultService {

    List<TestResult> getAllResults();

    List<TestResult> getResultsByTestId(String testId);

    List<TestResult> getResultsByStudentId(String studentId);

    TestResult getResultById(String resultId);

    List<Test> getAllTests();

    boolean saveTestResult(TestResult testResult);

    String generateResultId();

    List<TestResult> findByStudentId(String studentId) throws Exception;
  //  boolean terminateTestForViolations(String testId, String studentId, int violationCount);
    // boolean isTestAlreadyTerminated(String testId, String studentId);
    // TestResult getTestResult(String testId, String studentId);

}
