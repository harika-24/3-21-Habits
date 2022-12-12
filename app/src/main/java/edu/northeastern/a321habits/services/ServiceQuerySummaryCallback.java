package edu.northeastern.a321habits.services;

import java.util.List;

public interface ServiceQuerySummaryCallback {
    void onSummarySuccessful(int sevenDayStreakCount, String perfectHabit);
    void onFailure();
}


