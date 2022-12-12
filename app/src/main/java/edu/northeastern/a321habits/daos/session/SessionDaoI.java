package edu.northeastern.a321habits.daos.session;

import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.models.session.Session;

public interface SessionDaoI {
    void findSessionsOfUser(String userId, FirestoreQueryCallback callback);
    void getSessionById(String sessionId, FirestoreGetCallback callback);
    void createSession(Session session, FirestoreAddCallback callback);
    void deleteSession(String sessionId, FirestoreDeleteCallback callback);
    void getCurrentSession(String currentUser, FirestoreQueryCallback callback);
    void updateSession(Map<String, Object> updateObject, FireStoreUpdateCallback callback);
    void getCompletedSessions(String userId, FirestoreQueryCallback callback);
}
