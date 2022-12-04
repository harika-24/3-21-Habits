package edu.northeastern.a321habits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.northeastern.a321habits.databinding.ActivityMainBinding;
import edu.northeastern.a321habits.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private CardView activityCard;
    private final int[] icons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_groups_black_24dp,
    };
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        // set icons for tabs
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(icons[i]);
        }

//        activityCard = (CardView) findViewById(R.id.cardView1);
//
//        activityCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,Habit1.class));
//            }
//        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("onCreate", "menu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_progress) {

            // Do something

            Toast.makeText(this, "progress", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_item_settings) {

            // Do something
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_item_logout) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPref
                    .edit().remove(getString(R.string.saved_logged_in_handle_key)).commit();
            startActivity(new Intent(this, LoginActivity.class));
            // Do something
            Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}