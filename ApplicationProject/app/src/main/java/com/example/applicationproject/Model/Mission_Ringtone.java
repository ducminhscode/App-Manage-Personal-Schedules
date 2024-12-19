package com.example.applicationproject.Model;

public class Mission_Ringtone {
    private int mission_ringtone_id;
    private int mission_id;
    private int ringtone_id;

    public Mission_Ringtone(int mission_ringtone_id, int mission_id, int ringtone_id) {
        this.mission_id = mission_id;
        this.ringtone_id = ringtone_id;
        this.mission_ringtone_id = mission_ringtone_id;
    }

    public int getMission_ringtone_id() {
        return mission_ringtone_id;
    }

    public void setMission_ringtone_id(int mission_ringtone_id) {
        this.mission_ringtone_id = mission_ringtone_id;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public int getRingtone_id() {
        return ringtone_id;
    }

    public void setRingtone_id(int ringtone_id) {
        this.ringtone_id = ringtone_id;
    }
}
