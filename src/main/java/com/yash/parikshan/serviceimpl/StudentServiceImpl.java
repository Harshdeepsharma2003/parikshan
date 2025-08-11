package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.StudentDao;
import com.yash.parikshan.daoimpl.StudentDaoImpl;
import com.yash.parikshan.model.Student;
import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.util.PasswordUtil;

public class StudentServiceImpl implements StudentService {

    public StudentDao studentDao=new StudentDaoImpl();

    @Override
    public void insertStudent(Student student) throws Exception {

        studentDao.save(student);

    }

    @Override
    public Student getStudentById(String studentId) {
        try {
            return studentDao.getStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateStudentProfile(String studentId, Student student) {
        try {
            studentDao.updateStudentProfile(studentId,student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String studentId, String password) throws Exception {
        // Input validation
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            // Get student from database
            Student student = studentDao.getStudentById(studentId.trim());

            if (student == null) {
                System.out.println("No user found for ID: " + studentId);
                return false; // Student not found
            }

            if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                System.out.println("No password hash found for student: " + studentId);
                return false; // No password set
            }

            System.out.println("Retrieved user: " + student.getStudentId());
            System.out.println("Verifying password for: " + studentId);

            // Verify password using BCrypt
            boolean isValid = PasswordUtil.verifyPassword(password, student.getPassword());

            System.out.println("Password verification result: " + isValid);
            return isValid;

        } catch (Exception e) {
            System.err.println("Authentication error for student " + studentId + ": " + e.getMessage());
            throw new Exception("Authentication system error", e);
        }
    }

    @Override
    public boolean deleteProfile(String studentId, String password) {
        try {
            studentDao.deleteProfile(studentId,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
