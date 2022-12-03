

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
import android.widget.TextView;

import edu.northeastern.a321habits.Habit1;
import edu.northeastern.a321habits.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitLogFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentHabitLogBinding binding;
    private CardView activityCard1;
    private CardView activityCard2;
    private CardView activityCard3;
    private FloatingActionButton newActivityBtn;
    private TextView activity1Txt;
    private TextView activity2Txt;
    private TextView activity3Txt;
    private List<String> listOfHabits = new ArrayList<>();
    private int noOfHabits = 0;
    private List<CardView> listOfCards = new ArrayList<>();
    private List<TextView> listOfActivityText = new ArrayList<>();

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

        //populating listOfCardViews
        listOfCards.add(root.findViewById(R.id.cardView1));
        listOfCards.add(root.findViewById(R.id.cardView2));
        listOfCards.add(root.findViewById(R.id.cardView3));

        //populating listOfActivityText
        listOfActivityText.add(root.findViewById(R.id.textView));
        listOfActivityText.add(root.findViewById(R.id.textView2));
        listOfActivityText.add(root.findViewById(R.id.textView3));

        newActivityBtn = root.findViewById(R.id.floatingActionButton2);

//        activityCard1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), Habit1.class));
//            }
//        });

        newActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityBox();
            }
        });




        return root;
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

                //check if we already have 3 habits, then the user cannot add another one.

                //check if the activity is already entered
                if (listOfHabits.stream().anyMatch(habit -> activityName.toLowerCase(Locale.ROOT) == habit.toLowerCase(Locale.ROOT) ))
                {
                    //show that you are entering a duplicate
                    return;
                }
                noOfHabits++;
                listOfHabits.add(activityName);
                listOfActivityText.get(noOfHabits-1).setText(activityName);
                listOfCards.get(noOfHabits -1).setVisibility(View.VISIBLE);

                dialog.dismiss();

                if(noOfHabits == 3){
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