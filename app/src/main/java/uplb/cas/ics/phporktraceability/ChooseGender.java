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

import helper.TestSessionManager;

/**
 * Created by marmagno on 11/11/2015.
 */
public class ChooseGender extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = ChooseGender.class.getSimpleName();
    String gender = "";
    String boar_id = "";
    String sow_id = "";
    String foster_sow = "";
    String group_label = "";
    String breed = "";
    private Toolbar toolbar;
    private ImageView iv_male;
    private ImageView iv_female;
    private LinearLayout top_cont;
    private LinearLayout bot_cont;
    //String week_farrowed = "";

//    TestSessionManager test;
//    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        test = new TestSessionManager(getApplicationContext());
//        HashMap<String, Integer> testuser = test.getCount();
//        count = testuser.get(TestSessionManager.KEY_COUNT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        boar_id = i.getStringExtra("boar_id");
        sow_id = i.getStringExtra("sow_id");
        foster_sow = i.getStringExtra("foster_sow");
        group_label = i.getStringExtra("group_label");
        breed = i.getStringExtra("breed");

        iv_male = (ImageView) findViewById(R.id.iv_male);
        iv_female = (ImageView) findViewById(R.id.iv_female);
        top_cont = (LinearLayout) findViewById(R.id.top_container);
        bot_cont = (LinearLayout) findViewById(R.id.bottom_container);

        iv_male.setOnTouchListener(this);
        iv_female.setOnTouchListener(this);
        //top_cont.setOnDragListener(this);
        bot_cont.setOnDragListener(this);

    }

     /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    } */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
//                count++;
//                test.updateCount(count);
                Intent i = new Intent(ChooseGender.this, ChooseBreedPage.class);
                i.putExtra("boar_id", boar_id);
                i.putExtra("sow_id", sow_id);
                i.putExtra("foster_sow", foster_sow);
                i.putExtra("group_label", group_label);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                gender = view.findViewById(id).getTag().toString();

                Log.d(LOGCAT, "Dropped " + gender);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Toast.makeText(ChooseGender.this, "Chosen " + gender,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ChooseGender.this, AssignRFIDPage.class);
                    i.putExtra("boar_id", boar_id);
                    i.putExtra("sow_id", sow_id);
                    i.putExtra("foster_sow", foster_sow);
                    i.putExtra("group_label", group_label);
                    i.putExtra("breed", breed);
                    i.putExtra("gender", gender);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
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
//        count++;
//        test.updateCount(count);
        Intent i = new Intent(ChooseGender.this, ChooseBreedPage.class);
        i.putExtra("boar_id", boar_id);
        i.putExtra("sow_id", sow_id);
        i.putExtra("foster_sow", foster_sow);
        i.putExtra("group_label", group_label);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
