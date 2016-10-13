package it.polito.mad.polijob.company;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;

/**
 * Created by Francesco on 20/05/2015.
 */
public class StudentProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private View contactsStu;
    private View infoStu;

    private Map<String, String> infoMap = new HashMap<>();

    private String[] contactsElements = {"Address", "Number"};
    private int[] contactsIcons = { R.drawable.ic_place_grey600_24dp, R.drawable.ic_local_phone_black_24dp};

    private String[] studentElements = {"City", "BirthDate", "Country", "SSN", "Nationality", "Gender"};
    private int[] studentElementsIcons={R.drawable.ic_place_grey600_24dp, R.drawable.ic_cake_black_24dp,
            R.drawable.ic_place_grey600_24dp, R.drawable.ic_web_black_24dp,
            R.drawable.ic_map_grey600_24dp, R.drawable.ic_user};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_inside_company);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(255);

        Intent i = getIntent();
        infoMap.put("Address", i.getStringExtra("Address"));
        infoMap.put("City", i.getStringExtra("City"));
        infoMap.put("Country", i.getStringExtra("Country"));
        infoMap.put("Number", i.getStringExtra("Number"));
        infoMap.put("SSN", i.getStringExtra("SSN"));
        infoMap.put("Nationality", i.getStringExtra("Nationality"));
        infoMap.put("Gender", i.getStringExtra("Gender"));

        byte[] ab = i.getByteArrayExtra("Photo");
        FrameLayout f = (FrameLayout) findViewById(R.id.headerPosition);
        if(ab != null){
            Bitmap b = Utility.getBitmap(ab);
            BitmapDrawable bd = new BitmapDrawable(b);
            f.setBackground(bd);
        }
        else{
            f.setBackgroundResource(R.drawable.ic_user);
        }

        getSupportActionBar().setTitle(infoMap.get(i.getStringExtra("Name")));

        Date date = (Date) i.getExtras().get("BirthDate");
        if(date!=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH));
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            infoMap.put("BirthDate", day.concat("/").concat(month).concat("/").concat(year));
        }
        else {
            infoMap.put("BirthDate", "Not available");
        }
        getSupportActionBar().setTitle(i.getStringExtra("Name"));

        //Fill contacts student
        LinearLayout contactsStudentLayout = (LinearLayout) findViewById(R.id.contactsStudentLayout);
        for (int j = 0; j < contactsElements.length; j++) {
            if (infoMap.get(contactsElements[j]) != null) {
                contactsStu = getLayoutInflater().inflate(R.layout.item_listview, null);
                contactsStudentLayout.addView(contactsStu);

                ImageView icon = (ImageView) contactsStu.findViewById(R.id.item_icon);
                icon.setImageDrawable(getResources().getDrawable(contactsIcons[j]));
                TextView text = (TextView) contactsStu.findViewById(R.id.item_text);
                text.setText(infoMap.get(contactsElements[j]));
            }
        }

        //Fill personal info student
        LinearLayout infoStudentLayout = (LinearLayout) findViewById(R.id.infoStudentLayout);
        for (int j = 0; j < studentElements.length; j++) {
            if (infoMap.get(studentElements[j]) != null) {
                infoStu = getLayoutInflater().inflate(R.layout.item_listview, null);
                infoStudentLayout.addView(infoStu);

                ImageView icon = (ImageView) infoStu.findViewById(R.id.item_icon);
                icon.setImageDrawable(getResources().getDrawable(studentElementsIcons[j]));
                TextView text = (TextView) infoStu.findViewById(R.id.item_text);
                text.setText(infoMap.get(studentElements[j]));
            }
        }

        //Fill skills of student
        TextView textFields = (TextView) findViewById(R.id.skills);
        ArrayList<String> al = i.getStringArrayListExtra("Skills");
        for (int k = 0; k < al.size(); k++) {
            textFields.append(al.get(k));
            if(k < al.size()-1){
                textFields.append(" - ");
            }
        }
    }
}
