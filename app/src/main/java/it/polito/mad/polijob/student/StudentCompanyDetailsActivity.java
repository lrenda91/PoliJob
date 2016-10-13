package it.polito.mad.polijob.student;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.PoliJobDB;

/**
 * Created by Francesco on 21/05/15.
 */
public class StudentCompanyDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private View infoCompany;
    private View contactsCompany;

    private Map<String, String> infoMap = new HashMap<>();

    private boolean isFavorite = false;

    private String[] contactsElements = {"Address", "Number", "WebPage", "Fax"};
    private int[] contactsIcons = {R.drawable.ic_place_grey600_24dp, R.drawable.ic_local_phone_black_24dp,
            R.drawable.ic_web_grey600_24dp, R.drawable.fax_icon};

    private String[] infoElements = {"City", "Country", "Department", "ContactName", "Workers"};
    private int[] infoIcons = {R.drawable.ic_place_grey600_24dp, R.drawable.ic_place_grey600_24dp,
            R.drawable.ic_city_black_24dp,
            R.drawable.ic_user, R.drawable.ic_account_multiple_black_24dp};

    private String compID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_profile_inside_student);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(255);

        Intent i = getIntent();
        infoMap.put("Address", i.getStringExtra("Address"));
        infoMap.put("Number", i.getStringExtra("Number"));
        infoMap.put("WebPage", i.getStringExtra("WebPage"));
        infoMap.put("Fax", i.getStringExtra("Fax"));
        infoMap.put("City", i.getStringExtra("City"));
        infoMap.put("Country", i.getStringExtra("Country"));
        infoMap.put("Department", i.getStringExtra("Department"));
        infoMap.put("Workers", i.getStringExtra("Workers"));
        compID = i.getStringExtra("compID");
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

        getSupportActionBar().setTitle((i.getStringExtra("Name")));


        //Fill contacts company
        LinearLayout contactsCompanyLayout = (LinearLayout) findViewById(R.id.contactsCompanyLayout);
        for (int j = 0; j < contactsElements.length; j++) {
            if (infoMap.get(contactsElements[j]) != null) {
                contactsCompany = getLayoutInflater().inflate(R.layout.item_listview, null);
                contactsCompanyLayout.addView(contactsCompany);

                ImageView icon = (ImageView) contactsCompany.findViewById(R.id.item_icon);
                icon.setImageDrawable(getResources().getDrawable(contactsIcons[j]));
                TextView text = (TextView) contactsCompany.findViewById(R.id.item_text);
                text.setText(infoMap.get(contactsElements[j]));
            }
        }

        //Fill info company
        LinearLayout infoCompanyLayout = (LinearLayout) findViewById(R.id.infoCompanyLayout);
        for (int j = 0; j < infoElements.length; j++) {
            if (infoMap.get(infoElements[j]) != null) {
                infoCompany = getLayoutInflater().inflate(R.layout.item_listview, null);
                infoCompanyLayout.addView(infoCompany);

                ImageView icon = (ImageView) infoCompany.findViewById(R.id.item_icon);
                icon.setImageDrawable(getResources().getDrawable(infoIcons[j]));
                TextView text = (TextView) infoCompany.findViewById(R.id.item_text);
                text.setText(infoMap.get(infoElements[j]));
            }
        }

        //Fill about company
        TextView textAbout = (TextView) findViewById(R.id.about);
        textAbout.setText(i.getStringExtra("About"));

        //Fill mission company
        TextView textMission = (TextView) findViewById(R.id.mission);
        textMission.setText(i.getStringExtra("Mission"));

        //Fill fields of work company
        TextView textFields = (TextView) findViewById(R.id.fields);
        ArrayList<String> al = i.getStringArrayListExtra("Fields");
        for (int k = 0; k < al.size(); k++) {
            textFields.append(al.get(k));
            if(k < al.size()-1){
                textFields.append(" - ");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_company_search, menu);
        menu.findItem(R.id.action_save_company).setIcon(
                isFavorite ? R.drawable.star_yellow_24dp : R.drawable.star_white_24dp
        );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.action_save_company:
                if (!isFavorite){
                    PoliJobDB.addToFavoriteCompanies(compID);
                    item.setIcon(R.drawable.star_yellow_24dp);
                    Toast.makeText(this, "Company added to your favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    PoliJobDB.deleteCompanyFromList(compID);
                    item.setIcon(R.drawable.star_white_24dp);
                    Toast.makeText(this, "Company removed to your favorites", Toast.LENGTH_SHORT).show();
                }
                isFavorite = !isFavorite;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
