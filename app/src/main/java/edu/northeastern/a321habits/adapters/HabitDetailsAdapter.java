package edu.northeastern.a321habits.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.model.FeedAdapter;
import edu.northeastern.a321habits.models.habit.HabitProgress;

public class HabitDetailsAdapter extends RecyclerView.Adapter<HabitDetailsAdapter.ViewHolder> {

    private Context context;
    private List<HabitProgress> habitProgressList;

    public HabitDetailsAdapter(Context context, List<HabitProgress> habitProgressList) {
        this.context = context;
        this.habitProgressList = habitProgressList;
    }

    @NonNull
    @Override
    public HabitDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitDetailsAdapter
                .ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.habit_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HabitDetailsAdapter.ViewHolder holder, int position) {
        HabitProgress habitProgress = habitProgressList.get(position);
        holder.dayView.setText(String.format("Day %s", habitProgress.getLogDay()));
        if (habitProgress.getNote() != null && !habitProgress.getNote().equals("")) {
            holder.noteView.setText(habitProgress.getNote());
        } else {
            holder.noteView.setText("No note.");
        }
        String completedText = habitProgress.isCompleted() ? "Completed" : "Incomplete";
        holder.completedView.setText(completedText);
        Picasso.get().load(habitProgress.getPhotoUrl()).resize(512, 512).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return habitProgressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayView;
        private final TextView noteView;
        private final TextView completedView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayView = itemView.findViewById(R.id.habitProgressDayTextView);
            noteView = itemView.findViewById(R.id.habitProgressNoteTextView);
            imageView = itemView.findViewById(R.id.habitProgressImageView);
            completedView = itemView.findViewById(R.id.habitProgressCompletedTextView);
        }
    }
}
