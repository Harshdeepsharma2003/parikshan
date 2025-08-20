package com.yash.parikshan.dao;

import com.yash.parikshan.model.Recording;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.model.TestResult;
import com.yash.parikshan.model.VideoProcessorResult;

import java.sql.SQLException;
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

    List<TestResult> findByStudentId(String studentId) throws Exception;
    boolean saveTestResult(TestResult testResult);


    void updateTestResults(Recording recording, VideoProcessorResult result) throws SQLException;
}
