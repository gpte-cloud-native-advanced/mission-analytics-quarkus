package com.redhat.erdemo.analyzer.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.time.Instant;

public class Incident {

    private long id;

    private String incidentId;

    private String latitude;

    private String longitude;

    private int numberOfPeople;

    private boolean medicalNeeded;

    private String victimName;

    private String victimPhoneNumber;

    private Instant reportedTime;

    private String status;

    private long version;

    public long getId() {
        return id;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Boolean isMedicalNeeded() {
        return medicalNeeded;
    }

    public void setMedicalNeeded(boolean medicalNeeded) {
        this.medicalNeeded = medicalNeeded;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public void setVictimPhoneNumber(String victimPhoneNumber) {
        this.victimPhoneNumber = victimPhoneNumber;
    }

    public long getTimestamp() {
        return reportedTime.toEpochMilli();
    }

    public Instant getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(Instant reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

}
