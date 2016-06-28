package uplb.cas.ics.phporktraceability;

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
        int show_help_page;
        super.onCreate(savedInstanceState);
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
        else{
            setContentView(R.layout.activity_home_help);
            Toast.makeText(HelpPage.this, "No help available", Toast.LENGTH_SHORT).show();
        }
    }

    public void close_page(View view){
       finish();
    }
}
