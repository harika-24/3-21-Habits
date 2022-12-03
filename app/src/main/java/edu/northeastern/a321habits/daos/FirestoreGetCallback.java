package edu.northeastern.a321habits.daos;

import java.util.Map;

public interface FirestoreGetCallback {
    void onDocumentExists(Map<String, Object> value);
    void onNoDocumentExists();
    void failure();
}
