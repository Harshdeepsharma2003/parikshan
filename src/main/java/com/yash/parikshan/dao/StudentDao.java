package com.yash.parikshan.dao;

import com.yash.parikshan.model.Student;

public interface StudentDao {

    Student getStudentById(String studentId) throws Exception;
    void save(Student student) throws Exception;
   // for update operation,verifying id
    Student getStudentId(String studentId) throws Exception;
    void updateStudentProfile(String studentId,Student student);
    void deleteProfile(String studentId,String password);
}
