package edu.northeastern.a321habits.services.habit;

import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceGetCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;

public interface HabitServiceI {
    void findHabitsOfSession(String sessionId, ServiceQueryCallback<Habit> callback);
    void getProgressOfHabit(String habitId, ServiceGetCallback<HabitProgress> callback);
    void createHabit(Habit habit, ServiceAddCallback callback);
    void deleteHabit(String habitId, ServiceDeleteCallback callback);
    void addProgressToHabit(HabitProgress progress, ServiceAddCallback callback);
    void deleteProgressFromHabit(String habitProgressId, ServiceDeleteCallback callback);
}
