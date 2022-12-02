package edu.northeastern.a321habits.dao.user;

import edu.northeastern.a321habits.dao.FirestoreCallback;

public interface UserDaoI {
    void getUserById(String id, FirestoreCallback callback);
}
