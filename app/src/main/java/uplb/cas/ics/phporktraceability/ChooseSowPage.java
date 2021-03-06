package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/11/2015.
 */
public class ChooseSowPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    public final static String KEY_PARENTID= "parent_id";
    public final static String KEY_LABELID = "label_id";
    private static final String LOGCAT = ChooseSowPage.class.getSimpleName();
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String sow_id = "";
    String boar_id = "";
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};

//    TestSessionManager test;
//    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        test = new TestSessionManager(getApplicationContext());
//        HashMap<String, Integer> testuser = test.getCount();
//        count = testuser.get(TestSessionManager.KEY_COUNT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton home = (ImageButton) toolbar.findViewById(R.id.home_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ChooseSowPage.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        TextView pg_title = (TextView) toolbar.findViewById(R.id.page_title);
        pg_title.setText(R.string.sow_parent);

        Intent i = getIntent();
        boar_id = i.getStringExtra("boar_id");

        db = SQLiteHandler.getInstance();

        loadLists();

        bl = (LinearLayout) findViewById(R.id.bottom_container);

        bl.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(ChooseSowPage.this, lists, lists2, lists3, ids);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    // Log.i("View Pager", "page selected " + position);

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
            public void onPageScrollStateChanged(int state) {

            }
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
        String title = "Swipe to Choose a Sow Parent";
        tv_title.setText(title);
    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
        }

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }

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
                Intent i = new Intent(ChooseSowPage.this, ChooseBoarPage.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private String checkIfNull(String _value){
//        String result = "";
//        if(_value != null && !_value.isEmpty() && !_value.equals("null")) return _value;
//        else return result;
//    }
//
//    private String getLabel(String _id){
//        String result = "";
//
//        if(!checkIfNull(_id).equals("")) {
//            int size = _id.length();
//            String s = "0";
//            size = 6 - size;
//            for (int i = 0; i < size; i++) {
//                s = s + "0";
//            }
//            s = s + _id;
//            String temp1 = s.substring(0, 2);
//            String temp2 = s.substring(3, 7);
//            result = temp1 + "-" + temp2;
//        }
//        return result;
//    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> sows = db.getSows();

        lists = new String[sows.size() + 1];
        lists2 = new String[sows.size() + 1];
        lists3 = new String[sows.size() + 1];
        ids = new String[sows.size() + 1];

        lists[0] = "";
        lists2[0] = "---";
        lists3[0] = "";
        ids[0] = "";

        if(sows.size() > 0) {

            for (int i = 0; i < sows.size(); i++) {
                HashMap<String, String> c = sows.get(i);
                String sow_id = c.get(KEY_PARENTID);
                lists[i + 1] = "Label ID: " + c.get(KEY_LABELID);
                lists2[i + 1] = "Sow: " + sow_id;
                lists3[i + 1] = "";
                ids[i + 1] = c.get(KEY_PARENTID);
            }
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No sows available.")
                    .setMessage("Please update your database.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
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
                sow_id = view.findViewById(id).getTag().toString();

                Log.d(LOGCAT, "Dropped " + sow_id);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Intent i = new Intent(ChooseSowPage.this, ChooseFosterSowPage.class);
                    i.putExtra("boar_id", boar_id);
                    i.putExtra("sow_id", sow_id);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(null, shadowBuilder, v, 0);
            } else
                v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
        else { return false; }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(ChooseSowPage.this, ChooseBoarPage.class);
        startActivity(i);
        finish();

    }
}
