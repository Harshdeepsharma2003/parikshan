package com.yash.parikshan.dao;

import com.yash.parikshan.model.Recording;

public interface RecordingDao {
    boolean saveRecording(Recording recording);
    Recording getRecording(String testid, String userid);
}
