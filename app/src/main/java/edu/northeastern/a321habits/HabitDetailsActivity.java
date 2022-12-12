package edu.northeastern.a321habits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a321habits.adapters.HabitDetailsAdapter;
import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.model.FeedAdapter;
import edu.northeastern.a321habits.model.HabitLogModel;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.habit.HabitService;
import edu.northeastern.a321habits.services.habit.HabitServiceI;

public class HabitDetailsActivity extends AppCompatActivity {

    private String habitId;
    private List<HabitProgress> habitProgressList;
    private RecyclerView habitDetailsRecView;
    private HabitDetailsAdapter habitDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_details);
        habitId = getIntent().getStringExtra("habitId");
        habitDetailsRecView = findViewById(R.id.habitDetailsRecView);
        habitDetailsRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initialise();
    }

    private void initialise() {
        HabitServiceI habitService = new HabitService(new HabitDao());
        habitService.getProgressOfHabit(habitId, new ServiceQueryCallback<HabitProgress>() {
            @Override
            public void onObjectsExist(List<HabitProgress> objects) {
                habitProgressList = objects;
                habitDetailsAdapter = new HabitDetailsAdapter(getApplicationContext(), habitProgressList);
                habitDetailsRecView.setAdapter(habitDetailsAdapter);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Could not fetch habit progress. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}