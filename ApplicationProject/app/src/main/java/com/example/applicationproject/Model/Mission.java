package com.example.applicationproject.Model;

public class Mission {
    private int category_id, mission_id, ringTone_id, sticker_id;
    private String title, date, time, describe, repeatType, isRepeat, isNotify, repeatNo, reminderType, isSticker, isActive;

    public Mission(int sticker_id, int ringTone_id, String date, String describe, String isNotify,
                   String isRepeat, String repeatType, int mission_id, String time, String title,
                   int category_id, String repeatNo, String reminderType, String isSticker, String isActive) {
        this.date = date;
        this.describe = describe;
        this.isNotify = isNotify;
        this.isRepeat = isRepeat;
        this.repeatType = repeatType;
        this.mission_id = mission_id;
        this.time = time;
        this.title = title;
        this.category_id = category_id;
        this.repeatNo = repeatNo;
        this.reminderType = reminderType;
        this.ringTone_id = ringTone_id;
        this.sticker_id = sticker_id;
        this.isSticker = isSticker;
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsSticker() {
        return isSticker;
    }

    public void setIsSticker(String isSticker) {
        this.isSticker = isSticker;
    }

    public int getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(int sticker_id) {
        this.sticker_id = sticker_id;
    }

    public int getRingTone_id() {
        return ringTone_id;
    }

    public void setRingTone_id(int ringTone_id) {
        this.ringTone_id = ringTone_id;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String isNotify() {
        return isNotify;
    }

    public void setNotify(String notify) {
        isNotify = notify;
    }

    public String isRepeat() {
        return isRepeat;
    }

    public void setRepeat(String repeat) {
        isRepeat = repeat;
    }

    public String getRepeatNo() {
        return repeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        this.repeatNo = repeatNo;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

}
