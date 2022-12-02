package edu.northeastern.a321habits.services.user;

import edu.northeastern.a321habits.services.ServiceGetCallback;

public interface UserServiceI {
    void getUserById(String id, ServiceGetCallback callback);
}
