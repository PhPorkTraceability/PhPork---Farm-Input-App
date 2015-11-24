package uplb.cas.ics.phporktraceability;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ViewListOfPigs extends AppCompatActivity {

    private static final String LOGCAT = WeaningPage.class.getSimpleName();
    private Toolbar toolbar;
    private ListView lv;

    ArrayList<HashMap<String, String>> pig_list;
    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_GNAME = "group_name";
    public static final String KEY_BOAR = "boar_id";
    public static final String KEY_SOW = "sow_id";
    public static final String KEY_WEEKF = "week_farrowed";
    public static final String KEY_PENID = "pen_id";
    public static final String KEY_FUNCTION = "function";

    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_of_pigs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        db = new SQLiteHandler(getApplicationContext());

        lv = (ListView)findViewById(R.id.listview);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pig_id = ((TextView) view.findViewById(R.id.tv_pig)).getText().toString().trim();
                String label = ((TextView) view.findViewById(R.id.tv_pig_label)).getText().toString();

                Log.d(LOGCAT, "Pig_id: " + pig_id + " Label: " + label);

                String sow_id = "", boar_id = "", group_name = "",
                        week_farrowed = "", gender = "", breed = "",
                        pen_id = "", function = "";

                HashMap<String, String> c = db.getThePig(pig_id);
                HashMap<String, String> a = db.getPigGroup(pig_id);
                if(a.get(KEY_GNAME) == null && a.get(KEY_PENID) == null
                        && a.get(KEY_FUNCTION) == null){
                    group_name = "Group Label: none";
                    pen_id = "Pen ID: ";
                    function = "";
                } else {
                    group_name = "Group Label: " + a.get(KEY_GNAME);
                    pen_id = "Pen ID: " + a.get(KEY_PENID);
                    function = " -> " + a.get(KEY_FUNCTION);
                }
                if(c.get(KEY_BOAR).equals("null")) {
                    boar_id = "Boar Parent: ";
                } else {
                    boar_id = "Boar Parent: " + getLabel(c.get(KEY_BOAR));
                }
                if(c.get(KEY_SOW).equals("null")) {
                    sow_id = "Sow Parent: ";
                } else {
                    sow_id = "Sow Parent: " + getLabel(c.get(KEY_SOW));
                }
                week_farrowed = "Week Farrowed: " + c.get(KEY_WEEKF);
                gender = "Gender: " + c.get(KEY_GENDER);
                breed = "Breed: " + c.get(KEY_BREED);

                // Create Object of Dialog class
                final Dialog viewD = new Dialog(ViewListOfPigs.this);
                // Set GUI of login screen
                viewD.setContentView(R.layout.dialog_view_pig);
                viewD.setTitle("Viewing Pig Details");

                // Init button of login GUI
                Button btn_ok = (Button) viewD.findViewById(R.id.db_btn_ok);
                TextView tv_group = (TextView) viewD.findViewById(R.id.tv_group);
                TextView tv_pig = (TextView) viewD.findViewById(R.id.tv_pig);
                TextView tv_boar = (TextView) viewD.findViewById(R.id.tv_boar);
                TextView tv_sow = (TextView) viewD.findViewById(R.id.tv_sow);
                TextView tv_weekf = (TextView) viewD.findViewById(R.id.tv_weekF);
                TextView tv_gender = (TextView) viewD.findViewById(R.id.tv_gender);
                TextView tv_breed = (TextView) viewD.findViewById(R.id.tv_breed);
                TextView tv_pen = (TextView) viewD.findViewById(R.id.tv_pen);

                tv_group.setText(group_name);
                tv_pig.setText(label);
                tv_boar.setText(boar_id);
                tv_sow.setText(sow_id);
                tv_weekf.setText(week_farrowed);
                tv_gender.setText(gender);
                tv_breed.setText(breed);
                tv_pen.setText(pen_id + function);

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
                return false;
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
                Intent i = new Intent(ViewListOfPigs.this, WeaningPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> pigs = db.getPigs();

        pig_list = new ArrayList<>();
        for(int i = 0;i < pigs.size();i++)
        {
            HashMap<String, String> c = pigs.get(i);
            HashMap<String, String> b = new HashMap<>();
            b.put(KEY_LABEL, "Pig ID: " + getLabel(c.get(KEY_PIGID)));
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
    public void onBackPressed(){super.onBackPressed(); finish(); }
}
