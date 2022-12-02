package edu.northeastern.a321habits.models.user;

public class User {
    public User() {}
    public User(String handle) {
        this.handle = handle;
    }
    private String handle;

    public String getHandle() {
        return handle;
    }
}
