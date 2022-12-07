package edu.northeastern.a321habits.services.session;

import java.util.Map;

import edu.northeastern.a321habits.models.session.Session;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceGetCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceUpdateCallback;

public interface SessionServiceI {
    void findSessionsOfUser(String userId, ServiceQueryCallback<Session> callback);
    void getSessionById(String sessionId, ServiceGetCallback<Session> callback);
    void createSession(Session session, ServiceAddCallback callback);
    void deleteSession(String sessionId, ServiceDeleteCallback callback);
    void updateSession(Map<String, Object> updateObject, ServiceUpdateCallback callback);
    void getCurrentSession(String currentUser, ServiceQueryCallback<Session> callback);
}
