package uplb.cas.ics.phporktraceability;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import app.AppConfig;
import app.AppController;
import helper.NetworkUtil;
import helper.SQLiteHandler;
import helper.SessionManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 11/10/2015.
 */
public class LoadSyncAll extends Activity {

    private static final String TAG = LoadSyncAll.class.getSimpleName();

    private static final String TABLE_USER = "user";
    private static final String TABLE_LOC = "location";
    private static final String TABLE_HOUSE = "house";
    private static final String TABLE_PEN = "pen";
    private static final String TABLE_GROUP = "pig_groups";
    private static final String TABLE_PARENT = "parents";
    private static final String TABLE_PIG = "pig";
    private static final String TABLE_RFID_TAGS = "rfid_tags";
    private static final String TABLE_WEIGHT = "weight_record";
    private static final String TABLE_FEEDS = "feeds";
    private static final String TABLE_FT = "feed_transaction";
    private static final String TABLE_MEDS = "medication";
    private static final String TABLE_MR = "med_record";
    private static final String TABLE_PB = "pig_breeds";

    // Table Farm Users
    private static final String KEY_USERNAME = "user_name";
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

    private static final String QUERY = "INSERT OR REPLACE INTO ";

    JSONArray users = null;
    JSONArray locs = null;
    JSONArray houses = null;
    JSONArray hPens = null;
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

    //A ProgressDialog View
    private ProgressDialog progressDialog;

    private AlertDialog alertDialog;

    SessionManager session;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();
        session = new SessionManager(getApplicationContext());

