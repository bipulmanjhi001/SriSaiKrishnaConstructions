package com.SriSaiKrishnConstruction.model;

public class TripList {
    private String id,date,time,vehicle,owner,trip;

    public TripList(String id, String date, String time, String vehicle, String owner, String trip) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.vehicle = vehicle;
        this.owner = owner;
        this.trip = trip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}
