package com.example.gruppe43.idretts_app.application.model;

//Idretts-app bachelor oppgave 2017
//Ole-Kristian Steiro, Tasmia Faruque, Gebi Beshir

public class TrainerPostsModel {
    private String activityDate;
    private String endTime;
    private String icon;
    private String infoText;
    private String intensity;
    private String place;
    private String postedDate;
    private String postedUserId;
    private String startTime;
    private String timePosted;
    private String title;
    private String isNotified;


    public TrainerPostsModel() {
    }

    public TrainerPostsModel(String activityDate, String endTime, String icon, String infoText,
                             String intensity, String place, String postedDate, String postedUserId,
                             String startTime, String timePosted, String title, String isNotified) {
        this.activityDate = activityDate;
        this.endTime = endTime;
        this.icon = icon;
        this.infoText = infoText;
        this.intensity = intensity;
        this.place = place;
        this.postedDate = postedDate;
        this.postedUserId = postedUserId;
        this.startTime = startTime;
        this.timePosted = timePosted;
        this.title = title;
        this.isNotified = isNotified;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedUserId() {
        return postedUserId;
    }

    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(String isNotified) {
        this.isNotified = isNotified;
    }
}
