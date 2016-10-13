package it.polito.mad.polijob.CompanyActivities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.model.Student;

public class addPosition extends AppCompatActivity {

    EditText name;
    Spinner toj;
    Spinner toc;
    Spinner country;
    Spinner required;
    EditText city;
    EditText about;
    EditText skills;
    Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Position pos;
    private Company ob;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);
        ob = (Company) ParseUser.getCurrentUser().getParseObject("relationC");
        pos = new Position();

        Button button = (Button) findViewById(R.id.pos_create_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pos.setName(name.getText().toString());
                pos.setCity(city.getText().toString());
                pos.setAbout(about.getText().toString());
                pos.setTypeOfDegree(required.getSelectedItem().toString());
                pos.setTypeOfContract(toc.getSelectedItem().toString());
                pos.setTypeOfJob(toj.getSelectedItem().toString());
                pos.setCountry(country.getSelectedItem().toString());
                if(ob.getLogo()!=null) pos.setCLogo(ob.getLogo());
                if(arrayList!=null) pos.setSkills(arrayList);
                progressBar.setVisibility(View.VISIBLE);
                pos.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ob.addPosition(ob.getRelationPositions(), pos);
                        ob.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    pos.setParentCompany(ob);
                                    pos.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                finish();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                });
                //pos.setParentCompany(ParseUser.getCurrentUser().getParseObject("relationC"));
                //pos.saveInBackground();
                //finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.pos_cancel_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        name= (EditText) findViewById(R.id.pos_title_edit);
        city= (EditText) findViewById(R.id.pos_city_edit);
        about= (EditText) findViewById(R.id.pos_inf_edit);
        toc= (Spinner) findViewById(R.id.pos_typecontract_spinner);
        toj= (Spinner) findViewById(R.id.pos_type_spinner);
        country= (Spinner) findViewById(R.id.pos_spinner_country);
        required= (Spinner) findViewById(R.id.required_degree);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        list = (ListView) findViewById(R.id.pos_skills_list);
        skills = (EditText) findViewById(R.id.pos_skills_edit);
        btn = (Button) findViewById(R.id.pos_addskill_button);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add(skills.getText().toString());
                adapter.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);
            }
        });





    }


}
