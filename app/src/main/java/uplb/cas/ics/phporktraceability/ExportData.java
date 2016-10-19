package uplb.cas.ics.phporktraceability;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.NetworkUtil;
import helper.SQLiteHandler;

/**
 * Created by marmagno on 3/21/2016.
 */
public class ExportData extends Activity implements Runnable {

    private static final String TAG = ExportData.class.getSimpleName();
    private boolean tag_error = false;

    // Table Pen
    private static final String KEY_PENID = "pen_id";

    // Table Pig Breeds
    private static final String KEY_BREEDID = "breed_id";

    // Table Pig
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_BOARID = "boar_id";
    private static final String KEY_SOWID = "sow_id";
    private static final String KEY_FOSTER = "foster_sow";
    private static final String KEY_WEEKF = "week_farrowed";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_FARROWING = "farrowing_date";
    private static final String KEY_PIGSTAT = "pig_status";
    private static final String KEY_USER = "user";

    // Table Weight Record
    private static final String KEY_WRID = "record_id";
    private static final String KEY_RECDATE = "record_date";
    private static final String KEY_RECTIME = "record_time";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_REMARKS = "remarks";

    // Table Feeds
    private static final String KEY_FEEDID = "feed_id";

    // Table Feed Transaction
    private static final String KEY_FTID = "ft_id";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_DATEGIVEN = "date_given";
    private static final String KEY_TIMEGIVEN = "time_given";
    private static final String KEY_PRODDATE = "prod_date";

    // Table Med Record
    private static final String KEY_MRID = "mr_id";
    private static final String KEY_MEDID = "med_id";

