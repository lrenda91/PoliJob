package it.polito.mad.polijob.company;

import java.util.List;
import java.util.Map;

import it.polito.mad.polijob.model.Company;


/**
 * Created by giuseppe on 07/05/15.
 */
public interface CompanyProfilePresenter {

    void addModel(Company company);

    void addView(CompanyProfileView view);

    void removeView(CompanyProfileView view);

    void onResume();

    void updateAboutCompany(String description);

    void updateMission(String description);

    void updateFieldsOfWork(List<String> fields);

    void updateInfo(Map<String, String> infoMap);

    void saveUpdate();
}
