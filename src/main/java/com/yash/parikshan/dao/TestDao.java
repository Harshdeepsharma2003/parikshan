package com.yash.parikshan.dao;

import com.yash.parikshan.model.Test;
import java.sql.SQLException;
import java.util.List;

public interface TestDao {
    boolean insertTest(Test test) throws Exception;
    Test getTestById(String testId) throws Exception;
    void deleteTest(String testId) ;
     List<Test> findAllActive() throws SQLException;
}
