package com.dave.Main.State;

public class Status {

    private String errorCode;
    private ChargePointStatus status;
    private String timestamp;
    private String info;
    private String vendorId;
    private String vendorErrorCode;

    public String getTimestamp() {
        return timestamp;
    }

    public ChargePointStatus getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Status{" +
                "errorCode='" + errorCode + '\'' +
                ", status=" + status +
                ", timestamp='" + timestamp + '\'' +
                ", info='" + info + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", vendorErrorCode='" + vendorErrorCode + '\'' +
                '}';
    }
}
