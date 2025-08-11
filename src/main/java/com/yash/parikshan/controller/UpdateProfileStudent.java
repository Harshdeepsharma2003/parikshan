package com.yash.parikshan.controller;

import com.yash.parikshan.model.Student;
import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.serviceimpl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/UpdateProfileStudent")
public class UpdateProfileStudent extends HttpServlet {

        private StudentService studentService= new StudentServiceImpl();

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            String studentId = request.getParameter("studentId");
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                request.setAttribute("errorMessage", "No contact found for ID " + studentId);
            } else {
                request.setAttribute("Student", student);
            }

            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("studentid") != null) {
                String sessionUserId = (String) session.getAttribute("studentid");
                // Use sessionUserId as needed
            } else {
                response.sendRedirect("updateprofile.jsp");
                return;
            }
            request.getRequestDispatcher("updateprofile.jsp").forward(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String studentId = request.getParameter("studentId"); // read-only, used for identifying record
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone=request.getParameter("phone");

            Student student = new Student();
            student.setName(name);
            student.setPassword(password);
            student.setEmail(email);
            student.setPhone(phone);
            studentService.updateStudentProfile(studentId,student);

            response.sendRedirect("testformats.jsp"); // or wherever you want to redirect
        }
}





