package it.polito.mad.polijob.company;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;


public class ProfileFragment extends Fragment implements CompanyProfileView,
        PoliJobDB.ParseObjectObserver<Company> {

    private static final long DURATION = 400;

    private OnScrollListener mListener;
    private CompanyProfilePresenter presenter;
    private FrameLayout header;
    private LinearLayout fieldsTextLayout;
    private LinearLayout fieldsEditLayout;
    private Button addButton;
    private Map<String, View> contactAndInfoViews;
    private List<View> fieldsOfWorkViews;
    private List<View> fieldsOfWorkEditTextViews;
    private boolean editable = false;


    public ProfileFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnScrollListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnScrollListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        presenter = CompanyProfilePresenterImpl.newInstance();
        presenter.addView(this);

        contactAndInfoViews = new HashMap<>();
        fieldsOfWorkViews = new LinkedList<>();
        fieldsOfWorkEditTextViews = new LinkedList<>();

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        header = (FrameLayout) view.findViewById(R.id.header);
        fieldsTextLayout = (LinearLayout) view.findViewById(R.id.fieldsMainLayout);
        fieldsEditLayout = (LinearLayout) view.findViewById(R.id.fieldsEditLayout);


        //listener for the scrolling effect
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollX = scrollView.getScrollX(); //for horizontalScrollView
                int scrollY = scrollView.getScrollY(); //for verticalScrollView

                if (mListener != null) {
                    abbassaHeader(scrollY);
                    mListener.onScrolled(scrollX, scrollY);
                }

            }
        });


        //listener for more button
        Button moreButton = (Button) view.findViewById(R.id.moreButton);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start a new activity
                Intent intent = new Intent(getActivity(), InfoCompanyActivity.class);
                startActivity(intent);

            }
        });

        //listener for addField button
        addButton = (Button) view.findViewById(R.id.addFieldButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFields();
                fieldsOfWorkEditTextViews.get(fieldsOfWorkEditTextViews.size() - 1).requestFocus();
            }
        });


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                editCancelled();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateContent(Company company) {
        if (presenter == null) presenter = CompanyProfilePresenterImpl.newInstance();
        presenter.addModel(company);
    }

    private void abbassaHeader(int dy) {
        header.setTranslationY(dy / 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void showLogo(Bitmap logo) {

        BitmapDrawable ob = new BitmapDrawable(getResources(), logo);

        if (Build.VERSION.SDK_INT >= 16) {
            header.setBackground(ob);
        } else {
            header.setBackgroundDrawable(ob);
        }
    }

    @Override
    public void showInfo(Map<String, String> infoMap) {

        if (getActivity() == null) return;

        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle(infoMap.get("name"));

        }

        ((TextView) getActivity().findViewById(R.id.nameTextView)).setText(infoMap.get("name"));
        //set contact values
        LinearLayout contactsLayout = (LinearLayout) getActivity().findViewById(R.id.contactsLayout);
        View websiteItemView = contactsLayout.findViewById(R.id.website_item);
        View emailItemView = contactsLayout.findViewById(R.id.email_item);
        View phoneItemView = contactsLayout.findViewById(R.id.number_item);

        setItem(websiteItemView, R.drawable.ic_web_grey600_24dp, infoMap.get("website"));
        setItem(emailItemView, R.drawable.ic_mail, infoMap.get("email"));
        setItem(phoneItemView, R.drawable.ic_call_grey600_24dp, infoMap.get("phone"));

        contactAndInfoViews.put("website", websiteItemView);
        contactAndInfoViews.put("email", emailItemView);
        contactAndInfoViews.put("phone", phoneItemView);


        //set info values
        LinearLayout infoLayout = (LinearLayout) getActivity().findViewById(R.id.inforLayout);
        View addressItemView = infoLayout.findViewById(R.id.address_item);
        View cityItemView = infoLayout.findViewById(R.id.city_item);
        View countryItemView = infoLayout.findViewById(R.id.country_item);

        setItem(addressItemView, R.drawable.ic_place_grey600_24dp, infoMap.get("address"));
        setItem(cityItemView, R.drawable.ic_location_city_grey600_24dp, infoMap.get("city"));
        setItem(countryItemView, R.drawable.ic_map_grey600_24dp, infoMap.get("country"));

        contactAndInfoViews.put("address", addressItemView);
        contactAndInfoViews.put("city", cityItemView);
        contactAndInfoViews.put("country", countryItemView);
    }

    @Override
    public void showAboutCompanyDescription(String description, boolean htmlText) {
        if (getActivity() == null) return;

        TextView text = (TextView) getActivity().findViewById(R.id.aboutCompanyTextView);
        EditText editText = (EditText) getActivity().findViewById(R.id.aboutCompanyEditText);

        if (htmlText) {
            text.setText(Html.fromHtml(description));

        } else {
            text.setText(description);
        }
        editText.setText(description);
    }

    @Override
    public void showMissionDescription(String description, boolean htmlText) {

        if (getActivity() == null) return;

        TextView text = (TextView) getActivity().findViewById(R.id.companyMissionTextView);
        EditText editText = (EditText) getActivity().findViewById(R.id.companyMissionEditText);

        if (htmlText) {
            text.setText(Html.fromHtml(description));

        } else {
            text.setText(description);
        }
        editText.setText(description);

    }

    @Override
    public void showFieldsOfWorkDescription(List<String> fields) {
        if (getActivity() == null) return;

        //remove excess views
        while (fields.size() < fieldsOfWorkViews.size()) {
            fieldsOfWorkViews.remove(0);
            fieldsOfWorkEditTextViews.remove(0);
            fieldsTextLayout.removeViewAt(0);
            fieldsEditLayout.removeViewAt(0);
        }

        //add the missing views
        while (fields.size() > fieldsOfWorkViews.size()) {
            addFields();
        }

        //populate the views
        ListIterator<View> itEdit = fieldsOfWorkEditTextViews.listIterator();
        ListIterator<View> itText = fieldsOfWorkViews.listIterator();

        for (String txt : fields) {

            final RelativeLayout textV = (RelativeLayout) itText.next();
            final RelativeLayout editV = (RelativeLayout) itEdit.next();
            TextView textView = (TextView) textV.findViewById(R.id.fieldItemTextView);
            EditText editText = (EditText) editV.findViewById(R.id.fieldItemEditView);
            textView.setText(txt);
            editText.setText(txt);


        }

    }

    private void setItem(View layout, int icon, String text) {

        ImageView iconImageView = (ImageView) layout.findViewById(R.id.item_icon);
        iconImageView.setImageDrawable(getActivity().getResources().getDrawable(icon));
        TextView textTextView = (TextView) layout.findViewById(R.id.item_text);
        textTextView.setText(text);
        EditText editText = (EditText) layout.findViewById(R.id.item_editText);
        editText.setText(text);
    }

    private void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void makeEditable() {
        switchViews();
        EditText nameEditText = (EditText) getActivity().findViewById(R.id.nameTextView);
        nameEditText.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);

        editable = true;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().invalidateOptionsMenu();

    }

    private void editCancelled() {
        switchViews();

        //remove the focus and close the keyboard
        if (getActivity().getCurrentFocus() != null) {
            getActivity().getCurrentFocus().clearFocus();
        }
        header.requestFocus();
        hideSoftKeyboard();

        editable = false;

        //hide the up icon
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //hide the Name editText
        ((EditText) getActivity().findViewById(R.id.nameTextView)).setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
        //reload the optionsmenu
        getActivity().invalidateOptionsMenu();
    }

    private void editDone() {
        switchViews();
        //notify changes to the presenter
        EditText aboutEditText = (EditText) getActivity().findViewById(R.id.aboutCompanyEditText);
        EditText missionEditText = (EditText) getActivity().findViewById(R.id.companyMissionEditText);
        presenter.updateAboutCompany(aboutEditText.getText().toString());
        presenter.updateMission(missionEditText.getText().toString());


        Map<String, String> infoMap = new HashMap<>();
        for (String s : contactAndInfoViews.keySet()) {
            View v = contactAndInfoViews.get(s);
            String value = ((EditText) v.findViewById(R.id.item_editText)).getText().toString();
            infoMap.put(s, value);
        }
        EditText nameEditText = (EditText) getActivity().findViewById(R.id.nameTextView);
        infoMap.put("name", nameEditText.getText().toString());
        presenter.updateInfo(infoMap);


        List<String> fields = new LinkedList<>();
        for (View v : fieldsOfWorkEditTextViews) {
            EditText fieldEditText = (EditText) v.findViewById(R.id.fieldItemEditView);
            fields.add(fieldEditText.getText().toString());
        }
        presenter.updateFieldsOfWork(fields);

        presenter.saveUpdate();

        //remove the focus and close the keyboard
        if (getActivity().getCurrentFocus() != null) {
            getActivity().getCurrentFocus().clearFocus();
        }
        header.requestFocus();
        hideSoftKeyboard();


        editable = false;
        //hide the up icon
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //hide the Name editText and the add button
        nameEditText.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        //reload the optionsmenu
        getActivity().invalidateOptionsMenu();
    }

    private void switchViews() {
        // switch the text views with edit text views with animation
        final ViewSwitcher descriptionViewSwitcher = (ViewSwitcher) getActivity().findViewById(R.id.aboutCompanyViewSwitcher);
        descriptionViewSwitcher.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                .setListener(new MyAnimatorListener(descriptionViewSwitcher)).start();

        final ViewSwitcher missionViewSwitcher = (ViewSwitcher) getActivity().findViewById(R.id.companyMissionViewSwitcher);
        missionViewSwitcher.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                .setListener(new MyAnimatorListener(missionViewSwitcher)).start();

        final ViewSwitcher fieldsViewSwitcher = (ViewSwitcher) getActivity().findViewById(R.id.fieldsOfWorkViewSwitcher);
        fieldsViewSwitcher.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                .setListener(new MyAnimatorListener(fieldsViewSwitcher)).start();


        for (View view : contactAndInfoViews.values()) {
            final ViewSwitcher infoViewSwitcher = (ViewSwitcher) view.findViewById(R.id.item_viewSwitcher);
            infoViewSwitcher.getCurrentView().animate().alpha(0f).setDuration(DURATION)
                    .setListener(new MyAnimatorListener(infoViewSwitcher)).start();


        }
    }

    private void addFields() {

        final View view1 = getActivity().getLayoutInflater().inflate(R.layout.fields_of_work_item, null);
        final View view2 = getActivity().getLayoutInflater().inflate(R.layout.fields_of_work_edit_item, null);
        fieldsTextLayout.addView(view1);
        fieldsEditLayout.addView(view2);
        fieldsOfWorkViews.add(view1);
        fieldsOfWorkEditTextViews.add(view2);

        Button remove = (Button) view2.findViewById(R.id.removeItemButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldsOfWorkViews.remove(view1);
                fieldsOfWorkEditTextViews.remove(view2);
                fieldsTextLayout.removeView(view1);
                fieldsEditLayout.removeView(view2);
            }
        });


    }
}
