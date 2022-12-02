package edu.northeastern.a321habits.daos;

import com.google.firebase.firestore.DocumentReference;

public interface FirestoreAddCallback {
    void onSuccessfullyAdded(DocumentReference ref);
    void onFailure(Exception e);
}
