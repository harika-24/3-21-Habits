package edu.northeastern.a321habits.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.models.habit.Habit;

public class HabitLogAdapter extends RecyclerView.Adapter <HabitLogAdapter.ViewHolder> {

    private Context context;
    private List<Habit> habits;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
           activityName = itemView.findViewById(R.id.textView);
           cameraIcon = itemView.findViewById(R.id.camera);
           notesIcon = itemView.findViewById(R.id.notes);

           cameraIcon.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v)
               {
                   clickListeners.onCameraIconClicked(getAdapterPosition());
               }

           });

           notesIcon.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   clickListeners.onNoteIconClicked(getAdapterPosition());
               }
           });

        }
    }
}
