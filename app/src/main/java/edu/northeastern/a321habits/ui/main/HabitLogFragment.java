package edu.northeastern.a321habits.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.northeastern.a321habits.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;

import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;
import edu.northeastern.a321habits.model.ClickListener;
import edu.northeastern.a321habits.model.HabitLogAdapter;
import edu.northeastern.a321habits.model.HabitLogModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitLogFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentHabitLogBinding binding;
    private FloatingActionButton newActivityBtn;
    private HabitLogAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<HabitLogModel> habitList = new ArrayList<>();
    private String notes;

    public static HabitLogFragment newInstance(int index) {
        HabitLogFragment fragment = new HabitLogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentHabitLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.habit_log);

        newActivityBtn = root.findViewById(R.id.floatingActionButton2);


        newActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityBox();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete the Habit?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View parentLayout = root;
                        Snackbar snackMessage = Snackbar.make(parentLayout, "Habit Deleted", Snackbar.LENGTH_LONG).setAction("Action", null);
                        View snackView = snackMessage.getView();
                        TextView tView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                        tView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snackMessage.show();
                        int pos = viewHolder.getLayoutPosition();
                        habitList.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        if (habitList.size() != 3) {
                            newActivityBtn.setVisibility(View.VISIBLE);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int pos = viewHolder.getLayoutPosition();
                        HabitLogModel element = habitList.get(pos);
                        habitList.add(pos,element);
                        adapter.notifyItemInserted(pos);
                        habitList.remove(pos+1);
                        adapter.notifyItemRemoved(pos+1);
                        if (habitList.size() != 3) {
                            newActivityBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        setAdapter();
        return root;
    }

    private void setAdapter() {
        adapter = new HabitLogAdapter(binding.getRoot().getContext(), habitList, new ClickListener() {
            @Override
            public void onCameraIconClicked(int position) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }

            @Override
            public void onNoteIconClicked(int position) {
                final Dialog noteDialog = new Dialog(getActivity());
                noteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                noteDialog.setCancelable(true);
                //Mention the name of the layout of your custom dialog.
                noteDialog.setContentView(R.layout.custom_note);
                final EditText noteText = noteDialog.findViewById(R.id.editTextTextMultiLine);
                noteText.setText(notes);
                Button submitNoteButton = noteDialog.findViewById(R.id.note_submit_button);
                submitNoteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = noteText.getText().toString();
                        if (text.isEmpty()) {
                            noteDialog.dismiss();
                            Toast.makeText(getActivity(), "Note can't be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        notes = text;
                        Toast.makeText(getActivity(), "Note Added", Toast.LENGTH_LONG).show();
                        noteText.setText("");
                        noteDialog.dismiss();
                    }
                });
                noteDialog.show();

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void showActivityBox() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_new_activity);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityName = nameEt.getText().toString();
                if (activityName.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Habit can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                habitList.add(new HabitLogModel(activityName, null, new Date().toString(),1,null));
                adapter.notifyItemInserted(habitList.size()-1);
              //  adapter.notifyDataSetChanged();
                dialog.dismiss();
                if (habitList.size() == 3) {
                    newActivityBtn.setVisibility(View.INVISIBLE);
                }

            }
        });
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}