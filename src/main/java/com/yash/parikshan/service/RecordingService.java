package com.yash.parikshan.service;

import com.yash.parikshan.model.Recording;

public interface RecordingService {
    boolean saveRecording(String testid, String studentid, byte[] recordingData);
    Recording getRecording(String testid, String userid);
}
