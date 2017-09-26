package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import listeners.OnSwipeTouchListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 5/5/2016.
 */
public class ChooseViewHouse extends AppCompatActivity implements View.OnDragListener
{
    public final static String KEY_HOUSEID = "house_id";
    public final static String KEY_HOUSENO = "house_no";
    public final static String KEY_HOUSENAME = "house_name";
    public final static String KEY_FUNC = "function";
    private static final String LOGCAT = ChooseViewHouse.class.getSimpleName();
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String house_id = "";
    SessionManager session;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String location= "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                i.setClass(ChooseViewHouse.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        TextView pg_title = (TextView) toolbar.findViewById(R.id.page_title);
        pg_title.setText(R.string.function);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String > user = session.getUserLoc();
        location = user.get(SessionManager.KEY_LOC);

        db = SQLiteHandler.getInstance();

        loadLists();

        bl = (LinearLayout) findViewById(R.id.bottom_container);
        bl.setOnDragListener(this);
        bl.setOnTouchListener(new OnSwipeTouchListener(ChooseViewHouse.this) {
            @Override
            public void onSwipeLeft() {
                nextItem();
            }

            @Override
            public void onSwipeRight(){
                prevItem();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(ChooseViewHouse.this, lists, lists2, lists3, ids);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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
            public void onPageScrollStateChanged(int state) {

            }
        });

        iv_left = (ImageView)findViewById(R.id.iv_left);
        iv_right = (ImageView)findViewById(R.id.iv_right);

        checkList();

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevItem();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextItem();

            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        String title = "Swipe to Choose a House to View";
        tv_title.setText(title);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        if(fab != null) {
//            fab.setVisibility(View.VISIBLE);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    addHouse();
//                }
//            });
//        } else {
//            Log.e(LOGCAT, "fab is null.");
//        }
    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
        }
    }

    public void nextItem() {
        int item = viewPager.getCurrentItem();
        viewPager.setCurrentItem(item + 1);
    }

    public void prevItem() {
        int item = viewPager.getCurrentItem();
        viewPager.setCurrentItem(item - 1);
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> the_list = db.getHouses(location);

        if(the_list.size() > 0) {
            lists = new String[the_list.size()];
            lists2 = new String[the_list.size()];
            lists3 = new String[the_list.size()];
            ids = new String[the_list.size()];
            for (int i = 0; i < the_list.size(); i++) {
                HashMap<String, String> c = the_list.get(i);

                lists[i] = "House No: " + c.get(KEY_HOUSENO);
                lists2[i] = "House Name: " + c.get(KEY_HOUSENAME);
                lists3[i] = "Function: " + c.get(KEY_FUNC);
                ids[i] = c.get(KEY_HOUSEID);
            }
        } else {
            final int SPLASH_TIME_OUT = 2000;
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
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    Intent i = new Intent(ChooseViewHouse.this, ChooseModule.class);
                                    startActivity(i);
                                    finish();

                                    dialog.cancel();
                                }
                            }, SPLASH_TIME_OUT);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(ChooseViewHouse.this, ChooseModule.class);
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
                    Intent i = new Intent(ChooseViewHouse.this, ChooseViewPen.class);
                    i.putExtra("house_id", house_id);
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
        Intent i = new Intent(ChooseViewHouse.this, ChooseModule.class);
        startActivity(i);
        finish();
    }
    /*
    public void addHouse(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder verifier = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View subView = inflater.inflate(R.layout.add_house_fragment,null);
        final EditText et_housename = (EditText) subView.findViewById(R.id.et_house_name);
        final EditText et_houselocid = (EditText) subView.findViewById(R.id.et_house_loc_id);
        final EditText et_houseid = (EditText) subView.findViewById(R.id.et_house_id);
        final EditText et_houseno= (EditText) subView.findViewById(R.id.et_house_no);
        final EditText et_housefunc= (EditText) subView.findViewById(R.id.et_house_function);

        builder.setTitle("Add a House");
        builder.setView(subView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                verifier.setMessage("Are you sure all entries are correct?");
                verifier.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String house_loc_id = et_houselocid.getText().toString();
                        String house_id=et_houseid.getText().toString();
                        String house_name=et_housename.getText().toString();
                        String house_no=et_houseno.getText().toString();
                        String house_function=et_housefunc.getText().toString().trim().toLowerCase();
                        try{
                            Toast.makeText(ChooseViewHouse.this, "House Added", Toast.LENGTH_SHORT).show();

                            db.addHouse(house_id,house_no,house_name,house_function,house_loc_id);

                            refresh();
                        }
                        catch(Exception e){

                            Toast.makeText(ChooseViewHouse.this, "Error on inputs.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                verifier.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                    }
                });

                AlertDialog verify_prompt = verifier.create();
                verify_prompt.show();

            }



        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ChooseViewHouse.this, "Cancelled action", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog contact_prompt = builder.create();
        contact_prompt.show();


    }

    public void refresh() {
        loadLists();
        adapter = new CustomPagerAdapter(ChooseViewHouse.this, lists, lists2, lists3, ids);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        iv_left.setVisibility(View.INVISIBLE);
        iv_right.setVisibility(View.VISIBLE);
    }
    */
}

