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
import android.view.MotionEvent;
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

/**
 * Created by marmagno on 1/26/2016.
 */
public class ChooseMedPage extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    public final static String KEY_MEDID = "med_id";
    public final static String KEY_MEDNAME = "med_name";
    public final static String KEY_MEDTYPE = "med_type";
    private static final String LOGCAT = ChooseMedPage.class.getSimpleName();
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SQLiteHandler db;
    String med_id = "";
    String[] lists1 = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    private Toolbar toolbar;


    String selection = "";
    String module = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        db = SQLiteHandler.getInstance();

        loadLists();

        bl = (LinearLayout) findViewById(R.id.bottom_container);
        ll = (LinearLayout) findViewById(R.id.bottom_container);

        bl.setOnDragListener(this);
        //ll.setOnDragListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(ChooseMedPage.this, lists1, lists2, lists3, ids);
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
                    } else if (currentPage == lists1.length) {

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
        String title = "Swipe to Choose a Medication";
        tv_title.setText(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_med();
                }
            });
        } else {
            Log.e(LOGCAT, "fab is null.");
        }
    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < lists1.length){
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
                Intent i = new Intent(ChooseMedPage.this, ChooseSelection.class);
                i.putExtra("selection", selection);
                i.putExtra("module", module);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        ArrayList<HashMap<String, String>> meds = db.getMeds();

        lists1 = new String[meds.size()+1];
        lists2 = new String[meds.size()+1];
        lists3 = new String[meds.size()+1];
        ids = new String[meds.size()+1];

        lists1[0] = "";
        lists2[0] = "---";
        lists3[0] = "";
        ids[0] = "";

        for(int i = 0;i < meds.size();i++) {
            HashMap<String, String> c = meds.get(i);
            lists1[i+1] = "Med Name: " + c.get(KEY_MEDNAME);
            lists2[i+1] = "Med Type: " + c.get(KEY_MEDTYPE);
            lists3[i+1] = "";
            ids[i+1] = c.get(KEY_MEDID);
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
                med_id = view.findViewById(id).getTag().toString();

                Log.d(LOGCAT, "Dropped " + med_id);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    Intent i = new Intent(ChooseMedPage.this, ChooseHouse.class);
                    i.putExtra("selection", selection);
                    i.putExtra("module", module);
                    i.putExtra("med_id", med_id);
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
        Intent i = new Intent(ChooseMedPage.this, ChooseSelection.class);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        startActivity(i);
        finish();
    }

    public void add_med(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder verifier = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View subView = inflater.inflate(R.layout.add_med_fragment,null);
        final EditText et_med_id = (EditText) subView.findViewById(R.id.et_med_id);
        final EditText et_med_name = (EditText) subView.findViewById(R.id.et_med_name);
        final EditText et_med_type = (EditText) subView.findViewById(R.id.et_med_type);


        builder.setTitle("Add a new Medicine");
        builder.setView(subView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                verifier.setMessage("Are you sure all entries are correct?");
                verifier.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String med_id=et_med_id.getText().toString();
                        String med_name=et_med_name.getText().toString();
                        String med_type=et_med_type.getText().toString();
                        try{
                            Toast.makeText(ChooseMedPage.this, "New Med Added", Toast.LENGTH_SHORT).show();
                            db.addMeds(med_id,med_name,med_type);
                            refresh();
                        }
                        catch(Exception e){

                            Toast.makeText(ChooseMedPage.this, "Error on inputs.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChooseMedPage.this, "Cancelled action", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }

        });

        AlertDialog contact_prompt = builder.create();
        contact_prompt.show();


    }

    public void refresh() {
        loadLists();
        adapter = new CustomPagerAdapter(ChooseMedPage.this, lists1, lists2, lists3, ids);
        viewPager.setAdapter(adapter);
    }
}
