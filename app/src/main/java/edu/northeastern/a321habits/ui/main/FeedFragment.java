package edu.northeastern.a321habits.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a321habits.DateUtil;
import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.daos.habit.HabitDao;
import edu.northeastern.a321habits.databinding.FragmentFeedBinding;
import edu.northeastern.a321habits.model.FeedAdapter;
import edu.northeastern.a321habits.model.HabitLogModel;
import edu.northeastern.a321habits.model.UserModel;
import edu.northeastern.a321habits.models.habit.HabitProgress;
import edu.northeastern.a321habits.models.user.User;
import edu.northeastern.a321habits.services.ServiceGetCallback;
import edu.northeastern.a321habits.services.ServiceQueryCallback;
import edu.northeastern.a321habits.services.ServiceQueryPaginatedCallback;
import edu.northeastern.a321habits.services.habit.HabitService;
import edu.northeastern.a321habits.services.habit.HabitServiceI;
import edu.northeastern.a321habits.services.user.UserService;
import edu.northeastern.a321habits.services.user.UserServiceI;

/**
 * Fragment for showing habit logs of other users. Loads on scrolling.
 */
public class FeedFragment extends Fragment {

    int count = 0;
    private PageViewModel pageViewModel;
    private FragmentFeedBinding binding;

    private ArrayList<HabitLogModel> habitLogArrayList;
    private RecyclerView feedRV;
    private FeedAdapter feedRVAdapter;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private DocumentSnapshot lastVisible = null;

    public static FeedFragment newInstance(int index) {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        feedRV = root.findViewById(R.id.id_rv_habit_log);
        loadingPB = root.findViewById(R.id.idPBLoading);
        nestedSV = root.findViewById(R.id.idNestedSV);

        habitLogArrayList = new ArrayList<>();

        feedRV.setLayoutManager(new LinearLayoutManager(root.getContext()));
        
        getData(root);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    count++;
                    // on below line we are making our progress bar visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    if (count < 10) {
                        // on below line we are again calling
                        // a method to load data in our array list.
                        getData(root);
                    }
                }
            }
        });
        return root;
    }


    private void getData(View root) {

        feedRV.setVisibility(View.VISIBLE);

        HabitServiceI habitService = new HabitService(new HabitDao());
        habitService.findHabitProgressOfOthers(getLoggedInUser(), lastVisible, new ServiceQueryPaginatedCallback<HabitProgress>() {
            @Override
            public void onObjectsExistPaginate(List<HabitProgress> objects, DocumentSnapshot lastVisibleSnapshot) {

                if (lastVisibleSnapshot == null) {
                    return;
                }

                for (HabitProgress habitProgress: objects) {

                    UserModel user = new UserModel(habitProgress.getUserId(), "abc");
                    String logDateTime = DateUtil.formatDateTime(habitProgress.getDateLogged().toDate());
                    habitLogArrayList.add(new HabitLogModel(habitProgress.getName(),
                            habitProgress.getPhotoUrl(), logDateTime, habitProgress.getLogDay(), user));

                }

                feedRVAdapter = new FeedAdapter(root.getContext(), habitLogArrayList);
                feedRV.setAdapter(feedRVAdapter);
                lastVisible = lastVisibleSnapshot;
            }


            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Could not load the feed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private String getLoggedInUser() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultUserHandleString = getString(R.string.saved_default_handle);
        return sharedPref
                .getString(getString(R.string.saved_logged_in_handle_key),
                        defaultUserHandleString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}