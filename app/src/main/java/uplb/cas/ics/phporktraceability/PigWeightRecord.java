package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marmagno on 12/7/2015.
 */
public class PigWeightRecord extends AppCompatActivity {

    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_RECDATE = "record_date";
    public static final String KEY_REMARKS = "remarks";
    private static final String LOGCAT = PigWeightRecord.class.getSimpleName();
    Toolbar toolbar;
    LinearLayout rl;
    EditText et_newWeight;
    EditText et_remarks;
    Button btn_update;
    SQLiteHandler db;

    String pig_id = "";
    String house_id = "";
    String pen_id = "";
    DateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_graph);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        pig_id = i.getStringExtra("pig_id");
        house_id = i.getStringExtra("house_id");
        pen_id = i.getStringExtra("pen_id");

        db = SQLiteHandler.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                i.setClass(PigWeightRecord.this, ChooseModule.class);
                startActivity(i);
                finish();
            }
        });

        TextView pg_title = (TextView) toolbar.findViewById(R.id.page_title);
        pg_title.setText(R.string.weight_record);

        rl = (LinearLayout) findViewById(R.id.main_content);

        et_newWeight = (EditText) findViewById(R.id.et_updateWeight);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        btn_update = (Button) findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_newWeight.getText().length() > 0 &&
                        et_remarks.getText().length() > 0){
                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserSession();
                    String user_id = user.get(SessionManager.KEY_USERID);
                    String weight = et_newWeight.getText().toString().trim();
                    String date = curDate.format(dateObj);
                    String time = curTime.format(dateObj);
                    String remarks = et_remarks.getText().toString();
                    String sync_status = "new";

                    db.addWeightRecByAuto(weight, pig_id, date, time, remarks, user_id, sync_status);

                    Intent intent = new Intent(PigWeightRecord.this, PigWeightRecord.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("pig_id", pig_id);
                    intent.putExtra("house_id", house_id);
                    intent.putExtra("pen_id", pen_id);
                    finish();
                    startActivity(intent);
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Fill up details before proceeding", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }

    public void onStart(){
        super.onStart();

        loadGraph();
    }

    public void loadGraph() {

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
        XYSeries series = new XYSeries("Weight in kg");

        ArrayList<HashMap<String, String>> data = db.getWeightRec(pig_id);

        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(int i = 0;i < data.size();i++){
            HashMap<String, String> row = data.get(i);

            String weight = row.get(KEY_WEIGHT);
            String date = row.get(KEY_RECDATE);
            String remarks = row.get(KEY_REMARKS);

            //series.appendData(new DataPoint(Double.parseDouble(date),
            //Double.parseDouble(weight)), true, data.size());

            series.add(i, Double.parseDouble(weight));
            mRenderer.addXTextLabel(i, "Week " + date + "\n(" + remarks + ")");

        }

        // graph.addSeries(series);
        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series

        dataSet.addSeries(series);// number of series
        renderer.setColor(R.color.myGraph);
        renderer.setChartValuesTextSize(20);
        renderer.setDisplayChartValues(true);
        renderer.setDisplayChartValuesDistance(6);
        //renderer.setChartValuesSpacing((float) .10d);
        renderer.setLineWidth(2f);
        renderer.setDisplayBoundingPoints(true);

        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setChartTitle("WEIGHT RECORD");
        mRenderer.setYTitle("KILOGRAMS (kg)");
        mRenderer.setXTitle("\n\n\n\n\nYEAR WEEK No");
        mRenderer.setShowLegend(true);
        mRenderer.setFitLegend(true);
        mRenderer.setLegendHeight(50);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setAntialiasing(true);
        mRenderer.setScale(2f);
        mRenderer.setBarSpacing(1);   // adding spacing between the line or stacks
        mRenderer.setYAxisMin(0);
        mRenderer.setYAxisMax(150);
        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(3);
        mRenderer.setShowGridX(true);
        mRenderer.setShowGridY(true);
        mRenderer.setShowGrid(true);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setChartTitleTextSize(30);
        mRenderer.setLabelsTextSize(20);
        mRenderer.setLegendTextSize(20);
        mRenderer.setAxisTitleTextSize(20);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.LEFT);
        mRenderer.setTextTypeface("Georgia", Typeface.BOLD);
        mRenderer.setXLabels(0);
        //setting zoom buttons visiblity
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setZoomEnabled(false);
        mRenderer.setExternalZoomEnabled(false);
        mRenderer.setInScroll(false);
        mRenderer.setPanEnabled(true, false); // will fix the chart position
        //setting the margin size for the graph in the order top, left, bottom, right
        mRenderer.setMargins(new int[]{65, 50, 70, 50});

        GraphicalView chartView = ChartFactory.getBarChartView(getApplicationContext(),
                dataSet, mRenderer, BarChart.Type.DEFAULT);
        rl.removeAllViews();
        rl.addView(chartView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case android.R.id.home:
                Intent i = new Intent(PigWeightRecord.this, ViewListOfPigs.class);
                i.putExtra("house_id", house_id);
                i.putExtra("pen_id", pen_id);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(PigWeightRecord.this, ViewListOfPigs.class);
        i.putExtra("house_id", house_id);
        i.putExtra("pen_id", pen_id);
        startActivity(i);
        finish();
    }
}
