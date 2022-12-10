package edu.northeastern.a321habits.model;

import android.widget.ImageView;

public interface ClickListener {

    void onCameraIconClicked(int position);

    void onNoteIconClicked(int position);

    void onCheckIconClicked(int adapterPosition, ImageView checkIcon, ImageView resetIcon,
                            HabitLogAdapter.UpdatePillCallback callback);
}
