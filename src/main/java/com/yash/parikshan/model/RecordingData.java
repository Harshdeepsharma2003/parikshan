package com.yash.parikshan.model;

import java.sql.Timestamp;

public class RecordingData {
    private int id;
    private String testId;
    private String studentId;
    private byte[] recordingData;
    private long fileSize;
    private java.sql.Timestamp createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
