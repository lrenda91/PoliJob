package it.polito.mad.polijob.StudentActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.model.StudentStruct;

public class reg_s_1 extends Activity implements PoliJobDB.SignUpCallback, View.OnClickListener {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText cityEditText;
    Spinner country;

    StudentStruct studentStruct;
    ProgressDialog waitingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_student_page1);

        waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage("wait");
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setCancelable(false);
        waitingDialog.setIndeterminate(true);
        //user = new ParseUser();

        firstNameEditText = (EditText) findViewById(R.id.firstname_editText);
        lastNameEditText = (EditText) findViewById(R.id.lastname_editText);
        emailEditText = (EditText) findViewById(R.id.studentregister_email_editText);
        passwordEditText = (EditText) findViewById(R.id.studentregister_password_editText);
        cityEditText = (EditText) findViewById(R.id.student_city_editText);
        country = (Spinner) findViewById(R.id.spinner_country);

        Button signUpButton = (Button) findViewById(R.id.register_student_signup_button);
        signUpButton.setOnClickListener(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }

    @Override
    public void onClick(View v) {
        if (!Utility.networkIsUp(this)){
            Toast.makeText(getApplicationContext(),
                    "No network",
                    Toast.LENGTH_LONG).show();
            return;
        }

        EditText[] editTexts = {
            emailEditText, passwordEditText
        };
        for (EditText et : editTexts){
            if (et.getText() == null || et.getText().toString().isEmpty()){
                //track error on editText
                Toast.makeText(getApplicationContext(),
                        "Please complete the sign up form",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /* Now inputs are valid */

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        /* Now, try to sign up */
        ParseUser user = new ParseUser();

        user.setUsername(email);
        user.setPassword(password);
        user.put("type", "student");
        studentStruct = new StudentStruct();

        String emailtxt =(emailEditText.getText().toString());
        String passwordtxt = passwordEditText.getText().toString();
        studentStruct.firstNametxt=(firstNameEditText.getText().toString());
        studentStruct.lastNametxt = (lastNameEditText.getText().toString());
        studentStruct.citytxt=(cityEditText.getText().toString());
        studentStruct.countrytxt=(country.getSelectedItem().toString());

        PoliJobDB.signUpUser(user, this);


        waitingDialog.show();
    }

    @Override
    public void onSignUpException(ParseException pe) {
        waitingDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                pe.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
        waitingDialog.dismiss();


        Student stu = new Student();
        ParseUser.getCurrentUser().put("relation", stu);
        ParseUser.getCurrentUser().saveInBackground();

        Toast.makeText(getApplicationContext(),
                "you are signed up",
                Toast.LENGTH_LONG).show();

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String city = cityEditText.getText().toString();

        Intent intent = new Intent(reg_s_1.this, reg_s_2.class);
        intent.putExtra("studentStruct", studentStruct);
        startActivity(intent);
        finish();
    }
}