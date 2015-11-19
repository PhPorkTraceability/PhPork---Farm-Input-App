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

import uplb.cas.ics.phporktraceability.HomeActivity;
import uplb.cas.ics.phporktraceability.WeaningPage;


public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "PHPorkLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_FUNC = "function";
    public static final String KEY_LOC = "loc_name";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(String function, String loc_name) {

        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        editor.putString(KEY_FUNC, function);

        editor.putString(KEY_LOC, loc_name);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        if(isLoggedIn() == true){
           if(KEY_FUNC.equals("weaning")){
               // After logout redirect user to Login Activity
               Intent i = new Intent(_context, WeaningPage.class);

               // Closing all the Activities
               //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

               // Add new Flag to start new Activity
               //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               // Starting Login Activity
               _context.startActivity(i);
           } else if(KEY_FUNC.equals("growing")){
               // After logout redirect user to Login Activity
               Intent i = new Intent(_context, WeaningPage.class);
               // Closing all the Activities
               //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

               // Add new Flag to start new Activity
               //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               // Starting Login Activity
               _context.startActivity(i);
           }
        }
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserSession(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_FUNC, pref.getString(KEY_FUNC, null));

        user.put(KEY_LOC, pref.getString(KEY_LOC, null));

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
        Intent i = new Intent(_context, HomeActivity.class);
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
}