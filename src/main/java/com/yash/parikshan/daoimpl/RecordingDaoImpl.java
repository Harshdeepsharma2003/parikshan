package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.RecordingDao;
import com.yash.parikshan.model.Recording;
import com.yash.parikshan.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordingDaoImpl implements RecordingDao {
    @Override
    public boolean saveRecording(Recording recording) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO recordings (testid, studentid, tempuserid, recordingdata, filesize) VALUES (?, ?, ?, ?, ?)",
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recording.getTestid());
            stmt.setString(2, recording.getStudentid());
            stmt.setString(3, recording.getTempuserid());
            stmt.setBytes(4, recording.getRecordingData());
            stmt.setLong(5, recording.getFileSize());

            int result = stmt.executeUpdate();
            if (result > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long generatedId = keys.getLong(1);
                        recording.setId(Long.valueOf(String.valueOf(generatedId))); // or change id type to long in the model
                    }
                }
            }
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error saving recording: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Recording getRecording(String testid, String userid) {
        String sql = "SELECT * FROM recordings WHERE testid = ? AND (studentid = ? OR tempuserid = ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, testid);
            stmt.setString(2, userid);
            stmt.setString(3, userid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Recording recording = new Recording();
                    recording.setId(Long.valueOf(rs.getString("id")));
                    recording.setTestid(rs.getString("testid"));
                    recording.setStudentid(rs.getString("studentid"));
                    recording.setTempuserid(rs.getString("tempuserid"));
                    recording.setRecordingData(rs.getBytes("recordingdata"));
                    recording.setFileSize(rs.getLong("filesize"));
                    recording.setCreatedAt(rs.getTimestamp("createdat"));
                    return recording;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recording: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    }

