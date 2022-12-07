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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.SharedPrefUtil;
import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.daos.session.SessionDao;
import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;
import edu.northeastern.a321habits.model.ClickListener;
import edu.northeastern.a321habits.model.HabitLogAdapter;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.session.Session;
import edu.northeastern.a321habits.services.ServiceAddCallback;
import edu.northeastern.a321habits.services.ServiceDeleteCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceUpdateCallback;
import edu.northeastern.a321habits.services.habit.HabitService;
import edu.northeastern.a321habits.services.habit.HabitServiceI;
import edu.northeastern.a321habits.services.session.SessionService;
import edu.northeastern.a321habits.services.session.SessionServiceI;

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
    private List<Habit> habits = new ArrayList<>();
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

        HabitServiceI habitServiceI = new HabitService(new HabitDao());
        SessionServiceI sessionService = new SessionService(new SessionDao());
        sessionService.getCurrentSession(SharedPrefUtil.getHandleOfLoggedInUser(getContext()),
                new ServiceQueryCallback<Session>() {
                    @Override
                    public void onObjectsExist(List<Session> objects) {
                        if (objects.size() > 0) {
                            Session session = objects.get(0);
                            SharedPrefUtil.addCurrentSession(session.getSessionId(), getContext());
                            habitServiceI.findHabitsOfSession(SharedPrefUtil.getCurrentSession(getContext()), new ServiceQueryCallback<Habit>() {
                                @Override
                                public void onObjectsExist(List<Habit> objects) {
                                    for (Habit habit: objects) {
                                        habits.add(habit);
                                        adapter.notifyItemInserted(habits.size() - 1);
                                    }
                                    if (habits.size() == 3) {
                                        newActivityBtn.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(getContext(), "Could not find any " +
                                            "habits for this session. Try again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "Failed to fetch current session. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });

        newActivityBtn.setOnClickListener(view -> showActivityBox());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                        Habit habit = habits.get(pos);
                        HabitServiceI habitService = new HabitService(new HabitDao());
                        habitService.deleteHabit(habit.getId(), new ServiceDeleteCallback() {
                            @Override
                            public void onDeleted() {
                                habits.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                if (habits.size() < 3) {
                                    newActivityBtn.setVisibility(View.VISIBLE);
                                }
                                if (habits.size() == 0) {
                                    // Clear the current session.
                                    SessionServiceI sessionService = new SessionService(new SessionDao());
                                    Map<String, Object> updateObject = new HashMap<>();
                                    updateObject.put("sessionId", SharedPrefUtil.getCurrentSession(getContext()));
                                    updateObject.put("hasEnded", true);
                                    sessionService.updateSession(updateObject, new ServiceUpdateCallback() {
                                        @Override
                                        public void onUpdated() {
                                            Toast.makeText(getContext(), "The session has ended.", Toast.LENGTH_LONG).show();
                                            SharedPrefUtil.removeCurrentSession(getContext());
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(getContext(), "The session could not be ended. Try again.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(), "Could not delete habit. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int pos = viewHolder.getLayoutPosition();
                        Habit habit = habits.get(pos);
                        habits.add(pos, habit);
                        adapter.notifyItemInserted(pos);
                        habits.remove(pos + 1);
                        adapter.notifyItemRemoved(pos + 1);
                        if (habits.size() < 3) {
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
        adapter = new HabitLogAdapter(binding.getRoot().getContext(), habits, new ClickListener() {
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

        submitButton.setOnClickListener(v -> {
            String activityName = nameEt.getText().toString();
            if (activityName.isEmpty()) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Habit can't be empty", Toast.LENGTH_LONG).show();
                return;
            }


            if (habits.size() == 0) {
                // Start a new Session
                createNewSessionAndAdd(activityName);
            } else {
                String currentSession = SharedPrefUtil.getCurrentSession(getContext());
                if (currentSession.equals("")) {
                    habits.clear();
                    createNewSessionAndAdd(activityName);
                } else {
                    HabitServiceI habitService = new HabitService(new HabitDao());
                    habitService.createHabit(new Habit("", activityName, currentSession), new ServiceAddCallback() {
                        @Override
                        public void onCreated(String uniqueId) {
                            Toast.makeText(getContext(), "Habit Created", Toast.LENGTH_LONG).show();
                            habits.add(new Habit(uniqueId, activityName, currentSession));
                            adapter.notifyItemInserted(habits.size() - 1);
                            if (habits.size() == 3) {
                                newActivityBtn.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(), "Could not create habit. Try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            //  adapter.notifyDataSetChanged();
            dialog.dismiss();

        });
        dialog.show();
    }

    private void createNewSessionAndAdd(String activityName) {
        SessionServiceI sessionService = new SessionService(new SessionDao());
        HabitServiceI habitService = new HabitService(new HabitDao());
        LocalDate localDate = LocalDate.now();
        sessionService.createSession(new Session(null,
                        SharedPrefUtil.getHandleOfLoggedInUser(getContext()),
                        new Timestamp(localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0),
                        new Timestamp(localDate.plusDays(21).atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0),
                        false),
                new ServiceAddCallback() {
                    @Override
                    public void onCreated(String sessionId) {
                        Toast.makeText(getContext(), "Created a new session.", Toast.LENGTH_LONG).show();
                        SharedPrefUtil.addCurrentSession(sessionId, getContext());
                        habitService.createHabit(new Habit("", activityName, sessionId), new ServiceAddCallback() {
                            @Override
                            public void onCreated(String habitId) {
                                Toast.makeText(getContext(), "Habit Created", Toast.LENGTH_LONG).show();
                                habits.add(new Habit(habitId, activityName, sessionId));
                                adapter.notifyItemInserted(habits.size() - 1);
                                if (habits.size() == 3) {
                                    newActivityBtn.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(), "Could not create habit. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), "Failed to create a session.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}