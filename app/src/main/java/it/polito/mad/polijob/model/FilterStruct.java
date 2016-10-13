package it.polito.mad.polijob.model;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by positive on 20/05/15.
 */
public class FilterStruct  implements Serializable {
    //Company Student Search
    public String careerDegree = "";
    public List<String> languages = new LinkedList<>();
    public String studentAvailabilityStartDate = "";
    public String studentAvailabilityEndDate = "";
    public String studentCountry = "";
    public String studentCity = "";

    //Student Position Search
    public String typeOfJob = "";
    public String typeOfDegree = "";
    public String typeOfContract = "";
    public int positionFreshness;
    public String positionCountry = "";
    public String positionCity = "";


    //Student Position Search
    public String comName = "";
    public String depName = "";
    public String comCountry = "";
    public String comCity = "";


    public FilterStruct(){

    }

}