        //Create a new progress dialog.
        progressDialog = new ProgressDialog(LoadSyncAll.this);
        //Set the progress dialog to display a horizontal bar .
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Set the dialog title to 'Loading...'.
        progressDialog.setTitle("Setting Things Up.");
        //Set the dialog message to 'Loading application View, please wait...'.
        progressDialog.setMessage("Loading, please wait...");
        //This dialog can't be canceled by pressing the back key.
        progressDialog.setCancelable(false);
        //This dialog isn't indeterminate.
        progressDialog.setIndeterminate(true);
        //Display the progress dialog.
        progressDialog.show();

    }

    @Override
    public void onStart(){
        super.onStart();

        int status = NetworkUtil.getConnectivityStatus(getApplicationContext());
        if(status == 0) {
            displayAlert("Cannot establish connection to server.");
        }
        else {
            getAllDataByNet();
        }
    }

    // Called when there is connection to the server via Wi-Fi
    public void getAllDataByNet() {
//        final String tag_string_req = "req_alldata";

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
                                dataOperations(resp);
                                nextPage();
                            } else {
                                String errorMsg = resp.getString("error_message");
                                Log.e(TAG, "Error Response: " + errorMsg);
                            }
                        } catch (JSONException e) {
                            // JSON error
                            try {
                                e.printStackTrace();
                                Log.e(TAG, "JSON Error: " + resp.toString(3));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Volley error: " + error.getMessage());
                        displayAlert("Connection failed.");
                    }
                });

        _request.setRetryPolicy(new DefaultRetryPolicy(
                2000, //timeout in ms
                0, // no of max retries
                1)); // backoff multiplier

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(_request);//, tag_string_req);

    }

    public void dataOperations(JSONObject resp) throws JSONException {
        String sync_status = "old";
        String sql;
        SQLiteStatement stmt;
        SQLiteDatabase db2;

        sql = QUERY + TABLE_USER + " VALUES(?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        users = new JSONArray();
        users = resp.getJSONArray("user");

        for(int i = 0;i < users.length();i++)
        {
            JSONObject c = users.getJSONObject(i);

            // Now store the user in SQLite
            String username = c.getString(KEY_USERNAME);
            String password = c.getString(KEY_PASSWORD);
            String account = c.getString(KEY_ACCOUNT);

            stmt.bindString(1, String.valueOf(i+1));
            stmt.bindString(2, username);
            stmt.bindString(3, password);
            stmt.bindString(4, account);
            stmt.bindString(5, sync_status);

            stmt.execute();
            stmt.clearBindings();

        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_LOC + " VALUES(?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        locs = new JSONArray();
        locs = resp.getJSONArray("location");
        for(int i = 0;i < locs.length();i++)
        {
            JSONObject c = locs.getJSONObject(i);

            // Now store the user in SQLite
            String loc_id = c.getString(KEY_LOCID);
            String loc_name = c.getString(KEY_LOCNAME);
            String address = c.getString(KEY_ADD);

            stmt.bindString(1, loc_id);
            stmt.bindString(2, loc_name);
            stmt.bindString(3, address);
            stmt.bindString(4, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_HOUSE + " VALUES(?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, house_id);
            stmt.bindString(2, house_no);
            stmt.bindString(3, house_name);
            stmt.bindString(4, function);
            stmt.bindString(5, loc_id);
            stmt.bindString(6, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_PEN + " VALUES(?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, pen_id);
            stmt.bindString(2, pen_no);
            stmt.bindString(3, pen_func);
            stmt.bindString(4, house_id);
            stmt.bindString(5, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_PB + " VALUES(?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        pPigBreeds = new JSONArray();
        pPigBreeds = resp.getJSONArray("pig_breeds");
        for(int i = 0;i < pPigBreeds.length();i++)
        {
            JSONObject c = pPigBreeds.getJSONObject(i);

            // Now store the user in SQLite
            String breed_id = c.getString(KEY_BREEDID);
            String breed_name = c.getString(KEY_BREEDNAME);

            stmt.bindString(1, breed_id);
            stmt.bindString(2, breed_name);
            stmt.bindString(3, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_PARENT + " VALUES(?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        pigParents = new JSONArray();
        pigParents = resp.getJSONArray("parents");
        for(int i = 0;i < pigParents.length();i++)
        {
            JSONObject c = pigParents.getJSONObject(i);

            // Now store the user in SQLite
            String parent_id = c.getString(KEY_PARENTID);
            String label = c.getString(KEY_LABEL);
            String label_id = c.getString(KEY_LABELID);

            stmt.bindString(1, parent_id);
            stmt.bindString(2, label);
            stmt.bindString(3, label_id);
            stmt.bindString(4, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_PIG + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, pig_id);
            stmt.bindString(2, boar_id);
            stmt.bindString(3, sow_id);
            stmt.bindString(4, foster_sow);
            stmt.bindString(5, week_farrowed);
            stmt.bindString(6, gender);
            stmt.bindString(7, farrowing_date);
            stmt.bindString(8, pig_status);
            stmt.bindString(9, pen_id);
            stmt.bindString(10, breed_id);
            stmt.bindString(11, user);
            stmt.bindString(12, group_name);
            stmt.bindString(13, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_WEIGHT + " VALUES(?,?,?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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
            String user = c.getString(KEY_USER);

            stmt.bindString(1, record_id);
            stmt.bindString(2, record_date);
            stmt.bindString(3, record_time);
            stmt.bindString(4, weight);
            stmt.bindString(5, pig_id);
            stmt.bindString(6, remarks);
            stmt.bindString(7, user);
            stmt.bindString(8, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_RFID_TAGS + " VALUES(?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, tag_id);
            stmt.bindString(2, tag_rfid);
            stmt.bindString(3, pig_id);
            stmt.bindString(4, label);
            stmt.bindString(5, status);
            stmt.bindString(6, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_FEEDS + " VALUES(?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        feeds = new JSONArray();
        feeds = resp.getJSONArray("feeds");
        for(int i = 0;i < feeds.length();i++)
        {
            JSONObject c = feeds.getJSONObject(i);

            // Now store the user in SQLite
            String feed_id = c.getString(KEY_FEEDID);
            String feed_name = c.getString(KEY_FEEDNAME);
            String feed_type = c.getString(KEY_FEEDTYPE);

            stmt.bindString(1, feed_id);
            stmt.bindString(2, feed_name);
            stmt.bindString(3, feed_type);
            stmt.bindString(4, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_FT + " VALUES(?,?,?,?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, ft_id);
            stmt.bindString(2, quantity);
            stmt.bindString(3, unit);
            stmt.bindString(4, date_given);
            stmt.bindString(5, time_given);
            stmt.bindString(6, pig_id);
            stmt.bindString(7, feed_id);
            stmt.bindString(8, prod_date);
            stmt.bindString(9, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_MEDS + " VALUES(?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

        meds = new JSONArray();
        meds = resp.getJSONArray("medication");
        for(int i = 0;i < meds.length();i++)
        {
            JSONObject c = meds.getJSONObject(i);

            // Now store the user in SQLite
            String med_id = c.getString(KEY_MEDID);
            String med_name = c.getString(KEY_MEDNAME);
            String med_type = c.getString(KEY_MEDTYPE);

            stmt.bindString(1, med_id);
            stmt.bindString(2, med_name);
            stmt.bindString(3, med_type);
            stmt.bindString(4, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();

        sql = QUERY + TABLE_MR + " VALUES(?,?,?,?,?,?,?,?)";

        db2 = db.getWritableDatabase();
        db2.beginTransactionNonExclusive();

        stmt = db2.compileStatement(sql);

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

            stmt.bindString(1, mr_id);
            stmt.bindString(2, date_given);
            stmt.bindString(3, time_given);
            stmt.bindString(4, quantity);
            stmt.bindString(5, unit);
            stmt.bindString(6, pig_id);
            stmt.bindString(7, med_id);
            stmt.bindString(8, sync_status);

            stmt.execute();
            stmt.clearBindings();
        }

        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();
    }

    public void nextPage(){
        Intent i = new Intent();
        if (session.isLoggedIn()) {
            i.setClass(LoadSyncAll.this, HomeActivity.class);
        } else {
            i.setClass(LoadSyncAll.this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }

    public void displayAlert(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(LoadSyncAll.this);
        builder.setTitle(message)
                .setCancelable(false)
                .setMessage("Do you want to import offline?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Do intense testing on this part
                        importTables();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nextPage();
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
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
                            Log.d(TAG, "Importing Offline: " + ex.getMessage());
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
                Toast.makeText(LoadSyncAll.this, "Imported with warnings, check log for details", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoadSyncAll.this, "Imported with no errors", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Filename to import not found.");
            Toast.makeText(LoadSyncAll.this, "Filename to import not found.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error in importing tables, " + e.getMessage(), e);
            Toast.makeText(LoadSyncAll.this, "Something went wrong on importing database", Toast.LENGTH_SHORT).show();
        }

        nextPage();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Close all dialogs
        if(progressDialog.isShowing() || progressDialog != null)
            progressDialog.dismiss();
        if(alertDialog != null)
            alertDialog.dismiss();
    }

}