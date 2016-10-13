package it.polito.mad.polijob.company;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import it.polito.mad.polijob.MultiSelectionSpinner;
import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.FilterStruct;


public class CompanyStudentSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private FilterStruct filterData = new FilterStruct();
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_student_search);
        ((MultiSelectionSpinner) findViewById(R.id.languagesSpinner))
                .setItems(getResources().getStringArray(R.array.languages_arrays));
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        ((Button) findViewById(R.id.filter_button)).requestFocus();
        ((Button) findViewById(R.id.filter_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
                EditText cityEdit = (EditText) findViewById(R.id.city_spinner);
                Spinner careerSpinner = (Spinner) findViewById(R.id.career_spinner);
                filterData.careerDegree = careerSpinner.getSelectedItem() == null ? "" :
                        careerSpinner.getSelectedItem().toString();
                filterData.studentCountry = countrySpinner.getSelectedItem() == null ? "" :
                        countrySpinner.getSelectedItem().toString();
                filterData.studentCity = cityEdit.getText().toString();
                filterData.languages = ((MultiSelectionSpinner) findViewById(R.id.languagesSpinner)).getSelectedStrings();
                filterData.studentAvailabilityStartDate = fromDateEtxt.getText().toString();
                filterData.studentAvailabilityEndDate = toDateEtxt.getText().toString();


                Intent backIntent = new Intent();
                backIntent.putExtra("filters", filterData);
                setResult(AppCompatActivity.RESULT_OK, backIntent);
                finish();
            }
        });
    }
    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        //fromDateEtxt.setInputType(InputType.TYPE_NULL);
        //fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        //toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
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
