package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

/**
 * Created by marmagno on 7/20/2016.
 */
public class ChooseFeedPens extends AppCompatActivity {

    private static final String LOGCAT = ChooseFeedPens.class.getSimpleName();

    public static final String KEY_PENID = "pen_id";
    public static final String KEY_PENNO = "pen_no";
    public static final String KEY_FUNC = "function";

    SQLiteHandler db;
    String house_id = "";
    String feed_id = "";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button btn_submit;

    private List<CheckItemModel> chklist;

    String selection = "selection";
    String module = "module";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosepens);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        Intent i = getIntent();
        house_id = i.getStringExtra("house_id");
        feed_id = i.getStringExtra("feed_id");
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");

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

                    String[] selected_pens = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        selected_pens[i] = selectedItems.get(i);
                    }

                    Intent i = new Intent(ChooseFeedPens.this, AddFeedPig.class);
                    i.putExtra("selection", selection);
                    i.putExtra("module", module);
                    i.putExtra("pens", selected_pens);
                    i.putExtra("house_id", house_id);
                    i.putExtra("feed_id", feed_id);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(ChooseFeedPens.this, "Please select pigs to feed.",
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
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(ChooseFeedPens.this, ChooseFeedHousePage.class);
                i.putExtra("feed_id", feed_id);
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

        ArrayList<HashMap<String, String>> pens = db.getPensByHouse(house_id);
        chklist = new ArrayList<>();

        for(int i = 0;i < pens.size();i++)
        {
            HashMap<String, String> c = pens.get(i);

            CheckItemModel chkitem =
                    new CheckItemModel(
                            c.get(KEY_PENID),
                            "Pig No: " + c.get(KEY_PENNO),
                            "Function: " + c.get(KEY_FUNC),
                            false);
            chklist.add(chkitem);
        }

        adapter = new RecyclerAdapterWithCheckBox(chklist);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(ChooseFeedPens.this, ChooseFeedHousePage.class);
        i.putExtra("feed_id", feed_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}

