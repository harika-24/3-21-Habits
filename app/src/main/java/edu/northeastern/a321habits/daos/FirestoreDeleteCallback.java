package edu.northeastern.a321habits.daos;

public interface FirestoreDeleteCallback {
    void onSuccessfullyDeleted();
    void onFailure(Exception e);
}
