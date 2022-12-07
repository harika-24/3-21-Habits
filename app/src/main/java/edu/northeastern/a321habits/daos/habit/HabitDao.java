package edu.northeastern.a321habits.daos.habit;

import static edu.northeastern.a321habits.daos.DaoHelper.callOnComplete;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.a321habits.daos.FirestoreAddCallback;
import edu.northeastern.a321habits.daos.FirestoreDeleteCallback;
import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;

public class HabitDao implements HabitDaoI {

    private static final String COLLECTION = "habit";
    private static final String HABIT_PROGRESS_COLLECTION = "habit-progress";
    private static final String TAG = "HabitDao";
    private final FirebaseFirestore db;

    public HabitDao() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void findHabitsOfSession(String sessionId, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION).whereEqualTo("sessionId", sessionId);
        callOnComplete(query, callback);
    }

    @Override
    public void getProgressOfHabit(String habitId, FirestoreQueryCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION).whereEqualTo("habitId", habitId);
        callOnComplete(query, callback);
    }

    @Override
    public void createHabit(Habit habit, FirestoreAddCallback callback) {
        db.collection(COLLECTION).add(habit)
                .addOnSuccessListener(callback::onSuccessfullyAdded)
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void deleteHabit(String habitId, FirestoreDeleteCallback callback) {
        db.collection(COLLECTION).document(habitId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccessfullyDeleted())
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void addProgressToHabit(HabitProgress progress, String currentUser, FirestoreAddCallback callback) {
        db.collection(HABIT_PROGRESS_COLLECTION).add(progress)
                .addOnSuccessListener(callback::onSuccessfullyAdded)
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void deleteProgressFromHabit(String habitProgressId, FirestoreDeleteCallback callback) {
        db.collection(HABIT_PROGRESS_COLLECTION).document(habitProgressId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccessfullyDeleted())
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void findHabitProgressOfOthers(String currentUser, FirestoreQueryCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION).whereNotEqualTo("handle", currentUser);
        callOnComplete(query, callback);
    }


}
