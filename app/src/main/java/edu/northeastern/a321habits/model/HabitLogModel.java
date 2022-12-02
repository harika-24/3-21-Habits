package edu.northeastern.a321habits.model;

public class HabitLogModel {
    private String title;
    private String imageURL;
    private String createdAt;
    private Integer day; // day on which habit has been logged

    private UserModel user;

    // TODO: add field for notes


    public HabitLogModel(String title, String imageURL, String createdAt, Integer day, UserModel user) {
        this.title = title;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.day = day;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getDay() {
        return day;
    }

    public UserModel getUser() {
        return user;
    }
}
