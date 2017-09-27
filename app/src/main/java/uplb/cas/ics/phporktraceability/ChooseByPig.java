package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.SQLiteHandler;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 7/26/2016.
 */
public class ChooseByPig extends AppCompatActivity  {

    private static final String LOGCAT = ChooseByPig.class.getSimpleName();

    // Module
    private static final String FEED_MOD = "Feed Pig";

    public static final String KEY_PIGID = "pig_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_BREED = "breed_name";

    SQLiteHandler db;
    String pen_id = "";
    String house_id = "";
    String feed_id = "";
    String med_id = "";
    public CheckBox selectAll;
    private RecyclerView recyclerView;
    private RecyclerAdapterWithCheckBox adapter;
    TextView pg_title;

    String selection = "selection";
    String module = "module";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosepig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                i.setClass(ChooseByPig.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        pg_title = (TextView) toolbar.findViewById(R.id.page_title);

        db = SQLiteHandler.getInstance();

        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                    i.putExtra("pen_id", pen_id);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please select pig(s).", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });

        selectAll = (CheckBox) findViewById(R.id.cb_selectAll);

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectAll.isChecked()) {
                    adapter.selectAll();
                } else
                    adapter.unselectAll();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

        Intent i = getIntent();
        pen_id = i.getStringExtra("pen_id");
        house_id = i.getStringExtra("house_id");
        selection = i.getStringExtra("selection");
        module = i.getStringExtra("module");
        if(module.equals(FEED_MOD)) {
            pg_title.setText(R.string.feed);
            feed_id = i.getStringExtra("feed_id");
        }
        else {
            pg_title.setText(R.string.med);
            med_id = i.getStringExtra("med_id");
        }

        loadLists();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(ChooseByPig.this, ChoosePen.class);
                if(module.equals(FEED_MOD))
                    i.putExtra("feed_id", feed_id);
                else
                    i.putExtra("med_id", med_id);

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

    public void loadLists() {

        ArrayList<HashMap<String, String>> pigs = db.getPigsByPen(pen_id);
        if (pigs.size() > 0) {
            List<CheckItemModel> chklist = new ArrayList<>();

            for (int i = 0; i < pigs.size(); i++) {
                HashMap<String, String> c = pigs.get(i);

                CheckItemModel chkitem =
                        new CheckItemModel(
                                c.get(KEY_PIGID),
                                "Pig Label: " + c.get(KEY_LABEL),
                                "Breed: " + c.get(KEY_BREED),
                                false);
                chklist.add(chkitem);
            }

            adapter = new RecyclerAdapterWithCheckBox(chklist, selectAll);
            recyclerView.setAdapter(adapter);
        } else {
            final int SPLASH_TIME_OUT = 2000;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No data available.")
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
                                    Intent i = new Intent(ChooseByPig.this, ChoosePen.class);
                                    if(module.equals(FEED_MOD))
                                        i.putExtra("feed_id", feed_id);
                                    else
                                        i.putExtra("med_id", med_id);
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

    @Override
    public void onBackPressed(){
        Intent i = new Intent(ChooseByPig.this, ChoosePen.class);
        if(module.equals(FEED_MOD))
            i.putExtra("feed_id", feed_id);
        else
            i.putExtra("med_id", med_id);
        i.putExtra("house_id", house_id);
        i.putExtra("selection", selection);
        i.putExtra("module", module);
        startActivity(i);
        finish();
    }
}
