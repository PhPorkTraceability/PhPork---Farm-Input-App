package uplb.cas.ics.phporktraceability;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by marmagno on 8/12/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private EditText username;
    private EditText password;
    private Button submit;

    private SessionManager session;
    private SQLiteHandler db;

    private Dialog loginD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = SQLiteHandler.getInstance();

        session = new SessionManager(getApplicationContext());

        loginD = new Dialog(this);
        loginD.setContentView(R.layout.dialog_login);
        loginD.setCancelable(false);
        loginD.setTitle("Login to Farm Android Traceability");

        username = (EditText) loginD.findViewById(R.id.et_username);
        password = (EditText) loginD.findViewById(R.id.et_password);
        submit = (Button) loginD.findViewById(R.id.btn_submit);

        loginD.show();

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(username.getText().length() > 0 &&
                password.getText().length() > 0){

            String _username = username.getText().toString();
            String _password = password.getText().toString();

            HashMap<String, String> user = db.getUser(_username, _password);
            String _user = user.get(KEY_USERNAME);
            String _pass = user.get(KEY_PASSWORD);

            if(!checkIfNull(_user).equals("") && !checkIfNull(_pass).equals("")){
                session.setLogin(_username, _password);

                Intent i = new Intent(LoginActivity.this, IntroSliderActivity.class);
                startActivity(i);
                finish();
            } else {
                Snackbar snackbar = Snackbar
                        .make(v, "Incorrect username/password. Try Again.", Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
        else{
            Snackbar snackbar = Snackbar
                    .make(v, "Fill up details before proceeding", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    private String checkIfNull(String _value){
        String result = "";
        if(_value != null && !_value.isEmpty() && !_value.equals("null")) return _value;
        else return result;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        loginD.dismiss();
    }
}
