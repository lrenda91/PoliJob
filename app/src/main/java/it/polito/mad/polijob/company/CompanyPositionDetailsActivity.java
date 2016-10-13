package it.polito.mad.polijob.company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.FilterStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.PoliJobDB.CompaniesFoundCallback;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.student.StudentCompanyDetailsActivity;

/**
 * Created by Francesco on 17/05/2015.
 */
public class CompanyPositionDetailsActivity extends AppCompatActivity
        implements PoliJobDB.PositionFetchCallback, CompaniesFoundCallback {

    private Toolbar toolbar;
    private View infoPos;
    private View descrPos;
    private View compsPos;
    private ListView mListView;

    private Map<String, String> infoMap = new HashMap<>();

    private String[] positionElements = {"City", "Country", "TypeContract", "TypeDegree", "TypeJob"};

    private int[] positionIcons = {R.drawable.ic_place_grey600_24dp, R.drawable.ic_place_grey600_24dp,
            R.drawable.ic_web_grey600_24dp, R.drawable.ic_web_grey600_24dp,
            R.drawable.ic_web_grey600_24dp};

    private String description;
    private String posID;

    private CompanyStruct companyStruct;
    private ArrayList<String> skills;

    private boolean isFavorite = false;
    private Position pos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_main_inside_company);

        mListView = (ListView) findViewById(R.id.appliedStudentsListView);

        Intent intent = getIntent();
        description = intent.getStringExtra("About");
        skills = (ArrayList<String>) intent.getSerializableExtra("Skills");
        posID = intent.getStringExtra("posID");

        Bundle bundle=intent.getExtras();
        companyStruct = (CompanyStruct)bundle.getSerializable("companyStruct");
        infoMap.put("Name", intent.getStringExtra("Name"));
        infoMap.put("City", intent.getStringExtra("City"));
        infoMap.put("Country", intent.getStringExtra("Country"));
        infoMap.put("TypeContract", intent.getStringExtra("TypeContract"));
        infoMap.put("TypeDegree", intent.getStringExtra("TypeDegree"));
        infoMap.put("TypeJob", intent.getStringExtra("TypeJob"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(255);
        setTitle(infoMap.get("Name"));

        //Fill info about position
        LinearLayout infoPositionLayout = (LinearLayout) findViewById(R.id.infoPositionLayout);

        for (int i = 0; i < positionElements.length; i++) {
            infoPos = getLayoutInflater().inflate(R.layout.item_listview, null);

            infoPositionLayout.addView(infoPos);

            ImageView icon = (ImageView) infoPos.findViewById(R.id.item_icon);
            icon.setImageDrawable(getResources().getDrawable(positionIcons[i]));
            TextView text = (TextView) infoPos.findViewById(R.id.item_text);
            text.setText(infoMap.get(positionElements[i]));
        }

        //Fill fields of work company
        TextView textFields = (TextView) findViewById(R.id.skills);
        for (int k = 0; k < skills.size(); k++) {
            textFields.append(skills.get(k));
            if(k < skills.size()-1){
                textFields.append(" - ");
            }
        }

        //Fill info about position's description
        TextView textDescr = (TextView) findViewById(R.id.description);
        textDescr.setText(description);

        //Fill company info
        TextView textCompany = (TextView) findViewById(R.id.company);
        textCompany.setText(companyStruct.companyNametxt);
        textCompany.setOnClickListener(mOnClickCompanyNameListener);

       /*
        Button b = (Button) findViewById(R.id.company2);
        b.setText(companyStruct.companyNametxt);
        b.setOnClickListener(mOnClickCompanyNameListener);

        PoliJobDB.getAppliedStudents(posID, new PoliJobDB.StudentsFoundCallback() {
            @Override
            public void onListFound(List<Student> list) {
                mListView.setAdapter(new StudentsListAdapter(CompanyPositionDetailsActivity.this, list,posID));
            }
        });
        */
    }

    View.OnClickListener mOnClickCompanyNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FilterStruct filterData = new FilterStruct();
            filterData.comName = companyStruct.companyNametxt;
            PoliJobDB.filterCompany(filterData, CompanyPositionDetailsActivity.this);
        }
    };

    @Override
    public void onPositionFound(Position position) {
    }

    @Override
    public void onPositionNotFound() {
    }

    @Override
    public void onPositionFetchException(ParseException pe) {
    }

    @Override
    public void onListFound(List<Company> list) {
        if(!list.isEmpty()){
            Company c = list.get(0);
            String name = c.getCompanyName();
            String address = c.getAddress();
            String department = c.getDepartment();
            String webPage = c.getWebPage();
            String number = c.getPhoneNumber();
            String country = c.getCountry();
            String city = c.getCity();
            String contactName = c.getContactName();
            String fax = c.getFaxNumber();
            String workers = String.valueOf(c.getNumOfWorkers());
            String about = c.getAboutMe();
            String mission = c.getMission();
            List<String> fieldsList = c.getFieldsOfWork();
            ArrayList<String> fields;
            if(fieldsList!=null){
                fields = new ArrayList<>(fieldsList);
            }else{
                fields= new ArrayList<>();
            }
            Intent intent = new Intent(this, StudentCompanyDetailsActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("Address", address);
            intent.putExtra("Department", department);
            intent.putExtra("About", about);
            intent.putExtra("City", city);
            intent.putExtra("Number", number);
            intent.putExtra("Country", country);
            intent.putExtra("WebPage", webPage);
            intent.putExtra("ContactName", contactName);
            intent.putExtra("Fax", fax);
            intent.putExtra("Workers", workers);
            intent.putExtra("Mission", mission);
            intent.putExtra("Fields", fields);

            intent.putExtra("compID", c.getObjectId());

            try{
                ParseFile photo = c.getLogo();
                if(photo != null) {
                    byte[] b = photo.getData();
                    intent.putExtra("Photo", b);
                }
            }
            catch (ParseException pe){

            }
            this.startActivity(intent);
        }
    }
}
