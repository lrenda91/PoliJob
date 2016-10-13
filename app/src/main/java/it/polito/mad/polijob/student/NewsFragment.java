package it.polito.mad.polijob.student;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.PoliJobDB.ParseObjectObserver;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 15/05/15.
 */
public class NewsFragment extends Fragment
        implements ParseObjectObserver<Student>, PoliJobDB.GetPositionsCallback {

    private ListView listView;
    private List<Position> data;
    private List<Position> search_data;
    private PositionsListAdapter adapter;
    private EditText mtxt;

    private View content;
    private ProgressBar progressBar;

    private Student student;

    @Override
    public void updateContent(Student student) {
        this.student = student;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_with_filter, container, false);
        ((EditText) view.findViewById(R.id.search_Edittext)).setHint("Position name");
        content = view.findViewById(R.id.student_info);
        content.setVisibility(View.INVISIBLE);

        view.findViewById(R.id.advanced_search_button).setVisibility(View.GONE);

        listView = (ListView) view.findViewById(R.id.list);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mtxt = (EditText) view.findViewById(R.id.search_Edittext);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PoliJobDB.getSkillBasedPositions(student, this);
        progressBar.setVisibility(View.VISIBLE);

        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle("News");
        }
    }

    private void setData() {
        adapter = new PositionsListAdapter(getActivity(), data);
        listView.setAdapter(adapter);
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
        listView.setAdapter(adapter);
    }


    @Override
    public void onGetPositionsSuccess(List<Position> result) {
        progressBar.setVisibility(View.INVISIBLE);
        if (result.isEmpty()){
            // emptyResultView.setVisibility(View.VISIBLE);
            return;
        }

        content.setVisibility(View.VISIBLE);

        data = result;
        search_data = new ArrayList<>();

        mtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
        setData();
    }

    @Override
    public void onGetPositionsException(ParseException pe) {

    }


}