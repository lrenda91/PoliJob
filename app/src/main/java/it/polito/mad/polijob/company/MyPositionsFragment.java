package it.polito.mad.polijob.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.polijob.CompanyActivities.addPosition;
import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.student.PositionsListAdapter;

/**
 * Created by luigi on 18/05/15.
 */
public class MyPositionsFragment extends android.app.Fragment
        implements PoliJobDB.ParseObjectObserver<Company>, PoliJobDB.GetPositionsCallback {

    private PositionsListAdapter adapter;
    private ListView mListView;
    private Company company;
    private View content;
    private EditText mtxt;
    private ProgressBar progressBar;
    private List<Position> data;
    private List<Position> search_data;

    @Override
    public void updateContent(Company company) {
        this.company = company;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_with_filter, container, false);
        mtxt = (EditText) view.findViewById(R.id.search_Edittext);
        mtxt.setHint("Position name");
        content = view.findViewById(R.id.student_info);
        content.setVisibility(View.INVISIBLE);
        view.findViewById(R.id.advanced_search_button).setVisibility(View.GONE);

        mListView = (ListView) view.findViewById(R.id.list);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle("My Positions");
        }
        progressBar.setVisibility(View.VISIBLE);
        PoliJobDB.getPositionsList(this);

    }

    @Override
    public void onGetPositionsSuccess(List<Position> result) {
        progressBar.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        data = result;
        search_data = new ArrayList<>();
        mListView.setAdapter(new it.polito.mad.polijob.company.PositionsListAdapter(getActivity(), result,"myposition"));
        mtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (0 != mtxt.getText().length()) {
                    String spnId = mtxt.getText().toString();
                    setSearchResult(spnId);
                } else {
                    setData();
                }
            }
        });
    }

    private void setData() {
        adapter = new PositionsListAdapter(getActivity(), data);
        mListView.setAdapter(adapter);
    }

    public void setSearchResult(String str) {
        adapter = new PositionsListAdapter(getActivity());
        search_data.clear();
        for (Position temp : data) {
            if (temp.getName().toLowerCase().contains(str.toLowerCase())) {
                //adapter.addItem(temp);
                search_data.add(temp);
            }
        }
        adapter = new PositionsListAdapter(getActivity(), search_data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onGetPositionsException(ParseException pe) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_company_add_position, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_position:
                Intent i = new Intent(getActivity(), addPosition.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
