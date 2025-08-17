
package com.yash.parikshan.serviceimpl;
import com.yash.parikshan.dao.RecordingDao;

import com.yash.parikshan.daoimpl.RecordingDaoImpl;
import com.yash.parikshan.model.Recording;
import com.yash.parikshan.service.RecordingService;

public class RecordingServiceImpl implements RecordingService {

    private RecordingDao recordingDao;

    public RecordingServiceImpl() {
        this.recordingDao = new RecordingDaoImpl();
    }

    @Override
    public boolean saveRecording(String testid, String userid, byte[] recordingData) {

        if (testid == null || testid.trim().isEmpty()) {
            System.err.println("Test ID cannot be null or empty");
            return false;
        }

        if (userid == null || userid.trim().isEmpty()) {
            System.err.println("User ID cannot be null or empty");
            return false;
        }

        if (recordingData == null || recordingData.length == 0) {
            System.err.println("Recording data cannot be null or empty");
            return false;
        }

        try {

            Recording recording = new Recording(testid, userid, recordingData, (long) recordingData.length);
            return recordingDao.saveRecording(recording);
        } catch (Exception e) {
            System.err.println("Error saving recording: " + e.getMessage());
            return false;
        }
    }


    @Override
    public Recording getRecording(String testid, String userid)
    {
        return recordingDao.getRecording(testid, userid);
    }
}
