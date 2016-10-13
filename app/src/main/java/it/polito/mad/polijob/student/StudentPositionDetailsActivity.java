package it.polito.mad.polijob.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;

/**
 * Created by Francesco on 17/05/2015.
 */
public class StudentPositionDetailsActivity extends AppCompatActivity
        implements PoliJobDB.PositionFetchCallback, PoliJobDB.AppliedPositionSearchCallback {

    private Toolbar toolbar;
    private View infoPos;
    private View descrPos;
    private View compsPos;

    private Map<String, String> infoMap = new HashMap<>();

    private String[] positionElements = {"Name", "City", "Country", "TypeContract", "TypeDegree", "TypeJob"};
    private String description;
    private String posID;

    private CompanyStruct companyStruct;
    private ArrayList<String> skills;

    private boolean isFavorite = false;
    private boolean applied = false;
    private Position pos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_main_inside_student);

        Intent intent = getIntent();
        description = intent.getStringExtra("About");
        skills = (ArrayList<String>) intent.getSerializableExtra("Skills");

        posID = intent.getStringExtra("posID");
        PoliJobDB.findFavoritePositionByID(posID, this);
        PoliJobDB.tryFindAppliedPosition(posID, this);

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
            icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail));
            TextView text = (TextView) infoPos.findViewById(R.id.item_text);
            text.setText(infoMap.get(positionElements[i]));
        }

        //Fill the skills
        if(skills != null){
            infoPos = getLayoutInflater().inflate(R.layout.item_listview, null);

            infoPositionLayout.addView(infoPos);

            ImageView icon = (ImageView) infoPos.findViewById(R.id.item_icon);
            icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_event));
            TextView text = (TextView) infoPos.findViewById(R.id.item_text);
            for(String s : skills) {
                text.append(s + " ");
            }
        }

        //Fill info about position's description
        LinearLayout descrPositionLayout = (LinearLayout) findViewById(R.id.descriptionPositionLayout);
        descrPos = getLayoutInflater().inflate(R.layout.item_listview, null);
        descrPositionLayout.addView(descrPos);
        ImageView icon = (ImageView) descrPos.findViewById(R.id.item_icon);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        TextView text = (TextView) descrPos.findViewById(R.id.item_text);
        text.setText(description);

        LinearLayout compofPositionLayout = (LinearLayout) findViewById(R.id.companyPositionLayout);
        compsPos = getLayoutInflater().inflate(R.layout.item_listview, null);
        compofPositionLayout.addView(compsPos);
        ImageView icon2 = (ImageView) compsPos.findViewById(R.id.item_icon);
        icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        TextView text2 = (TextView) compsPos.findViewById(R.id.item_text);
        text2.setText(companyStruct.companyNametxt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_position_search, menu);
        menu.findItem(R.id.action_save_position).setIcon(
            isFavorite ? R.drawable.star_yellow_24dp : R.drawable.star_white_24dp
        );
        menu.findItem(R.id.action_apply_position).setIcon(
            applied ? R.drawable.ic_done_yellow_24dp : R.drawable.ic_done_white_24dp
        );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch(itemID){
            case R.id.action_apply_position:
                if (!applied){
                    PoliJobDB.addToAppliedPositons(posID);
                    item.setIcon(R.drawable.ic_done_yellow_24dp);
                    Toast.makeText(this, "You applied for this position", Toast.LENGTH_SHORT).show();
                }
                else{
                    PoliJobDB.removeFromAppliedPositons(posID);
                    item.setIcon(R.drawable.ic_done_white_24dp);
                    Toast.makeText(this, "You retired your candidacy for this position", Toast.LENGTH_SHORT).show();
                }
                applied = !applied;
                break;
            case R.id.action_save_position:
                if (!isFavorite){
                    PoliJobDB.addToFavoritePositons(posID);
                    item.setIcon(R.drawable.star_yellow_24dp);
                    Toast.makeText(this, "Position added to your favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    PoliJobDB.deletePositionFromList(posID, "favouriteOffers");
                    item.setIcon(R.drawable.star_white_24dp);
                    Toast.makeText(this, "Position removed to your favorites", Toast.LENGTH_SHORT).show();
                }
                isFavorite = !isFavorite;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositionFound(Position position) {
        isFavorite = true;
        pos = position;
        invalidateOptionsMenu();
    }

    @Override
    public void onPositionNotFound() {
        isFavorite = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onPositionFetchException(ParseException pe) {

    }

    @Override
    public void onAppliedPositionSearchResult(boolean found) {
        applied = found;
        invalidateOptionsMenu();
    }
}
