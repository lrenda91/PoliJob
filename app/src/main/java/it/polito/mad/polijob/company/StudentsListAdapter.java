package it.polito.mad.polijob.company;

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
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 19/05/15.
 */
public class StudentsListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Student> mCompanies;
    private Intent intent;
    private String posID;
    public StudentsListAdapter(){}

    public StudentsListAdapter(Context context){
        mContext = context;
    }

    public StudentsListAdapter(Context context, List<Student> data, String posID){
        this.posID = posID;
        mContext = context;
        mCompanies = data;
    }
    public StudentsListAdapter(Context context, List<Student> data){

        mContext = context;
        mCompanies = data;
    }

    public void setData(List<Student> data) {
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
    public Student getItem(int position) {
        return mCompanies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_student, parent, false);
        }

        Student item = getItem(position);
        ((TextView) convertView.findViewById(R.id.item_title_student))
                .setText(item.getFirstName()+" "+item.getLastName());
        ((TextView) convertView.findViewById(R.id.item_aboutme_student))
                .setText(item.getAddress()+"\n"+item.getCity()+"\n"+item.getCountry());
        ((TextView) convertView.findViewById(R.id.item_availability_student))
                .setText(item.getUpdatedAt()!=null ? item.getUpdatedAt().toString() : "null");
        ((TextView) convertView.findViewById(R.id.item_skills_student))
                .setText(item.getSkills()!=null ? item.getSkills().toString() : "null");

        String creationDateTxt = new SimpleDateFormat("MM/dd/yyyy").format(item.getCreatedAt());
        ((TextView) convertView.findViewById(R.id.item_availability_student)).setText(creationDateTxt);

        ImageView logoImage = (ImageView) convertView.findViewById(R.id.item_image_student);
        if (item.getPhotoFile() == null){
            logoImage.setImageResource(R.drawable.ic_user);
        }
        else{
            try{
                logoImage.setImageBitmap(Utility.getBitmap(item.getPhotoFile().getData()));
            }catch(ParseException pe){
                logoImage.setImageResource(R.drawable.ic_user);
            }
        }

        ListView l = (ListView) parent;
        l.setOnItemClickListener(mOnItemStudentClick);
       // l.setOnItemLongClickListener(mOnItemStudentLongClick);

        return convertView;
    }

    private AdapterView.OnItemClickListener mOnItemStudentClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Student s = getItem(position);

            String name = s.getFirstName()+" "+s.getLastName();
            Date birthDate = s.getBirthDate();
            String gender = s.getGender();
            String address = s.getAddress();
            String city = s.getCity();
            String country = s.getCountry();
            String number = s.getPhoneNumber();
            String ssn = s.getSSN();
            String nationality = s.getNationality();
            List<String> listSkills = s.getSkills();
            ArrayList<String> skills;
            if(listSkills!=null){
                skills = new ArrayList<>(listSkills);
            }else{
                skills= new ArrayList<>();
            }
            Intent intent = new Intent(mContext, StudentProfileActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("Address", address);
            intent.putExtra("City", city);
            intent.putExtra("Country", country);
            intent.putExtra("Number", number);
            intent.putExtra("SSN", ssn);
            intent.putExtra("Nationality", nationality);
            intent.putExtra("BirthDate", birthDate);
            intent.putExtra("Gender", gender);
            intent.putExtra("Skills", skills);

            try{
                ParseFile photo = s.getPhotoFile();
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

    private AdapterView.OnItemLongClickListener mOnItemStudentLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final int pos=position;
            new AlertDialog.Builder(mContext)
                    .setTitle("Reject")
                    .setMessage("Are you sure you want to reject this student?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PoliJobDB.deleteStudentFromList(posID, getItem(pos), new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    mCompanies.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
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
}
