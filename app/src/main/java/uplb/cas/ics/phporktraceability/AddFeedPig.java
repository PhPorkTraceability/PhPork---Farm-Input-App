package uplb.cas.ics.phporktraceability;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
 * Created by marmagno on 11/25/2015.
 */
public class AddFeedPig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddFeedPig.class.getSimpleName();
    private static final String KEY_FEEDNAME = "feed_name";
    private static final String KEY_FEEDTYPE = "feed_type";
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_LABEL = "label";
    private static final String SEL_PIG = "by_pig";
    private static final String SEL_PEN = "by_pen";
    Button btn_submit;
    Button btn_proddate;
    Button btn_dateGiven;
    Button btn_timeGiven;
    TextView tv_subs;
    TextView tv_feedname;
    TextView tv_feedtype;
    TextView tv_proddate;
    TextView tv_dateGiven;
    TextView tv_timeGiven;
    EditText et_quantity;
    ListView lv;
    SQLiteHandler db;
    String feed_id = "";
    String feed_name = "";
    String feed_type = "";
    String pen_id = "";
    String house_id = "";
    String[] total_pigs;
    String[] pigs;
    String[] pens;
    Dialog addD = null;

    Calendar dateAndTime=Calendar.getInstance();
    private int year, month, day;

    String selection = "";
    String module = "";

    @SuppressLint("SimpleDateFormat")
    DateFormat curTime = new SimpleDateFormat("HH:mm");

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            tv_timeGiven.setText(curTime.format(dateAndTime.getTime()));
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeedpig);
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
                i.setClass(AddFeedPig.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        TextView pg_title = (TextView) toolbar.findViewById(R.id.page_title);
        pg_title.setText(R.string.feed);

        db = SQLiteHandler.getInstance();

        Intent i = getIntent();
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");
        feed_id = i.getStringExtra("feed_id");
        house_id = i.getStringExtra("house_id");
        if(selection.equals(SEL_PIG)){
            pigs = i.getStringArrayExtra("pigs");
            pen_id = i.getStringExtra("pen_id");
            total_pigs = pigs;
        } else {
            pens = i.getStringArrayExtra("pens");
        }

        tv_feedname = (TextView) findViewById(R.id.tv_feedname);
        tv_feedtype = (TextView) findViewById(R.id.tv_feedtype);
        tv_proddate = (TextView) findViewById(R.id.tv_chosenDate);
        tv_dateGiven = (TextView) findViewById(R.id.tv_chosenDate2);
        tv_timeGiven = (TextView) findViewById(R.id.tv_chosenDate3);

        et_quantity = (EditText) findViewById(R.id.et_quantity);
        btn_proddate = (Button) findViewById(R.id.btn_proddate);
        btn_dateGiven = (Button) findViewById(R.id.btn_dateGiven);
        btn_timeGiven = (Button) findViewById(R.id.btn_timeGiven);
        btn_submit = (Button) findViewById(R.id.btn_addFeed);
        lv = (ListView) findViewById(R.id.listview);

        btn_proddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = dateAndTime.get(Calendar.YEAR);
                month = dateAndTime.get(Calendar.MONTH);
                day = dateAndTime.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddFeedPig.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                tv_proddate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        btn_dateGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = dateAndTime.get(Calendar.YEAR);
                month = dateAndTime.get(Calendar.MONTH);
                day = dateAndTime.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddFeedPig.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                tv_dateGiven.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        btn_timeGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddFeedPig.this, t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_quantity.getText().toString().length() > 0 &&
                        tv_dateGiven.getText().toString().length() > 0 &&
                        tv_timeGiven.getText().toString().length() > 0 ){
                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserSession();
                    String user_id = user.get(SessionManager.KEY_USERID);
                    String quantity = et_quantity.getText().toString();
                    String unit = "kg";
                    String date = tv_dateGiven.getText().toString();
                    String time = tv_timeGiven.getText().toString();
                    String status = "new";
                    String prod_date = tv_proddate.getText().toString();

                    Double kg = Double.parseDouble(quantity) / total_pigs.length;
                    quantity = String.valueOf(kg);

                    db.addFeedTransByGroup(quantity, unit, date, time, total_pigs,
                            feed_id, prod_date, status, user_id);

                    // Create Object of Dialog class
                    addD = new Dialog(AddFeedPig.this);
                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    ImageView iv_another = (ImageView) addD.findViewById(R.id.iv_another);
                    ImageView iv_finish = (ImageView) addD.findViewById(R.id.iv_finish);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddFeedPig.this);

                    iv_another.setOnTouchListener(AddFeedPig.this);
                    iv_finish.setOnTouchListener(AddFeedPig.this);

                    addD.show();
                } else {

                    Snackbar snackbar = Snackbar
                            .make(v, "Fill up details before proceeding", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        loadList();

    }

    public void loadList(){

        HashMap<String, String> feed = db.getFeedNameAndType(feed_id);
        feed_name = feed.get(KEY_FEEDNAME);
        feed_type = feed.get(KEY_FEEDTYPE);
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
                                            i.setClass(AddFeedPig.this, ChooseByPig.class);
                                            i.putExtra("pen_id", pen_id);
                                        } else
                                            i.setClass(AddFeedPig.this, ChooseByPen.class);
                                        i.putExtra("feed_id", feed_id);
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

        tv_feedname.setText("Feed Name: " + feed_name);
        tv_feedtype.setText("Feed Type: " + feed_type);
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
                    i.setClass(AddFeedPig.this, ChooseByPig.class);
                    i.putExtra("pen_id", pen_id);
                } else
                    i.setClass(AddFeedPig.this, ChooseByPen.class);
                i.putExtra("feed_id", feed_id);
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
                        i.setClass(AddFeedPig.this, ChooseSelection.class);
                        i.putExtra("module", module);
                    } else if(choice.equals("finish"))
                        i.setClass(AddFeedPig.this, ChooseModule.class);
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
            i.setClass(AddFeedPig.this, ChooseByPig.class);
            i.putExtra("pen_id", pen_id);
        } else
            i.setClass(AddFeedPig.this, ChooseByPen.class);

        i.putExtra("feed_id", feed_id);
        i.putExtra("house_id", house_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        startActivity(i);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(addD != null && addD.isShowing()){
            addD.dismiss();
        }
    }
}
