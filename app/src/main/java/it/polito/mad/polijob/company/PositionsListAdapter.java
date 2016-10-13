package it.polito.mad.polijob.company;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;

/**
 * Created by luigi on 15/05/15.
 */
public class PositionsListAdapter extends BaseAdapter implements PoliJobDB.CompanyFetchedCallback {

    private Context mContext;
    private List<Position> mPositions;
    private Intent intent;
    String flag;
    public PositionsListAdapter(){}

    public PositionsListAdapter(Context context){
        mContext = context;
    }

    public PositionsListAdapter(Context context, List<Position> data){
        mContext = context;
        mPositions = data;
    }
    public PositionsListAdapter(Context context, List<Position> data, String flag){
        mContext = context;
        mPositions = data;
        this.flag=flag;
    }

    public void setData(List<Position> data) {
        mPositions = data;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mPositions.size();
    }

    @Override
    public Position getItem(int position) {
        return mPositions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_position_student, parent, false);
        }
        Position item = getItem(position);
        ((TextView) convertView.findViewById(R.id.item_title_position)).setText(item.getName());
        ((TextView) convertView.findViewById(R.id.item_about_position)).setText(item.getAbout());

        ImageView logoImage = (ImageView) convertView.findViewById(R.id.item_image_company);
        if (item.getCLogo() == null){
            logoImage.setImageResource(R.drawable.ic_user);
        }else{
            try{
                logoImage.setImageBitmap(Utility.getBitmap(item.getCLogo().getData()));
            }catch(ParseException pe){
                logoImage.setImageResource(R.drawable.ic_user);
            }
        }

        List<String> skills = item.getSkills();
        ((TextView) convertView.findViewById(R.id.item_skills_position)).setText(
                skills == null ? "" : skills.toString()
        );
        String creationDateTxt = new SimpleDateFormat("MM/dd/yyyy").format(item.getCreatedAt());
        ((TextView) convertView.findViewById(R.id.item_date_position)).setText(creationDateTxt);
        //Set listener on itemClick
        ListView l = (ListView) parent;
        l.setOnItemClickListener(mOnItemPositionStudentClick);
       // l.setOnItemLongClickListener(mOnItemPositionStudentLongClick);
        return convertView;
    }

    private AdapterView.OnItemClickListener mOnItemPositionStudentClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Position p = getItem(position);

            String name = p.getName();
            String about = p.getAbout();
            String city = p.getCity();
            String country = p.getCountry();
            List<String> listSkills = p.getSkills();
            ArrayList<String> skills;
            if(listSkills!=null){
                skills = new ArrayList<>(listSkills);
            }else{
                skills= new ArrayList<>();
            }
            String typeContract = p.getTypeOfContract();
            String typeDegree = p.getTypeOfDegree();
            String typeJob = p.getTypeOfJob();

            intent = new Intent(mContext, CompanyPositionDetailsActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("About", about);
            intent.putExtra("City", city);
            intent.putExtra("Country", country);
            intent.putExtra("Skills", skills);
            intent.putExtra("TypeContract", typeContract);
            intent.putExtra("TypeDegree", typeDegree);
            intent.putExtra("TypeJob", typeJob);
            intent.putExtra("posID", p.getObjectId());


            PoliJobDB.getCompanyStruct(p.getParentCompany().getObjectId(), PositionsListAdapter.this);

        }
    };

    private AdapterView.OnItemLongClickListener mOnItemPositionStudentLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete position")
                    .setMessage("Are you sure you want to delete this position?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            PoliJobDB.deletePositionFromList(getItem(pos).getObjectId(),flag);

                            mPositions.remove(pos);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
    };

    @Override
    public void onCompanyFetchedException(ParseException pe) {

    }

    @Override
    public void onCompanyFetchedSuccess(CompanyStruct company) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("companyStruct", company);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
