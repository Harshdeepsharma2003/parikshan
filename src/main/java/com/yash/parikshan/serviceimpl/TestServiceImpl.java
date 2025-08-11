package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.TestDao;
import com.yash.parikshan.daoimpl.TestDaoImpl;
import com.yash.parikshan.model.Test;
import com.yash.parikshan.service.TestService;

import java.sql.SQLException;
import java.util.List;


public class TestServiceImpl implements TestService {

    private TestDao testDao = new TestDaoImpl();


    @Override
    public boolean registerTest(Test test) throws Exception {
        return testDao.insertTest(test);
    }

    @Override
    public boolean authenticate(String testId) throws Exception {
        Test test = testDao.getTestById(testId);
        if (test != null ) {
            System.out.println("Retrieved test: " + test);
            System.out.println("User input testId: '" + testId + "'");
            System.out.println("DB testId: '" + test.getTestId() + "'");

            boolean match = testId != null && testId.trim().equals(test.getTestId().trim());
            System.out.println("Match result: " + match);
            return match;
        } else {
            System.out.println("Found test id: " + (test != null ? test.getTestId() : "No test found"));
            return false;
        }
    }

    @Override
    public void deleteTest(String testId) {
        try {
            testDao.deleteTest(testId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean loginTest(String testId) throws Exception {
        Test test = testDao.getLoginTestId(testId);
        if (test != null ) {
            System.out.println("Retrieved test: " + test);
            System.out.println("User input testId: '" + testId + "'");
            System.out.println("DB testId: '" + test.getTestId() + "'");

            boolean match = testId != null && testId.trim().equals(test.getTestId().trim());
            System.out.println("Match result: " + match);
            return match;
        } else {
            System.out.println("Found test id: " + (test != null ? test.getTestId() : "No test found"));
            return false;
        }
    }

    @Override
    public List<Test> findAllActive() throws Exception {
        System.out.println("=== TestServiceImpl.findAllActive() called ===");
        try {
            System.out.println("Calling testDao.findAllActive()...");
            List<Test> result = testDao.findAllActive();
            System.out.println("DAO returned: " + (result != null ? result.size() + " tests" : "NULL"));
            return result;
        } catch (Exception e) {
            System.err.println("=== ERROR in TestServiceImpl.findAllActive() ===");
            e.printStackTrace();
            throw new Exception("Unable to fetch available tests", e);
        }
    }
}


