package edu.northeastern.a321habits.dao.session;

import edu.northeastern.a321habits.dao.FirestoreQueryCallback;

public interface SessionDaoI {
    void getUserSessions(String userId, FirestoreQueryCallback callback);
    void getSessionStatus(String sessionId, FirestoreQueryCallback callback);
}
