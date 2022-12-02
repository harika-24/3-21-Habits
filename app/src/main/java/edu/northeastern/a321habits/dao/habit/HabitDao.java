package edu.northeastern.a321habits.dao.habit;

import static edu.northeastern.a321habits.dao.DaoHelper.callOnComplete;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import edu.northeastern.a321habits.dao.FirestoreQueryCallback;

public class HabitDao implements HabitDaoI {

    private static final String COLLECTION = "habit";
    private static final String HABIT_PROGRESS_COLLECTION = "habit-progress";
    private static final String TAG = "HabitDao";
    private FirebaseFirestore db;

    public HabitDao() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void getHabitsOfSession(String sessionId, FirestoreQueryCallback callback) {
        Query query = db.collection(COLLECTION).whereEqualTo("sessionId", sessionId);
        callOnComplete(query, callback);
    }

    @Override
    public void getProgressOfHabit(String habitId, FirestoreQueryCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION).whereEqualTo("habitId", habitId);
        callOnComplete(query, callback);
    }




}
