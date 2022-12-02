package edu.northeastern.a321habits.dao;

import com.google.firebase.firestore.QuerySnapshot;

public interface FirestoreQueryCallback {
    void onQuerySucceeds(QuerySnapshot snapshot);
    void failure();
}
