package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 1/26/2016.
 */
public class AddMedPig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddMedPig.class.getSimpleName();
    private static final String KEY_MEDNAME = "med_name";
    private static final String KEY_MEDTYPE = "med_type";
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_LABEL = "label";
    private static final String SEL_PIG = "by_pig";
    private static final String SEL_PEN = "by_pen";
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
    String pen_id = "";
    String house_id = "";
    String unit = "";
    String[] total_pigs;
    String[] pigs;
    String[] pens;
    Dialog addD = null;

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
    private int year, month, day;

    String selection = "";
    String module = "";

    SessionManager session;
    String user_id = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedpig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
                i.setClass(AddMedPig.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        TextView pg_title = (TextView) toolbar.findViewById(R.id.page_title);
        pg_title.setText(R.string.med);

        db = SQLiteHandler.getInstance();
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserSession();
        user_id = user.get(SessionManager.KEY_USERID);

        Intent i = getIntent();
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");
        med_id = i.getStringExtra("med_id");
        house_id = i.getStringExtra("house_id");
        if(selection.equals(SEL_PIG)){
            pigs = i.getStringArrayExtra("pigs");
            pen_id = i.getStringExtra("pen_id");
            total_pigs = pigs;
        } else {
            pens = i.getStringArrayExtra("pens");
        }

        tv_medname = (TextView) findViewById(R.id.tv_medname);
        tv_medtype = (TextView) findViewById(R.id.tv_medtype);
        tv_chosenDate = (TextView) findViewById(R.id.tv_chosenDate);
        tv_chosenTime = (TextView) findViewById(R.id.tv_chosenTime);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        sp_units = (Spinner) findViewById(R.id.sp_units);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_chooseDate = (Button) findViewById(R.id.btn_dateGiven);
        btn_chooseTime = (Button) findViewById(R.id.btn_timeGiven);
        lv = (ListView) findViewById(R.id.listview);

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

                if(et_quantity.getText().toString().length() > 0 &&
                        tv_chosenDate.getText().length() > 0 &&
                        tv_chosenTime.getText().length() > 0) {
                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserSession();
                    String user_id = user.get(SessionManager.KEY_USERID);
                    String date = tv_chosenDate.getText().toString();
                    String time = tv_chosenTime.getText().toString();
                    String quantity = et_quantity.getText().toString();
                    String status = "new";

                    Double measurement = Double.parseDouble(quantity) / total_pigs.length;
                    quantity = String.valueOf(measurement);

                    db.addMedRecByGroup(quantity, unit, date, time, total_pigs, med_id, status, user_id);
                    //db.addMedHistory(); add med history here

                    // Create Object of Dialog class
                    addD = new Dialog(AddMedPig.this);
                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    ImageView iv_another = (ImageView) addD.findViewById(R.id.iv_another);
                    ImageView iv_finish = (ImageView) addD.findViewById(R.id.iv_finish);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddMedPig.this);

                    iv_another.setOnTouchListener(AddMedPig.this);
                    iv_finish.setOnTouchListener(AddMedPig.this);

                    addD.show();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Fill up details before proceeding", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        loadList();

    }

    public void loadList(){

        HashMap<String, String> med = db.getMedNameAndType(med_id);
        med_name = med.get(KEY_MEDNAME);
        med_type = med.get(KEY_MEDTYPE);
        String[] labels;

        if(selection.equals(SEL_PEN)) {
            ArrayList<HashMap<String, String>> pig_list = new ArrayList<>();
            for (String pen1 : pens) {
                ArrayList<HashMap<String, String>> pigs_by_pen = db.getPigsByPen(pen1);
                for (int k = 0; k < pigs_by_pen.size(); k++) {
                    HashMap<String, String> p = pigs_by_pen.get(k);
                    HashMap<String, String> a = new HashMap<>();
                    a.put(KEY_PIGID, p.get(KEY_PIGID));
                    a.put(KEY_LABEL, p.get(KEY_LABEL));
                    pig_list.add(a);
                }
            }

            labels = new String[pig_list.size()];
            total_pigs = new String[pig_list.size()];
            for(int j = 0;j < pig_list.size();j++) {
                HashMap<String, String> c = pig_list.get(j);
                labels[j] = "Pig Label: " + c.get(KEY_LABEL);
                total_pigs[j] = c.get(KEY_PIGID);
            }
        } else {
            labels = new String[pigs.length];
            if(pigs.length > 0) {
                for (int j = 0; j < pigs.length; j++) {
                    HashMap<String, String> b = db.getLabel(pigs[j]);
                    labels[j] = "Pig Label: " + b.get(KEY_LABEL);
                }
            } else {
                final int SPLASH_TIME_OUT = 4000;
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("There are no pigs selected.")
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
                                        Intent i = new Intent();
                                        if(selection.equals(SEL_PIG)){
                                            i.setClass(AddMedPig.this, ChooseByPig.class);
                                            i.putExtra("pen_id", pen_id);
                                        } else
                                            i.setClass(AddMedPig.this, ChooseByPen.class);

                                        i.putExtra("med_id", med_id);
                                        i.putExtra("house_id", house_id);
                                        i.putExtra("selection", selection);
                                        i.putExtra("module", module);
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, labels);
        lv.setAdapter(adapter);

        tv_medname.setText("Med Name: " + med_name);
        tv_medtype.setText("Med Type: " + med_type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent();
                if(selection.equals(SEL_PIG)){
                    i.setClass(AddMedPig.this, ChooseByPig.class);
                    i.putExtra("pen_id", pen_id);
                } else
                    i.setClass(AddMedPig.this, ChooseByPen.class);
                i.putExtra("med_id", med_id);
                i.putExtra("house_id", house_id);
                i.putExtra("selection", selection);
                i.putExtra("module", module);
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
                tv_subs.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                String choice = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){

                    Intent i = new Intent();
                    if(choice.equals("add_another")){
                        i.setClass(AddMedPig.this, ChooseSelection.class);
                        i.putExtra("module", module);
                    } else if(choice.equals("finish"))
                        i.setClass(AddMedPig.this, ChooseModule.class);
                    startActivity(i);
                    finish();
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
        Intent i = new Intent();
        if(selection.equals(SEL_PIG)){
            i.setClass(AddMedPig.this, ChooseByPig.class);
            i.putExtra("pen_id", pen_id);
        } else
            i.setClass(AddMedPig.this, ChooseByPen.class);
        i.putExtra("med_id", med_id);
        i.putExtra("house_id", house_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        startActivity(i);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(addD != null && addD.isShowing())
            addD.dismiss();

    }
}
