package uplb.cas.ics.phporktraceability;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import helper.SessionManager;

/**
 * Created by marmagno on 7/11/2016.
 */
public class ChooseSelection extends AppCompatActivity
        implements View.OnTouchListener, View.OnDragListener{

    private static final String LOGCAT = GrowingPage.class.getSimpleName();
    private static final String FEED_MOD = "Feed Pig";
    private static final String MED_MOD = "Medicate Pig";
    private Toolbar toolbar;
    private ImageView iv_bypig;
    private ImageView iv_bypen;
    private LinearLayout bot_cont;
    //private LinearLayout text_cont;

    String selection = "";
    String module = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_selection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        module = i.getStringExtra("module");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        iv_bypig = (ImageView) findViewById(R.id.iv_bypig);
        iv_bypen = (ImageView) findViewById(R.id.iv_bypen);
        //text_cont = (LinearLayout) findViewById(R.id.text_container);
        bot_cont = (LinearLayout) findViewById(R.id.bottom_container);

        iv_bypig.setOnTouchListener(this);
        iv_bypen.setOnTouchListener(this);
        bot_cont.setOnDragListener(this);

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
                Intent i = new Intent(ChooseSelection.this, ChooseModule.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        int action = e.getAction();
        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(LOGCAT, "Drag event started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(LOGCAT, "Drag event entered into "+ v.toString());
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(LOGCAT, "Drag event exited from " + v.toString());
                break;
            case DragEvent.ACTION_DROP:
                TextView tv_drag = (TextView) findViewById(R.id.tv_dragHere);
                TextView tv_subs = (TextView) findViewById(R.id.tv_subs);
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
                tv_subs.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                selection = findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){
                      /*
                        TextView tv_rem = (TextView) findViewById(R.id.tv_cf);
                        text_cont.removeView(tv_rem);
                        tv_rem.invalidate(); */
                    Toast.makeText(ChooseSelection.this, "Chosen " + selection.toUpperCase(),
                            Toast.LENGTH_LONG).show();

                    if(selection.equals("by_pig")) {
                        if(module.equals(FEED_MOD)){
                            Intent i = new Intent(ChooseSelection.this, ChooseFeedPage.class);
                            i.putExtra("module", module);
                            i.putExtra("selection", selection);
                            startActivity(i);
                            finish();
                        } else if(module.equals(MED_MOD)){
                            Intent i = new Intent(ChooseSelection.this, ChooseMedPage.class);
                            i.putExtra("module", module);
                            i.putExtra("selection", selection);
                            startActivity(i);
                            finish();
                        }
                    } else if(selection.equals("by_pen")) {
                        if(module.equals(FEED_MOD)){
                            Intent i = new Intent(ChooseSelection.this, ChooseFeedPage.class);
                            i.putExtra("module", module);
                            i.putExtra("selection", selection);
                            startActivity(i);
                            finish();
                        } else if(module.equals(MED_MOD)){
                            Intent i = new Intent(ChooseSelection.this, ChooseMedPage.class);
                            i.putExtra("module", module);
                            i.putExtra("selection", selection);
                            startActivity(i);
                            finish();
                        }
                    }
                }

                Log.d(LOGCAT, "Dropped " + selection);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOGCAT, "Drag ended");
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
        else { return false; }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(ChooseSelection.this, ChooseModule.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
