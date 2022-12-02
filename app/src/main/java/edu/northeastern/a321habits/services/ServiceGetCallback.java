package edu.northeastern.a321habits.services;

public interface ServiceGetCallback {
    void onGetExists(Object object);
    void onGetDoesNotExist();
    void onFailure();
}
