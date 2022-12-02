package edu.northeastern.a321habits.daos.session;

import static edu.northeastern.a321habits.daos.DaoHelper.callOnComplete;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.models.session.Session;

public class SessionDao implements SessionDaoI {


    private static final String COLLECTION = "session";
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
}
