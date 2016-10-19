package uplb.cas.ics.phporktraceability;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import app.AppConfig;
import app.AppController;
import helper.IntroSliderSession;
import helper.NetworkUtil;
import helper.SQLiteHandler;

/**
 * Created by marmagno on 11/10/2015.
 */
public class LoadSyncAll extends Activity implements Runnable{

    private static final String TAG = LoadSyncAll.class.getSimpleName();
    private boolean tag_error = false;

    // Table Farm Users
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ACCOUNT = "user_type";

    // Table Location
    private static final String KEY_LOCID = "loc_id";
    private static final String KEY_LOCNAME = "loc_name";
    private static final String KEY_ADD = "address";
    // Table House
    private static final String KEY_HOUSEID = "house_id";
    private static final String KEY_HOUSENO = "house_no";
    private static final String KEY_HNAME = "house_name";
    private static final String KEY_FUNCTION = "function";
    // Table Pen
    private static final String KEY_PENID = "pen_id";
    private static final String KEY_PENNO = "pen_no";
    // Table Pig Groups
    private static final String KEY_GNAME = "pig_batch";
    // Table Pig Breeds
    private static final String KEY_BREEDID = "breed_id";
    private static final String KEY_BREEDNAME = "breed_name";
    // Table Pig Parents
    private static final String KEY_PARENTID = "parent_id";
    private static final String KEY_LABELID = "label_id";
    // Table Pig
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_BOARID = "boar_id";
    private static final String KEY_SOWID = "sow_id";
    private static final String KEY_FOSTER = "foster_sow";
    private static final String KEY_WEEKF = "week_farrowed";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_FDATE = "farrowing_date";
    private static final String KEY_PIGSTAT = "pig_status";
    private static final String KEY_USER = "user";
    // Table Weight Record
    private static final String KEY_WRID = "record_id";
    private static final String KEY_RECDATE = "record_date";
    private static final String KEY_RECTIME = "record_time";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_REMARKS = "remarks";
    // Table RFID Tags
    private static final String KEY_TAGID = "tag_id";
    private static final String KEY_TAGRFID = "tag_rfid";
    private static final String KEY_TAGSTAT = "status";
    private static final String KEY_LABEL = "label";
    // Table Feeds
    private static final String KEY_FEEDID = "feed_id";
    private static final String KEY_FEEDNAME = "feed_name";
    private static final String KEY_FEEDTYPE = "feed_type";
    private static final String KEY_PRODDATE = "prod_date";
    // Table Feed Transaction
    private static final String KEY_FTID = "ft_id";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_DATEGIVEN = "date_given";
    private static final String KEY_TIMEGIVEN = "time_given";
    // Table Medication
    private static final String KEY_MEDID = "med_id";
    private static final String KEY_MEDNAME = "med_name";
    private static final String KEY_MEDTYPE = "med_type";
    // Table Med Record
    private static final String KEY_MRID = "mr_id";

    JSONArray users = null;
    JSONArray locs = null;
    JSONArray houses = null;
    JSONArray hPens = null;
    JSONArray pigGroups = null;
    JSONArray pPigBreeds = null;
    JSONArray pigParents = null;
    JSONArray pigs = null;
    JSONArray weightRecord = null;
    JSONArray tags = null;
    JSONArray feeds = null;
    JSONArray feedTransaction = null;
    JSONArray meds = null;
    JSONArray medRecord = null;
    SQLiteHandler db;
    //Initialize a counter integer to zero
    int counter = 0;
    private ProgressBar progressBar;
    //A ProgressDialog View
    private ProgressDialog progressDialog;
    //A thread, that will be used to execute code in parallel with the UI thread
    private Thread thread;
    //Create a Thread handler to queue code execution on a thread
    private Handler handler;

    IntroSliderSession introSession;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();
        //db.deleteTables();

        introSession = new IntroSliderSession(getApplicationContext());

        //Create a new progress dialog.
        progressDialog = new ProgressDialog(LoadSyncAll.this);
        //Set the progress dialog to display a horizontal bar .
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //Set the dialog title to 'Loading...'.
        progressDialog.setTitle("Setting Things Up.");
        //Set the dialog message to 'Loading application View, please wait...'.
        progressDialog.setMessage("Loading, please wait...");
        //This dialog can't be canceled by pressing the back key.
        progressDialog.setCancelable(false);
        //This dialog isn't indeterminate.
        progressDialog.setIndeterminate(false);
        //The maximum number of progress items is 100.
        progressDialog.setMax(100);
        //Set the current progress to zero.
        progressDialog.setProgress(0);
        //Display the progress dialog.
        progressDialog.show();

