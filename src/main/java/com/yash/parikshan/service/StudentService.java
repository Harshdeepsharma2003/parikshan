package com.yash.parikshan.service;

import com.yash.parikshan.model.Student;

public interface StudentService {

    void insertStudent(Student student) throws Exception;
    Student getStudentById(String studentId);
    void updateStudentProfile(String userId,Student student);
    boolean authenticate(String studentId, String password) throws Exception;
    void deleteProfile(String userId,String password);
}
