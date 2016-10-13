package it.polito.mad.polijob;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import it.polito.mad.polijob.CompanyActivities.addPosition;
import java.io.IOException;
import java.io.InputStream;

import it.polito.mad.polijob.company.CompanyMainActivity;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.student.StudentMainActivity;


public class MainActivity extends Activity implements PoliJobDB.LogInCallback {

    private ProgressBar progressBar;
    private EditText username, password;
    private Button loginButton, signUpButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = (EditText) findViewById(R.id.email_editText);
        password = (EditText) findViewById(R.id.password_editText);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        loginButton = (Button) findViewById(R.id.login_button);
        signUpButton = (Button) findViewById(R.id.signup_loginpage_button);

        ParseObject loggedInUser = PoliJobDB.tryLocalLogin();
        if (loggedInUser != null){
            onLogInSuccess(loggedInUser);
        }



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), signupselect.class);
                startActivity(intent);
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!Utility.networkIsUp(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "Please connect to any network",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                setUserInteractionEnabled(false);
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                PoliJobDB.loginUser(usernameText, passwordText, MainActivity.this);

            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView welcomeText = (TextView) findViewById(R.id.textView);
        welcomeText.setTypeface(custom_font);
    }


    @Override
    public void onLogInException(ParseException pe) {
        setUserInteractionEnabled(true);
        Toast.makeText(getApplicationContext(),
                pe.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogInSuccess(ParseObject po) {
        setUserInteractionEnabled(true);
        assert po != null;
        Class nextActivityClass = (po instanceof Student) ?
                StudentMainActivity.class : CompanyMainActivity.class;
         intent = new Intent(MainActivity.this, nextActivityClass);
        // here I am getting the photos
        if(po instanceof Company) {
            Company com = (Company)po;
            if(com.getLogo()!=null) {
                PoliJobDB.getLogoCompany(com, new PoliJobDB.LogoCallback() {
                    @Override
                    public void onLogoSuccess() {
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                startActivity(intent);
                finish();
            }
        }else{
            Student com = (Student)po;
            if(com.getPhotoFile()!=null) {
                PoliJobDB.getLogoStudent(com, new PoliJobDB.LogoCallback() {
                    @Override
                    public void onLogoSuccess() {
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                startActivity(intent);
                finish();
            }
        }



    }



    private void setUserInteractionEnabled(boolean enabled){
        loginButton.setEnabled(enabled);
        signUpButton.setEnabled(enabled);
        username.setEnabled(enabled);
        password.setEnabled(enabled);
        progressBar.setVisibility(enabled ? View.INVISIBLE : View.VISIBLE);
    }

}
