package edu.northeastern.a321habits.daos.user;

import static edu.northeastern.a321habits.daos.DaoHelper.callOnComplete;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;

public class UserDao implements UserDaoI {

    private static final String COLLECTION = "users";
    private static final String TAG = "UserDao";
    final private FirebaseFirestore db;
    public UserDao() {
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void getUserById(String id, FirestoreGetCallback callback) {
        DocumentReference ref = db.collection(COLLECTION).document(id);
        ref.get().addOnCompleteListener(task -> {
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
    public void getUserByHandle(String handle, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION).whereEqualTo("handle", handle);
        callOnComplete(query, callback);
    }
}
