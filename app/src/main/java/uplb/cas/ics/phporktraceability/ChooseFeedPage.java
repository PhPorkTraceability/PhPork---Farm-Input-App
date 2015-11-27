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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/11/2015.
 */
public class ChooseFeedPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = ChooseFeedPage.class.getSimpleName();
    private Toolbar toolbar;

    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;

    SQLiteHandler db;
    String boar_id = "";

    public final static String KEY_PIGID = "pig_id";
    public final static String KEY_BREED = "breed_name";
    ArrayList<HashMap<String, String>> boar_list;
    String[] lists;
    String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseboar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        db = new SQLiteHandler(getApplicationContext());

        loadLists();

        bl = (LinearLayout) findViewById(R.id.bottom_container);
        ll = (LinearLayout) findViewById(R.id.bottom_container);

        bl.setOnDragListener(this);
        //ll.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(ChooseFeedPage.this, lists, ids);
        viewPager.setAdapter(adapter);

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
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                Intent i = new Intent(ChooseFeedPage.this, WeaningPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getLabel(String _id){
        String result = "";

        int size = _id.length();
        String s = "0";
        size = 6 - size;
        for(int i = 0; i < size;i++){
            s = s + "0";
        }
        s = s + _id;
        String temp1 = s.substring(0,2);
        String temp2 = s.substring(3,7);
        result = temp1 + "-" + temp2;
        return result;
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> boars = db.getBoars();

        boar_list = new ArrayList<>();
        lists = new String[boars.size()];
        ids = new String[boars.size()];
        for(int i = 0;i < boars.size();i++)
        {
            HashMap<String, String> c = boars.get(i);
            String boar_id = getLabel(c.get(KEY_PIGID));
            lists[i] = "Boar: " + boar_id + " -> " + c.get(KEY_BREED);
            ids[i] = c.get(KEY_PIGID);
            boar_list.add(c);
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
                boar_id = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Toast.makeText(ChooseFeedPage.this, "Chosen Boar Parent: " +
                                    getLabel(boar_id),
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ChooseFeedPage.this, ChooseSowPage.class);
                    i.putExtra("boar_id", boar_id);
                    startActivity(i);
                    finish();
                }

                Log.d(LOGCAT, "Dropped " + boar_id);
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
}
