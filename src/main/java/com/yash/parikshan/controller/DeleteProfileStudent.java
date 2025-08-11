package com.yash.parikshan.controller;

import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.serviceimpl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteProfileStudent")
public class DeleteProfileStudent extends HttpServlet {

    private StudentService studentService = new StudentServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("studentid");
        String password = request.getParameter("password");

        studentService.deleteProfile(studentId,password);

        response.sendRedirect("testformats.jsp");

    }

}
