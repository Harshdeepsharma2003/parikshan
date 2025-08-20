package com.yash.parikshan.dao;

import com.yash.parikshan.model.Recording;
import com.yash.parikshan.model.VideoProcessorResult;

import java.sql.SQLException;

public interface VideoProcessorDao {
    Recording getRecordingById(int recordingId) throws SQLException;
    void saveOrUpdateResult(int recordingId, VideoProcessorResult result) throws SQLException;
}
