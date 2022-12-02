package edu.northeastern.a321habits.dao.habit;

import edu.northeastern.a321habits.dao.FirestoreQueryCallback;

public interface HabitDaoI {
    void getHabitsOfSession(String sessionId, FirestoreQueryCallback callback);
    void getProgressOfHabit(String habitId, FirestoreQueryCallback callback);
}
