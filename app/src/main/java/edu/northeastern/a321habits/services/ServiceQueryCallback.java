package edu.northeastern.a321habits.services;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface ServiceQueryCallback<T> {
    void onObjectsExist(List<T> objects);
    void onFailure();
}