    String[] pigs = {};
    String[] weights = {};
    String[] fed_lists = {};
    String[] med_lists = {};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_syncall);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();

        //Create a new progress dialog.
        progressDialog = new ProgressDialog(ExportData.this);
        //Set the progress dialog to display a horizontal bar .
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //Set the dialog title to 'Loading...'.
        progressDialog.setTitle("Exporting Data to Server.");
        //Set the dialog message to 'Loading application View, please wait...'.
        progressDialog.setMessage("Sending, please wait...");
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
            sendDataToServer();

        }
    }


    private void sendDataToServer() {
        final String tag_string_send = "send_alldata";

        ArrayList<HashMap<String, String>> weight_records = db.getWeightRecs();
        ArrayList<HashMap<String, String>> feed_records = db.getFeedRecs();
        ArrayList<HashMap<String, String>> med_records = db.getMedRecs();
        ArrayList<HashMap<String, String>> newPigs = db.getNewPigs();

        //ArrayList<HashMap<String, String>> updatePigs = db.getUpdatedPigs();
        final JSONObject allData = new JSONObject();

        try{

            JSONArray wr_jsonArray = new JSONArray();
            JSONArray ft_jsonArray = new JSONArray();
            JSONArray mr_jsonArray = new JSONArray();
            JSONArray pig_jsonArray = new JSONArray();

            pigs = new String[newPigs.size()];
            fed_lists = new String[feed_records.size()];
            weights = new String[weight_records.size()];
            med_lists = new String[med_records.size()];

            for(int i = 0;i < newPigs.size();i++){
                HashMap<String, String> data = newPigs.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_BOARID, data.get(KEY_BOARID));
                jsonObjectData.put(KEY_SOWID, data.get(KEY_SOWID));
                jsonObjectData.put(KEY_FOSTER, data.get(KEY_FOSTER));
                //jsonObjectData.put(KEY_WEEKF, data.get(KEY_WEEKF));
                jsonObjectData.put(KEY_GENDER, data.get(KEY_GENDER));
                //jsonObjectData.put(KEY_FARROWING, data.get(KEY_FARROWING));
                jsonObjectData.put(KEY_PIGSTAT, data.get(KEY_PIGSTAT));
                jsonObjectData.put(KEY_PENID, data.get(KEY_PENID));
                jsonObjectData.put(KEY_BREEDID, data.get(KEY_BREEDID));
                //jsonObjectData.put(KEY_USER, data.get(KEY_USER));
                pig_jsonArray.put(jsonObjectData);

                pigs[i] = data.get(KEY_PIGID);
            }

            for(int i = 0;i < weight_records.size();i++){
                HashMap<String, String> data = weight_records.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_RECDATE, data.get(KEY_RECDATE));
                jsonObjectData.put(KEY_RECTIME, data.get(KEY_RECTIME));
                jsonObjectData.put(KEY_WEIGHT, data.get(KEY_WEIGHT));
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_REMARKS, data.get(KEY_REMARKS));
                wr_jsonArray.put(jsonObjectData);

                weights[i] = data.get(KEY_WRID);
            }

            for(int i = 0;i < feed_records.size();i++){
                HashMap<String, String> data = feed_records.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_QUANTITY, data.get(KEY_QUANTITY));
                jsonObjectData.put(KEY_UNIT, data.get(KEY_UNIT));
                jsonObjectData.put(KEY_DATEGIVEN, data.get(KEY_DATEGIVEN));
                jsonObjectData.put(KEY_TIMEGIVEN, data.get(KEY_TIMEGIVEN));
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_FEEDID, data.get(KEY_FEEDID));
                jsonObjectData.put(KEY_PRODDATE, data.get(KEY_PRODDATE));
                ft_jsonArray.put(jsonObjectData);

                fed_lists[i] = data.get(KEY_FTID);
            }

            for(int i = 0;i < med_records.size();i++){
                HashMap<String, String> data = med_records.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_DATEGIVEN, data.get(KEY_DATEGIVEN));
                jsonObjectData.put(KEY_TIMEGIVEN, data.get(KEY_TIMEGIVEN));
                jsonObjectData.put(KEY_QUANTITY, data.get(KEY_QUANTITY));
                jsonObjectData.put(KEY_UNIT, data.get(KEY_UNIT));
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_MEDID, data.get(KEY_MEDID));
                mr_jsonArray.put(jsonObjectData);

                med_lists[i] = data.get(KEY_MRID);
            }

            /*
            jsonObject.put("weight_record", wr_jsonArray);
            jsonObject.put("feed_transaction", ft_jsonArray);
            jsonObject.put("med_record", mr_jsonArray);
            jsonObject.put("pig", newPigs);

            allDataArray.put(jsonObject);

            allData.put("response", allDataArray);

            */

            allData.put("pig", pig_jsonArray);
            allData.put("weight_record", wr_jsonArray);
            allData.put("feed_transaction", ft_jsonArray);
            allData.put("med_record", mr_jsonArray);

            //Toast.makeText(this, String.valueOf(allData), Toast.LENGTH_LONG).show();
            Log.d(TAG,  String.valueOf(allData));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_SENDNEWDATA, allData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Getting response : " + response.toString());

                        try {
                            //JSONObject resp = new JSONObject(response);
                            boolean error = response.getBoolean("error");

                            if(error){
                                tag_error = true;
                                String errorMsg = response.getString("error_msg");
                                Toast.makeText(ExportData.this, errorMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            tag_error = true;
                            e.printStackTrace();
                            Toast.makeText(ExportData.this, "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try{
                    Log.d(TAG, "Response error: " + error);
                    Toast.makeText(ExportData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                }catch (NullPointerException ex){}
                tag_error = true;
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);//, tag_string_send);

    }

    public void nextPage(){
        Intent i = new Intent();
        i.setClass(ExportData.this, ChooseModule.class);
        startActivity(i);
        finish();
    }

    @Override
    public void run()
    {
        try
        {
            //Obtain the thread's token
            synchronized (thread)
            {
                //While the counter is smaller than four
                while(counter <= 10)
                {
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
                            progressDialog.setProgress(counter*10);
                        }
                    });
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //This works just like the onPostExecute method from the AsyncTask class
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ExportData.this, ChooseModule.class);
                startActivity(i);
                finish();

                if (tag_error) {
                    new AlertDialog.Builder(ExportData.this)
                            .setTitle("Connection Failed")
                            .setMessage("Your phone cannot establish a connection to the server. " +
                                    "Want to store the database content on your phone?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: Do intense testing on this part
                                    exportOffline();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else{

                    for (String pig : pigs) {
                        db.updatePig(pig);
                    }

                    for (String weight : weights) {
                        db.updateWeightRec(weight);
                    }

                    for (String fed_list : fed_lists) {
                        db.updateFeedRec(fed_list);
                    }

                    for (String med_list : med_lists) {
                        db.updateMedRec(med_list);
                    }

                    nextPage();
                }

            }
        });

        //Try to "kill" the thread, by interrupting its execution
        synchronized (thread)
        {
            thread.interrupt();
        }
    }

    public void exportOffline() {
        /* code from: http://stackoverflow.com/questions/23527767/ */
        boolean hasPermissionWrite =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
        boolean hasPermissionRead =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;

        if (!(hasPermissionWrite || hasPermissionRead)) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            db.exportTables(this);
        }

        Intent i = new Intent(ExportData.this, ChooseModule.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    db.exportTables(this);
                }
                return;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Close the progress dialog
        progressDialog.dismiss();
    }
}
