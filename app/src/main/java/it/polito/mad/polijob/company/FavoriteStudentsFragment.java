package it.polito.mad.polijob.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import it.polito.mad.polijob.CompanyActivities.addPosition;
import it.polito.mad.polijob.CompanyActivities.reg_c_3;
import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.FilterStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 18/05/15.
 */
public class FavoriteStudentsFragment extends android.app.Fragment
        implements PoliJobDB.ParseObjectObserver<Company> ,PoliJobDB.StudentsFoundCallback{
    private ListView mListView;
    private ProgressBar progressBar;

    private List<Student> list;

    @Override
    public void updateContent(Company company) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_with_filter, container, false);
        ((EditText) root.findViewById(R.id.search_Edittext)).setHint("Student name");
        mListView = (ListView) root.findViewById(R.id.list);
        progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        //mListView.setAdapter(new PositionsListAdapter(getActivity()));

        //////////////////////////////////////// temporary for adding position
        ImageButton ib = (ImageButton) root.findViewById(R.id.advanced_search_button);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toSearchActivity = new Intent(getActivity(), CompanyStudentSearchActivity.class);
                startActivityForResult(toSearchActivity, 10);
            }
        });
        /////////////////////////////

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle("Favorite Students");
        }

        PoliJobDB.getFavoriteStudents(this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            FilterStruct filter = (FilterStruct) data.getSerializableExtra("filters");
            //List<Student> filtered = PoliJobDB.filterByStruct(list, filter, "favoriteStudents");
            //List<Student> l = PoliJobDB.filterStudents( filter);
            //mListView.setAdapter(new StudentsListAdapter(getActivity(), l));
        }
    }

    @Override
    public void onListFound(List<Student> list) {
        this.list = list;
        progressBar.setVisibility(View.INVISIBLE);
        mListView.setAdapter(new StudentsListAdapter(getActivity(), list, "favstu"));
    }
}
