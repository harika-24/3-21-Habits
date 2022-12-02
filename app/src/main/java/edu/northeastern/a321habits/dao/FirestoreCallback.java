package edu.northeastern.a321habits.dao;

import java.util.Map;

public interface FirestoreCallback {
    void onDocumentExists(Map<String, Object> value);
    void onNoDocumentExists();
    void failure();
}
