package edu.northeastern.a321habits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.northeastern.a321habits.daos.FirestoreQueryCallback;
import edu.northeastern.a321habits.daos.user.UserDao;
import edu.northeastern.a321habits.daos.user.UserDaoI;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button loginButton;
    private TextView handleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialise();
    }

    private void initialise() {
        handleTextView = findViewById(R.id.LoginUsernameTextView);
        loginButton = findViewById(R.id.LoginButton);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultUserHandleString = getString(R.string.saved_default_handle);
        String loggedInUser = sharedPref
                .getString(getString(R.string.saved_logged_in_handle_key),
                        defaultUserHandleString);
        tryLogin(loggedInUser, new LoginCallback() {
            @Override
            public void onSuccessfulLogin() {
                goToMainActivity();
            }

            @Override
            public void onFailedLogin() {
                Log.d(TAG, "No valid handle in SharedPreferences.");
            }
        });

        loginButton.setOnClickListener(view -> {

            tryLogin(handleTextView.getText().toString(), new LoginCallback() {
                @Override
                public void onSuccessfulLogin() {
                    sharedPref
                            .edit()
                            .putString(getString(R.string.saved_logged_in_handle_key),
                                    handleTextView.getText().toString())
                            .apply();
                    goToMainActivity();
                }

                @Override
                public void onFailedLogin() {
                    Toast.makeText(getApplicationContext(),
                                    "Invalid user handle. You can try @jim.",
                                    Toast.LENGTH_LONG)
                            .show();
                }
            });
        });
    }

    private void tryLogin(String loggedInUser, LoginCallback loginCallback) {
        UserDaoI userDao = new UserDao();
        userDao.getUserByHandle(loggedInUser, new FirestoreQueryCallback() {
            @Override
            public void onQuerySucceeds(QuerySnapshot snapshot) {
                boolean userFound = false;
                for (QueryDocumentSnapshot document: snapshot) {
                    if (document.getString("handle") != null &&
                            document.getString("handle").equals(loggedInUser)) {
                        userFound = true;
                        break;
                    }
                }
                if (userFound) {
                    loginCallback.onSuccessfulLogin();
                } else {
                    loginCallback.onFailedLogin();
                }
            }

            @Override
            public void failure() {
                loginCallback.onFailedLogin();
            }
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}