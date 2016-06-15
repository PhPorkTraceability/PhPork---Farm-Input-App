package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/26/2015.
 */

public class ChooseFeedPigs extends AppCompatActivity
        implements View.OnClickListener{

    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";
    public static final String KEY_GENDER = "gender";
    private static final String LOGCAT = ChooseFeedPigs.class.getSimpleName();
    ArrayList<HashMap<String, String>> pig_list;
    SQLiteHandler db;
    String pen = "";
    String house_id = "";
    String feed_id = "";
    private Toolbar toolbar;
    private ListView lv;
    //private Button btn_submit;
    private ListAdapter adapter;

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
        feed_id = i.getStringExtra("feed_id");

        db = new SQLiteHandler(getApplicationContext());

        //btn_submit = (Button) findViewById(R.id.btn_submit);
        lv = (ListView) findViewById(R.id.listview);

        loadLists();

        /*
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);

                    HashMap<String, String> item =
                            (HashMap<String, String>) adapter.getItem(position);
                    if (checked.valueAt(i))
                        selectedItems.add(item.get(KEY_PIGID));
                }

                if (selectedItems.size() > 0) {

                    String[] selected_pigs = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        selected_pigs[i] = selectedItems.get(i);
                    }

                    Intent i = new Intent(ChooseFeedPigs.this, AddFeedPig.class);
                    i.putExtra("pigs", selected_pigs);
                    i.putExtra("house_id", house_id);
                    i.putExtra("pen", pen);
                    i.putExtra("feed_id", feed_id);
                    startActivity(i);
                } else {
                    Toast.makeText(ChooseFeedPigs.this,
                            "Please select pigs to feed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        */

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
                Intent i = new Intent(ChooseFeedPigs.this, ChooseFeedPenPage.class);
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
                ChooseFeedPigs.this, pig_list,
                R.layout.feedpig_list, new String[]{
                KEY_PIGID, KEY_LABEL, KEY_BREED, KEY_GENDER}, new int[]{
                R.id.tv_pig, R.id.tv_pig_label, R.id.tv_breed, R.id.tv_gender});

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

    @Override
    public void onClick(View v) {

    }
}
