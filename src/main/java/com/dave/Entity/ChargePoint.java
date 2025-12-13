package com.dave.Entity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ChargePoint {

    private String ipAddress;

    @Deprecated
    private String chargeBoxSerialNumber;
    private String chargePointModel;
    private String chargePointSerialNumber;
    private String chargePointVendor;
    private String firmwareVersion;
    private String iccid;
    private String imsi;
    private String iccd;
    private String meterSerialNumber;
    private String meterType;

    private Instant lastHeartbeat; // TODO create isAlive() method and check
    private Status status;
    private final Map<Integer, Connector> connectors = new HashMap<>();

    public ChargePoint(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getChargeBoxSerialNumber() {
        return chargeBoxSerialNumber;
    }

    public void setChargeBoxSerialNumber(String chargeBoxSerialNumber) {
        this.chargeBoxSerialNumber = chargeBoxSerialNumber;
    }

    public String getModel() {
        return chargePointModel;
    }

    public void setModel(String model) {
        this.chargePointModel = model;
    }

    public String getChargePointSerialNumber() {
        return chargePointSerialNumber;
    }

    public void setChargePointSerialNumber(String chargePointSerialNumber) {
        this.chargePointSerialNumber = chargePointSerialNumber;
    }

    public String getVendor() {
        return chargePointVendor;
    }

    public void setVendor(String vendor) {
        this.chargePointVendor = vendor;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccd() {
        return iccd;
    }

    public void setIccd(String iccd) {
        this.iccd = iccd;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Instant getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Instant lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public Map<Integer, Connector> getConnectors() {
        return connectors;
    }

    public void updateConnector(Connector connector) {
        this.connectors.put(connector.getConnectorId(), connector);
    }

    @Override
    public String toString() {
        return "ChargePoint{" +
                "ipAddress='" + ipAddress + '\'' +
                ", chargeBoxSerialNumber='" + chargeBoxSerialNumber + '\'' +
                ", chargePointModel='" + chargePointModel + '\'' +
                ", chargePointSerialNumber='" + chargePointSerialNumber + '\'' +
                ", chargePointVendor='" + chargePointVendor + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", iccid='" + iccid + '\'' +
                ", imsi='" + imsi + '\'' +
                ", iccd='" + iccd + '\'' +
                ", meterSerialNumber='" + meterSerialNumber + '\'' +
                ", meterType='" + meterType + '\'' +
                ", lastHeartbeat=" + lastHeartbeat +
                ", status=" + status +
                ", connectors=" + connectors +
                '}';
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
