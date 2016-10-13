package it.polito.mad.polijob.student;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.FilterStruct;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.PoliJobDB.ParseObjectObserver;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 15/05/15.
 */
public class SearchCompaniesFragment extends Fragment
        implements ParseObjectObserver<Student>, PoliJobDB.CompaniesFoundCallback {

    private ListView mListView;
    private List<Company> data;
    private List<Company> search_data;
    private CompaniesListAdapter adapter;
    private EditText mtxt;

    private View content;
    private ProgressBar progressBar;

    private Student student;

    @Override
    public void updateContent(Student student) {
        this.student = student;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent toSearchActivity = new Intent(getActivity(), StudentCompanySearchActivity.class);
        startActivityForResult(toSearchActivity, 10);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_with_filter, container, false);
        ((EditText) view.findViewById(R.id.search_Edittext)).setHint("Company name");
        content = view.findViewById(R.id.student_info);

        adapter = new CompaniesListAdapter(getActivity(), new LinkedList<Company>());

        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setAdapter(adapter);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mtxt = (EditText) view.findViewById(R.id.search_Edittext);

        ImageButton ib = (ImageButton) view.findViewById(R.id.advanced_search_button);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toSearchActivity = new Intent(getActivity(), StudentCompanySearchActivity.class);
                startActivityForResult(toSearchActivity, 10);
            }
        });
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mtxt.setText("");
        if (requestCode == 10){
            progressBar.setVisibility(View.VISIBLE);
            FilterStruct filter = (FilterStruct) data.getSerializableExtra("filters");
            PoliJobDB.filterCompany(filter, this);
        }
    }

    private void setData() {
        adapter.setData(data);
        mListView.setAdapter(adapter);
    }

    public void setSearchResult(String str) {
        adapter = new CompaniesListAdapter(getActivity());
        search_data.clear();
        for (Company temp : data) {
            if (temp.getName().toLowerCase().contains(str.toLowerCase())) {
                //adapter.addItem(temp);
                search_data.add(temp);
            }
        }
        adapter.setData(search_data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onListFound(List<Company> list) {
        progressBar.setVisibility(View.INVISIBLE);

        content.setVisibility(View.VISIBLE);

        data = list;
        setData();
    }

}
