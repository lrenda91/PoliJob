package it.polito.mad.polijob.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by positive on 08/05/15.
 */
public class CompanyStruct implements Serializable {

    public String companyNametxt;
    public String departmenttxt;
    public String countrytxt;
    public String citytxt;
    public String contactNametxt;
    public String phonetxt;
    public String faxtxt;
    public String webPagetxt;
    public String addresstxt;
    public String aboutCompany;
    public String missions;
    public String awards;
    public ArrayList<String> fields;
    public int numberofWorkers;


    public CompanyStruct() {
    }
}
