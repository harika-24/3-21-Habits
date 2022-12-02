package edu.northeastern.a321habits.models.habit;

public class Habit {
    private String sessionId;
    private String name;

    public Habit() {}
    public Habit(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }
}
