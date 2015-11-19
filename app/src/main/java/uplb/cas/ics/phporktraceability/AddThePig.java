package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/14/2015.
 */
public class AddThePig extends AppCompatActivity {

    private static final String LOGCAT = AddThePig.class.getSimpleName();
    private static final String KEY_PENNO = "pen_no";
    private static final String KEY_FUNC = "function";

    EditText et_weight;
    Button btn_addpig;
    Button btn_finish;
    TextView tv_group;
    TextView tv_boar;
    TextView tv_sow;
    TextView tv_breed;
    TextView tv_weekf;
    TextView tv_gender;
    TextView tv_rfid;
    TextView tv_pen;
    TextView tv_pigid;

    SQLiteHandler db;
    String pen = "";
    String rfid = "";
    String gender = "";
    String boar_id = "";
    String sow_id = "";
    String group_label = "";
    String breed = "";
    String week_farrowed = "";
    String weight = "";
    String pig_id = "";
    String pen_disp = "";
    String rfid_disp = "";
    String breed_name = "";
    int curID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addthepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        boar_id = i.getStringExtra("boar_id");
        sow_id = i.getStringExtra("sow_id");
        group_label = i.getStringExtra("group_label");
        breed = i.getStringExtra("breed");
        week_farrowed = i.getStringExtra("week_farrowed");
        gender = i.getStringExtra("gender");
        rfid = i.getStringExtra("rfid");
        pen = i.getStringExtra("pen");

        db = new SQLiteHandler(getApplicationContext());

        curID = db.getMaxPigID();
        curID++;
        pig_id = getLabel(String.valueOf(curID));
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_boar = (TextView) findViewById(R.id.tv_boar);
        tv_sow = (TextView) findViewById(R.id.tv_sow);
        tv_breed = (TextView) findViewById(R.id.tv_breed);
        tv_weekf = (TextView) findViewById(R.id.tv_week);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_rfid = (TextView) findViewById(R.id.tv_rfid);
        tv_pen = (TextView) findViewById(R.id.tv_pen);
        tv_pigid = (TextView) findViewById(R.id.tv_pig);

        Resources res = getResources();
        String[] arr = res.getStringArray(R.array.pig_breeds);
        breed_name = arr[Integer.parseInt(breed) - 1];
        rfid_disp = db.getRFID(rfid);
        displayPen(pen);

        tv_group.setText("Group Label: " + group_label);
        tv_boar.setText("Boar Parent: " + getLabel(boar_id));
        tv_sow.setText("Sow Parent: " + getLabel(sow_id));
        tv_breed.setText("Breed: " + breed_name);
        tv_weekf.setText("Week Farrowed: " + week_farrowed);
        tv_gender.setText("Gender: " + gender);
        tv_rfid.setText("RFID: " + rfid_disp);
        tv_pen.setText(pen_disp);
        tv_pigid.setText("Pig Label: " + pig_id);

        btn_addpig = (Button) findViewById(R.id.btn_addPig);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        et_weight = (EditText) findViewById(R.id.et_weight);

        btn_addpig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_weight.getText().toString().trim().length() > 0){
                    weight = et_weight.getText().toString();
                    db.addPig(String.valueOf(curID), boar_id, sow_id, gender, "", "weaning",
                            pen, breed);
                    db.assignPigTag(rfid, pig_id, String.valueOf(curID));
                    db.updateTag(rfid, "Active");
                    Intent i = new Intent(AddThePig.this, WeekFarrowedPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("breed", breed);
                    i.putExtra("boar_id", boar_id);
                    i.putExtra("sow_id", sow_id);
                    i.putExtra("group_label", group_label);
                    startActivity(i);
                    finish();

                    Toast.makeText(AddThePig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(AddThePig.this, "Please Enter the weight before proceeding.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_weight.getText().toString().trim().length() > 0) {
                    weight = et_weight.getText().toString();
                    db.addPig(String.valueOf(curID), boar_id, sow_id, gender, "", "weaning",
                            pen, breed);
                    db.assignPigTag(rfid, pig_id, String.valueOf(curID));
                    db.updateTag(rfid, "Active");
                    Intent i = new Intent(AddThePig.this, WeaningPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                    Toast.makeText(AddThePig.this, "Added Successfully.",
                            Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(AddThePig.this, "Please Enter the weight before proceeding.",
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayPen(String _pen){

        ArrayList<HashMap<String, String>> the_list = db.getPen(_pen);

        for(int i = 0;i < the_list.size();i++)
        {
            HashMap<String, String> c = the_list.get(i);

            pen_disp = "Pen: " + c.get(KEY_PENNO) + " -> Function: " + c.get(KEY_FUNC);
        }
    }

    private String getLabel(String _id){
        String result = "";

        int size = _id.length();
        String s = "0";
        size = 10 - size;
        for(int i = 0; i < size;i++){
            s = s + "0";
        }
        s = s + _id;
        String temp1 = s.substring(0,4);
        String temp2 = s.substring(5,9);
        String temp3 = s.substring(9);
        result = temp1 + "-" + temp2 + "-" +temp3;
        return result;
    }

    @Override
    public void onBackPressed(){ super.onBackPressed(); finish(); }
}
