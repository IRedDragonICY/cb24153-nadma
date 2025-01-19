package com.ireddragonicy.nadma;

public class Report {
    private String documentId;
    private String incidentType;
    private String date;
    private String time;
    private String location;
    private String description;
    private String name;
    private String phone;
    private String email;
    private String base64Image;
    private String userId;

    public Report() {
    }

    public Report(String incidentType, String date, String time, String location, String description, String name, String phone, String email, String base64Image, String userId) {
        this.incidentType = incidentType;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.base64Image = base64Image;
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}