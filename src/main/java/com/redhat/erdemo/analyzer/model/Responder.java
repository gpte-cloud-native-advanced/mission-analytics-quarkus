package com.redhat.erdemo.analyzer.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Responder {

    private String id;

    private String name;

    private String phoneNumber;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer boatCapacity;

    private Boolean medicalKit;

    private Boolean available;

    private Boolean person;

    private Boolean enrolled;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String name) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getBoatCapacity() {
        return boatCapacity;
    }

    public void setBoatCapacity(Integer boatCapacity) {
        this.boatCapacity = boatCapacity;
    }

    public Boolean isMedicalKit() {
        return medicalKit;
    }

    public void setMedicalKit(Boolean medicalKit) {
        this.medicalKit = medicalKit;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean isPerson() {
        return person;
    }

    public void setPerson(Boolean person) {
        this.person = person;
    }

    public Boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }


    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;

    //     Responder responder = (Responder) o;

    //     if (!id.equals(responder.id)) return false;
    //     if (!Objects.equals(name, responder.name)) return false;
    //     if (!Objects.equals(phoneNumber, responder.phoneNumber))
    //         return false;
    //     if (!Objects.equals(latitude, responder.latitude)) return false;
    //     if (!Objects.equals(longitude, responder.longitude)) return false;
    //     if (!Objects.equals(boatCapacity, responder.boatCapacity))
    //         return false;
    //     if (!Objects.equals(medicalKit, responder.medicalKit)) return false;
    //     return Objects.equals(available, responder.available);
    // }

    // @Override
    // public int hashCode() {
    //     int result = (id != null ? id.hashCode() : 0);
    //     result = 31 * result + (name != null ? name.hashCode() : 0);
    //     result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
    //     result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
    //     result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
    //     result = 31 * result + (boatCapacity != null ? boatCapacity.hashCode() : 0);
    //     result = 31 * result + (medicalKit != null ? medicalKit.hashCode() : 0);
    //     result = 31 * result + (available != null ? available.hashCode() : 0);
    //     return result;
    // }
}
