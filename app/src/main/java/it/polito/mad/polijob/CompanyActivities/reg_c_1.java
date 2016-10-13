package it.polito.mad.polijob.CompanyActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.StudentActivities.reg_s_2;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.model.StudentStruct;

public class reg_c_1 extends Activity implements PoliJobDB.SignUpCallback, View.OnClickListener {


    EditText companynameEdittxt;
    EditText emailEditText;
    EditText passwordEditText;
    EditText cityEditText;
    Spinner country;
    Spinner department;

    CompanyStruct companyStruct;
    ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_company_page1);

        waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage("wait");
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setCancelable(false);
        waitingDialog.setIndeterminate(true);

        companynameEdittxt = (EditText) findViewById(R.id.comname_editText);
        emailEditText = (EditText) findViewById(R.id.companyregister_email_editText);
        passwordEditText = (EditText) findViewById(R.id.companyregister_password_editText);
        cityEditText = (EditText) findViewById(R.id.company_city_editText);
        department = (Spinner) findViewById(R.id.spinner_department);
        country = (Spinner) findViewById(R.id.spinner_country);


        Button signUpButton = (Button) findViewById(R.id.register_company_signup_button);
        signUpButton.setOnClickListener(this);

                //startActivity(new Intent(reg_c_1.this, reg_c_2.class));

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }

    @Override
    public void onClick(View v) {

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
        user.put("type","company");

        companyStruct = new CompanyStruct();

        companyStruct.companyNametxt = (companynameEdittxt.getText().toString());
        companyStruct.citytxt = (cityEditText.getText().toString());
        companyStruct.departmenttxt=(department.getSelectedItem().toString());
        companyStruct.countrytxt=(country.getSelectedItem().toString());
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

        Company stu = new Company();
        ParseUser.getCurrentUser().put("relationC", stu);
        ParseUser.getCurrentUser().saveInBackground();

        Toast.makeText(getApplicationContext(),
                "you are signed up",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(reg_c_1.this, reg_c_2.class);

        intent.putExtra("companyStruct", companyStruct);
        startActivity(intent);
        finish();
    }

}