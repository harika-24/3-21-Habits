package edu.northeastern.a321habits.models.session;

import android.util.Log;

import com.google.firebase.Timestamp;

public class Session {

    private static final String TAG = "Session";
    private String sessionId;
    private String userId;
    private Timestamp startDate;
    private Timestamp endDate;

    public Session() {}

    public Session(String sessionId, String userId, Timestamp startDate, Timestamp endDate) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Session(Object sessionId, Object userId, Object startDate, Object endDate) {
        try {
            if (sessionId != null) {
                this.sessionId = sessionId.toString();
            }
            if (userId != null) {
                this.userId = userId.toString();
            }
            if (startDate != null) {
                this.startDate = (Timestamp) startDate;
            }
            if (endDate != null) {
                this.endDate = (Timestamp) endDate;
            }
        } catch (ClassCastException cce) {
            Log.d(TAG, "Issue while casting Map object data in constructor.");
        }

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

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
