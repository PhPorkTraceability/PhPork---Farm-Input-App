package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/26/2015.
 */

public class ChooseMedPig extends AppCompatActivity {

    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";
    public static final String KEY_GENDER = "gender";
    private static final String LOGCAT = ChooseMedPig.class.getSimpleName();
    ArrayList<HashMap<String, String>> pig_list;
    SQLiteHandler db;
    String pen = "";
    String house_id = "";
    String med_id = "";
    private Toolbar toolbar;
    private ListView lv;
    private ListAdapter adapter;
    private EditText et_searchPig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        pen = i.getStringExtra("pen");
        house_id = i.getStringExtra("house_id");
        med_id = i.getStringExtra("med_id");

        db = SQLiteHandler.getInstance();

        et_searchPig = (EditText) findViewById(R.id.et_searchPig);
        lv = (ListView) findViewById(R.id.listview);
        lv.setTextFilterEnabled(true);

        loadLists();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String pig_id = ((TextView)
                        view.findViewById(R.id.tv_pig)).getText().toString().trim();

                final AlertDialog.Builder builder = new AlertDialog.Builder(ChooseMedPig.this);
                builder.setTitle("Chosen pig: " + getLabel(pig_id))
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(ChooseMedPig.this, AddMedPig.class);
                                i.putExtra("pig_id", pig_id);
                                i.putExtra("pen", pen);
                                i.putExtra("house_id", house_id);
                                i.putExtra("med_id", med_id);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                                finish();

                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("No", null);

                AlertDialog alert = builder.create();
                alert.show();


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
            case android.R.id.home:
                Intent i = new Intent(ChooseMedPig.this, ChooseMedPenPage.class);
                i.putExtra("med_id", med_id);
                i.putExtra("house_id", house_id);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadLists(){

        ArrayList<HashMap<String, String>> pigs = db.getPigsByPen(pen);

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

       adapter = new SimpleAdapter(
                ChooseMedPig.this, pig_list,
                R.layout.pig_list, new String[]{
                KEY_PIGID, KEY_LABEL, KEY_BREED, KEY_GENDER}, new int[]{
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
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(ChooseMedPig.this, ChooseMedPenPage.class);
        i.putExtra("med_id", med_id);
        i.putExtra("house_id", house_id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}
