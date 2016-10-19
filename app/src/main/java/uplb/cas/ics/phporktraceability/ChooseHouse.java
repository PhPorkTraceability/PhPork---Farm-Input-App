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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import helper.TestSessionManager;

/**
 * Created by marmagno on 7/26/2016.
 */
public class ChooseHouse extends AppCompatActivity implements View.OnDragListener {

    public final static String KEY_HOUSEID = "house_id";
    public final static String KEY_HOUSENO = "house_no";
    public final static String KEY_HOUSENAME = "house_name";
    public final static String KEY_FUNC = "function";
    private static final String FEED_MOD = "Feed Pig";
    private static final String SEL_PIG = "by_pig";
    private static final String SEL_PEN = "by_pen";

    private static final String LOGCAT = ChooseHouse.class.getSimpleName();

    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String house_id = "";
    String med_id = "";
    String feed_id = "";
    SessionManager session;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String location= "";
    private Toolbar toolbar;

    String selection = "";
    String module = "";

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String > user = session.getUserLoc();
        location = user.get(SessionManager.KEY_LOC);

        Intent i = getIntent();
        module = i.getStringExtra("module");
        selection = i.getStringExtra("selection");
        if(module.equals(FEED_MOD)) {
            getSupportActionBar().setTitle(R.string.feed);
            feed_id = i.getStringExtra("feed_id");
        }
       else{
            getSupportActionBar().setTitle(R.string.med);
            med_id = i.getStringExtra("med_id");
        }

        db = SQLiteHandler.getInstance();

        loadLists();

        ll = (LinearLayout) findViewById(R.id.top_container);
        bl = (LinearLayout) findViewById(R.id.bottom_container);
        //ll.setOnDragListener(this);
        bl.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setOnDragListener(this);
        adapter = new CustomPagerAdapter(ChooseHouse.this, lists, lists2, lists3, ids);
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
        String title = "Swipe to Choose a House";
        tv_title.setText(title);
    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
        }

    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> the_list = db.getHouses(location);

        lists = new String[the_list.size()];
        lists2 = new String[the_list.size()];
        lists3 = new String[the_list.size()];
        ids = new String[the_list.size()];
        for(int i = 0;i < the_list.size();i++)
        {
            HashMap<String, String> c = the_list.get(i);

            lists[i] = "House: " + c.get(KEY_HOUSENO);
            lists2[i] = "House Name: " + c.get(KEY_HOUSENAME);
            lists3[i] = "Function: " + c.get(KEY_FUNC);
            ids[i] = c.get(KEY_HOUSEID);

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
//                count++;
//                test.updateCount(count);

                Intent i = new Intent();
                if(module.equals(FEED_MOD))
                    i.setClass(ChooseHouse.this, ChooseFeedPage.class);
                else
                    i.setClass(ChooseHouse.this, ChooseMedPage.class);

                i.putExtra("selection", selection);
                i.putExtra("module", module);
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
                house_id = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Intent i = new Intent();

                    if(selection.equals(SEL_PIG)) {
                        i.setClass(ChooseHouse.this, ChoosePen.class);
                        if(module.equals(FEED_MOD))
                            i.putExtra("feed_id", feed_id);
                        else
                            i.putExtra("med_id", med_id);
                    } else if(selection.equals(SEL_PEN)) {
                        i.setClass(ChooseHouse.this, ChooseByPen.class);
                        if(module.equals(FEED_MOD))
                            i.putExtra("feed_id", feed_id);
                        else
                            i.putExtra("med_id", med_id);
                    }

                    i.putExtra("house_id", house_id);
                    i.putExtra("selection", selection);
                    i.putExtra("module", module);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

                Log.d(LOGCAT, "Dropped " + house_id);
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
    public void onBackPressed(){
        super.onBackPressed();
//        count++;
//        test.updateCount(count);

        Intent i = new Intent();
        if(module.equals(FEED_MOD))
            i.setClass(ChooseHouse.this, ChooseFeedPage.class);
        else
            i.setClass(ChooseHouse.this, ChooseMedPage.class);

        i.putExtra("selection", selection);
        i.putExtra("module", module);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
