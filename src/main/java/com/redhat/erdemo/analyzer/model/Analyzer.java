package com.redhat.erdemo.analyzer.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Analyzer {

    private String missionId;

    private String name;

    private String phoneNumber;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer boatCapacity;

    private Boolean medicalKit;

    private Boolean available;

    private Boolean person;

    private Boolean enrolled;

    // incident info

    private String incidentId;

    private int numberOfPeople;
    
    // responder info

    private String responderId;

    private String responderName;

    private String responderPhoneNumber;

    // mission getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Integer getBoatCapacity() {
        return boatCapacity;
    }

    public Boolean isMedicalKit() {
        return medicalKit;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Boolean isPerson() {
        return person;
    }

    public Boolean isEnrolled() {
        return enrolled;
    }

    //incident getters
    public String getIncidentId() {
	return incidentId;
    }

    public Integer getNumberOfPeople() {
	return numberOfPeople;
    }

    //responder getters
    public String getResponderId() {
        return responderId;
    }

    public String getResponderName() {
        return responderName;
    }

    public String getResponderPhoneNumber() {
        return responderPhoneNumber;
    }
    
    public static class Builder {

        private final Analyzer analyzer;

        public Builder(String id) {
            this.analyzer = new Analyzer();
            analyzer.id = id;
        }

        public Builder name(String name) {
            analyzer.name = name;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            analyzer.phoneNumber = phoneNumber;
            return this;
        }

        public Builder latitude(BigDecimal latitude) {
            analyzer.latitude = latitude;
            return this;
        }

        public Builder longitude(BigDecimal longitude) {
            analyzer.longitude = longitude;
            return this;
        }

        public Builder boatCapacity(Integer boatCapacity) {
            analyzer.boatCapacity = boatCapacity;
            return this;
        }

        public Builder medicalKit(Boolean medicalKit) {
            analyzer.medicalKit = medicalKit;
            return this;
        }

        public Builder available(Boolean available) {
            analyzer.available = available;
            return this;
        }

        public Builder person(Boolean person) {
            analyzer.person = person;
            return this;
        }

        public Builder enrolled(Boolean enrolled) {
            analyzer.enrolled = enrolled;
            return this;
        }

        public Builder incidentId(String incidentId) {
            analyzer.incidentId = incidentId;
            return this;
        }

        public Builder numberOfPeople(Integer numberOfPeople) {
            analyzer.numberOfPeople = numberOfPeople;
            return this;
        }

        public Analyzer build() {
            return analyzer;
        }


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
