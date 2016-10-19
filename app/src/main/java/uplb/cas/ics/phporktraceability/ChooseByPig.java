package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.SQLiteHandler;
import helper.TestSessionManager;

/**
 * Created by marmagno on 7/26/2016.
 */
public class ChooseByPig extends AppCompatActivity  {

    private static final String LOGCAT = ChooseByPig.class.getSimpleName();

    //Module
    private static final String FEED_MOD = "Feed Pig";

    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";

    SQLiteHandler db;
    String pen = "";
    String house_id = "";
    String feed_id = "";
    String med_id = "";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button btn_submit;

    private List<CheckItemModel> chklist;

    String selection = "selection";
    String module = "module";

//    TestSessionManager test;
//    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        test = new TestSessionManager(getApplicationContext());
//        HashMap<String, Integer> testuser = test.getCount();
//        count = testuser.get(TestSessionManager.KEY_COUNT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        pen = i.getStringExtra("pen");
        house_id = i.getStringExtra("house_id");
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");
        if(module.equals(FEED_MOD)) {
            getSupportActionBar().setTitle(R.string.feed);
            feed_id = i.getStringExtra("feed_id");
        }
        else {
            getSupportActionBar().setTitle(R.string.med);
            med_id = i.getStringExtra("med_id");
        }

        db = SQLiteHandler.getInstance();

        btn_submit = (Button) findViewById(R.id.btn_submit);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadLists();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CheckItemModel> checkItemModel =
                        ((RecyclerAdapterWithCheckBox) adapter).getCheckList();

                ArrayList<String> selectedItems = new ArrayList<>();
                for(int i = 0;i < checkItemModel.size();i++){
                    CheckItemModel item = checkItemModel.get(i);
                    if(item.isSelected()){
                        selectedItems.add(item.getID());
                    }
                }

                if (selectedItems.size() > 0) {

                    String[] selected_pigs = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        selected_pigs[i] = selectedItems.get(i);
                    }
                    Intent i = new Intent();
                    if(module.equals(FEED_MOD)) {
                        i.setClass(ChooseByPig.this, AddFeedPig.class);
                        i.putExtra("feed_id", feed_id);
                    }
                   else {
                        i.setClass(ChooseByPig.this, AddMedPig.class);
                        i.putExtra("med_id", med_id);
                    }

                    i.putExtra("selection", selection);
                    i.putExtra("module", module);
                    i.putExtra("pigs", selected_pigs);
                    i.putExtra("house_id", house_id);
                    i.putExtra("pen", pen);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please select pig(s).", Snackbar.LENGTH_LONG);

                    snackbar.show();
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
            case android.R.id.home:
//                count++;
//                test.updateCount(count);

                Intent i = new Intent(ChooseByPig.this, ChoosePen.class);
                if(module.equals(FEED_MOD))
                    i.putExtra("feed_id", feed_id);
                else
                    i.putExtra("med_id", med_id);

                i.putExtra("house_id", house_id);
                i.putExtra("selection", selection);
                i.putExtra("module", module);
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
        chklist = new ArrayList<>();

        for(int i = 0;i < pigs.size();i++)
        {
            HashMap<String, String> c = pigs.get(i);

            CheckItemModel chkitem =
                    new CheckItemModel(
                            c.get(KEY_PIGID),
                            "Pig Label: " + c.get(KEY_LABEL),
                            "Breed: " + c.get(KEY_BREED),
                            false);
            chklist.add(chkitem);
        }

        adapter = new RecyclerAdapterWithCheckBox(chklist);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(ChooseByPig.this, ChoosePen.class);
        if(module.equals(FEED_MOD))
            i.putExtra("feed_id", feed_id);
        else
            i.putExtra("med_id", med_id);

        i.putExtra("house_id", house_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }
}
