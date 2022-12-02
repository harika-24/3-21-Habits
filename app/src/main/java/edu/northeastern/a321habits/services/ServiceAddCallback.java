package edu.northeastern.a321habits.services;

public interface ServiceAddCallback {
    void onCreated(String uniqueId);

    void onFailure(Exception e);
}
