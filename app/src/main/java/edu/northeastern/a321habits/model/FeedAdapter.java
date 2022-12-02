package edu.northeastern.a321habits.model;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.northeastern.a321habits.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HabitLogModel> habitLogsArrayList;

    // creating a constructor class.
    public FeedAdapter(Context context, ArrayList<HabitLogModel> courseModalArrayList) {
        this.context = context;
        this.habitLogsArrayList = courseModalArrayList;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.feed_habit_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        HabitLogModel habit_log = habitLogsArrayList.get(position);
        holder.habitTitleTV.setText(habit_log.getTitle());
        holder.userNameTV.setText(habit_log.getUser().getFullName());
        holder.logDateTimeTV.setText(habit_log.getCreatedAt());
        holder.dayNumTV.setText("DAY " + habit_log.getDay());
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(habit_log.getUser().getProfilePic()).into(holder.userProfilePicIV);
    }

    @Override
    public int getItemCount() {
        return habitLogsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView userNameTV;
        private final ImageView userProfilePicIV;
        private final TextView logDateTimeTV;
        private final TextView habitTitleTV;
        private final TextView dayNumTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            userNameTV = itemView.findViewById(R.id.user_name);
            userProfilePicIV = itemView.findViewById(R.id.img_user);
            logDateTimeTV = itemView.findViewById(R.id.log_date_time);
            habitTitleTV = itemView.findViewById(R.id.tv_logged_habit_title);
            dayNumTV = itemView.findViewById(R.id.tv_day_num);
        }
    }
}

