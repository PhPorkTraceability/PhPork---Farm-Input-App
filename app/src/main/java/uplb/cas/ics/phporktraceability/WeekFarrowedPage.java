package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import helper.SessionManager;

/**
 * Created by marmagno on 11/11/2015.
 */
public class WeekFarrowedPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = WeekFarrowedPage.class.getSimpleName();

    LinearLayout ll;
    LinearLayout bl;
    TextView tv_rf11;
    TextView tv_rf18;
    TextView tv_rf19;

    SessionManager session;

    String function = "";
    String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        session = new SessionManager(getApplicationContext());

        Intent i = getIntent();
        function = i.getStringExtra("function");

        ll = (LinearLayout) findViewById(R.id.top_container);
        bl = (LinearLayout) findViewById(R.id.bottom_container);
        //ll.setOnDragListener(this);
        bl.setOnDragListener(this);

        tv_rf11 = (TextView) findViewById(R.id.tv_rf11);
        tv_rf18 = (TextView) findViewById(R.id.tv_rf18);
        tv_rf19 = (TextView) findViewById(R.id.tv_rf19);

        tv_rf11.setOnTouchListener(this);
        tv_rf18.setOnTouchListener(this);
        tv_rf19.setOnTouchListener(this);

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
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                if(findViewById(id) == tv_rf11){
                    location = tv_rf11.getText().toString();
                } else if(findViewById(id) == tv_rf18){
                    location = tv_rf18.getText().toString();
                } else if(findViewById(id) == tv_rf19){
                    location = tv_rf19.getText().toString();
                }

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    session.setLogin(function, location);
                    Intent i = new Intent(WeekFarrowedPage.this, WeaningPage.class);
                    startActivity(i);
                    finish();
                }

                Log.d(LOGCAT, "Dropped ");
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
}
