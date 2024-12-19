package com.example.applicationproject.Model;

public class Category {
    private int category_id;
    private String category_name;
    private int user_id;

    public Category(int category_id, String category_name, int user_id) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.user_id = user_id;
    }



    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
