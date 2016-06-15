package uplb.cas.ics.phporktraceability;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/19/2015.
 */

public class ViewListOfPigs extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener {

    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_GNAME = "group_name";
    public static final String KEY_BOAR = "boar_id";
    public static final String KEY_SOW = "sow_id";
    public static final String KEY_FOSTER = "foster_sow";
    public static final String KEY_WEEKF = "week_farrowed";
    public static final String KEY_PENID = "pen_id";
    public static final String KEY_FUNCTION = "function";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_FEEDNAME = "feed_name";
    public static final String KEY_FEEDTYPE = "feed_type";
    public static final String KEY_TAGLABEL = "label";
    public static final String KEY_TAGRFID = "tag_rfid";
    public static final String KEY_MEDNAME = "med_name";
    private static final String LOGCAT = ViewListOfPigs.class.getSimpleName();
    ArrayList<HashMap<String, String>> pig_list;
    SQLiteHandler db;
    String pig_id = "";
    String house_id = "";
    String pen = "";
    private Toolbar toolbar;
    private ListView lv;
    private TextView tv_drag;
    private EditText et_searchPig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_of_pigs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        house_id = i.getStringExtra("house_id");
        pen = i.getStringExtra("pen");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        db = new SQLiteHandler(getApplicationContext());

        et_searchPig = (EditText) findViewById(R.id.et_searchPig);
        lv = (ListView)findViewById(R.id.listview);
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itempig_id = ((TextView)
                        view.findViewById(R.id.tv_pig)).getText().toString().trim();
                String label = ((TextView)
                        view.findViewById(R.id.tv_pig_label)).getText().toString().trim();

                String sow_id = "", boar_id = "", foster_sow = "", group_name = "", tag_rfid = "",
                        week_farrowed = "", gender = "", breed = "", tag_label = "",
                        pen_id = "", weight = "", feed_name = "", feed_type = "",
                        med_name = "", med_type = "";

                HashMap<String, String> c = db.getThePig(itempig_id);
                HashMap<String, String> b = db.getPigFeed(itempig_id);
                HashMap<String, String> d = db.getPigMed(itempig_id);
                HashMap<String, String> e = db.getPigWeight(itempig_id);
                HashMap<String, String> a = db.getPigGroup(itempig_id);

                group_name = "Group Label: " + checkIfNull(a.get(KEY_GNAME));
                boar_id = "Boar Parent: " + checkIfNull(c.get(KEY_BOAR));
                sow_id = "Sow Parent: " + checkIfNull(c.get(KEY_SOW));
                foster_sow = "Foster Sow Parent: " + c.get(KEY_FOSTER);
                pen_id = "Pen No: " + a.get(KEY_PENID) + " (" + a.get(KEY_FUNCTION) + ")";
                week_farrowed = "Week Farrowed: " + c.get(KEY_WEEKF);
                gender = "Gender: " + c.get(KEY_GENDER);
                breed = "Breed: " + c.get(KEY_BREED);
                weight = "Current weight(in kg): " + checkIfNull(e.get(KEY_WEIGHT));
                feed_type = checkIfNull(b.get(KEY_FEEDTYPE));
                feed_name = "Last feed given: " + checkIfNull(b.get(KEY_FEEDNAME))
                        + " - " + feed_type;
                med_name = "Last Med given: " + checkIfNull(d.get(KEY_MEDNAME));
                tag_rfid = " (" + checkIfNull(c.get(KEY_TAGRFID)) + ")";
                tag_label = "Tag: " + checkIfNull(c.get(KEY_TAGLABEL));

                // Create Object of Dialog class
                final Dialog viewD = new Dialog(ViewListOfPigs.this);
                // Set GUI of login screen
                viewD.setContentView(R.layout.dialog_view_pig);
                viewD.setTitle("Viewing Pig Details");
                viewD.setCancelable(false);

