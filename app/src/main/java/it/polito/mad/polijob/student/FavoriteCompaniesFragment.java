package it.polito.mad.polijob.student;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 18/05/15.
 */
public class FavoriteCompaniesFragment extends Fragment
        implements PoliJobDB.ParseObjectObserver<Student>, PoliJobDB.CompaniesFoundCallback {

    private ListView mListView;
    private ProgressBar progressBar;
    private Student data;

    @Override
    public void updateContent(Student student) {
        data = student;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_without_filter, container, false);
        mListView = (ListView) root.findViewById(R.id.list);
        progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle("Saved Companies");
        }

        PoliJobDB.getFavoriteCompanies(this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListFound(List<Company> list) {
        progressBar.setVisibility(View.INVISIBLE);
        mListView.setAdapter(new CompaniesListAdapter(getActivity(), list));
    }


}
