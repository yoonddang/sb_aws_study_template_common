package com.template.common.model.batch;

import static com.template.common.util.TimeUtil.getNowDateToString;

public class BatchLog {

    private int jobId;
    private String jobName;
    private String startTime;
    private String endTime;
    private String status;
    private String message;
    private int commitCount;
    private int readCount;
    private int writeCount;

    public BatchLog() {   }

    public BatchLog(String JobName) {
        this.jobName = JobName;
        this.startTime = getNowDateToString();
        //this.endTime = "0000-00-00 00:00:00";
        this.endTime = null;
        this.status = BatchConstants.BATCH_STATUS_START;
        this.message = "";
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(int writeCount) {
        this.writeCount = writeCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
