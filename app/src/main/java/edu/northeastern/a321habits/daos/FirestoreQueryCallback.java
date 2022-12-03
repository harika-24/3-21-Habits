package edu.northeastern.a321habits.daos;

import com.google.firebase.firestore.QuerySnapshot;

public interface FirestoreQueryCallback {
    void onQuerySucceeds(QuerySnapshot snapshot);
    void failure();
}
