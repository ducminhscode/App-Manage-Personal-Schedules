package com.example.applicationproject.Model;

public class Taskbeside {
    private int taskBesideId;
    private String name;
    private int mission_id;

    public Taskbeside(int taskBesideId, String name, int mission_id) {
        this.taskBesideId = taskBesideId;
        this.name = name;
        this.mission_id = mission_id;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskbeside_id() {
        return taskBesideId;
    }

    public void setTaskbeside_id(int taskbeside_id) {
        this.taskBesideId = taskbeside_id;
    }
}
