package edu.northeastern.a321habits.daos.user;

import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;

public interface UserDaoI {
    void getUserById(String id, FirestoreGetCallback callback);
    void getUserByHandle(String handle, FirestoreQueryCallback callback);
}
