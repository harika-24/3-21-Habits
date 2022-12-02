package edu.northeastern.a321habits.services;

public interface ServiceDeleteCallback {
    void onDeleted();
    void onFailure(Exception e);
}
