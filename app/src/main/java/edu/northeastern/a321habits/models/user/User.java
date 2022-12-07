package edu.northeastern.a321habits.models.user;

public class User {
    public User() {}
    public User(String handle, String userId) {
        this.handle = handle;
        this.id = userId;
    }
    private String handle;
    private String id;

    public String getHandle() {
        return handle;
    }

    public String getId() {
        return id;
    }
}
