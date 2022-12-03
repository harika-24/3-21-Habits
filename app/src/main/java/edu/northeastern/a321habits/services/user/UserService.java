package edu.northeastern.a321habits.services.user;

import java.util.Map;

import edu.northeastern.a321habits.daos.FirestoreGetCallback;
import edu.northeastern.a321habits.daos.user.UserDao;
import edu.northeastern.a321habits.daos.user.UserDaoI;
import edu.northeastern.a321habits.models.user.User;
import edu.northeastern.a321habits.services.ServiceGetCallback;

public class UserService implements UserServiceI{
    @Override
    public void getUserById(String id, ServiceGetCallback<User> callback) {
        UserDaoI userDao = new UserDao();
        userDao.getUserById(id, new FirestoreGetCallback() {
            @Override
            public void onDocumentExists(Map<String, Object> value) {
                User user = new User(value.getOrDefault("handle", "").toString());
                callback.onGetExists(user);
            }

            @Override
            public void onNoDocumentExists() {
                callback.onGetDoesNotExist();
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }
}
