package edu.northeastern.a321habits.daos.session;

import static edu.northeastern.a321habits.daos.DaoHelper.callOnComplete;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.models.session.Session;

public class SessionDao implements SessionDaoI {


    private static final String COLLECTION = "session";
    private static final String TAG = "SessionDao";
    final private FirebaseFirestore db;
    public SessionDao() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void findSessionsOfUser(String userId, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION).whereEqualTo("userId", userId);
        callOnComplete(query, callback);
    }



    @Override
    public void getSessionById(String sessionId, FirestoreGetCallback callback) {
        db.collection(COLLECTION).document(sessionId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            callback.onDocumentExists(document.getData());
                        } else {
                            callback.onNoDocumentExists();
                        }
                    } else {
                        callback.failure();
                    }
                });
    }

    @Override
    public void createSession(Session session, FirestoreAddCallback callback) {
        db.collection(COLLECTION).add(session)
                .addOnSuccessListener(callback::onSuccessfullyAdded)
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void deleteSession(String sessionId, FirestoreDeleteCallback callback) {
        db.collection(COLLECTION).document(sessionId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccessfullyDeleted())
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void getCurrentSession(String currentUser, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION)
                .whereEqualTo("userId", currentUser)
                .whereEqualTo("hasEnded", false)
                .whereLessThan("startDate", new Timestamp(new Date()));
        callOnComplete(query, callback);
    }

    @Override
    public void updateSession(Map<String, Object> updateObject, FireStoreUpdateCallback callback) {
        String sessionId = (String) updateObject.get("sessionId");
        if (sessionId == null || sessionId.equals("")) {
            throw new IllegalArgumentException("SessionId cannot be null or empty");
        }
        updateObject.remove("sessionId");
        db.collection(COLLECTION).document(sessionId)
                .set(updateObject, SetOptions.merge())
                .addOnSuccessListener(unused -> callback.onUpdate())
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.toString());
                    callback.onFailure();
                });

    }

    @Override
    public void getCompletedSessions(String userId, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("hasEnded", true);
        callOnComplete(query, callback);
    }
}
