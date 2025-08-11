// RecordingServiceImpl.java
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
        try {
            // Check if userid is student or tempuser
            String studentid = null;
            String tempuserid = null;

            // Simple logic: if userid length > 10, it's probably a tempuser UUID
            if (userid.length() > 10) {
                tempuserid = userid;
            } else {
                studentid = userid;
            }

            // RecordingServiceImpl.java - Fix in saveRecording method
            Recording recording = new Recording(testid, studentid, tempuserid, recordingData, (long) recordingData.length);
            return recordingDao.saveRecording(recording);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Recording getRecording(String testid, String userid) {
        return recordingDao.getRecording(testid, userid);
    }
}
