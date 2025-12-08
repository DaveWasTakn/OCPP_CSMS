package com.dave.State;

import java.util.ArrayList;
import java.util.List;

public class ChargePoint {

    @Deprecated
    private String chargeBoxSerialNumber;
    private String model;
    private String chargePointSerialNumber;
    private String vendor;
    private String firmwareVersion;
    private String iccid;
    private String imsi;
    private String iccd;
    private String meterSerialNumber;
    private String meterType;

    private final List<Runnable> observers = new ArrayList<>();

    public void registerObserver(Runnable runnable) {
        this.observers.add(runnable);
    }

    private void notifyObservers() {
        for (Runnable runnable : this.observers) {  // TODO in separate threads
            runnable.run();
        }
    }

    public String getChargeBoxSerialNumber() {
        return chargeBoxSerialNumber;
    }

    public void setChargeBoxSerialNumber(String chargeBoxSerialNumber) {
        this.chargeBoxSerialNumber = chargeBoxSerialNumber;
        this.notifyObservers();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        this.notifyObservers();
    }

    public String getChargePointSerialNumber() {
        return chargePointSerialNumber;
    }

    public void setChargePointSerialNumber(String chargePointSerialNumber) {
        this.chargePointSerialNumber = chargePointSerialNumber;
        this.notifyObservers();
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
        this.notifyObservers();
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        this.notifyObservers();
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
        this.notifyObservers();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
        this.notifyObservers();
    }

    public String getIccd() {
        return iccd;
    }

    public void setIccd(String iccd) {
        this.iccd = iccd;
        this.notifyObservers();
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
        this.notifyObservers();
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
        this.notifyObservers();
    }
}
