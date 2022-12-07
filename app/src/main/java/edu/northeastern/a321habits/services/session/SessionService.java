package edu.northeastern.a321habits.services.session;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.daos.session.SessionDaoI;
import edu.northeastern.a321habits.models.session.Session;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceGetCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceUpdateCallback;

public class SessionService implements SessionServiceI {

    private final SessionDaoI sessionDao;

    public SessionService(SessionDaoI sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public void findSessionsOfUser(String userId, ServiceQueryCallback<Session> callback) {
        this.sessionDao.findSessionsOfUser(userId, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<Session> sessions = new ArrayList<>();
                for(QueryDocumentSnapshot document: snapshot) {
                    sessions.add(new Session(document.getId(),document.getString("userId"),
                            document.getTimestamp("startDate"),
                            document.getTimestamp("endDate"),
                            Boolean.TRUE.equals(document.getBoolean("hasEnded"))));
                }
                callback.onObjectsExist(sessions);
            }

            @Override
            public void failure() {

            }
        });
    }

    @Override
    public void getSessionById(String sessionId, ServiceGetCallback<Session> callback) {
        this.sessionDao.getSessionById(sessionId, new FirestoreGetCallback() {
            @Override
            public void onDocumentExists(Map<String, Object> value) {
                Session session = new Session(sessionId ,value.get("userId"),
                        value.get("startDate"), value.get("endDate"), (boolean) value.get("hasEnded"));
                callback.onGetExists(session);
            }

            @Override
            public void onNoDocumentExists() {
                callback.onGetDoesNotExist();
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void createSession(Session session, ServiceAddCallback callback) {
        this.sessionDao.createSession(session, new FirestoreAddCallback() {
            @Override
            public void onSuccessfullyAdded(DocumentReference ref) {
                callback.onCreated(ref.getId());
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void deleteSession(String sessionId, ServiceDeleteCallback callback) {
        this.sessionDao.deleteSession(sessionId, new FirestoreDeleteCallback() {
            @Override
            public void onSuccessfullyDeleted() {
                callback.onDeleted();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void updateSession(Map<String, Object> updateObject, ServiceUpdateCallback callback) {
        sessionDao.updateSession(updateObject, new FireStoreUpdateCallback() {
            @Override
            public void onUpdate() {
                callback.onUpdated();
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void getCurrentSession(String currentUser, ServiceQueryCallback<Session> callback) {
        sessionDao.getCurrentSession(currentUser, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<Session> sessions = new ArrayList<>();
                for (QueryDocumentSnapshot document: snapshot) {
                    sessions.add(new Session(document.getId(), document.getString("userId"),
                            document.getTimestamp("startDate"),
                            document.getTimestamp("endDate"),
                            Boolean.TRUE.equals(document.getBoolean("hasEnded"))));
                }
                callback.onObjectsExist(sessions);
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }
}
