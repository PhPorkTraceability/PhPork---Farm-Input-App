package uplb.cas.ics.phporktraceability;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 1/26/2016.
 */
public class AddMedPig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddMedPig.class.getSimpleName();
    private static final String KEY_MEDNAME = "med_name";
    private static final String KEY_MEDTYPE = "med_type";
    Button btn_submit;
    Button btn_chooseDate;
    Button btn_chooseTime;
    TextView tv_subs;
    TextView tv_medname;
    TextView tv_medtype;
    TextView tv_pig;
    TextView tv_chosenDate;
    TextView tv_chosenTime;
    EditText et_quantity;
    Spinner sp_units;
    ListView lv;
    SQLiteHandler db;
    String med_id = "";
    String med_name = "";
    String med_type = "";
    String pen = "";
    String house_id = "";
    String pig_id = "";
    String unit = "";
    DateFormat curTime = new SimpleDateFormat("HH:mm");
    Calendar dateAndTime=Calendar.getInstance();
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            tv_chosenTime.setText(curTime.format(dateAndTime.getTime()));
        }
    };
    private Toolbar toolbar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedpig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        med_id = i.getStringExtra("med_id");
        pen = i.getStringExtra("pen");
        house_id = i.getStringExtra("house_id");
        pig_id = i.getStringExtra("pig_id");

        db = new SQLiteHandler(getApplicationContext());

        tv_medname = (TextView) findViewById(R.id.tv_medname);
        tv_medtype = (TextView) findViewById(R.id.tv_medtype);
        tv_pig = (TextView) findViewById(R.id.tv_pig);
        tv_chosenDate = (TextView) findViewById(R.id.tv_chosenDate);
        tv_chosenTime = (TextView) findViewById(R.id.tv_chosenTime);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        sp_units = (Spinner) findViewById(R.id.sp_units);

        btn_submit = (Button) findViewById(R.id.btn_addMed);
        btn_chooseDate = (Button) findViewById(R.id.btn_chooseDate);
        btn_chooseTime = (Button) findViewById(R.id.btn_chooseTime);
        lv = (ListView) findViewById(R.id.listview);

        HashMap<String, String> a = db.getMedNameAndType(med_id);
        med_name = a.get(KEY_MEDNAME);
        med_type = a.get(KEY_MEDTYPE);

        tv_medname.setText("Med Name: " + med_name);
        tv_medtype.setText("Med Type: " + med_type);
        tv_pig.setText("Pig ID: " + getLabel(pig_id));

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

        btn_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = dateAndTime.get(Calendar.YEAR);
                month = dateAndTime.get(Calendar.MONTH);
                day = dateAndTime.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddMedPig.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                tv_chosenDate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        btn_chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddMedPig.this, t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String date = curDate.format(dateObj);
                //String time = curTime.format(dateObj);

                if(et_quantity.getText().toString().length() > 0) {
                    String date = tv_chosenDate.getText().toString();
                    String time = tv_chosenTime.getText().toString();
                    String quantity = et_quantity.getText().toString();
                    String status = "new";

                    db.addMedRecAuto(date, time, quantity, unit, pig_id, med_id, status);

                    Toast.makeText(AddMedPig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                    // Create Object of Dialog class
                    final Dialog addD = new Dialog(AddMedPig.this);
                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    Button btn_addAnother = (Button) addD.findViewById(R.id.db_btn_addAnother);
                    Button btn_finish = (Button) addD.findViewById(R.id.db_btn_finishAdd);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddMedPig.this);

                    btn_addAnother.setOnTouchListener(AddMedPig.this);
                    btn_finish.setOnTouchListener(AddMedPig.this);

                    addD.show();
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
                Intent i = new Intent(AddMedPig.this, ChooseMedPig.class);
                i.putExtra("med_id", med_id);
                i.putExtra("house_id", house_id);
                i.putExtra("pen", pen);
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
                        Intent i = new Intent(AddMedPig.this, ChooseMedPage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else if(choice.equals("finish")) {
                        Intent i = new Intent(AddMedPig.this, ChooseModule.class);
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
        Intent i = new Intent(AddMedPig.this, ChooseMedPig.class);
        i.putExtra("med_id", med_id);
        i.putExtra("house_id", house_id);
        i.putExtra("pen", pen);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}
