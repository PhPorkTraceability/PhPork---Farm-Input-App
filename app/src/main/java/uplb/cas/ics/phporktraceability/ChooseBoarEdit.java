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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/11/2015.
 */
public class ChooseBoarEdit extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    public final static String KEY_PARENTID = "parent_id";
    public final static String KEY_LABELID = "label_id";
    private static final String LOGCAT = ChooseBoarPage.class.getSimpleName();
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String boar_id;
    String sow_id;
    String foster_sow;
    String group_label;
    String breed;
    //String week_farrowed;
    String gender;
    String rfid;
    String pen;
    String feed_id;
    String med_id;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        db = SQLiteHandler.getInstance();

        loadLists();

        bl = (LinearLayout) findViewById(R.id.bottom_container);

        retrieveIntentExtra(getIntent());

        bl.setOnDragListener(this);
        //ll.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(ChooseBoarEdit.this, lists, lists2, lists3, ids);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                try {
                    int currentPage = position + 1;
                    if (currentPage == 1) {
                        iv_left.setVisibility(View.INVISIBLE);
                        iv_right.setVisibility(View.VISIBLE);
                    } else if (currentPage == lists.length) {

                        iv_left.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.INVISIBLE);
                    } else {
                        iv_left.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        iv_left = (ImageView)findViewById(R.id.iv_left);
        iv_right = (ImageView)findViewById(R.id.iv_right);

        checkList();

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = viewPager.getCurrentItem();
                viewPager.setCurrentItem(item - 1);
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = viewPager.getCurrentItem();
                viewPager.setCurrentItem(item + 1);

            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        String title = "Swipe to Choose a Boar Parent";
        tv_title.setText(title);
    }

    private Intent createIntent(Intent i) {
        i.putExtra("rfid", rfid);
        i.putExtra("boar_id", boar_id);
        i.putExtra("sow_id", sow_id);
        i.putExtra("foster_sow", foster_sow);
        i.putExtra("group_label", group_label);
        i.putExtra("breed", breed);
        //i.putExtra("week_farrowed", week_farrowed);
        i.putExtra("gender", gender);
        i.putExtra("feed_id", feed_id);
        i.putExtra("pen", pen);
        i.putExtra("med_id", med_id);

        return i;
    }

    private void retrieveIntentExtra(Intent i) {
        boar_id = i.getStringExtra("boar_id");
        sow_id = i.getStringExtra("sow_id");
        foster_sow = i.getStringExtra("foster_sow");
        group_label = i.getStringExtra("group_label");
        breed = i.getStringExtra("breed");
        //week_farrowed = i.getStringExtra("week_farrowed");
        gender = i.getStringExtra("gender");
        rfid = i.getStringExtra("rfid");
        pen = i.getStringExtra("pen");
        feed_id = i.getStringExtra("feed_id");
        med_id = i.getStringExtra("med_id");
    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
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
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(ChooseBoarEdit.this, AddThePig.class);
                createIntent(i);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String checkIfNull(String _value){
        String result = "";
        if(_value != null && !_value.isEmpty() && !_value.equals("null")) return _value;
        else return result;
    }

    private String getLabel(String _id){
        String result = "";

        if(!checkIfNull(_id).equals("")) {
            int size = _id.length();
            String s = "0";
            size = 6 - size;
            for (int i = 0; i < size; i++) {
                s = s + "0";
            }
            s = s + _id;
            String temp1 = s.substring(0, 2);
            String temp2 = s.substring(3, 7);
            result = temp1 + "-" + temp2;
        }
        return result;
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> boars = db.getBoars();
        //int size = boars.size();
        //if(size > 0) {

        lists = new String[boars.size()+1];
        lists2 = new String[boars.size()+1];
        lists3 = new String[boars.size()+1];
        ids = new String[boars.size()+1];

        lists[0] = "";
        lists2[0] = "---";
        lists3[0] = "";
        ids[0] = "";

        for (int i = 0; i < boars.size(); i++) {
            HashMap<String, String> c = boars.get(i);
            String boar_id = c.get(KEY_PARENTID);
            lists[i+1] = "Label ID: " + c.get(KEY_LABELID);
            lists2[i+1] = "Boar: " + getLabel(boar_id);
            lists3[i+1] = "";
            ids[i+1] = c.get(KEY_PARENTID);
        }
       /* } else{

            final int SPLASH_TIME_OUT = 1500;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No data available.")
                    .setMessage("Please update your database. Cannot proceed any further.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int id) {

                            new Handler().postDelayed(new Runnable() {

                            /*
                             * Showing splash screen with a timer. This will be useful when you
                             * want to show case your app logo / company
                             */
                        /*
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    Intent i = new Intent(ChooseBoarPage.this, eaningPage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                    dialog.cancel();
                                }
                            }, SPLASH_TIME_OUT);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }*/
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

                Log.d(LOGCAT, "Dropped " + boar_id);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Toast.makeText(ChooseBoarEdit.this, "Chosen Boar Parent: " +
                                    getLabel(boar_id),
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ChooseBoarEdit.this, AddThePig.class);
                    createIntent(i);
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
        Intent i = new Intent(this, AddThePig.class);
        createIntent(i);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
