package com.yash.parikshan.controller;


import com.yash.parikshan.service.RecordingService;
import com.yash.parikshan.serviceimpl.RecordingServiceImpl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/recordingUpload")
@MultipartConfig(maxFileSize = 50 * 1024 * 1024) // 50MB max
public class RecordingServlet extends HttpServlet {

    private RecordingService recordingService = new RecordingServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String testid = request.getParameter("testid");
            String userid = request.getParameter("userid");
            Part recordingPart = request.getPart("recording");

            if (recordingPart != null && recordingPart.getSize() > 0) {
                // Read recording data
                InputStream inputStream = recordingPart.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[16384];
                int nRead;
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                byte[] recordingBytes = buffer.toByteArray();

                // Save recording
                boolean saved = recordingService.saveRecording(testid, userid, recordingBytes);

                if (saved) {
                    out.write("{\"success\": true, \"message\": \"Recording saved\"}");
                } else {
                    out.write("{\"success\": false, \"message\": \"Failed to save\"}");
                }
            } else {
                out.write("{\"success\": false, \"message\": \"No recording data\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}