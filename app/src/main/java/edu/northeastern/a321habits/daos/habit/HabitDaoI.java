package edu.northeastern.a321habits.daos.habit;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;

public interface HabitDaoI {
    void findHabitsOfSession(String sessionId, FirestoreQueryCallback callback);
    void getProgressOfHabit(String habitId, FirestoreQueryCallback callback);
    void createHabit(Habit habit, FirestoreAddCallback callback);
    void deleteHabit(String habitId, FirestoreDeleteCallback callback);
    void addProgressToHabit(HabitProgress progress, String currentUser, FirestoreAddCallback callback);
    void deleteProgressFromHabit(String habitProgressId, FirestoreDeleteCallback callback);
    void findHabitProgressOfOthers(String currentUser, DocumentSnapshot lastVisible, FirestoreQueryCallback callback);

    void updateHabitProgress(String id, Map<String, Object> updateObject,
                             FireStoreUpdateCallback callback);
    void getAllHabitProgressOfUser(String userId, FirestoreQueryCallback callback);
}
