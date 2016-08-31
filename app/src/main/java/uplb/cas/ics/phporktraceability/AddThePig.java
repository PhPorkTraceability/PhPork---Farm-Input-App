package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import helper.TestSessionManager;

/**
 * Created by marmagno on 11/14/2015.
 */
public class AddThePig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddThePig.class.getSimpleName();
    private static final String KEY_PENNO = "pen_no";
    private static final String KEY_FUNC = "function";
    private static final String KEY_LABELID = "label_id";
    SQLiteHandler db;
    SessionManager session;
    EditText et_weight;
    EditText et_quantity;
    EditText et_quantity2;
    Spinner sp_units;
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
    String pen = "";
    String rfid = "";
    String gender = "";
    String boar_id = "";
    String sow_id = "";
    String foster_sow = "";
    String group_label = "";
    String breed = "";
    //String week_farrowed = "";
    String farrowing_date = "";
    String feed_id = "";
    String med_id = "";
    String weight = "";
    String quantity = "";
    String quantity2 = "";
    String label = "";
    String pen_disp = "";
    String rfid_disp = "";
    String breed_name = "";
    String feed_name = "";
    String med_name = "";
    String function = "";
    String unit = "";
    int curID;
    DateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();
    private Toolbar toolbar;
    Dialog addD = null;

    /**
     * For testing only
     */
    TestSessionManager test;
    int count = 0;
    String testID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addthepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        test = new TestSessionManager(getApplicationContext());
        HashMap<String, Integer> testcount = test.getCount();
        count = testcount.get(TestSessionManager.KEY_COUNT);

        HashMap<String, String> testuser = test.getID();
        testID = testuser.get(TestSessionManager.KEY_ID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        retrieveIntentExtra(getIntent());

        db = SQLiteHandler.getInstance();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String > user = session.getUserLoc();

        function = user.get(SessionManager.KEY_FUNC);

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

        HashMap<String, String> p1 = db.getParentLabel(boar_id, "boar");
        HashMap<String, String> p2 = db.getParentLabel(sow_id, "sow");
        HashMap<String, String> p3 = db.getParentLabel(foster_sow, "sow");
        tv_group.setText("Group Label: " + group_label);
        tv_boar.setText("Boar Parent: " + checkIfNull(p1.get(KEY_LABELID)));
        tv_sow.setText("Sow Parent: " + checkIfNull(p2.get(KEY_LABELID)));
        tv_foster.setText("Foster Sow Parent: " + checkIfNull(p3.get(KEY_LABELID)));
        tv_breed.setText("Breed: " + breed_name);
        //tv_weekf.setText("Week Farrowed: " + week_farrowed);
        tv_gender.setText("Gender: " + gender);
        tv_rfid.setText("Tag Label: " + rfid_disp);
        tv_pen.setText(pen_disp);
        tv_feed.setText("Last Feed Given: " + feed_name);
        tv_med.setText("Last Vaccine Given: " + med_name);
        tv_pigid.setText("Pig Label: " + label);

        btn_addpig = (Button) findViewById(R.id.btn_addPig);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_quantity2 = (EditText) findViewById(R.id.et_quantity2);
        sp_units = (Spinner) findViewById(R.id.sp_units);

        // Create Object of Dialog class
        addD = new Dialog(AddThePig.this);

        sp_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit = parent.getItemAtPosition(position).toString();
                Log.d(LOGCAT, unit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_addpig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_weight.getText().toString().trim().length() > 0 &&
                        et_quantity.getText().toString().trim().length() > 0 &&
                        et_quantity2.getText().toString().trim().length() > 0){

                    weight = et_weight.getText().toString();
                    quantity = et_quantity.getText().toString();
                    quantity2 = et_quantity2.getText().toString();
                    String pig_id = String.valueOf(curID);
                    String date = curDate.format(dateObj);
                    String time = curTime.format(dateObj);
                    String status = "new";
                    String prod_date = "";
                    String feed_unit = "kg";
                    String user = "farm_user";

                    //db.addGroup(group_label, pen);

                    db.addPig(pig_id, boar_id, sow_id, foster_sow, "", gender,
                            farrowing_date, function, pen, breed, user, group_label, status);

                    db.updateTag(rfid, pig_id, "active");
                    //db.updateTagLabel(rfid, label);
                    db.addWeightRecByAuto(weight, pig_id, date, time, "initial weight", status);
                    db.feedPigRecAuto(quantity, feed_unit, date, time, pig_id, feed_id, prod_date, status);
                    db.addMedRecAuto(date, time, quantity2, unit, pig_id, med_id, status);

                    Toast.makeText(AddThePig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    ImageView iv_another = (ImageView) addD.findViewById(R.id.iv_another);
                    ImageView iv_finish = (ImageView) addD.findViewById(R.id.iv_finish);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddThePig.this);

                    iv_another.setOnTouchListener(AddThePig.this);
                    iv_finish.setOnTouchListener(AddThePig.this);

                    addD.show();

                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddThePig.this);
                    builder.setTitle("Fill up data.")
                            .setMessage("Please fill up the blank fields before proceeding.")
                            .setCancelable(false)
                            .setNeutralButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final String[] choices = {
                "Edit Boar",
                "Edit Sow",
                "Edit Foster Sow",
                "Edit Breed Name",
                "Edit Gender",
                "Edit RFID Tag",
                "Edit Pig Pen",
                "Edit Feed Name",
                "Edit Medicine Name"
        };

        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.action_help:
                show_help();
                return true;
            case android.R.id.home:
                count++;
                test.updateCount(count);
                Intent i = new Intent(AddThePig.this, LastMedicationGiven.class);
                createIntent(i);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            case R.id.action_edit:
                new AlertDialog.Builder(this).setTitle("Edit Which Part?")
                        .setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i;
                                switch(which) {
                                    case 0:
                                        i = new Intent(AddThePig.this, ChooseBoarEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 1:
                                        i = new Intent(AddThePig.this, ChooseSowEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 2:
                                        i = new Intent(AddThePig.this, ChooseFosterSowEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 3:
                                        i = new Intent(AddThePig.this, ChooseBreedEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 4:
                                        i = new Intent(AddThePig.this, ChooseGenderEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 5:
                                        i = new Intent(AddThePig.this, AssignRFIDEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 6:
                                        i = new Intent(AddThePig.this, AssignPenEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 7:
                                        i = new Intent(AddThePig.this, LastFeedGivenEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    case 8:
                                        i = new Intent(AddThePig.this, LastMedicationGivenEdit.class);
                                        createIntent(i);
                                        startActivity(i);
                                        finish();
                                        break;
                                    default:
                                        Log.e(LOGCAT, "which value out of bounds.");
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void show_help(){
        Intent intent = new Intent(this, HelpPage.class);
        intent.putExtra("help_page", 4);
        startActivity(intent);
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

    public void displayPen(String _pen){

        ArrayList<HashMap<String, String>> the_list = db.getPen(_pen);

        for(int i = 0;i < the_list.size();i++)
        {
            HashMap<String, String> c = the_list.get(i);

            pen_disp = "Pen No: " + c.get(KEY_PENNO) + " -> Function: " + c.get(KEY_FUNC);
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
                        Intent i = new Intent(AddThePig.this, ChooseGender.class);
                        createIntent(i);
                        startActivity(i);
                        finish();
                    }
                        if(choice.equals("finish")) {
                        String time = curTime.format(dateObj);
                        db.updateTest(testID, time, String.valueOf(count));

                        test.logoutUser();

                        Intent i = new Intent(AddThePig.this, HomeActivity.class);
                        //Intent i = new Intent(AddThePig.this, ChooseModule.class);
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
    public void onBackPressed(){
        super.onBackPressed();
        count++;
        test.updateCount(count);
        Intent i = new Intent(AddThePig.this, LastMedicationGiven.class);
        createIntent(i);
        startActivity(i);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        addD.dismiss();
    }
}