        //Initialize the handler
        handler = new Handler();
        //Initialize the thread
        thread = new Thread(this, "ProgressDialogThread");
        //start the thread
        thread.start();

    }

    @Override
    public void onStart(){
        super.onStart();

        int status = NetworkUtil.getConnectivityStatus(getApplicationContext());
        if(status == 0){
            tag_error = true;

        } else {
            getAllDataByNet();

        }


    }

    public void getAllDataByNet() {
        final String tag_string_req = "req_alldata";

        // Request a string response from the provided URL.
        final JsonObjectRequest _request = new JsonObjectRequest(AppConfig.URL_GETALLDATA, null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject resp) {

                        Log.d(TAG, "Getting tables : " + resp.toString());

                        try {
                            //JSONObject resp = new JSONObject(response);
                            boolean error = resp.getBoolean("error");

                            // Check for error node in json
                            if (!error) {

                                String sync_status = "old";

                                users = new JSONArray();
                                users = resp.getJSONArray("user");
                                for(int i = 0;i < users.length();i++)
                                {
                                    JSONObject c = users.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String username = c.getString(KEY_USERNAME);
                                    String password = c.getString(KEY_PASSWORD);
                                    String account = c.getString(KEY_ACCOUNT);

                                    db.addFarmUser(username, password, account);
                                }

                                locs = new JSONArray();
                                locs = resp.getJSONArray("location");
                                for(int i = 0;i < locs.length();i++)
                                {
                                    JSONObject c = locs.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String loc_id = c.getString(KEY_LOCID);
                                    String loc_name = c.getString(KEY_LOCNAME);
                                    String address = c.getString(KEY_ADD);

                                    db.addLoc(loc_id, loc_name, address);
                                }

                                houses = new JSONArray();
                                houses = resp.getJSONArray("house");
                                for(int i = 0;i < houses.length();i++)
                                {
                                    JSONObject c = houses.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String house_id = c.getString(KEY_HOUSEID);
                                    String house_no = c.getString(KEY_HOUSENO);
                                    String house_name = c.getString(KEY_HNAME);
                                    String function = c.getString(KEY_FUNCTION);
                                    String loc_id = c.getString(KEY_LOCID);

                                    db.addHouse(house_id, house_no, house_name, function, loc_id);
                                }

                                hPens = new JSONArray();
                                hPens = resp.getJSONArray("pen");
                                for(int i = 0;i < hPens.length();i++)
                                {
                                    JSONObject c = hPens.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String pen_id = c.getString(KEY_PENID);
                                    String pen_no = c.getString(KEY_PENNO);
                                    String pen_func = c.getString(KEY_FUNCTION);
                                    String house_id = c.getString(KEY_HOUSEID);

                                    db.addPen(pen_id, pen_no, pen_func, house_id);
                                }


                                /*
                                pigGroups = new JSONArray();
                                pigGroups = resp.getJSONArray("pig_groups");
                                for(int i = 0;i < pigGroups.length();i++)
                                {
                                    JSONObject c = pigGroups.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String group_name = c.getString(KEY_GNAME);
                                    String pen_id = c.getString(KEY_PENID);

                                    db.addGroup(group_name, pen_id);
                                }
                                */

                                pPigBreeds = new JSONArray();
                                pPigBreeds = resp.getJSONArray("pig_breeds");
                                for(int i = 0;i < pPigBreeds.length();i++)
                                {
                                    JSONObject c = pPigBreeds.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String breed_id = c.getString(KEY_BREEDID);
                                    String breed_name = c.getString(KEY_BREEDNAME);

                                    db.addBreed(breed_id, breed_name);
                                }

                                pigParents = new JSONArray();
                                pigParents = resp.getJSONArray("parents");
                                for(int i = 0;i < pigParents.length();i++)
                                {
                                    JSONObject c = pigParents.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String parent_id = c.getString(KEY_PARENTID);
                                    String label = c.getString(KEY_LABEL);
                                    String label_id = c.getString(KEY_LABELID);

                                    db.addParent(parent_id, label, label_id);
                                }

                                pigs = new JSONArray();
                                pigs = resp.getJSONArray("pig");
                                for(int i = 0;i < pigs.length();i++)
                                {
                                    JSONObject c = pigs.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String pig_id = c.getString(KEY_PIGID);
                                    String boar_id = c.getString(KEY_BOARID);
                                    String sow_id = c.getString(KEY_SOWID);
                                    String foster_sow = c.getString(KEY_FOSTER);
                                    String week_farrowed = c.getString(KEY_WEEKF);
                                    String gender = c.getString(KEY_GENDER);
                                    String farrowing_date = c.getString(KEY_FDATE);
                                    String pig_status = c.getString(KEY_PIGSTAT);
                                    String pen_id = c.getString(KEY_PENID);
                                    String breed_id = c.getString(KEY_BREEDID);
                                    String user = c.getString(KEY_USER);
                                    String group_name = c.getString(KEY_GNAME);

                                    db.addPig(pig_id, boar_id, sow_id, foster_sow, week_farrowed,
                                            gender, farrowing_date, pig_status, pen_id, breed_id,
                                            user, group_name, sync_status);
                                }

                                weightRecord = new JSONArray();
                                weightRecord = resp.getJSONArray("weight_record");
                                for(int i = 0;i < weightRecord.length();i++)
                                {
                                    JSONObject c = weightRecord.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String record_id = c.getString(KEY_WRID);
                                    String record_date = c.getString(KEY_RECDATE);
                                    String record_time = c.getString(KEY_RECTIME);
                                    String weight = c.getString(KEY_WEIGHT);
                                    String pig_id = c.getString(KEY_PIGID);
                                    String remarks = c.getString(KEY_REMARKS);

                                    db.addWeightRec(record_id, record_date, record_time,
                                            weight, pig_id, remarks, sync_status);
                                }

                                tags = new JSONArray();
                                tags = resp.getJSONArray("rfid_tags");
                                for(int i = 0;i < tags.length();i++)
                                {
                                    JSONObject c = tags.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String tag_id = c.getString(KEY_TAGID);
                                    String tag_rfid = c.getString(KEY_TAGRFID);
                                    String pig_id = c.getString(KEY_PIGID);
                                    String label = c.getString(KEY_LABEL);
                                    String status = c.getString(KEY_TAGSTAT);

                                    db.addTags(tag_id, tag_rfid, pig_id, label, status);
                                }

                                feeds = new JSONArray();
                                feeds = resp.getJSONArray("feeds");
                                for(int i = 0;i < feeds.length();i++)
                                {
                                    JSONObject c = feeds.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String feed_id = c.getString(KEY_FEEDID);
                                    String feed_name = c.getString(KEY_FEEDNAME);
                                    String feed_type = c.getString(KEY_FEEDTYPE);
                                    //String prod_date = c.getString(KEY_PRODDATE);

                                    db.addFeeds(feed_id, feed_name, feed_type);//, prod_date);
                                }

                                feedTransaction = new JSONArray();
                                feedTransaction = resp.getJSONArray("feed_transaction");
                                for(int i = 0;i < feedTransaction.length();i++)
                                {
                                    JSONObject c = feedTransaction.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String ft_id = c.getString(KEY_FTID);
                                    String quantity = c.getString(KEY_QUANTITY);
                                    String unit = c.getString(KEY_UNIT);
                                    String date_given = c.getString(KEY_DATEGIVEN);
                                    String time_given = c.getString(KEY_TIMEGIVEN);
                                    String pig_id = c.getString(KEY_PIGID);
                                    String feed_id = c.getString(KEY_FEEDID);
                                    String prod_date = c.getString(KEY_PRODDATE);

                                    db.feedPigRec(ft_id, quantity, unit, date_given, time_given,
                                            pig_id, feed_id, prod_date, sync_status);
                                }

                                meds = new JSONArray();
                                meds = resp.getJSONArray("medication");
                                for(int i = 0;i < meds.length();i++)
                                {
                                    JSONObject c = meds.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String med_id = c.getString(KEY_MEDID);
                                    String med_name = c.getString(KEY_MEDNAME);
                                    String med_type = c.getString(KEY_MEDTYPE);

                                    db.addMeds(med_id, med_name, med_type);
                                }

                                medRecord = new JSONArray();
                                medRecord = resp.getJSONArray("med_record");
                                for(int i = 0;i < medRecord.length();i++)
                                {
                                    JSONObject c = medRecord.getJSONObject(i);

                                    // Now store the user in SQLite
                                    String mr_id = c.getString(KEY_MRID);
                                    String date_given = c.getString(KEY_DATEGIVEN);
                                    String time_given = c.getString(KEY_TIMEGIVEN);
                                    String quantity = c.getString(KEY_QUANTITY);
                                    String unit = c.getString(KEY_UNIT);
                                    String pig_id = c.getString(KEY_PIGID);
                                    String med_id = c.getString(KEY_MEDID);

                                    db.addMedRec(mr_id, date_given, time_given, quantity, unit,
                                            pig_id, med_id, sync_status);
                                }

                            } else {

                                String errorMsg = resp.getString("error_msg");
                                Toast.makeText(LoadSyncAll.this, errorMsg, Toast.LENGTH_LONG).show();
                                tag_error = true;
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(LoadSyncAll.this, "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            try {
                                Log.e(TAG, "Response in string:\n" + resp.toString(3));
                            } catch(Exception ex) {}
                            tag_error = true;
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            Log.d(TAG, "Response error: " + error.getMessage());
                            Toast.makeText(LoadSyncAll.this, error.getMessage(),
                                    Toast.LENGTH_LONG).show();

                            tag_error = true;
                        }catch (NullPointerException ex){}
                    }
                });

        _request.setRetryPolicy(new DefaultRetryPolicy(
                0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(_request);//, tag_string_req);

    }

    public void nextPage(){
        Intent i = new Intent();
        if (introSession.isLoggedIn()) {
            i.setClass(LoadSyncAll.this, HomeActivity.class);
        } else {
            i.setClass(LoadSyncAll.this, IntroSliderActivity.class);
            //i.setClass(LoadSyncAll.this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }

    public void importTables() {
        String line1;
        String line2;
        int errors = 0;

        try {
            final String dbFileName = "phpork_in.sql";

            File dbFile = new File(Environment.getExternalStorageDirectory() , dbFileName);
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            SQLiteDatabase load_db;

            db.deleteTables();

            load_db = db.getWritableDatabase();

            while((line1=reader.readLine()) != null){
                if(line1.startsWith("INSERT")) {
                    while (true) {
                        boolean stop;

                        line2 = reader.readLine();
                        while (line2.startsWith("--")) {
                            line2 = reader.readLine();
                        }

                        stop = line2.endsWith(";");
                        line2 = line1 + line2.substring(0, line2.length()-1) + ";";

                        try {
                            load_db.execSQL(line2);
                        } catch(Exception ex) {
                            errors++;
                        }

                        if(stop) {
                            break;
                        }
                    }
                }
            }
            load_db.close();

            if(errors > 0) {
                Log.d(TAG, errors + " statements skipped.");
            }
            Toast.makeText(LoadSyncAll.this, "Successfully imported database", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Filename to import not found.");
            Toast.makeText(LoadSyncAll.this, "Filename to import not found.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error in importing tables", e);
            Toast.makeText(LoadSyncAll.this, "Something went wrong on importing database", Toast.LENGTH_SHORT).show();
        }

        nextPage();
    }

    @Override
    public void run() {
        try {
            //Obtain the thread's token
            synchronized (thread) {
                //While the counter is smaller than four
                while(counter <= 5) {
                    //Wait 1000 milliseconds
                    thread.wait(1000);
                    //Increment the counter
                    counter++;

                    //update the changes to the UI thread
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //Set the current progress.
                            progressDialog.setProgress(counter*20);
                        }
                    });
                }
            }
        }
        catch (InterruptedException e) { e.printStackTrace();}

        //This works just like the onPostExecute method from the AsyncTask class
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag_error) {
                    new AlertDialog.Builder(LoadSyncAll.this)
                            .setTitle("Connection Failed")
                            .setMessage("Your phone cannot establish a connection to the server. " +
                                    "Want to import offline?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: Do intense testing on this part
                                    importTables();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nextPage();
                                }
                            }).show();
                } else nextPage();
            }
        });

        //Try to "kill" the thread, by interrupting its execution
        synchronized (thread) {
            thread.interrupt();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Close the progress dialog
        progressDialog.dismiss();
    }
}
