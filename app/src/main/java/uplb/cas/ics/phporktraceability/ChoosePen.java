package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import helper.TestSessionManager;

/**
 * Created by marmagno on 7/26/2016.
 */
public class ChoosePen extends AppCompatActivity implements View.OnDragListener {

    private static final String LOGCAT = ChoosePen.class.getSimpleName();

    public final static String KEY_PENID = "pen_id";
    public final static String KEY_PENNO = "pen_no";
    public final static String KEY_FUNC = "function";
    public final static String KEY_GNAME = "group_name";
    private static final String FEED_MOD = "Feed Pig";

    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String pen = "";
    String house_id = "";
    String feed_id = "";
    String med_id = "";
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    private Toolbar toolbar;

    String selection = "selection";
    String module = "module";

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

        Intent i = getIntent();
        house_id = i.getStringExtra("house_id");
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");

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
        adapter = new CustomPagerAdapter(ChoosePen.this, lists, lists2, lists3, ids);
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
        String title = "Swipe to Choose a Pen";
        tv_title.setText(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_pen();
                }
            });
        } else {
            Log.e(LOGCAT, "fab is null.");
        }

    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
        }

    }

    public void loadLists(){
        ArrayList<HashMap<String, String>> the_list = db.getPensByHouse(house_id);

        lists = new String[the_list.size()];
        lists2 = new String[the_list.size()];
        lists3 = new String[the_list.size()];
        ids = new String[the_list.size()];

        for(int i = 0;i < the_list.size();i++) {
            HashMap<String, String> c = the_list.get(i);

            lists[i] = "Pen: " + c.get(KEY_PENNO);
            lists2[i] = "Function: " + c.get(KEY_FUNC);
            lists3[i] = "";
            ids[i] = c.get(KEY_PENID);
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

                Intent i = new Intent(ChoosePen.this, ChooseHouse.class);
                if(module.equals(FEED_MOD))
                    i.putExtra("feed_id", feed_id);
                else
                    i.putExtra("med_id", med_id);
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
                pen = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    /*
                    Toast.makeText(ChooseFeedPenPage.this, "Chosen " + pen,
                            Toast.LENGTH_LONG).show(); */

                    Intent i = new Intent(ChoosePen.this, ChooseByPig.class);
                    if(module.equals(FEED_MOD))
                        i.putExtra("feed_id", feed_id);
                    else
                        i.putExtra("med_id", med_id);

                    i.putExtra("pen", pen);
                    i.putExtra("house_id", house_id);
                    i.putExtra("selection", selection);
                    i.putExtra("module", module);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

                Log.d(LOGCAT, "Dropped " + pen);
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
        Intent i = new Intent(ChoosePen.this, ChooseHouse.class);
        if(module.equals(FEED_MOD))
            i.putExtra("feed_id", feed_id);
        else
            i.putExtra("med_id", med_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public void add_pen(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder verifier = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View subView = inflater.inflate(R.layout.add_pen_fragment,null);

        final EditText et_penid = (EditText) subView.findViewById(R.id.et_pen_id);
        final EditText et_penno= (EditText) subView.findViewById(R.id.et_pen_no);
        final EditText et_penfunc= (EditText) subView.findViewById(R.id.et_pen_function);
        final EditText et_penhouseid= (EditText) subView.findViewById(R.id.et_pen_house_id);

        builder.setTitle("Add a House");
        builder.setView(subView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                verifier.setMessage("Are you sure all entries are correct?");
                verifier.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pen_id=et_penid.getText().toString();
                        String pen_no=et_penno.getText().toString();
                        String pen_function=et_penfunc.getText().toString().trim().toLowerCase();
                        String pen_penhouseid=et_penhouseid.getText().toString();
                        try{
                            Toast.makeText(ChoosePen.this, "Pen Added", Toast.LENGTH_SHORT).show();
                            db.addPen(pen_id,pen_no,pen_function,pen_penhouseid);
                            refresh();
                        }
                        catch(Exception e){

                            Toast.makeText(ChoosePen.this, "Error on inputs.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChoosePen.this, "Cancelled action", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog contact_prompt = builder.create();
        contact_prompt.show();


    }

    public void refresh() {
        loadLists();
        adapter = new CustomPagerAdapter(ChoosePen.this, lists, lists2, lists3, ids);
        viewPager.setAdapter(adapter);
    }
}
