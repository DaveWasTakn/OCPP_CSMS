package com.dave.Main.State;

public class Connector {
    private int connectorId;
    private Status status;

    public Connector(int connectorId, Status status) {
        this.connectorId = connectorId;
        this.status = status;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Connector{" +
                "connectorId=" + connectorId +
                ", status=" + status +
                '}';
    }
}
