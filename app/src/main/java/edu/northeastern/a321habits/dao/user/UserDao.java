package edu.northeastern.a321habits.dao.user;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.northeastern.a321habits.dao.FirestoreCallback;

public class UserDao implements UserDaoI {

    private static final String COLLECTION = "users";
    private static final String TAG = "UserDao";
    final private FirebaseFirestore db;
    public UserDao() {
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void getUserById(String id, FirestoreCallback callback) {
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
}
