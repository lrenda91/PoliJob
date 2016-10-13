package it.polito.mad.polijob.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;

/**
 * Created by luigi on 19/05/15.
 */
public class CompaniesListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Company> mCompanies;
    private Intent intent;
    public CompaniesListAdapter(){}

    public CompaniesListAdapter(Context context){
        mContext = context;
    }

    public CompaniesListAdapter(Context context, List<Company> data){
        mContext = context;
        mCompanies = data;
    }

    public void setData(List<Company> data) {
        mCompanies = data;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCompanies.size();
    }

    @Override
    public Company getItem(int position) {
        return mCompanies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_company, parent, false);
        }

        Company item = getItem(position);
        ((TextView) convertView.findViewById(R.id.item_title_company)).setText(item.getName());
        ((TextView) convertView.findViewById(R.id.item_aboutme_company)).setText(item.getAboutMe());
        ((TextView) convertView.findViewById(R.id.item_department_company)).setText(item.getDepartment());
        ((TextView) convertView.findViewById(R.id.item_webpage_company)).setText(item.getWebPage());
        ImageView logoImage = (ImageView) convertView.findViewById(R.id.item_image_company);
        if (item.getLogo() == null){
            logoImage.setImageResource(R.drawable.ic_user);
        }
        else{
            try{
                logoImage.setImageBitmap(Utility.getBitmap(item.getLogo().getData()));
            }catch(ParseException pe){
                logoImage.setImageResource(R.drawable.ic_user);
            }
        }
        //Set listener on itemClick
        ListView l = (ListView) parent;
        l.setOnItemClickListener(mOnItemPositionStudentClick);
       // l.setOnItemLongClickListener(mOnItemPositionStudentLongClick);

        return convertView;
    }

    private AdapterView.OnItemClickListener mOnItemPositionStudentClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Company c = getItem(position);

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


            intent = new Intent(mContext, StudentCompanyDetailsActivity.class);
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
            mContext.startActivity(intent);
        }
    };


    private AdapterView.OnItemLongClickListener mOnItemPositionStudentLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            final int pos = i;

            new AlertDialog.Builder(mContext)
                    .setTitle("Delete Company")
                    .setMessage("Are you sure you want to delete this company from favorites?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PoliJobDB.deleteCompanyFromList(getItem(pos).getObjectId());
                            mCompanies.remove(pos);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }
    };
}
