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


        String hashedPassword = PasswordUtil.hashPassword(password);
        student.setPassword(hashedPassword);


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

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }


        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            String password = student.getPassword();


            if (password.length() < 6) {
                try {
                    throw new Exception("Password must be at least 6 characters long");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


            if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                try {
                    throw new Exception("Password must contain at least 1 special character");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


            String hashedPassword = PasswordUtil.hashPassword(password);
            student.setPassword(hashedPassword);
        }

        studentDao.updateStudentProfile(studentId, student);
    }

    @Override
    public boolean authenticate(String studentId, String password) throws Exception {

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

            try {
                Student student = studentDao.getStudentById(studentId.trim());

                if (student == null) {
                    System.out.println("No user found for ID: " + studentId);
                    return false;
                }

                String storedHash = student.getPassword(); // BCrypt hash

                System.out.println("DEBUG: Raw password length: " + password.length());
                System.out.println("DEBUG: Stored hash: " + storedHash);
                System.out.println("DEBUG: Hash starts with $2: " + (storedHash != null && storedHash.startsWith("$2")));
                System.out.println("DEBUG: Hash length: " + (storedHash != null ? storedHash.length() : "null"));

                if (storedHash == null || storedHash.trim().isEmpty()) {
                    System.out.println("No password hash found for student: " + studentId);
                    return false;
                }


                if (!storedHash.startsWith("$2")) {
                    System.out.println("WARNING: Password doesn't appear to be BCrypt hashed!");
                    System.out.println("Stored value: " + storedHash);
                }

                boolean isValid = PasswordUtil.verifyPassword(password, storedHash);
                System.out.println("Password verification result: " + isValid);

                return isValid;

            } catch (Exception e) {
                System.err.println("Authentication error for student " + studentId + ": " + e.getMessage());
                e.printStackTrace();
                throw new Exception("Authentication system error", e);
            }
        }



    @Override
    public boolean deleteProfile(String studentId, String password) {
        try {

            if (!authenticate(studentId, password)) {
                return false;
            }


            String hashedPassword = PasswordUtil.hashPassword(password);
            studentDao.deleteProfile(studentId, hashedPassword);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting profile: " + e.getMessage());
            return false;
        }
    }
}
