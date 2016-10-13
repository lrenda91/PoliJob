package it.polito.mad.polijob.student;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.List;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Position;
import it.polito.mad.polijob.model.Student;

/**
 * Created by luigi on 18/05/15.
 */
public class FavoritePositionsFragment extends android.app.Fragment
        implements PoliJobDB.ParseObjectObserver<Student>, PoliJobDB.PositionsFoundCallback {

    private ListView mListView;
    private Student data;
    private ProgressBar progressBar;

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
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle("Saved Positions");
        }
        PoliJobDB.getFavoritePositions(this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListFound(List<Position> list) {
        progressBar.setVisibility(View.INVISIBLE);
        mListView.setAdapter(new PositionsListAdapter(getActivity(), list, "favouriteOffers"));
    }

    @Override
    public void onFindPositionException(ParseException pe) {

    }


}
