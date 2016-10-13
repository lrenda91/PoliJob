package it.polito.mad.polijob.company;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.mad.polijob.model.Company;
import it.polito.mad.polijob.model.PoliJobDB;

/**
 * Created by giuseppe on 19/05/15.
 */
public class CompanyProfilePresenterImpl implements CompanyProfilePresenter {

    private static CompanyProfilePresenterImpl instance;

    private List<CompanyProfileView> views;
    private Company company;

    private CompanyProfilePresenterImpl() {
        views = new LinkedList<>();
    }

    public static CompanyProfilePresenter newInstance() {
        if (instance == null) {
            instance = new CompanyProfilePresenterImpl();
        }
        return instance;
    }

    @Override
    public void addModel(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("company can not be null");
        }
        this.company = company;
    }

    @Override
    public void addView(CompanyProfileView view) {
        if (view == null) {
            throw new IllegalArgumentException("View can not be null");
        }
        views.add(view);

    }

    @Override
    public void removeView(CompanyProfileView view) {
        views.remove(view);
    }

    @Override
    public void onResume() {



        for (CompanyProfileView view : views) {
            updateView(view);
        }

    }

    @Override
    public void updateAboutCompany(String description) {
        company.setAboutMe(description);
    }

    @Override
    public void updateMission(String description) {
        company.setMission(description);
    }

    @Override
    public void updateFieldsOfWork(List<String> fields) {
        company.setFieldsOfWork(fields);
    }

    @Override
    public void updateInfo(Map<String, String> map) {
        if (map.containsKey("name")) company.setName(map.get("name"));
        if (map.containsKey("website")) company.setWebPage(map.get("website"));
        if (map.containsKey("email")) ParseUser.getCurrentUser().setUsername(map.get("email"));
        ParseUser.getCurrentUser().saveEventually();

        if (map.containsKey("phone")) company.setPhoneNumber(map.get("phone"));
        if (map.containsKey("fax")) company.setFaxNumber(map.get("fax"));
        if (map.containsKey("contact name")) company.setContactName(map.get("contact name"));
        if (map.containsKey("department")) company.setDepartment(map.get("department"));
        int num = (tryParseInt(map.get("num. of worker"))) ? Integer.parseInt(map.get("num. of worker")) : company.getNumOfWorkers();
        company.setNumOfWorkers(num);
        if (map.containsKey("address")) company.setAddress(map.get("address"));
        if (map.containsKey("country")) company.setCountry(map.get("country"));
        if (map.containsKey("city")) company.setCity(map.get("city"));


    }

    @Override
    public void saveUpdate() {

        company.saveInBackground();
        for (CompanyProfileView view : views) {
            updateView(view);
        }
    }


    private void updateView(final CompanyProfileView view) {

        try {
            if (company.getLogo() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(company.getLogo().getData(), 0, company.getLogo().getData().length);
                if (bitmap == null) {
                    Log.println(Log.WARN, CompanyProfilePresenterImpl.class.getName(), "The Logo can not be decode!");
                } else {
                    //TODO something wrong with this function?
                    //view.showLogo(bitmap);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (company.getMission() != null) {
            view.showMissionDescription(company.getMission(), true);
        }
        if (company.getAboutMe() != null) {
            view.showAboutCompanyDescription(company.getAboutMe(), true);
        }
        if (company.getFieldsOfWork() != null) {
            view.showFieldsOfWorkDescription(company.getFieldsOfWork());
        }

        HashMap<String, String> map = new HashMap<>();
        if (company.getName() != null) map.put("name", company.getName());
        if (company.getWebPage() != null) map.put("website", company.getWebPage());
        String email = ParseUser.getCurrentUser().getUsername();
        map.put("email", email);
        Log.d("email","  "+email);
        if (company.getPhoneNumber() != null) map.put("phone", company.getPhoneNumber());
        if (company.getFaxNumber() != null) map.put("fax", company.getFaxNumber());
        if (company.getContactName() != null) map.put("contact name", company.getContactName());
        if (company.getDepartment() != null) map.put("department", company.getDepartment());
        map.put("num. of worker", Integer.toString(company.getNumOfWorkers()));
        if (company.getAddress() != null) map.put("address", company.getAddress());
        if (company.getCountry() != null) map.put("country", company.getCountry());
        if (company.getCity() != null) map.put("city", company.getCity());
        view.showInfo(map);


    }

    boolean tryParseInt(String value) {
        if (value == null) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


}
