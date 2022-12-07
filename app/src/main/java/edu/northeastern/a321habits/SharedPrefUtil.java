package edu.northeastern.a321habits;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import edu.northeastern.a321habits.models.session.Session;

public class SharedPrefUtil {
    public static String getHandleOfLoggedInUser(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultUserHandleString = context.getString(R.string.saved_default_handle);
        return sharedPref
                .getString(context.getString(R.string.saved_logged_in_handle_key),
                        defaultUserHandleString);
    }

    public static void addCurrentSession(String sessionId, Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref
                .edit()
                .putString(context.getString(R.string.saved_session_id_key),sessionId)
                .apply();

    }
    public static void removeCurrentSession(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref
                .edit().remove(context.getString(R.string.saved_session_id_key)).commit();
    }

    public static String getCurrentSession(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref
                .getString(context.getString(R.string.saved_session_id_key),
                        "");
    }

    public static void clearAll(Context applicationContext) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        sharedPref.edit().clear().commit();
    }
}
