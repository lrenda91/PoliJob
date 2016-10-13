package it.polito.mad.polijob.company;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.mad.polijob.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoCompanyActivityFragment extends Fragment implements CompanyProfileView {
    private static final long DURATION = 400;
    private LinearLayout contactsEditLayout;
    private LinearLayout contactsTextLayout;
    private LinearLayout infoEditLayout;
    private LinearLayout infoTextLayout;

    private Map<String, View> textViews;
    private Map<String, View> editTextViews;

    private boolean editable = false;


    private CompanyProfilePresenter presenter;

    private InfoFragmentListener mListener;


    public InfoCompanyActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = CompanyProfilePresenterImpl.newInstance();
        presenter.addView(this);
        textViews = new HashMap<>();
        editTextViews = new HashMap<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_info_company, container, false);

        contactsEditLayout = (LinearLayout) v.findViewById(R.id.contactsMoreEditLayout);
        infoEditLayout = (LinearLayout) v.findViewById(R.id.infoMoreEditLayout);
        contactsTextLayout = (LinearLayout) v.findViewById(R.id.contactsMoreTextLayout);
        infoTextLayout = (LinearLayout) v.findViewById(R.id.infoMoreTextLayout);

        addItem(inflater, contactsEditLayout, contactsTextLayout, "phone");
        addItem(inflater, contactsEditLayout, contactsTextLayout, "website");
        addItem(inflater, contactsEditLayout, contactsTextLayout, "email");
        addItem(inflater, contactsEditLayout, contactsTextLayout, "fax");
        addItem(inflater, contactsEditLayout, contactsTextLayout, "contact name");

        addItem(inflater, infoEditLayout, infoTextLayout, "country");
        addItem(inflater, infoEditLayout, infoTextLayout, "department");
        addItem(inflater, infoEditLayout, infoTextLayout, "city");
        addItem(inflater, infoEditLayout, infoTextLayout, "address");
        addItem(inflater, infoEditLayout, infoTextLayout, "num. of worker");


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (editable) inflater.inflate(R.menu.menu_company_profile_editable, menu);
        else inflater.inflate(R.menu.menu_company_main, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_edit:
                makeEditable();
                return true;
            case R.id.action_edit_done:
                editDone();
                return true;
            case android.R.id.home:
                if (editable) editCancelled();
                else NavUtils.navigateUpFromSameTask(getActivity());
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void editCancelled() {
        editable = false;
        switchViews();
        hideSoftKeyboard();


    }

    private void editDone() {
        editable = false;
        HashMap<String, String> infoMap = new HashMap<>();

        for (String key : editTextViews.keySet()) {
            View v = editTextViews.get(key);
            EditText editText = (EditText) v.findViewById(R.id.valueEditText);
            infoMap.put(key, editText.getText().toString());
        }

        presenter.updateInfo(infoMap);
        presenter.saveUpdate();

        switchViews();
        hideSoftKeyboard();


    }

    private void makeEditable() {

        editable = true;
        switchViews();
    }

    @Override
    public void showLogo(Bitmap logo) {//do nothing
    }

    @Override
    public void showInfo(Map<String, String> infoMap) {

        if (getActivity() == null) return;

        for (String key : infoMap.keySet()) {
            if (textViews.containsKey(key)) {
                ((TextView) textViews.get(key).findViewById(R.id.valueText)).setText(infoMap.get(key));
                ((EditText) editTextViews.get(key).findViewById(R.id.valueEditText)).setText(infoMap.get(key));
            }

        }


    }

    @Override
    public void showAboutCompanyDescription(String description, boolean htmlText) {
    }

    @Override
    public void showMissionDescription(String description, boolean htmlText) {
    }

    @Override
    public void showFieldsOfWorkDescription(List<String> filds) {
    }

    private void addItem(LayoutInflater inflater, ViewGroup editViewGroup, ViewGroup textViewGroup, String type) {
        View v = inflater.inflate(R.layout.info_edit_item, null);
        TextView typeTextView = (TextView) v.findViewById(R.id.typeText);
        typeTextView.setText(type);
        editTextViews.put(type, v);
        editViewGroup.addView(v);


        View v2 = inflater.inflate(R.layout.info_item, null);
        typeTextView = (TextView) v2.findViewById(R.id.typeText);
        typeTextView.setText(type);
        textViews.put(type, v2);
        textViewGroup.addView(v2);


    }

    private void switchViews() {
        // switch the text views with edit text views with animation
        ViewSwitcher viewSwitcher1 = (ViewSwitcher) getActivity().findViewById(R.id.infoMoreViewSwitcher);
        viewSwitcher1.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                .setListener(new MyAnimatorListener(viewSwitcher1)).start();

        ViewSwitcher viewSwitcher2 = (ViewSwitcher) getActivity().findViewById(R.id.contactsMoreViewSwitcher);
        viewSwitcher2.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                .setListener(new MyAnimatorListener(viewSwitcher2)).start();
        getActivity().invalidateOptionsMenu();
    }

    private void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    interface InfoFragmentListener {

    }
}
