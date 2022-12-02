package edu.northeastern.a321habits.daos;

import android.util.Log;

import com.google.firebase.firestore.Query;

public class DaoHelper {
    private static final String TAG = "DaoHelper";

    public static void callOnComplete(Query query, FirestoreQueryCallback callback) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onQuerySucceeds(task.getResult());
            } else {
                callback.failure();
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
