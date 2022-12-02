package edu.northeastern.a321habits.models.session;

import com.google.firebase.Timestamp;

public class Session {

    private String userId;
    private Timestamp startDate;
    private Timestamp endDate;

    public Session() {}

    public Session(String userId, Timestamp startDate, Timestamp endDate) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }
}
