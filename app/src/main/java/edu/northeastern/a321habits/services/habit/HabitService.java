package edu.northeastern.a321habits.services.habit;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.daos.habit.HabitDaoI;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceUpdateCallback;

public class HabitService implements HabitServiceI {

    private static final String TAG = "HabitService";
    private final HabitDaoI habitDao;

    public HabitService(HabitDaoI habitDao) {
        this.habitDao = habitDao;
    }
    @Override
    public void findHabitsOfSession(String sessionId, ServiceQueryCallback<Habit> callback) {
        habitDao.findHabitsOfSession(sessionId, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<Habit> habitList = new ArrayList<>();
                for (QueryDocumentSnapshot document: snapshot) {
                    Habit habit = new Habit(document.getId(), document.getString("name"), sessionId);
                    habitList.add(habit);
                }
                callback.onObjectsExist(habitList);
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void getProgressOfHabit(String habitId, ServiceQueryCallback<HabitProgress> callback) {
        habitDao.getProgressOfHabit(habitId, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<HabitProgress> habitList = new ArrayList<>();
                for (QueryDocumentSnapshot document: snapshot) {
                    HabitProgress habitProgress =
                            new HabitProgress(document.getString("habitId"),
                                    document.getString("photoUrl"),
                                    document.getTimestamp("photoUploadDate"),
                                    document.getTimestamp("dateLogged"),
                                    Boolean.TRUE.equals(document.getBoolean("completed")),
                                    document.getString("note"),
                                    document.getString("userId"),
                                    document.getString("name"));
                    habitList.add(habitProgress);
                }
                callback.onObjectsExist(habitList);
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void createHabit(Habit habit, ServiceAddCallback callback) {
        habitDao.createHabit(habit, new FirestoreAddCallback() {
            @Override
            public void onSuccessfullyAdded(DocumentReference ref) {
                callback.onCreated(ref.getId());
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void deleteHabit(String habitId, ServiceDeleteCallback callback) {
        habitDao.deleteHabit(habitId, new FirestoreDeleteCallback() {
            @Override
            public void onSuccessfullyDeleted() {
                callback.onDeleted();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void addProgressToHabit(HabitProgress progress, String currentUser, ServiceAddCallback callback) {
        habitDao.addProgressToHabit(progress, currentUser, new FirestoreAddCallback() {
            @Override
            public void onSuccessfullyAdded(DocumentReference ref) {
                callback.onCreated(ref.getId());
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void deleteProgressFromHabit(String habitProgressId, ServiceDeleteCallback callback) {
        habitDao.deleteProgressFromHabit(habitProgressId, new FirestoreDeleteCallback() {
            @Override
            public void onSuccessfullyDeleted() {
                callback.onDeleted();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void findHabitProgressOfOthers(String currentUser, ServiceQueryCallback<HabitProgress> callback) {
        habitDao.findHabitProgressOfOthers(currentUser, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<HabitProgress> habitProgresses = new ArrayList<>();
                for (QueryDocumentSnapshot document : snapshot) {
                    habitProgresses.add(new HabitProgress(document.getString("habitId"),
                            document.getString("photoUrl"),
                            document.getTimestamp("photoUploadDate"),
                            document.getTimestamp("dateLogged"),
                            Boolean.TRUE.equals(document.getBoolean("completed")),
                            document.getString("note"),
                            document.getString("userId"),
                            document.getString("name")));
                }
                callback.onObjectsExist(habitProgresses);
            }

            @Override
            public void failure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void updateHabitProgress(String id, Map<String, Object> updateObject, ServiceUpdateCallback callback) {
        habitDao.updateHabitProgress(id, updateObject, new FireStoreUpdateCallback() {
            @Override
            public void onUpdate() {
                callback.onUpdated();
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

//    @Override
//    public void findHabitProgressOfOthers(String currentUser, ServiceQueryCallback<HabitProgress> callback) {
//        UserServiceI userService = new UserService();
//        userService.getOtherUsers(currentUser, new ServiceQueryCallback<User>() {
//            @Override
//            public void onObjectsExist(List<User> objects) {
//                SessionServiceI sessionService = new SessionService(new SessionDao());
//                for (User user: objects) {
//                    sessionService.findSessionsOfUser(user.getId(), new ServiceQueryCallback<Session>() {
//                        @Override
//                        public void onObjectsExist(List<Session> objects) {
//                            HabitServiceI habitServiceI = new HabitService(new HabitDao());
//                            for (Session session: objects) {
//                                habitServiceI.findHabitsOfSession(session.getSessionId(), new ServiceQueryCallback<Habit>() {
//                                    @Override
//                                    public void onObjectsExist(List<Habit> objects) {
//                                        for(Habit habit: objects) {
//                                            habitServiceI.getProgressOfHabit(habit.getId(), new ServiceQueryCallback<HabitProgress>() {
//                                                @Override
//                                                public void onObjectsExist(List<HabitProgress> objects) {
//                                                    callback.onObjectsExist(objects);
//                                                }
//
//                                                @Override
//                                                public void onFailure() {
//                                                    callback.onFailure();
//                                                }
//                                            });
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onFailure() {
//                                        callback.onFailure();
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onFailure() {
//                            callback.onFailure();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure() {
//                callback.onFailure();
//            }
//        });
//    }
}
