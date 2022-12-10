package edu.northeastern.a321habits.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.northeastern.a321habits.DateUtil;
import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.daos.session.SessionDao;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.models.session.Session;
import edu.northeastern.a321habits.services.ServiceGetCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.habit.HabitService;
import edu.northeastern.a321habits.services.habit.HabitServiceI;
import edu.northeastern.a321habits.services.session.SessionService;
import edu.northeastern.a321habits.services.session.SessionServiceI;

public class HabitLogAdapter extends RecyclerView.Adapter<HabitLogAdapter.ViewHolder> {

    private static final String TAG = "HabitLogAdapter";
    private final Context context;
    private final List<Habit> habits;
    private final ClickListener clickListeners;

    public HabitLogAdapter(Context context, List<Habit> habits, ClickListener listener) {
        this.context = context;
        this.habits = habits;
        this.clickListeners = listener;
    }


    @NonNull
    @Override
    public HabitLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.habit_log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HabitLogAdapter.ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.activityName.setText(habit.getName());

        SessionServiceI sessionService = new SessionService(new SessionDao());
        sessionService.getSessionById(habit.getSessionId(), new ServiceGetCallback<Session>() {
            @Override
            public void onGetExists(Session session) {
                HabitServiceI habitService = new HabitService(new HabitDao());
                habitService.getProgressOfHabit(habit.getId(), new ServiceQueryCallback<HabitProgress>() {
                    @Override
                    public void onObjectsExist(List<HabitProgress> objects) {
                        if (objects.size() == 0) {
                            return;
                        }

                        Date today = new Date();
                        LocalDate sessionStart = DateUtil.convertToLocalDate(
                                session.getStartDate().toDate());
                        LocalDate habitLogged;
                        for (HabitProgress habitProgress : objects) {
                            if (habitProgress.isCompleted()) {
                                habitLogged = DateUtil.convertToLocalDate(
                                        habitProgress.getDateLogged().toDate());
                                int daysDifference = Period.between(sessionStart, habitLogged).getDays();
                                holder.pills.get(daysDifference).setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.vertical_pill_filled,null));
                            }
                            if (DateUtils.isSameDay(habitProgress.getDateLogged().toDate(), today)) {
                                holder.checkIcon.setVisibility(View.INVISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, String.format("Failed fetching habit progress for %s", habit.getId()));
                    }
                });
            }

            @Override
            public void onGetDoesNotExist() {
                Log.d(TAG, String.format("No session exists for session by id %s", habit.getSessionId()));
            }

            @Override
            public void onFailure() {
                Log.d(TAG, String.format("Failed fetching session for session by id %s", habit.getSessionId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return habits.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView activityName;
        private final ImageView cameraIcon;
        private final ImageView notesIcon;
        private final ImageView checkIcon;

        private final View pill1;
        private final View pill2;
        private final View pill3;
        private final View pill4;
        private final View pill5;
        private final View pill6;
        private final View pill7;

        private final View pill8;
        private final View pill9;
        private final View pill10;
        private final View pill11;
        private final View pill12;
        private final View pill13;
        private final View pill14;

        private final View pill15;
        private final View pill16;
        private final View pill17;
        private final View pill18;
        private final View pill19;
        private final View pill20;
        private final View pill21;

        private final List<View> pills;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            activityName = itemView.findViewById(R.id.textView);
            cameraIcon = itemView.findViewById(R.id.camera);
            notesIcon = itemView.findViewById(R.id.notes);
            checkIcon = itemView.findViewById(R.id.check);

            pill1 = itemView.findViewById(R.id.pill_1);
            pill2 = itemView.findViewById(R.id.pill_2);
            pill3 = itemView.findViewById(R.id.pill_3);
            pill4 = itemView.findViewById(R.id.pill_4);
            pill5 = itemView.findViewById(R.id.pill_5);
            pill6 = itemView.findViewById(R.id.pill_6);
            pill7 = itemView.findViewById(R.id.pill_7);

            pill8 = itemView.findViewById(R.id.pill_8);
            pill9 = itemView.findViewById(R.id.pill_9);
            pill10 = itemView.findViewById(R.id.pill_10);
            pill11 = itemView.findViewById(R.id.pill_11);
            pill12 = itemView.findViewById(R.id.pill_12);
            pill13 = itemView.findViewById(R.id.pill_13);
            pill14 = itemView.findViewById(R.id.pill_14);

            pill15 = itemView.findViewById(R.id.pill_15);
            pill16 = itemView.findViewById(R.id.pill_16);
            pill17 = itemView.findViewById(R.id.pill_17);
            pill18 = itemView.findViewById(R.id.pill_18);
            pill19 = itemView.findViewById(R.id.pill_19);
            pill20 = itemView.findViewById(R.id.pill_20);
            pill21 = itemView.findViewById(R.id.pill_21);

            pills = new ArrayList<View>(){{
                add(pill1);add(pill2);add(pill3);add(pill4);add(pill5);add(pill6);add(pill7);
                add(pill8);add(pill9);add(pill10);add(pill11);add(pill12);add(pill13);add(pill14);
                add(pill15);add(pill16);add(pill17);add(pill18);add(pill19);add(pill20);add(pill21);
            }};


            cameraIcon.setOnClickListener(v -> clickListeners.onCameraIconClicked(getAdapterPosition()));

            notesIcon.setOnClickListener(view -> clickListeners.onNoteIconClicked(getAdapterPosition()));

            checkIcon.setOnClickListener(view -> clickListeners.onCheckIconClicked(getAdapterPosition(), checkIcon));
        }
    }
}
