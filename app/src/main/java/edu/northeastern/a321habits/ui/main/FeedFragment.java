package edu.northeastern.a321habits.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.a321habits.R;
import edu.northeastern.a321habits.databinding.FragmentFeedBinding;
import edu.northeastern.a321habits.model.FeedAdapter;
import edu.northeastern.a321habits.model.HabitLogModel;
import edu.northeastern.a321habits.model.UserModel;

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

//        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // on scroll change we are checking when users scroll as bottom.
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    count++;
//                    // on below line we are making our progress bar visible.
//                    loadingPB.setVisibility(View.VISIBLE);
//                    if (count < 20) {
//                        // on below line we are again calling
//                        // a method to load data in our array list.
//                        getData(root);
//                    }
//                }
//            }
//        });
        getData(root);
        return root;
    }


    private void getData(View root) {
        // todo: connect to database (this need not be a new connection)
        // todo: get all habit logs other than logged in users
        // todo: serialize response data into HabitLogModel objects
        // todo:
        feedRV.setVisibility(View.VISIBLE);
        String imageUrl = "https://i.imgur.com/tGbaZCY.jpg";
        UserModel user = new UserModel("SWATI", "AGARWAL", imageUrl);
        habitLogArrayList.add(new HabitLogModel("Read 10 pages", "", "12/2 7:00PM", 7, user));
        habitLogArrayList.add(new HabitLogModel("Read 10 pages", imageUrl, "12/2 7:00PM", 7, user));
        habitLogArrayList.add(new HabitLogModel("Read 10 pages", "", "12/2 7:00PM", 7, user));
        habitLogArrayList.add(new HabitLogModel("Read 10 pages", imageUrl, "12/2 7:00PM", 7, user));
        habitLogArrayList.add(new HabitLogModel("Read 10 pages", "", "12/2 7:00PM", 7, user));

        // on below line we are adding our array list to our adapter class.
        feedRVAdapter = new FeedAdapter(root.getContext(), habitLogArrayList);

        // on below line we are setting
        // adapter to our recycler view.
        feedRV.setAdapter(feedRVAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}