                // Init button of login GUI
                Button btn_ok = (Button) viewD.findViewById(R.id.db_btn_ok);
                TextView tv_group = (TextView) viewD.findViewById(R.id.tv_group);
                TextView tv_pig = (TextView) viewD.findViewById(R.id.tv_pig);
                TextView tv_boar = (TextView) viewD.findViewById(R.id.tv_boar);
                TextView tv_sow = (TextView) viewD.findViewById(R.id.tv_sow);
                TextView tv_fostersow = (TextView) viewD.findViewById(R.id.tv_fostersow);
                TextView tv_weekf = (TextView) viewD.findViewById(R.id.tv_weekF);
                TextView tv_gender = (TextView) viewD.findViewById(R.id.tv_gender);
                TextView tv_breed = (TextView) viewD.findViewById(R.id.tv_breed);
                TextView tv_pen = (TextView) viewD.findViewById(R.id.tv_pen);
                TextView tv_weight = (TextView) viewD.findViewById(R.id.tv_weight);
                TextView tv_feed = (TextView) viewD.findViewById(R.id.tv_feed);
                TextView tv_med = (TextView) viewD.findViewById(R.id.tv_med);
                TextView tv_tag = (TextView) viewD.findViewById(R.id.tv_tagLabel);

                tv_group.setText(group_name);
                tv_pig.setText(label);
                tv_boar.setText(boar_id);
                tv_sow.setText(sow_id);
                tv_fostersow.setText(foster_sow);
                tv_weekf.setText(week_farrowed);
                tv_gender.setText(gender);
                tv_breed.setText(breed);
                tv_pen.setText(pen_id);
                tv_weight.setText(weight);
                tv_feed.setText(feed_name);
                tv_med.setText(med_name);
                tv_tag.setText(tag_label);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        viewD.dismiss();
                    }
                });

                // Make dialog box visible.
                viewD.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pig_id = ((TextView)
                        view.findViewById(R.id.tv_pig)).getText().toString().trim();

                // Create Object of Dialog class
                final Dialog longViewD = new Dialog(ViewListOfPigs.this);
                // Set GUI of login screen
                longViewD.setContentView(R.layout.dialog_longclick);
                longViewD.setTitle("What else to do?");
                longViewD.setCancelable(false);

                // Init button of login GUI
                Button btn_weight = (Button) longViewD.findViewById(R.id.db_btn_weight);
                Button btn_update = (Button) longViewD.findViewById(R.id.db_btn_update);
                LinearLayout bl = (LinearLayout) longViewD.findViewById(R.id.bottom_container);
                tv_drag = (TextView) longViewD.findViewById(R.id.tv_dragHere);
                bl.setOnDragListener(ViewListOfPigs.this);

                btn_weight.setOnTouchListener(ViewListOfPigs.this);
                btn_update.setOnTouchListener(ViewListOfPigs.this);

                longViewD.show();

                return true;
            }
        });

        loadLists();
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
                Intent i = new Intent(ViewListOfPigs.this, ChooseViewPen.class);
                i.putExtra("house_id", house_id);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> pigs = db.getPigsByPen(pen);

        pig_list = new ArrayList<>();
        for(int i = 0;i < pigs.size();i++)
        {
            HashMap<String, String> c = pigs.get(i);
            HashMap<String, String> b = new HashMap<>();
            b.put(KEY_LABEL, "Pig Label: " + getLabel(c.get(KEY_PIGID)));
            b.put(KEY_PIGID, c.get(KEY_PIGID));
            b.put(KEY_BREED, "Breed: " + c.get(KEY_BREED));
            b.put(KEY_GENDER, "Gender: " + c.get(KEY_GENDER));

            pig_list.add(b);
        }

        final ListAdapter adapter = new SimpleAdapter(
                ViewListOfPigs.this, pig_list,
                R.layout.pig_list, new String[] {
                KEY_PIGID, KEY_LABEL, KEY_BREED, KEY_GENDER}, new int[] {
                R.id.tv_pig, R.id.tv_pig_label, R.id.tv_breed, R.id.tv_gender});

         lv.setAdapter(adapter);

        et_searchPig.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub
                ((Filterable) adapter).getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
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
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                String choice = view.findViewById(id).getTag().toString();

                Log.d(LOGCAT, "Dropped " + choice);

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                    if(choice.equals("weight")){

                        Intent i = new Intent(ViewListOfPigs.this, PigWeightRecord.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("pig_id", pig_id);
                        i.putExtra("house_id", house_id);
                        i.putExtra("pen", pen);
                        startActivity(i);
                        finish();
                    } else if(choice.equals("update")) {

                        Intent i = new Intent(ViewListOfPigs.this, UpdatePigActivity.class);
                        i.putExtra("pig_id", pig_id);
                        i.putExtra("house_id", house_id);
                        i.putExtra("pen", pen);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }

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

        Intent i = new Intent(ViewListOfPigs.this, ChooseViewPen.class);
        i.putExtra("house_id", house_id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
