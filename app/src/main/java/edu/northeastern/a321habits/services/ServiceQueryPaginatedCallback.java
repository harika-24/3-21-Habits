package edu.northeastern.a321habits.services;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface ServiceQueryPaginatedCallback<T> {
    void onObjectsExistPaginate(List<T> objects, DocumentSnapshot lastVisible);
    void onFailure();
}


