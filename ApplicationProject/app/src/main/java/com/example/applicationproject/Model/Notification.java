package com.example.applicationproject.Model;

public class Notification {
    private int notification_id;
    private String time;
    private String date;
    private int mission_id;

    public Notification(int notification_id, String time, int mission_id, String date) {
        this.date = date;
        this.notification_id = notification_id;
        this.time = time;
        this.mission_id = mission_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
