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

import edu.northeastern.a321habits.R;

public class HabitLogAdapter extends RecyclerView.Adapter <HabitLogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HabitLogModel> habitList = new ArrayList<>();

    public HabitLogAdapter(Context context, ArrayList<HabitLogModel> habitList) {
        this.context = context;
        this.habitList = habitList;
    }


    @NonNull
    @Override
    public HabitLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.habit_log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HabitLogAdapter.ViewHolder holder, int position) {
        HabitLogModel habit = habitList.get(position);
        holder.activityName.setText(habit.getTitle());
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView activityName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
           activityName = itemView.findViewById(R.id.textView);
        }
    }
}
