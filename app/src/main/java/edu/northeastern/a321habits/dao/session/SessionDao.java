package edu.northeastern.a321habits.dao.session;

import static edu.northeastern.a321habits.dao.DaoHelper.callOnComplete;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.a321habits.dao.FirestoreQueryCallback;

public class SessionDao implements SessionDaoI {


    private static final String COLLECTION = "session";
    private static final String SESSION_STATUS_COLLECTION = "session-status";
    final private FirebaseFirestore db;
    public SessionDao() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void getUserSessions(String userId, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION).whereEqualTo("userId", userId);
        callOnComplete(query, callback);
    }

    @Override
    public void getSessionStatus(String sessionId, FirestoreQueryCallback callback) {
        Query query = db.collection(SESSION_STATUS_COLLECTION).whereEqualTo("sessionId", sessionId);
        callOnComplete(query, callback);
    }
}
