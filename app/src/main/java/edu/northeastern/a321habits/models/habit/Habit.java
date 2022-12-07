package edu.northeastern.a321habits.models.habit;

public class Habit {
    private String id;
    private String sessionId;
    private String name;

    public Habit() {}

    public Habit(String id, String name, String sessionId) {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }
}
