package com.yash.parikshan.service;

import com.yash.parikshan.model.Question;
import com.yash.parikshan.model.Test;

import java.sql.SQLException;
import java.util.List;

public interface TestService {

    boolean registerTest(Test test) throws Exception;
    boolean authenticate(String testId) throws Exception;
    void deleteTest(String testId);
    boolean loginTest(String testId) throws Exception;
    List<Test> findAllActive() throws SQLException, Exception;

}
