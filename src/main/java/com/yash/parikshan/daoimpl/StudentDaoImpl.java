package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.StudentDao;
import com.yash.parikshan.model.Student;
import com.yash.parikshan.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDaoImpl implements StudentDao {


    @Override
    public Student getStudentById(String studentId) throws Exception {
        String sql = "SELECT studentid,name,email,passwordhash,phone FROM students WHERE studentid = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            System.out.println("Searching for student with ID: " + studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String passwordHash = rs.getString("passwordhash");
                    String phone = rs.getString("phone");

                    return new Student(studentId,name,email,passwordHash,phone);
                }
            }
        }
        return null;
    }


    @Override
    public void save(Student student) throws Exception {

        System.out.println("Attempting DB insert for student: " + student.getStudentId());
        String sql = "INSERT INTO students (studentid,name,email,passwordhash,phone) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPassword());
            ps.setString(5, student.getPhone());

            int rowsAffected = ps.executeUpdate();  // Executes the insert once
            System.out.println("Inserted rows: " + rowsAffected);  // Logs the outcome

        }
     catch (SQLException e) {
        e.printStackTrace();
        throw new Exception("Error inserting student", e);
    }

}

    @Override
    public Student getStudentId(String studentId) throws Exception {
        Student student = null;
        String query = "SELECT * FROM students WHERE studentid = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1,studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setStudentId(rs.getString("studentid"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPassword(rs.getString("passwordHash"));
                student.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            throw new Exception("Error fetching contact from database", e);
        }
        return student;
    }

    @Override
    public void updateStudentProfile(String studentId, Student student) {
            String sql = "UPDATE students SET name = ?, email = ?,passwordhash = ?,phone=? WHERE studentid = ? ";
            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1,student.getName());
                System.out.println("Username: " + student.getName());
                ps.setString(2, student.getEmail());
                ps.setString(3, student.getPassword());
                ps.setString(4, student.getPhone());
                ps.setString(5,student.getStudentId());

                System.out.println("Updating user with ID: " + studentId);
                System.out.println("New username: " + student.getName());
                System.out.println("New password: " + student.getPassword());
                System.out.println("New email: " + student.getEmail());

                int rowsAffected = ps.executeUpdate();
                System.out.println("Rows updated: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void deleteProfile(String studentId, String password) {
        String sql = "DELETE FROM students WHERE studentid = ? and passwordhash= ? ";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,studentId);
            ps.setString(2,password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
