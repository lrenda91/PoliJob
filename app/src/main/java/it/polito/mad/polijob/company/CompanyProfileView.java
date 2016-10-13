package it.polito.mad.polijob.company;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

/**
 * Created by giuseppe on 07/05/15.
 */
public interface CompanyProfileView {

    void showLogo(Bitmap logo);

    void showInfo(Map<String, String> infoMap);

    void showAboutCompanyDescription(String description, boolean htmlText);

    void showMissionDescription(String description, boolean htmlText);

    void showFieldsOfWorkDescription(List<String> filds);
}
