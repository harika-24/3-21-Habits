package edu.northeastern.a321habits.models.habit;

import com.google.firebase.Timestamp;

public class HabitProgress {
    private String name;
    private String habitId;
    private String photoUrl;
    private Timestamp photoUploadDate;
    private Timestamp dateLogged;
    private boolean isCompleted;
    private String note;
    private int logDay;
    private String id;

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    private String userId;

    public String getHabitId() {
        return habitId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Timestamp getPhotoUploadDate() {
        return photoUploadDate;
    }

    public Timestamp getDateLogged() {
        return dateLogged;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getNote() {
        return note;
    }

    public HabitProgress(){}

    public HabitProgress(String habitId, String photoUrl, Timestamp photoUploadDate,
                         Timestamp dateLogged, boolean isCompleted, String note, String userId,
                         String name, int logDay, String id) {
        this.habitId = habitId;
        this.photoUrl = photoUrl;
        this.photoUploadDate = photoUploadDate;
        this.dateLogged = dateLogged;
        this.isCompleted = isCompleted;
        this.note = note;
        this.userId = userId;
        this.name = name;
        this.logDay = logDay;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getLogDay() {
        return logDay;
    }

}
