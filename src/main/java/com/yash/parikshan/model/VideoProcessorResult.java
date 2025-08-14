package com.yash.parikshan.model;

import java.sql.Timestamp;

public class VideoProcessorResult {
    private String status;
    private int totalFrames;
    private int poseDetectedFrames;
    private double detectionPercentage;
    private String error;
    private String studentId;
    private String testId;
    private Timestamp processedAt;
    private Long recordingId; // Add recording ID field

    // Default constructor
    public VideoProcessorResult() {}

    // Constructor for error results
    public VideoProcessorResult(String error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getPoseDetectedFrames() {
        return poseDetectedFrames;
    }

    public void setPoseDetectedFrames(int poseDetectedFrames) {
        this.poseDetectedFrames = poseDetectedFrames;
    }

    public double getDetectionPercentage() {
        return detectionPercentage;
    }

    public void setDetectionPercentage(double detectionPercentage) {
        this.detectionPercentage = detectionPercentage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Timestamp getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Timestamp processedAt) {
        this.processedAt = processedAt;
    }

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }

    @Override
    public String toString() {
        return "VideoProcessorResult{" +
                "status='" + status + '\'' +
                ", totalFrames=" + totalFrames +
                ", poseDetectedFrames=" + poseDetectedFrames +
                ", detectionPercentage=" + detectionPercentage +
                ", error='" + error + '\'' +
                ", studentId='" + studentId + '\'' +
                ", testId='" + testId + '\'' +
                ", processedAt=" + processedAt +
                ", recordingId=" + recordingId +
                '}';
    }
}