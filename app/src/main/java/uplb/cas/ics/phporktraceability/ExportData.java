package uplb.cas.ics.phporktraceability;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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

    // Table Weight Record
    private static final String KEY_RECDATE = "record_date";
    private static final String KEY_RECTIME = "record_time";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_REMARKS = "remarks";

    // Table Feeds
    private static final String KEY_FEEDID = "feed_id";

    // Table Feed Transaction
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_DATEGIVEN = "date_given";
    private static final String KEY_TIMEGIVEN = "time_given";
    private static final String KEY_PRODDATE = "prod_date";

    // Table Medication
    private static final String KEY_MEDID = "med_id";
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

        db = new SQLiteHandler(getApplicationContext());
        //db.deleteTables();

        int status = NetworkUtil.getConnectivityStatus(getApplicationContext());
        if(status == 0){
            Toast.makeText(ExportData.this, "Not connected to internet.",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(ExportData.this, HomeActivity.class);
            startActivity(i);
            finish();

            return;
        }

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

        sendDataToServer();

    }

    private void sendDataToServer() {

        final String tag_string_send = "send_alldata";

        ArrayList<HashMap<String, String>> weight_records = db.getWeightRecs();
        ArrayList<HashMap<String, String>> feed_records = db.getFeedRecs();
        ArrayList<HashMap<String, String>> med_records = db.getMedRecs();
        ArrayList<HashMap<String, String>> newPigs = db.getNewPigs();

        ArrayList<HashMap<String, String>> updatePigs = db.getUpdatedPigs();
        final JSONObject allData = new JSONObject();

        try{

            JSONArray wr_jsonArray = new JSONArray();
            JSONArray ft_jsonArray = new JSONArray();
            JSONArray mr_jsonArray = new JSONArray();
            JSONArray pig_jsonArray = new JSONArray();

            for(int i = 0;i < weight_records.size();i++){
                HashMap<String, String> data = weight_records.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_RECDATE, data.get(KEY_RECDATE));
                jsonObjectData.put(KEY_RECTIME, data.get(KEY_RECTIME));
                jsonObjectData.put(KEY_WEIGHT, data.get(KEY_WEIGHT));
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_REMARKS, data.get(KEY_REMARKS));
                wr_jsonArray.put(jsonObjectData);
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
            }

            for(int i = 0;i < newPigs.size();i++){
                HashMap<String, String> data = newPigs.get(i);
                JSONObject jsonObjectData = new JSONObject();
                jsonObjectData.put(KEY_PIGID, data.get(KEY_PIGID));
                jsonObjectData.put(KEY_BOARID, data.get(KEY_BOARID));
                jsonObjectData.put(KEY_SOWID, data.get(KEY_SOWID));
                jsonObjectData.put(KEY_FOSTER, data.get(KEY_FOSTER));
                jsonObjectData.put(KEY_WEEKF, data.get(KEY_WEEKF));
                jsonObjectData.put(KEY_GENDER, data.get(KEY_GENDER));
                jsonObjectData.put(KEY_FARROWING, data.get(KEY_FARROWING));
                jsonObjectData.put(KEY_PIGSTAT, data.get(KEY_PIGSTAT));
                jsonObjectData.put(KEY_PENID, data.get(KEY_PENID));
                jsonObjectData.put(KEY_BREEDID, data.get(KEY_BREEDID));
                pig_jsonArray.put(jsonObjectData);
            }
            /*
            jsonObject.put("weight_record", wr_jsonArray);
            jsonObject.put("feed_transaction", ft_jsonArray);
            jsonObject.put("med_record", mr_jsonArray);
            jsonObject.put("pig", newPigs);

            allDataArray.put(jsonObject);

            allData.put("response", allDataArray);

            */

            allData.put("weight_record", wr_jsonArray);
            allData.put("feed_transaction", ft_jsonArray);
            allData.put("med_record", mr_jsonArray);
            allData.put("pig", pig_jsonArray);

            Toast.makeText(this, allData.toString(), Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_SENDNEWDATA, allData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Getting response : " + response.toString());

                        try {
                            //JSONObject resp = new JSONObject(response);
                            boolean error = response.getBoolean("error");

                            if(error){
                                String errorMsg = response.getString("error_msg");
                                Toast.makeText(ExportData.this, errorMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExportData.this, "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Log.d(TAG, "Response error: " + error.getMessage());
                    Toast.makeText(ExportData.this, error.getMessage(),
                            Toast.LENGTH_LONG).show();

                }catch (NullPointerException ex){}
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

    @Override
    public void run()
    {
        try
        {
            //Obtain the thread's token
            synchronized (thread)
            {
                //While the counter is smaller than four
                while(counter <= 5)
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
                            progressDialog.setProgress(counter*20);
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
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                //Close the progress dialog
                progressDialog.hide();

                Intent i = new Intent(ExportData.this, ChooseModule.class);
                startActivity(i);
                finish();

            }
        });

        //Try to "kill" the thread, by interrupting its execution
        synchronized (thread)
        {
            thread.interrupt();
        }
    }
}
