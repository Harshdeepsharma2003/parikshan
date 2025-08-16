package com.yash.parikshan.serviceimpl;

import com.yash.parikshan.dao.StudentDao;
import com.yash.parikshan.daoimpl.StudentDaoImpl;
import com.yash.parikshan.model.Student;
import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.util.PasswordUtil;

public class StudentServiceImpl implements StudentService {

    public StudentDao studentDao = new StudentDaoImpl();

    @Override
    public void insertStudent(Student student) throws Exception {
        // Basic password validation
        String password = student.getPassword();

        // Check minimum 6 characters
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be at least 6 characters long");
        }

        // Check for at least 1 special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new Exception("Password must contain at least 1 special character");
        }

        // Hash the password after validation
        String hashedPassword = PasswordUtil.hashPassword(password);
        student.setPassword(hashedPassword);

        // Save to database
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
        // Validate student ID
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        // If password is being updated, validate it
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            String password = student.getPassword();

            // Check minimum 6 characters
            if (password.length() < 6) {
                try {
                    throw new Exception("Password must be at least 6 characters long");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Check for at least 1 special character
            if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                try {
                    throw new Exception("Password must contain at least 1 special character");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Hash the password
            String hashedPassword = PasswordUtil.hashPassword(password);
            student.setPassword(hashedPassword);
        }

        studentDao.updateStudentProfile(studentId, student);
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
            // First authenticate the user
            if (!authenticate(studentId, password)) {
                return false; // Wrong password
            }

            // Hash the password for deletion (DAO expects hashed password)
            String hashedPassword = PasswordUtil.hashPassword(password);
            studentDao.deleteProfile(studentId, hashedPassword);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting profile: " + e.getMessage());
            return false;
        }
    }
}
