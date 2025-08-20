package com.yash.parikshan.daoimpl;

import com.yash.parikshan.dao.VideoProcessorDao;
import com.yash.parikshan.model.Recording;
import com.yash.parikshan.model.VideoProcessorResult;
import com.yash.parikshan.util.DbUtil;

import java.sql.*;

public class VideoProcessorDaoImpl implements VideoProcessorDao {

    @Override
    public Recording getRecordingById(int recordingId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT id, testid, studentid, recordingdata, filesize, createdat FROM recordings WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordingId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Recording recording = new Recording();
                recording.setId(rs.getLong("id"));
                recording.setTestid(rs.getString("testid"));
                recording.setStudentid(rs.getString("studentid"));
                recording.setRecordingData(rs.getBytes("recordingdata"));
                recording.setFileSize(rs.getLong("filesize"));
                recording.setCreatedAt(rs.getTimestamp("createdat"));
                return recording;
            }
            return null;

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public void saveOrUpdateResult(int recordingId, VideoProcessorResult result) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS video_analysis_results (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    recording_id BIGINT NOT NULL,
                    total_frames INT,
                    pose_detected_frames INT,
                    detection_percentage DECIMAL(5,2),
                    status VARCHAR(50),
                    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (recording_id) REFERENCES recordings(id)
                )
            """;
            conn.prepareStatement(createTableSQL).executeUpdate();

            // Check if result already exists
            String checkSQL = "SELECT COUNT(*) FROM video_analysis_results WHERE recording_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, recordingId);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            boolean exists = checkRs.getInt(1) > 0;
            checkRs.close();
            checkStmt.close();

            String sql;
            if (exists) {
                sql = """
                    UPDATE video_analysis_results
                    SET total_frames = ?, pose_detected_frames = ?, detection_percentage = ?,
                        status = ?, processed_at = CURRENT_TIMESTAMP
                    WHERE recording_id = ?
                """;
            } else {
                sql = """
                    INSERT INTO video_analysis_results
                    (recording_id, total_frames, pose_detected_frames, detection_percentage, status)
                    VALUES (?, ?, ?, ?, ?)
                """;
            }

            pstmt = conn.prepareStatement(sql);
            if (exists) {
                pstmt.setInt(1, result.getTotalFrames());
                pstmt.setInt(2, result.getPoseDetectedFrames());
                pstmt.setDouble(3, result.getDetectionPercentage());
                pstmt.setString(4, result.getStatus());
                pstmt.setInt(5, recordingId);
            } else {
                pstmt.setInt(1, recordingId);
                pstmt.setInt(2, result.getTotalFrames());
                pstmt.setInt(3, result.getPoseDetectedFrames());
                pstmt.setDouble(4, result.getDetectionPercentage());
                pstmt.setString(5, result.getStatus());
            }
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
}
