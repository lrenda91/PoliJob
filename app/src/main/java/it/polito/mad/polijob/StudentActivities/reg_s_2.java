package it.polito.mad.polijob.StudentActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.FilterStruct;
import it.polito.mad.polijob.model.StudentStruct;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Student;

public class reg_s_2 extends Activity {
    StudentStruct studentStruct;

    EditText birthdate;
    EditText SSN;
    EditText phone;
    EditText address;
    Spinner gender;
    EditText nation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_student_page2);




        studentStruct = (StudentStruct) getIntent().getSerializableExtra("studentStruct");

        Button button = (Button) findViewById(R.id.register2_student_skip_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Utility.networkIsUp(reg_s_2.this)){
                    Toast.makeText(getApplicationContext(),
                            "No network",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                updateStudent();
                Intent intent = new Intent(reg_s_2.this, reg_s_3.class);
                intent.putExtra("studentStruct", studentStruct);
                startActivity(intent);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.register2__student_update_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Utility.networkIsUp(reg_s_2.this)){
                    Toast.makeText(getApplicationContext(),
                            "No network",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                updateStudent();
                Intent intent = new Intent(reg_s_2.this, reg_s_3.class);
                intent.putExtra("studentStruct", studentStruct);
                startActivity(intent);
                finish();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }

    private void updateStudent(){
        birthdate= (EditText) findViewById(R.id.dateofB_editText);
        SSN= (EditText) findViewById(R.id.snn_editText);
        phone= (EditText) findViewById(R.id.phoneN_student_editText);
        address= (EditText) findViewById(R.id.address_student_editText);
        gender= (Spinner) findViewById(R.id.spinner_gender);
        nation= (EditText) findViewById(R.id.nation_editText);


        studentStruct.BirhtDate=(birthdate.getText().toString());
        studentStruct.SSN=(SSN.getText().toString());
        studentStruct.PhoneNumber=(phone.getText().toString());
        studentStruct.Address=(address.getText().toString());
        studentStruct.Gender=(gender.getSelectedItem().toString());
        studentStruct.Nationality=(nation.getText().toString());

    }






}