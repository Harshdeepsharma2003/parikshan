package com.yash.parikshan.model;

import java.sql.Timestamp;

public class Recording {
    private Long id;
    private String testid;
    private String studentid;
    private byte[] recordingData;
    private long fileSize;
    private Timestamp createdAt;

    public Recording(){}

    public Recording(Long id, String testid, String studentid, byte[] recordingData, long fileSize, Timestamp createdAt) {
        this.id = id;
        this.testid = testid;
        this.studentid = studentid;
        this.recordingData = recordingData;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }


    public Recording(String testid, String studentid,byte[] recordingData, long fileSize) {

        this.testid = testid;
        this.studentid = studentid;
        this.recordingData = recordingData;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }


    public byte[] getRecordingData() {
        return recordingData;
    }

    public void setRecordingData(byte[] recordingData) {
        this.recordingData = recordingData;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
