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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by marmagno on 11/11/2015.
 */
public class WeekFarrowedPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = WeekFarrowedPage.class.getSimpleName();
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_curweek;
    TextView tv_week1;
    TextView tv_week2;
    String weekf = "";

    /*
    ImageView iv_weekf1;
    ImageView iv_weekf2;
    ImageView iv_weekf3;
    */
    String boar_id = "";
    String sow_id = "";
    String foster_sow = "";
    String breed = "";
    String group_label = "";
    DateFormat curDate = new SimpleDateFormat(" MMM yyyy");
    Date dateObj = new Date();
    Calendar now = Calendar.getInstance();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekfarrowed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        boar_id = i.getStringExtra("boar_id");
        sow_id = i.getStringExtra("sow_id");
        foster_sow = i.getStringExtra("foster_sow");
        breed = i.getStringExtra("breed");
        group_label = i.getStringExtra("group_label");

        int curweek = now.get(Calendar.WEEK_OF_MONTH);
        String disp1 = "Week " + curweek + curDate.format(dateObj);
        String disp2 = displayDate();
        String disp3 = displayDate();

        ll = (LinearLayout) findViewById(R.id.top_container);
        bl = (LinearLayout) findViewById(R.id.bottom_container);
        //ll.setOnDragListener(this);
        bl.setOnDragListener(this);

        tv_curweek = (TextView) findViewById(R.id.tv_curweek);
        tv_week1 = (TextView) findViewById(R.id.tv_week1);
        tv_week2 = (TextView) findViewById(R.id.tv_week2);

        tv_curweek.setText(disp1);
        tv_week1.setText(disp2);
        tv_week2.setText(disp3);

        tv_curweek.setTag(disp1);
        tv_week1.setTag(disp2);
        tv_week2.setTag(disp3);

        tv_curweek.setOnTouchListener(this);
        tv_week1.setOnTouchListener(this);
        tv_week2.setOnTouchListener(this);

        /*
        iv_weekf1 = (ImageView) findViewById(R.id.iv_wf1);
        iv_weekf2 = (ImageView) findViewById(R.id.iv_wf2);
        iv_weekf3 = (ImageView) findViewById(R.id.iv_wf3);

        iv_weekf1.setOnTouchListener(this);
        iv_weekf2.setOnTouchListener(this);
        iv_weekf3.setOnTouchListener(this);
        */


    }

    public String displayDate(){
        String[] months = {"Jan ", "Feb ", "Mar ", "Apr ", "May ", "Jun ",
                "July ", "Aug ", "Sep ", "Oct ", "Nov ", "Dec "};
        String display = "";

        now.add(Calendar.WEEK_OF_MONTH, -1);
        int _week = now.get(Calendar.WEEK_OF_MONTH);
        int _month = now.get(Calendar.MONTH);
        int _year = now.get(Calendar.YEAR);
        display = "Week " + _week + " " + months[_month] + _year;

        return display;
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
                Intent i = new Intent(WeekFarrowedPage.this, ChooseBreedPage.class);
                i.putExtra("boar_id", boar_id);
                i.putExtra("sow_id", sow_id);
                i.putExtra("foster_sow", foster_sow);
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
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                weekf = view.findViewById(id).getTag().toString();

                Log.d(LOGCAT, "Dropped " + weekf);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Toast.makeText(WeekFarrowedPage.this, "Chosen " + weekf,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(WeekFarrowedPage.this, ChooseGender.class);
                    i.putExtra("boar_id", boar_id);
                    i.putExtra("sow_id", sow_id);
                    i.putExtra("foster_sow", foster_sow);
                    i.putExtra("breed", breed);
                    i.putExtra("week_farrowed", weekf);
                    i.putExtra("group_label", group_label);
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
        Intent i = new Intent(WeekFarrowedPage.this, ChooseBreedPage.class);
        i.putExtra("boar_id", boar_id);
        i.putExtra("sow_id", sow_id);
        i.putExtra("foster_sow", foster_sow);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
