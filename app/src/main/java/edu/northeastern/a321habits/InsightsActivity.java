package edu.northeastern.a321habits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.daos.session.SessionDao;
import edu.northeastern.a321habits.models.session.Session;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceQuerySummaryCallback;
import edu.northeastern.a321habits.services.habit.HabitService;
import edu.northeastern.a321habits.services.habit.HabitServiceI;
import edu.northeastern.a321habits.services.session.SessionService;
import edu.northeastern.a321habits.services.session.SessionServiceI;

public class InsightsActivity extends AppCompatActivity {

    private TextView completedSessionNumTV;
    private TextView sevenDayStreakCountTV;
    private TextView perfectedHabitTV;
    private TextView sessionsTextTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);
        init();
    }

    private void init() {
        completedSessionNumTV = findViewById(R.id.tv_completed_sessions_num);
        sevenDayStreakCountTV = findViewById(R.id.tv_7_day_streak_num);
        perfectedHabitTV = findViewById(R.id.tv_perfected_habit);
        sessionsTextTV = findViewById(R.id.tv_sessions_text);
        updateSessionNum();
        updateStreakCountAndPerfectHabit();
    }

    private void updateStreakCountAndPerfectHabit() {
        String userId = SharedPrefUtil.getHandleOfLoggedInUser(getApplicationContext());
        HabitServiceI habitService = new HabitService(new HabitDao());
        habitService.getAllHabitProgressOfUser(userId, new ServiceQuerySummaryCallback() {
            @Override
            public void onSummarySuccessful(int sevenDayStreakCount, String perfectHabit) {
                sevenDayStreakCountTV.setText(String.valueOf(sevenDayStreakCount));
                if (perfectHabit == null) {
                    perfectedHabitTV.setText("");
                }
                else {
                    perfectedHabitTV.setText("You perfected " + perfectHabit + "!");
                }
            }

            @Override
            public void onFailure() {
                sevenDayStreakCountTV.setText("-");
                perfectedHabitTV.setText("");
            }
        });
    }

    private void updateSessionNum() {
        String userId = SharedPrefUtil.getHandleOfLoggedInUser(getApplicationContext());
        SessionServiceI sessionService = new SessionService(new SessionDao());
        sessionService.getCompletedSessions(userId, new ServiceQueryCallback<Session>() {
            @Override
            public void onObjectsExist(List<Session> objects) {
                if (objects.size() < 2) {
                    sessionsTextTV.setText("SESSION");
                }
                String completedSessions = String.valueOf(objects.size());
                completedSessionNumTV.setText(completedSessions);
            }

            @Override
            public void onFailure() {
                completedSessionNumTV.setText("-");
            }
        });

    }




}