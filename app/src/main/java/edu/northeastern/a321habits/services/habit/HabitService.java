package edu.northeastern.a321habits.services.habit;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import edu.northeastern.a321habits.services.ServiceQueryPaginatedCallback;
import edu.northeastern.a321habits.services.ServiceQuerySummaryCallback;
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

                    int progressDay = 1;
                    if (document.get("progressDay") != null) {
                        progressDay = ((Long)document.get("logDay")).intValue();
                    }

                    HabitProgress habitProgress =
                            new HabitProgress(document.getString("habitId"),
                                    document.getString("photoUrl"),
                                    document.getTimestamp("photoUploadDate"),
                                    document.getTimestamp("dateLogged"),
                                    Boolean.TRUE.equals(document.getBoolean("completed")),
                                    document.getString("note"),
                                    document.getString("userId"),
                                    document.getString("name"),
                                    progressDay,
                                    document.getId());
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
    public void findHabitProgressOfOthers(String currentUser, DocumentSnapshot lastVisible, ServiceQueryPaginatedCallback<HabitProgress> callback) {
        habitDao.findHabitProgressOfOthers(currentUser, lastVisible, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                List<HabitProgress> habitProgresses = new ArrayList<>();

                DocumentSnapshot lastVisible = snapshot.getDocuments()
                        .get(snapshot.size() -1);

                for (QueryDocumentSnapshot document : snapshot) {
                    // todo: remove this condition when database becomes backward compatible and
                    //  all records have a progressDay field
                    int progressDay = 1;
                    if (document.get("logDay") != null) {
                        progressDay = ((Long)document.get("logDay")).intValue();
                    }
                    habitProgresses.add(new HabitProgress(document.getString("habitId"),
                            document.getString("photoUrl"),
                            document.getTimestamp("photoUploadDate"),
                            document.getTimestamp("dateLogged"),
                            Boolean.TRUE.equals(document.getBoolean("completed")),
                            document.getString("note"),
                            document.getString("userId"),
                            document.getString("name"),
                            progressDay,
                            document.getId()));
                }
                Log.d("HABIT SCROLL","VALUE OF LASTvISIBLE" +lastVisible.toString());
                callback.onObjectsExistPaginate(habitProgresses, lastVisible);
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

    @Override
    public void getAllHabitProgressOfUser(String userId, ServiceQuerySummaryCallback callback) {
        habitDao.getAllHabitProgressOfUser(userId, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {

                Map<String, ArrayList<Integer>> habitIDtoLoggedDays = new HashMap<>();
                Map<String, String> habitIDtoName = new HashMap<>();
                int streakCount = 0;
                String perfectedHabit = null;

                for (QueryDocumentSnapshot document : snapshot) {
                    String habitId = document.getString("habitId");
                    String habitName = document.getString("name");
                    Integer progressDay = ((Long)document.get("logDay")).intValue();

                    habitIDtoName.put(habitId, habitName);
                    if (habitIDtoLoggedDays.get(habitId) != null) {
                        habitIDtoLoggedDays.get(habitId).add(progressDay);
                    }
                    else {
                        habitIDtoLoggedDays.put(habitId, new ArrayList<>());
                        habitIDtoLoggedDays.get(habitId).add(progressDay);
                    }
                }


                for (Map.Entry<String, ArrayList<Integer>> habit :
                        habitIDtoLoggedDays.entrySet()) {

                    ArrayList<Integer> days = habit.getValue();
                    Collections.sort(days);
                    Log.d("HABIT SUMMARY name", habitIDtoName.get(habit.getKey()));
                    Log.d("HABIT SUMMARY log days", days.toString());

                    int consecutiveDays = 1;
                    for (int i = 1; i<days.size(); i++) {
                        if (days.get(i) - days.get(i-1) == 1) {
                            consecutiveDays++;
                        }
                        if(consecutiveDays == 7) {
                            Log.d("HABIT SUMMARY", "streak found!!!!");
                            streakCount++;
                            consecutiveDays = 1;
                        }
                    }
                    if (days.size() == 21) {
                        perfectedHabit = habitIDtoName.get(habit.getKey());
                    }
                }
                Log.d("HABIT SUMMARY", String.valueOf(streakCount));

                callback.onSummarySuccessful(streakCount, perfectedHabit);
            }

            @Override
            public void failure() {
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
