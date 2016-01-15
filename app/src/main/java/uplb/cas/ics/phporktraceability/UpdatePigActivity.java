package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 12/9/2015.
 */
public class UpdatePigActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static final String KEY_BOAR = "boar_id";
    public static final String KEY_SOW = "sow_id";
    public static final String KEY_FOSTER = "foster_sow";
    public static final String KEY_WEEKF = "week_farrowed";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BREED = "breed_name";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_PEN = "pen_id";
    public static final String KEY_FUNC = "function";
    public static final String KEY_TAGID = "tag_id";
    public static final String KEY_TAGRFID = "tag_rfid";
    public static final String KEY_TAGLABEL = "label";

    SQLiteHandler db;
    TextView tv_pig;
    TextView tv_boar;
    TextView tv_sow;
    TextView tv_foster;
    TextView tv_weekfarrowed;
    TextView tv_gender;
    TextView tv_breed;
    TextView tv_weight;
    TextView tv_pen;
    TextView tv_rfid;

    ImageView iv_boar;
    ImageView iv_sow;
    ImageView iv_foster;
    ImageView iv_weekfarrowed;
    ImageView iv_gender;
    ImageView iv_breed;
    ImageView iv_weight;
    ImageView iv_pen;
    ImageView iv_rfid;

    String pig_id = "";
    String boar_id = "";
    String sow_id = "";
    String foster_sow = "";
    String week_farrowed = "";
    String gender = "";
    String breed = "";
    String weight = "";
    String pen = "";
    String function = "";
    String tag_id = "";
    String label = "";
    String tag_rfid = "";

    String pig_disp = "Pig Label: ";
    String boar_disp = "Boar Parent: ";
    String sow_disp = "Sow Parent: ";
    String foster_disp = "Foster Sow: ";
    String weekf_disp = "Week Farrowed: ";
    String gender_disp = "Gender: ";
    String breed_disp = "Breed: ";
    String weight_disp = "Current Weight: ";
    String pen_disp = "Pen: ";
    String rfid = "Tag Label: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        pig_id = i.getStringExtra("pig_id");

        db = new SQLiteHandler(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        tv_pig = (TextView) findViewById(R.id.tv_piglabel);
        tv_boar = (TextView) findViewById(R.id.tv_boarid);
        tv_sow = (TextView) findViewById(R.id.tv_sowid);
        tv_foster = (TextView) findViewById(R.id.tv_foster);
        tv_weekfarrowed = (TextView) findViewById(R.id.tv_weekfarrowed);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_breed = (TextView) findViewById(R.id.tv_breed);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_pen = (TextView) findViewById(R.id.tv_pen);
        tv_rfid = (TextView) findViewById(R.id.tv_tag);

        iv_boar = (ImageView) findViewById(R.id.iv_editboar);
        iv_sow = (ImageView) findViewById(R.id.iv_editsow);
        iv_foster = (ImageView) findViewById(R.id.iv_editfoster);
        iv_weekfarrowed = (ImageView) findViewById(R.id.iv_editweekf);
        iv_gender = (ImageView) findViewById(R.id.iv_editgender);
        iv_breed = (ImageView) findViewById(R.id.iv_editbreed);
        iv_weight = (ImageView) findViewById(R.id.iv_editweight);
        iv_pen = (ImageView) findViewById(R.id.iv_editpen);
        iv_rfid = (ImageView) findViewById(R.id.iv_edittag);

        iv_boar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadDetails();
    }

    public void loadDetails()
    {
        HashMap<String, String> d = db.getThePig(pig_id);
        HashMap<String, String> p = db.getPigGroup(pig_id);
        boar_id = d.get(KEY_BOAR);
        sow_id = d.get(KEY_SOW);
        foster_sow = d.get(KEY_FOSTER);
        week_farrowed= d.get(KEY_WEEKF);
        gender = d.get(KEY_GENDER);
        breed = d.get(KEY_BREED);
        weight = d.get(KEY_WEIGHT);
        pen = d.get(KEY_PEN);
        tag_id = d.get(KEY_TAGID);
        tag_rfid = d.get(KEY_TAGRFID);
        label = d.get(KEY_TAGLABEL);

        pen = p.get(KEY_PEN);
        function = p.get(KEY_FUNC);

        if(!boar_id.equals("null"))
            boar_disp += getLabel(boar_id);
        if(!sow_id.equals("null"))
            sow_disp += getLabel(sow_id);
        if(!foster_sow.equals("null"))
            foster_disp += getLabel(foster_sow);
        pig_disp += getLabel(pig_id);
        weekf_disp += week_farrowed;
        gender_disp += gender;
        breed_disp += breed;
        weight_disp += weight + " kg";
        pen_disp += pen + " (" + function + ")";
        rfid += label + " (" + tag_rfid + ")";

        tv_pig.setText(pig_disp);
        tv_boar.setText(boar_disp);
        tv_sow.setText(sow_disp);
        tv_foster.setText(foster_disp);
        tv_weekfarrowed.setText(weekf_disp);
        tv_gender.setText(gender_disp);
        tv_breed.setText(breed_disp);
        tv_weight.setText(weight_disp);
        tv_pen.setText(pen_disp);
        tv_rfid.setText(rfid);
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
                Intent i = new Intent(UpdatePigActivity.this, ViewListOfPigs.class);
                startActivity(i);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

