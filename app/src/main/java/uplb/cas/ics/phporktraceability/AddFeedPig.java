package uplb.cas.ics.phporktraceability;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/25/2015.
 */
public class AddFeedPig extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = AddFeedPig.class.getSimpleName();
    private static final String KEY_FEEDNAME = "feed_name";
    private static final String KEY_FEEDTYPE = "feed_type";
    private static final String KEY_PIGID = "pig_id";
    Button btn_submit;
    Button btn_chooseDate;
    TextView tv_subs;
    TextView tv_feedname;
    TextView tv_feedtype;
    TextView tv_chosenDate;
    EditText et_quantity;
    ListView lv;
    SQLiteHandler db;
    String feed_id = "";
    String feed_name = "";
    String feed_type = "";
    String pen = "";
    String house_id = "";
    String[] pigs;
    DateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();
    Calendar dateAndTime=Calendar.getInstance();
    private Toolbar toolbar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeedpig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        feed_id = i.getStringExtra("feed_id");
        pen = i.getStringExtra("pen");
        house_id = i.getStringExtra("house_id");

        db = new SQLiteHandler(getApplicationContext());

        tv_feedname = (TextView) findViewById(R.id.tv_feedname);
        tv_feedtype = (TextView) findViewById(R.id.tv_feedtype);
        tv_chosenDate = (TextView) findViewById(R.id.tv_chosenDate);

        et_quantity = (EditText) findViewById(R.id.et_quantity);
        btn_chooseDate = (Button) findViewById(R.id.btn_chooseDate);
        btn_submit = (Button) findViewById(R.id.btn_addFeed);
        lv = (ListView) findViewById(R.id.listview);

        loadList();

        tv_feedname.setText("Feed Name: " + feed_name);
        tv_feedtype.setText("Feed Type: " + feed_type);

        btn_chooseDate.setOnClickListener(new View.OnClickListener() {
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
                                tv_chosenDate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_quantity.getText().toString().length() > 0) {
                    String quantity = et_quantity.getText().toString();
                    String unit = "kg";
                    String date = curDate.format(dateObj);
                    String time = curTime.format(dateObj);
                    String status = "new";
                    String prod_date = tv_chosenDate.getText().toString();

                    Double kg = Double.parseDouble(quantity) / pigs.length;
                    quantity = String.valueOf(kg);

                    db.feedPigRecByGroup(quantity, unit, date, time, pigs, feed_id, prod_date, status);

                    Toast.makeText(AddFeedPig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                    // Create Object of Dialog class
                    final Dialog addD = new Dialog(AddFeedPig.this);
                    // Set GUI of login screen
                    addD.setContentView(R.layout.dialog_add_pig);
                    addD.setTitle("Added Successfully.");

                    // Init button of login GUI
                    Button btn_addAnother = (Button) addD.findViewById(R.id.db_btn_addAnother);
                    Button btn_finish = (Button) addD.findViewById(R.id.db_btn_finishAdd);
                    LinearLayout bl = (LinearLayout) addD.findViewById(R.id.bottom_container);
                    tv_subs = (TextView) addD.findViewById(R.id.tv_subs);
                    bl.setOnDragListener(AddFeedPig.this);

                    btn_addAnother.setOnTouchListener(AddFeedPig.this);
                    btn_finish.setOnTouchListener(AddFeedPig.this);

                    addD.show();
                } else {
                    Toast.makeText(AddFeedPig.this, "Please enter quantity of feed to be given.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void loadList(){

        HashMap<String, String> feed = db.getFeedNameAndType(feed_id);
        feed_name = feed.get(KEY_FEEDNAME);
        feed_type = feed.get(KEY_FEEDTYPE);

        ArrayList<HashMap<String, String>> pig_list = db.getPigsByPen(pen);

        String[] labels = new String[pig_list.size()];
        pigs = new String[pig_list.size()];
        for(int j = 0;j < pig_list.size();j++){

            HashMap<String, String> c = pig_list.get(j);

            labels[j] = "Pig ID: " + getLabel(c.get(KEY_PIGID));
            pigs[j] = c.get(KEY_PIGID);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, labels);
        lv.setAdapter(adapter);
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
                Intent i = new Intent(AddFeedPig.this, ChooseFeedPenPage.class);
                i.putExtra("feed_id", feed_id);
                i.putExtra("house_id", house_id);
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
                        Intent i = new Intent(AddFeedPig.this, ChooseFeedPage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else if(choice.equals("finish")) {
                        Intent i = new Intent(AddFeedPig.this, ChooseModule.class);
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
        Intent i = new Intent(AddFeedPig.this, ChooseFeedPenPage.class);
        i.putExtra("feed_id", feed_id);
        i.putExtra("house_id", house_id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}
