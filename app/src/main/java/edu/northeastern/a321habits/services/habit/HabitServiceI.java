package edu.northeastern.a321habits.services.habit;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceQueryPaginatedCallback;
import edu.northeastern.a321habits.services.ServiceQuerySummaryCallback;
import edu.northeastern.a321habits.services.ServiceUpdateCallback;

public interface HabitServiceI {
    void findHabitsOfSession(String sessionId, ServiceQueryCallback<Habit> callback);
    void getProgressOfHabit(String habitId, ServiceQueryCallback<HabitProgress> callback);
    void createHabit(Habit habit, ServiceAddCallback callback);
    void deleteHabit(String habitId, ServiceDeleteCallback callback);
    void addProgressToHabit(HabitProgress progress, String currentUser, ServiceAddCallback callback);
    void deleteProgressFromHabit(String habitProgressId, ServiceDeleteCallback callback);
    void findHabitProgressOfOthers(String currentUser, DocumentSnapshot lastVisible, ServiceQueryPaginatedCallback<HabitProgress> callback);
    void updateHabitProgress(String id, Map<String, Object> updateObject,
                             ServiceUpdateCallback callback);

    void getProgressByDayByHabitId(int day, String habitId, ServiceQueryCallback<HabitProgress> callback);
    void getAllHabitProgressOfUser(String userId, ServiceQuerySummaryCallback callback);
}
