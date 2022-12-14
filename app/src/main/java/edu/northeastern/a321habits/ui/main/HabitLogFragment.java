package edu.northeastern.a321habits.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.a321habits.DateUtil;
import edu.northeastern.a321habits.HabitDetailsActivity;
import edu.northeastern.a321habits.MainActivity;
import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.SharedPrefUtil;
import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.daos.session.SessionDao;
import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;
import edu.northeastern.a321habits.model.ClickListener;
import edu.northeastern.a321habits.model.HabitLogAdapter;
import edu.northeastern.a321habits.models.habit.Habit;
import edu.northeastern.a321habits.models.habit.HabitProgress;
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
    private static final String TAG = "HabitLogFragment";

    private PageViewModel pageViewModel;
    private FragmentHabitLogBinding binding;
    private FloatingActionButton newActivityBtn;
    private HabitLogAdapter adapter;
    private RecyclerView recyclerView;
    private List<Habit> habits = new ArrayList<>();
    private Session currentSession;
    private String notes;
    private HabitServiceI habitService;
    private ActivityResultLauncher<Uri> mCaptureImage;
    private Uri localUri;
    private Uri remoteUri;
    private StorageReference storageReference;
    private int positionSelected;
    private Date photoUploadDate;
    private TextView currentDayTV;


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
        storageReference = FirebaseStorage.getInstance().getReference();
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        habitService = new HabitService(new HabitDao());
        mCaptureImage = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Log.d("IMAGE CAPTURED", "image was taken");
                    StorageReference imgRef = storageReference.child("/images/" + localUri.getLastPathSegment());
                    UploadTask uploadTask = imgRef.putFile(localUri);
                    uploadTask.addOnSuccessListener(res -> {
                        imgRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                            photoUploadDate = new Date(storageMetadata.getCreationTimeMillis());
                            imgRef.getDownloadUrl().addOnSuccessListener(resUri -> {
                                remoteUri = resUri;
                                Log.d("REMOTE URI", remoteUri.toString());
                                updatePhotoMetaDataInDb();
                            });
                        });
                    });
                } else {
                    Log.e("IMAGE FAILED/CANCELLED", "capture image failed/cancelled");
                }
            }
        });
    }

    /**
     * This method is used to update the photo metadata in the DB
     */
    public void updatePhotoMetaDataInDb() {
        Habit habit = habits.get(positionSelected);
        Map<String, Object> updateObject = new HashMap<>();
        updateObject.put("photoUrl", remoteUri);
        updateObject.put("photoUploadDate", photoUploadDate);
        habitService.updateHabitProgress(habit.getId(), updateObject, new ServiceUpdateCallback() {
            @Override
            public void onUpdated() {
                Toast.makeText(getActivity(), "Photo Added", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getActivity(), "Could not add photo. Try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentHabitLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.habit_log);

        newActivityBtn = root.findViewById(R.id.floatingActionButton2);

        currentDayTV = root.findViewById(R.id.tv_current_session_day);

        SessionServiceI sessionService = new SessionService(new SessionDao());
        sessionService.getCurrentSession(SharedPrefUtil.getHandleOfLoggedInUser(getContext()),
                new ServiceQueryCallback<Session>() {
                    @Override
                    public void onObjectsExist(List<Session> objects) {
                        if (objects.size() > 0) {
                            Session session = objects.get(0);
                            currentSession = session;

                            updateCurrentDayofSession();
                            SharedPrefUtil.addCurrentSession(session.getSessionId(), getContext());
                            habitService.findHabitsOfSession(SharedPrefUtil.getCurrentSession(getContext()), new ServiceQueryCallback<Habit>() {
                                @Override
                                public void onObjectsExist(List<Habit> objects) {
                                    for (Habit habit : objects) {
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
                        else {
                            hideDay();
                        }
                    }

                    @Override
                    public void onFailure() {
                        currentSession = null;
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

    private Uri createTmpAndGetUri() throws IOException {
        String s = Environment.DIRECTORY_PICTURES;
        File imageDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File tmpFile = File.createTempFile("Habit_" + timestamp, ".jpg", imageDirectory);
        localUri = FileProvider.getUriForFile(getContext(), "edu.northeastern.a321habits.fileprovider", tmpFile);
        return localUri;
    }

    private void setAdapter() {
        adapter = new HabitLogAdapter(binding.getRoot().getContext(), habits, new ClickListener() {
            @Override
            public void onCameraIconClicked(int position) {
                positionSelected = position;
                try {
                    mCaptureImage.launch(createTmpAndGetUri());

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                Button cancelNoteButton = noteDialog.findViewById(R.id.note_cancel_button);
                submitNoteButton.setOnClickListener(view -> {
                    String text = noteText.getText().toString();
                    if (text.isEmpty()) {
                        noteDialog.dismiss();
                        Toast.makeText(getActivity(), "Note can't be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    notes = text;
                    Habit habit = habits.get(position);
                    Map<String, Object> updateObject = new HashMap<>();
                    updateObject.put("note", notes);
                    habitService.updateHabitProgress(habit.getId(), updateObject, new ServiceUpdateCallback() {
                        @Override
                        public void onUpdated() {
                            Toast.makeText(getActivity(), "Note Added", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getActivity(), "Could not add/update note. Try again", Toast.LENGTH_LONG).show();
                        }
                    });

                    noteText.setText("");
                    noteDialog.dismiss();
                });
                cancelNoteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteDialog.dismiss();
                    }
                });
                noteDialog.show();

            }

            @Override
            public void onCheckIconClicked(int adapterPosition, ImageView checkIcon,
                                           ImageView resetIcon,
                                           HabitLogAdapter.UpdatePillCallback callback) {
                Habit habit = habits.get(adapterPosition);

                String currentUserHandle = SharedPrefUtil.getHandleOfLoggedInUser(getContext());

                Date logDate = new Date();
                Timestamp logTimestamp = new Timestamp(logDate);
                int progressDay = DateUtil.getDaysBetween(currentSession.getStartDate().toDate(), logDate);
                Log.d("HABIT LOG", "days between calculated as = "+progressDay);
                habitService.getProgressByDayByHabitId(progressDay, habit.getId(), new ServiceQueryCallback<HabitProgress>() {
                    @Override
                    public void onObjectsExist(List<HabitProgress> objects) {
                        if (objects.size() == 0) {
                            HabitProgress habitProgress = new HabitProgress(habit.getId(), null,
                                    null, logTimestamp,
                                    true, null, currentUserHandle,habit.getName(), progressDay,"");
                            habitService.addProgressToHabit(habitProgress, currentUserHandle, new ServiceAddCallback() {
                                @Override
                                public void onCreated(String uniqueId) {
                                    Toast.makeText(getContext(), "Added today's log to habit.",
                                            Toast.LENGTH_SHORT).show();
                                    checkIcon.setVisibility(View.INVISIBLE);
                                    resetIcon.setVisibility(View.VISIBLE);
                                    callback.updatePills();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getContext(), "Could not add today's " +
                                            "log to habit. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        boolean found = false;
                        HabitProgress toUpdate = null;
                        for (HabitProgress progress: objects) {
                            if (progress.getLogDay() == progressDay) {
                                found = true;
                                toUpdate = progress;
                                break;
                            }
                        }
                        if (found) {
                            Map<String, Object> updateObject = new HashMap<>();
                            updateObject.put("completed", true);
                            habitService.updateHabitProgress(habit.getId(), updateObject, new ServiceUpdateCallback() {
                                @Override
                                public void onUpdated() {
                                    Toast.makeText(getContext(), "Progress updated.", Toast.LENGTH_SHORT).show();
                                    checkIcon.setVisibility(View.INVISIBLE);
                                    resetIcon.setVisibility(View.VISIBLE);
                                    callback.updatePills();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(getContext(), "Could not update progress. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "Could not update progress. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });




            }

            @Override
            public void onResetIconClicked(int position, ImageView checkIcon, ImageView resetIcon,HabitLogAdapter.UpdatePillCallback callback ) {
                Habit habit = habits.get(position);
                habitService.getProgressOfHabit(habit.getId(), new ServiceQueryCallback<HabitProgress>() {
                    @Override
                    public void onObjectsExist(List<HabitProgress> objects) {
                        Date today = new Date();
                        HabitProgress progressToUpdate = null;
                        for (HabitProgress progress: objects) {
                            if (DateUtils.isSameDay(progress.getDateLogged().toDate(), today)) {
                                progressToUpdate = progress;
                            }
                        }
                        if (progressToUpdate == null) {
                            Toast.makeText(getContext(), "Could not reset. Please try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Map<String, Object> updateObject = new HashMap<>();
                        updateObject.put("completed", false);
                        habitService.updateHabitProgress(habit.getId(), updateObject, new ServiceUpdateCallback() {
                            @Override
                            public void onUpdated() {
                                Toast.makeText(getContext(), "Removes today's log from habit.",
                                        Toast.LENGTH_SHORT).show();
                                checkIcon.setVisibility(View.VISIBLE);
                                resetIcon.setVisibility(View.INVISIBLE);
                                callback.updatePills();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(getContext(), "Could not reset. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, String.format("Failed fetching habit progress for %s", habit.getId()));
                    }
                });
            }

            @Override
            public void onCardLongPressed(int adapterPosition) {
                Habit habit = habits.get(adapterPosition);
                Intent intent = new Intent(getContext(), HabitDetailsActivity.class);
                intent.putExtra("habitId", habit.getId());
                startActivity(intent);
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void showActivityBox() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog by clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_new_activity);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);
        Button cancelActivityButton = dialog.findViewById(R.id.cancel_button);

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
        cancelActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void createNewSessionAndAdd(String activityName) {
        SessionServiceI sessionService = new SessionService(new SessionDao());
        LocalDate localDate = LocalDate.now();

        Session newSession = new Session(null,
                SharedPrefUtil.getHandleOfLoggedInUser(getContext()),
                new Timestamp(localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0),
                new Timestamp(localDate.plusDays(21).atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0),
                false);
        currentSession = newSession;
        sessionService.createSession(newSession,
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

    void updateCurrentDayofSession() {
        Log.d("HABIT SESSION", currentSession.getStartDate().toString());
        int days = DateUtil.getDaysBetween(currentSession.getStartDate().toDate(), new Date());
        currentDayTV.setText("DAY "+ String.valueOf(days) + " of 21");
    }

    private void hideDay() {
        currentDayTV.setText("Add a habit to begin");
    }
}