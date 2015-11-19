package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/11/2015.
 */
public class AssignRFIDPage extends AppCompatActivity implements View.OnDragListener
{
    private static final String LOGCAT = AssignRFIDPage.class.getSimpleName();
    private Toolbar toolbar;
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;

    SQLiteHandler db;
    String rfid = "";
    String gender = "";
    String boar_id = "";
    String sow_id = "";
    String group_label = "";
    String breed = "";
    String week_farrowed = "";

    public final static String KEY_TAGID = "tag_id";
    public final static String KEY_TAGRFID = "tag_rfid";
    ArrayList<HashMap<String, String>> rfid_list;
    String[] lists;
    String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignrfid);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        boar_id = i.getStringExtra("boar_id");
        sow_id = i.getStringExtra("sow_id");
        group_label = i.getStringExtra("group_label");
        breed = i.getStringExtra("breed");
        week_farrowed = i.getStringExtra("week_farrowed");
        gender = i.getStringExtra("gender");

        db = new SQLiteHandler(getApplicationContext());

        loadRFIDSBySQL();

        bl = (LinearLayout) findViewById(R.id.bottom_container);
        ll = (LinearLayout) findViewById(R.id.bottom_container);

        bl.setOnDragListener(this);
        //ll.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(AssignRFIDPage.this, lists, ids);
        viewPager.setAdapter(adapter);
    }

    public void loadRFIDSBySQL(){

        ArrayList<HashMap<String, String>> rfids = db.getInactiveTags();

        rfid_list = new ArrayList<>();
        lists = new String[rfids.size()];
        ids = new String[rfids.size()];
        for(int i = 0;i < rfids.size();i++)
        {
            HashMap<String, String> c = rfids.get(i);

            lists[i] = c.get(KEY_TAGRFID);
            ids[i] = c.get(KEY_TAGID);

            rfid_list.add(c);
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                //view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                rfid = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Intent i = new Intent(AssignRFIDPage.this, AssignPenPage.class);
                    i.putExtra("rfid", rfid);
                    i.putExtra("boar_id", boar_id);
                    i.putExtra("sow_id", sow_id);
                    i.putExtra("group_label", group_label);
                    i.putExtra("breed", breed);
                    i.putExtra("week_farrowed", week_farrowed);
                    i.putExtra("gender", gender);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

                Log.d(LOGCAT, "Dropped " + rfid);
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
    public void onBackPressed(){super.onBackPressed(); finish(); }
}
