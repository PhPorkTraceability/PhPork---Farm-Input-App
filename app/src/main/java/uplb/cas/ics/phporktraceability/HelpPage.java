package uplb.cas.ics.phporktraceability;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/*************************************
 **Created by JPMenguito on 6/23/16.**
 *******Centralized Help Source*******
 *************************************
 *Determines what "Help Page" to show**/
public class HelpPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int show_help_page;

        show_help_page=getIntent().getExtras().getInt("help_page", 0);

        if(show_help_page==1) {
            setContentView(R.layout.activity_home_help);
        }
        else if(show_help_page==2) {
            setContentView(R.layout.activity_location_help);
        }
        else if(show_help_page==3){
            setContentView(R.layout.activity_choose_module_help);
        }
        else if(show_help_page==4){
            setContentView(R.layout.edit_help);
        }
        else{
            setContentView(R.layout.activity_home_help);
            Toast.makeText(HelpPage.this, "No help available", Toast.LENGTH_SHORT).show();
        }
    }

    public void close_page(View view){
       finish();
    }
}
