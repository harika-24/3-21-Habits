package edu.northeastern.a321habits.daos.habit;

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
    void findHabitProgressOfOthers(String currentUser, FirestoreQueryCallback callback);
}
