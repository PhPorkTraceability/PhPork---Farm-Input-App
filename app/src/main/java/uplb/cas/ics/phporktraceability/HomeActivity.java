package uplb.cas.ics.phporktraceability;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import helper.SQLiteHandler;
import helper.SessionManager;
import helper.TestSessionManager;

public class HomeActivity extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener {

    private static final String LOGCAT = HomeActivity.class.getSimpleName();
    String function = "";
    SessionManager session;
    SQLiteHandler db;
    private Toolbar toolbar;
    private ImageView iv_weaning;
    private ImageView iv_growing;
    private LinearLayout bot_cont;

    /**
     * For Testing only
     * Delete when done
     */
    TestSessionManager test;
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();
    int testID;
    String time;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();

        time = curTime.format(dateObj);

        test = new TestSessionManager(getApplicationContext());

        session = new SessionManager(getApplicationContext());
        //session.checkLogin();

        /* Adding the navdrawer
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        iv_weaning = (ImageView) findViewById(R.id.iv_weaning);
        iv_growing = (ImageView) findViewById(R.id.iv_growing);
        bot_cont = (LinearLayout) findViewById(R.id.bottom_container);

        iv_weaning.setOnTouchListener(this);
        iv_growing.setOnTouchListener(this);
        bot_cont.setOnDragListener(this);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = getIntent();
                finish();
                startActivity(i);
                return true;
            case R.id.action_export:
                exportOffline();
                return true;
           /* case R.id.action_logout:
                test.logoutUser();
                session.logoutUser();
                finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        int action = e.getAction();
        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(LOGCAT, "Drag event started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(LOGCAT, "Drag event entered into "+ v.toString());
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(LOGCAT, "Drag event exited from " + v.toString());
                break;
            case DragEvent.ACTION_DROP:
                TextView tv_drag = (TextView) findViewById(R.id.tv_dragHere);
                TextView tv_subs = (TextView) findViewById(R.id.tv_subs);
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
                tv_subs.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                function = findViewById(id).getTag().toString();
                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)) {

                    Log.d(LOGCAT, "Logged in value: " + test.isLoggedIn());

                    if (!test.isLoggedIn()){
                        testID = db.getMaxTestID() + 1;
                        test.userStart(String.valueOf(testID));
                        db.insertNewTest(String.valueOf(testID), time);
                    }

                    Toast.makeText(HomeActivity.this, "Chosen " + function.toUpperCase(),
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(HomeActivity.this, LocationPage.class);
                    i.putExtra("function", function);
                    startActivity(i);
                    finish();
                }
                Log.d(LOGCAT, "Dropped " + function);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOGCAT, "Drag ended");
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
        else { return false; }
    }

    @Override
    public void onBackPressed(){super.onBackPressed(); finish(); }

    public void exportOffline() {
        /* code from: http://stackoverflow.com/questions/23527767/ */
        boolean hasPermissionWrite =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
        boolean hasPermissionRead =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;

        if (!(hasPermissionWrite || hasPermissionRead)) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            db.exportTest(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    db.exportTest(this);
                }
                return;
            }
        }
    }
}
