package com.template.common.collect;

public enum BatchErrorEnum {

    READ(1, "(Read Data Failed!)"),
    WRITE(2, "(Write Data Failed!)"),
    FAIL(3, "(Batch Failed!)"),
    CALLPROCEDURE(4, "(Call Procedure Failed!)"),
    VALIDITY(5, "(Data Validity!)");

    private int error;
    private String reason;

    BatchErrorEnum(int error, String reason) {
        this.error = error;
        this.reason = reason;
    }

    public int getError() {
        return error;
    }

    public String getReason() {
        return reason;
    }

}
