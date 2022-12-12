package edu.northeastern.a321habits.daos.habit;

import static edu.northeastern.a321habits.daos.DaoHelper.callOnComplete;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map;

import edu.northeastern.a321habits.daos.FireStoreUpdateCallback;
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
    public void findHabitProgressOfOthers(String currentUser, DocumentSnapshot lastVisible, FirestoreQueryCallback callback) {

        if (lastVisible != null) {
            Log.d("HABIT SCROLL HABIT DAO", lastVisible.toString());
            Query query = db.collection(HABIT_PROGRESS_COLLECTION)
                    .orderBy("dateLogged", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(10);
            callOnComplete(query, callback);
        } else {
            Log.d("HABIT SCROLL HABIT DAO", "null");
            Query query = db.collection(HABIT_PROGRESS_COLLECTION)
                    .orderBy("dateLogged", Query.Direction.DESCENDING)
                    .limit(10);
            callOnComplete(query, callback);
        }

    }

    @Override
    public void updateHabitProgress(String id, Map<String, Object> updateObject, FireStoreUpdateCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION)
                .whereEqualTo("habitId", id);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Date today = new Date();
                boolean foundLog = false;
                DocumentSnapshot foundDocument = null;
                for (QueryDocumentSnapshot snapshot: task.getResult()) {
                    Timestamp dateLogged = snapshot.getTimestamp("dateLogged");
                    if (dateLogged != null && DateUtils.isSameDay(dateLogged.toDate(), today)) {
                        foundLog = true;
                        foundDocument = snapshot;
                    }
                }
                if (foundLog) {
                    db.collection(HABIT_PROGRESS_COLLECTION).document(foundDocument.getId())
                            .update(updateObject)
                            .addOnSuccessListener(unused -> callback.onUpdate())
                            .addOnFailureListener(e -> callback.onFailure());
                } else {
                    callback.onFailure();
                }
            } else {
                Log.d(TAG, "Error while fetching ", task.getException());
                callback.onFailure();
            }
        });


    }

    @Override
    public void getAllHabitProgressOfUser(String userId, FirestoreQueryCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION).whereEqualTo("userId", userId);
        callOnComplete(query, callback);
    }

    @Override
    public void getProgressByDayByHabitId(int day, String habitId, FirestoreQueryCallback callback) {
        Query query = db.collection(HABIT_PROGRESS_COLLECTION).whereEqualTo("habitId", habitId)
                .whereEqualTo("logDay", day);
        callOnComplete(query, callback);
    }


}
