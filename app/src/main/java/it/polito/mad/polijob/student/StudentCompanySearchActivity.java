package it.polito.mad.polijob.student;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.FilterStruct;

public class StudentCompanySearchActivity extends AppCompatActivity   {

    private FilterStruct filterData = new FilterStruct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_company_search);

        ((Button) findViewById(R.id.filter_button)).requestFocus();
        ((Button) findViewById(R.id.filter_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
                EditText cityEdit = (EditText) findViewById(R.id.city_spinner);
                EditText comname = (EditText) findViewById(R.id.company_edittext);
                Spinner department = (Spinner) findViewById(R.id.spinner_department);

                filterData.depName = department.getSelectedItem() == null ? "" :
                        department.getSelectedItem().toString();
                filterData.comCountry = countrySpinner.getSelectedItem() == null ? "" :
                        countrySpinner.getSelectedItem().toString();
                filterData.comCity = cityEdit.getText().toString();
                filterData.comName = comname.getText().toString();


                Intent backIntent = new Intent();
                backIntent.putExtra("filters", filterData);
                setResult(AppCompatActivity.RESULT_OK, backIntent);
                finish();
            }
        });


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }



    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("filters", filterData);
        setResult(AppCompatActivity.RESULT_OK, backIntent);
        finish();
        super.onBackPressed();
    }

}
