package com.example.applicationproject.Model;

public class Sticker {
    private int sticker_id;
    private String sticker_path;
    private String sticker_title;

    public Sticker(int sticker_id, String sticker_path, String sticker_title) {
        this.sticker_id = sticker_id;
        this.sticker_path = sticker_path;
        this.sticker_title = sticker_title;
    }

    public String getSticker_title() {
        return sticker_title;
    }

    public void setSticker_title(String sticker_title) {
        this.sticker_title = sticker_title;
    }

    public int getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(int sticker_id) {
        this.sticker_id = sticker_id;
    }

    public String getSticker_path() {
        return sticker_path;
    }

    public void setSticker_path(String sticker_path) {
        this.sticker_path = sticker_path;
    }
}
