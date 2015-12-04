package uplb.cas.ics.phporktraceability;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/14/2015.
 */
public class AddThePig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddThePig.class.getSimpleName();
    private Toolbar toolbar;
    private static final String KEY_PENNO = "pen_no";
    private static final String KEY_FUNC = "function";

    EditText et_weight;
    EditText et_quantity;
    Button btn_addpig;
    TextView tv_subs;
    TextView tv_group;
    TextView tv_boar;
    TextView tv_sow;
    TextView tv_foster;
    TextView tv_breed;
    TextView tv_weekf;
    TextView tv_gender;
    TextView tv_rfid;
    TextView tv_pen;
    TextView tv_feed;
    TextView tv_med;
    TextView tv_pigid;

    SQLiteHandler db;
    String pen = "";
    String rfid = "";
    String gender = "";
    String boar_id = "";
    String sow_id = "";
    String foster_sow = "";
    String group_label = "";
    String breed = "";
    String week_farrowed = "";
    String feed_id = "";
    String med_id = "";
    String weight = "";
    String quantity = "";

    String label = "";
    String pen_disp = "";
    String rfid_disp = "";
    String breed_name = "";
    String feed_name = "";
    String med_name = "";
    int curID;

    DateFormat curDate = new SimpleDateFormat("yyyy-MM-DD");
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addthepig);
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
        group_label = i.getStringExtra("group_label");
        breed = i.getStringExtra("breed");
        week_farrowed = i.getStringExtra("week_farrowed");
        gender = i.getStringExtra("gender");
        rfid = i.getStringExtra("rfid");
        pen = i.getStringExtra("pen");
        feed_id = i.getStringExtra("feed_id");
        med_id = i.getStringExtra("med_id");

        db = new SQLiteHandler(getApplicationContext());

        curID = db.getMaxPigID();
        curID++;
        label = getLabel(String.valueOf(curID));

        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_boar = (TextView) findViewById(R.id.tv_boar);
        tv_sow = (TextView) findViewById(R.id.tv_sow);
        tv_foster = (TextView) findViewById(R.id.tv_fostersow);
        tv_breed = (TextView) findViewById(R.id.tv_breed);
        tv_weekf = (TextView) findViewById(R.id.tv_week);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_rfid = (TextView) findViewById(R.id.tv_rfid);
        tv_pen = (TextView) findViewById(R.id.tv_pen);
        tv_feed = (TextView) findViewById(R.id.tv_feed);
        tv_med = (TextView) findViewById(R.id.tv_med);
        tv_pigid = (TextView) findViewById(R.id.tv_pig);

        Resources res = getResources();
        String[] arr = res.getStringArray(R.array.pig_breeds);
        breed_name = arr[Integer.parseInt(breed) - 1];
        rfid_disp = db.getRFID(rfid);
        feed_name = db.getFeedName(feed_id);
        med_name = db.getMedName(med_id);
        displayPen(pen);

        tv_group.setText("Group Label: " + group_label);
        tv_boar.setText("Boar Parent: " + getLabel(boar_id));
        tv_sow.setText("Sow Parent: " + getLabel(sow_id));
        tv_foster.setText("Foster Sow Parent: " + getLabel(foster_sow));
        tv_breed.setText("Breed: " + breed_name);
        tv_weekf.setText("Week Farrowed: " + week_farrowed);
        tv_gender.setText("Gender: " + gender);
        tv_rfid.setText("Tag Label: " + rfid_disp);
        tv_pen.setText(pen_disp);
        tv_feed.setText("Last Feed Given: " + feed_name);
        tv_med.setText("Last Vaccine Given: " + med_name);
        tv_pigid.setText("Pig Label: " + label);

        btn_addpig = (Button) findViewById(R.id.btn_addPig);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_quantity = (EditText) findViewById(R.id.et_quantity);

        btn_addpig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_weight.getText().toString().trim().length() > 0 &&
                        et_quantity.getText().toString().trim().length() > 0){

                    weight = et_weight.getText().toString();
                    quantity = et_quantity.getText().toString();
                    String pig_id = String.valueOf(curID);
                    String date = curDate.format(dateObj);
                    String time = curTime.format(dateObj);

                    db.addGroup(group_label, pen);
                    db.addPig(pig_id, boar_id, sow_id, foster_sow, week_farrowed,
                            gender, "weaning", pen, breed, group_label);
                    db.updateTag(rfid, String.valueOf(curID), "used");
                    db.addWeightRecByAuto(weight, pig_id, date, time);
                    db.feedPigRecAuto(quantity, date, time, pig_id, feed_id);
                    db.addMedRecAuto(date, time, pig_id, med_id);

                    Toast.makeText(AddThePig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                    // Create Object of Dialog class
                    final Dialog addD = new Dialog(AddThePig.this);
                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    Button btn_addAnother = (Button) addD.findViewById(R.id.db_btn_addAnother);
                    Button btn_finish = (Button) addD.findViewById(R.id.db_btn_finishAdd);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddThePig.this);

                    btn_addAnother.setOnTouchListener(AddThePig.this);
                    btn_finish.setOnTouchListener(AddThePig.this);

                    addD.show();

                }
                else{
                    Toast.makeText(AddThePig.this,
                            "Please Enter the feed quantity and pig weight before proceeding.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
                Intent i = new Intent(AddThePig.this, LastMedicationGiven.class);
                i.putExtra("rfid", rfid);
                i.putExtra("boar_id", boar_id);
                i.putExtra("sow_id", sow_id);
                i.putExtra("foster_sow", foster_sow);
                i.putExtra("group_label", group_label);
                i.putExtra("breed", breed);
                i.putExtra("week_farrowed", week_farrowed);
                i.putExtra("gender", gender);
                i.putExtra("feed_id", feed_id);
                i.putExtra("pen", pen);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayPen(String _pen){

        ArrayList<HashMap<String, String>> the_list = db.getPen(_pen);

        for(int i = 0;i < the_list.size();i++)
        {
            HashMap<String, String> c = the_list.get(i);

            pen_disp = "Pen No: " + c.get(KEY_PENNO) + " -> Function: " + c.get(KEY_FUNC);
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
                tv_subs.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                String choice = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    if(choice.equals("add_another")){
                        Intent i = new Intent(AddThePig.this, WeekFarrowedPage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                        i.putExtra("breed", breed);
                        i.putExtra("boar_id", boar_id);
                        i.putExtra("sow_id", sow_id);
                        i.putExtra("foster_sow", foster_sow);
                        i.putExtra("group_label", group_label);
                        startActivity(i);
                        finish();
                    } else if(choice.equals("finish")) {
                        Intent i = new Intent(AddThePig.this, WeaningPage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }

                Log.d(LOGCAT, "Dropped " + choice);
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
    public void onBackPressed(){ super.onBackPressed(); finish(); }

}
