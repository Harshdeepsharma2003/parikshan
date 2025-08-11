package com.yash.parikshan.controller;

import com.yash.parikshan.service.StudentService;
import com.yash.parikshan.service.TestService;
import com.yash.parikshan.serviceimpl.StudentServiceImpl;
import com.yash.parikshan.serviceimpl.TestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteTest")
public class DeleteTest extends HttpServlet {

    private TestService testService = new TestServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String testId = request.getParameter("deleteTestid");

        testService.deleteTest(testId);

        response.sendRedirect("testformats.jsp");

    }

}
