package com.example.applicationproject.Model;

public class Ringtone {
    private int ringTone_id;
    private String ringTone_name;
    private String ringTone_path;

    public Ringtone(int ringTone_id, String ringTone_name, String ringTone_path) {
        this.ringTone_id = ringTone_id;
        this.ringTone_name = ringTone_name;
        this.ringTone_path = ringTone_path;
    }

    public int getRingTone_id() {
        return ringTone_id;
    }

    public void setRingTone_id(int ringTone_id) {
        this.ringTone_id = ringTone_id;
    }

    public String getRingTone_name() {
        return ringTone_name;
    }

    public void setRingTone_name(String ringTone_name) {
        this.ringTone_name = ringTone_name;
    }

    public String getRingTone_path() {
        return ringTone_path;
    }

    public void setRingTone_path(String ringTone_path) {
        this.ringTone_path = ringTone_path;
    }
}
