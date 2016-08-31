package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

import java.util.HashMap;

import helper.SessionManager;

/**
 * Created by marmagno on 11/25/2015.
 */
public class GrowingPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = GrowingPage.class.getSimpleName();
    SessionManager session;
    String farm_funct = "";
    String function = "";
    private Toolbar toolbar;
    private ImageView iv_addfeed;
    private ImageView iv_addmeds;
    private ImageView iv_viewpig;
    private LinearLayout bot_cont;
    private LinearLayout text_cont;
    private LinearLayout top_cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String > user = session.getUserLoc();
        farm_funct = user.get(SessionManager.KEY_FUNC);

        iv_addfeed = (ImageView) findViewById(R.id.iv_addfeed);
        iv_addmeds = (ImageView) findViewById(R.id.iv_addmeds);
        iv_viewpig = (ImageView) findViewById(R.id.iv_viewpig);
        text_cont = (LinearLayout) findViewById(R.id.text_container);
        bot_cont = (LinearLayout) findViewById(R.id.bottom_container);

        iv_addfeed.setOnTouchListener(this);
        iv_addmeds.setOnTouchListener(this);
        iv_viewpig.setOnTouchListener(this);
        //top_cont.setOnDragListener(this);
        bot_cont.setOnDragListener(this);

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
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(GrowingPage.this, LocationPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("function", farm_funct);
                startActivity(i);
                finish();
                return true;
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
                //TextView tv_subs = (TextView) findViewById(R.id.tv_subs);
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
               // tv_subs.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                function = findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    if(function.equals("addfeed")) {
                        TextView tv_rem = (TextView) findViewById(R.id.tv_cf);
                        text_cont.removeView(tv_rem);
                        tv_rem.invalidate();
                        Toast.makeText(GrowingPage.this, "Chosen " + function.toUpperCase(),
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(GrowingPage.this, ChooseFeedPage.class);
                        startActivity(i);
                        finish();
                    } else if(function.equals("addmeds")) {
                        TextView tv_rem = (TextView) findViewById(R.id.tv_cam);
                        text_cont.removeView(tv_rem);
                        tv_rem.invalidate();
                        Toast.makeText(GrowingPage.this, "Chosen " + function.toUpperCase(),
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(GrowingPage.this, ChooseMedPage.class);
                        startActivity(i);
                        finish();
                    } else if(function.equals("viewpig")) {
                        TextView tv_rem = (TextView) findViewById(R.id.tv_cvp);
                        text_cont.removeView(tv_rem);
                        tv_rem.invalidate();
                        Toast.makeText(GrowingPage.this, "Chosen " + function.toUpperCase(),
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(GrowingPage.this, ViewListOfPigs.class);
                        startActivity(i);
                        finish();
                    }

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
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(GrowingPage.this, LocationPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("function", function);
        startActivity(i);
        finish();
    }
}
