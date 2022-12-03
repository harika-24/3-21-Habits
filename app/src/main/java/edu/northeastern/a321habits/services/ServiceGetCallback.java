package edu.northeastern.a321habits.services;

public interface ServiceGetCallback<T> {
    void onGetExists(T object);
    void onGetDoesNotExist();
    void onFailure();
}
