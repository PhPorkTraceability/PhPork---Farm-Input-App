package uplb.cas.ics.phporktraceability;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 8/12/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String KEY_USERID = "user_id";
    private static final String KEY_USERNAME = "user_name";

    private ArrayList<HashMap<String, String>> userlist;
    private Dialog loginD = null;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();
        session = new SessionManager(getApplicationContext());

        loginD = new Dialog(LoginActivity.this);
        loginD.setContentView(R.layout.login_page);
        loginD.setCancelable(false);

        ListView lv = (ListView) loginD.findViewById(R.id.listview);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> user = userlist.get(position);
                final String username = user.get(KEY_USERNAME);
                final String password = db.getPassword(user.get(KEY_USERID), username);
                final String user_id = user.get(KEY_USERID);

                session.setLogin(user_id, username, password);
                Intent i = new Intent(LoginActivity.this, IntroSliderActivity.class);
                startActivity(i);
                finish();

            }
        });

        loadList();

        ListAdapter adapter = new SimpleAdapter(
                LoginActivity.this,
                userlist,
                R.layout.user_model, new String[] {KEY_USERID, KEY_USERNAME },
                new int[] { R.id.tv_userid, R.id.tv_username});

        lv.setAdapter(adapter);

        loginD.show();

    }

    public void loadList(){

        ArrayList<HashMap<String, String>> user = db.getUsers();
        userlist = new ArrayList<>();

        for(int i = 0;i < user.size();i++){
            HashMap<String, String> a = user.get(i);
            HashMap<String, String> b = new HashMap<>();

            b.put(KEY_USERID, a.get(KEY_USERID));
            b.put(KEY_USERNAME, a.get(KEY_USERNAME));

            userlist.add(b);

        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        if(loginD.isShowing() || loginD != null)
            loginD.dismiss();
    }
}
