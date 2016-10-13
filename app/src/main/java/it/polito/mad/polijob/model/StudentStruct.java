package it.polito.mad.polijob.model;

import com.parse.ParseFile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by positive on 08/05/15.
 */
public class StudentStruct implements Serializable {

    public String firstNametxt;
    public String aboutMe;
    public String lastNametxt;
    public String countrytxt;
    public String citytxt;
    public String startDate;
    public String hobbies;
    public String endDate;
    public String fileName;
    public String career;
    public String projects;
    public String BirhtDate;
    public String SSN ;
    public String PhoneNumber;
    public String Address;
    public String Gender;
    public String Nationality;
    public ArrayList<String> skills;
    public ParseFile parseFile;

    public StudentStruct() {
    }
}
