package edu.northeastern.a321habits.ui.main;

import android.app.Dialog;
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

import edu.northeastern.a321habits.Habit1;
import edu.northeastern.a321habits.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;
import edu.northeastern.a321habits.model.HabitLogAdapter;
import edu.northeastern.a321habits.model.HabitLogModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitLogFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private ImageView deleteIcon;
    private FragmentHabitLogBinding binding;
    private FloatingActionButton newActivityBtn;
    private HabitLogAdapter adapter;
//    private List<String> listOfHabits = new ArrayList<>();
//    private int noOfHabits = 0;
//    private List<CardView> listOfCards = new ArrayList<>();
//    private List<TextView> listOfActivityText = new ArrayList<>();

    private RecyclerView recyclerView;
    private ArrayList<HabitLogModel> habitList = new ArrayList<>();

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

        setAdapter();
        return root;
    }

    private void setAdapter() {
        adapter = new HabitLogAdapter(binding.getRoot().getContext(),habitList);
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