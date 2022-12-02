

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

import edu.northeastern.a321habits.Habit1;
import edu.northeastern.a321habits.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.a321habits.databinding.FragmentHabitLogBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitLogFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentHabitLogBinding binding;
    private CardView activityCard1;
    private FloatingActionButton newActivity;

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
        activityCard1 = root.findViewById(R.id.cardView1);
        newActivity = root.findViewById(R.id.floatingActionButton2);

        activityCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Habit1.class));
            }
        });

        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityBox();
            }
        });

//        final TextView textView = binding.sectionLabel;
//        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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
                dialog.dismiss();
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