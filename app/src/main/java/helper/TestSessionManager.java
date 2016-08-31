package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by marmagno on 7/14/2016.
 */
public class TestSessionManager {

    public static final String KEY_COUNT = "count";
    public static final String KEY_ID = "id";

    // Shared preferences file name
    private static final String PREF_NAME = "PHPorkTest";
    private static final String KEY_IS_STARTED = "isStarted";

    // LogCat tag
    private static String TAG = TestSessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public TestSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void userStart(String _id) {

        editor.putBoolean(KEY_IS_STARTED, true);

        editor.putString(KEY_ID, _id);

        editor.putInt(KEY_COUNT, 0);

        // commit changes
        editor.commit();

        Log.d(TAG, "Started test session");
    }

    /**
     * Update count per back
     */
    public void updateCount(int _count){
        if(isLoggedIn()){
            editor.putInt(KEY_COUNT, _count);

            // commit changes
            editor.commit();

            Log.d(TAG, "Updated count");
        }
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, Integer> getCount(){
        HashMap<String, Integer> user = new HashMap<>();

        user.put(KEY_COUNT, pref.getInt(KEY_COUNT, 0));

        // return user

        return user;
    }

    public HashMap<String, String> getID(){
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_ID, pref.getString(KEY_ID, null));

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

        Log.d(TAG, "Logout user " + editor.toString());

    }
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_STARTED, false);
    }
}
