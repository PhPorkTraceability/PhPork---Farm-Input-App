package helper;

/**
 * Created by marmagno on 10/12/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

import uplb.cas.ics.phporktraceability.LoginActivity;


public class SessionManager {
    public static final String KEY_FUNC = "function";
    public static final String KEY_LOC = "loc_id";

    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERNAME = "user_name";
    public static final String KEY_PASSWORD = "password";

    // Shared preferences file name
    private static final String PREF_NAME = "PHPorkLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_LOC_LOGGEDIN = "locLoggedIn";

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }


    public void setLogin(String user_id, String username, String password) {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        editor.putString(KEY_USERID, user_id);

        editor.putString(KEY_USERNAME, username);

        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLocation(String function, String loc_id) {

        editor.putBoolean(KEY_LOC_LOGGEDIN, true);

        editor.putString(KEY_FUNC, function);

        editor.putString(KEY_LOC, loc_id);

        // commit changes
        editor.commit();

        Log.d(TAG, "User location session modified!");
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserLoc(){
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_FUNC, pref.getString(KEY_FUNC, null));

        user.put(KEY_LOC, pref.getString(KEY_LOC, null));

        // return user
        return user;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserSession(){
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Starting Login Activity
        _context.startActivity(i);

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean locLoggedIn() {
        return pref.getBoolean(KEY_LOC_LOGGEDIN, false);
    }
}