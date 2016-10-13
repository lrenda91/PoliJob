package it.polito.mad.polijob.model;

/**
 * Created by luigi on 07/05/15.
 */
import com.parse.*;

import java.io.Serializable;
import java.util.List;

@ParseClassName("Company")
public class Company extends ParseObject {

    private static final String NAME = "companyName";
    private static final String DEPARTMENT = "department";
    private static final String LOCATION = "location";
    private static final String COUNTRY = "country";
    private static final String CITY= "city";
    private static final String ADDRESS= "address";
    private static final String CONTACTNAME = "contactName";
    private static final String COMPANYNAME = "companyName";

    private static final String PHONENUMBER = "phoneNumber";
    private static final String FAXNUMBER = "faxNumber";
    private static final String WEBPAGE = "webPage";
    private static final String POSITION = "position";
    private static final String NUMOFWORKERS = "numOfWorkers";
    private static final String ABOUTME = "aboutMe";
    private static final String MISSION = "mission";
    private static final String FIELDSOFWORK = "fieldsOfWork";
    private static final String LOGO = "logo";
    private static final String FAVOURITESTUDENTS = "favouriteStudents";
    private static final String POSITIONS = "positions";
    private static final String TEAMS = "teams";
    private static final String AWARDS= "awards";


    public String getAwards(){
        return (String) get(AWARDS);
    }

    public void setAwards(String value){
        put(AWARDS, value);
    }


    public ParseFile getLogo(){
        return (ParseFile) get(LOGO);
    }


    public void setLogo(ParseFile resumeFile) {
        put(LOGO, resumeFile);
    }

    public int getNumOfWorkers(){
        return (int) get(NUMOFWORKERS);
    }

    public void setNumOfWorkers(int numOfWorkers){
        put(NUMOFWORKERS, numOfWorkers);
    }
    public String getLocation(){
        return (String) get(LOCATION);
    }

    public void setLocation(String location){
        put(LOCATION, location);
    }
    public String getDepartment(){
        return (String) get(DEPARTMENT);
    }

    public void setDepartment(String department){
        put(DEPARTMENT, department);
    }
    public List<String> getFieldsOfWork(){
        return (List<String>) get(FIELDSOFWORK);
    }

    public void setFieldsOfWork(List<String> fieldsOfWork){
        put(FIELDSOFWORK, fieldsOfWork);
    }
    public String getAboutMe(){
        return (String) get(ABOUTME);
    }

    public void setAboutMe(String aboutMe){
        put(ABOUTME, aboutMe);
    }

    public List<Position> getPositions(){
        return (List<Position>) get(POSITIONS);
    }

    public void setPositions(List<Position> positions){
        put(POSITIONS, positions);
    }
    public List<Student> getFavouriteStudents(){
        return (List<Student>) get(FAVOURITESTUDENTS);
    }

    public void setFavouriteStudents(List<Student> favouriteStudents){
        put(FAVOURITESTUDENTS, favouriteStudents);
    }
    public String getFaxNumber(){
        return (String) get(FAXNUMBER);
    }

    public void setFaxNumber(String faxNumber){
        put(FAXNUMBER, faxNumber);
    }
    public String getPhoneNumber(){
        return (String) get(PHONENUMBER);
    }

    public void setPhoneNumber(String phoneNumber){
        put(PHONENUMBER, phoneNumber);
    }
    public String getWebPage(){
        return (String) get(WEBPAGE);
    }

    public void setWebPage(String webPage){
        put(WEBPAGE, webPage);
    }
    public String getMission(){
        return (String) get(MISSION);
    }

    public void setMission(String mission){
        put(MISSION, mission);
    }
    public String getContactName(){
        return (String) get(CONTACTNAME);
    }

    public void setContactName(String contactName){
        put(CONTACTNAME, contactName);
    }
    public String getName(){
        return (String) get(NAME);
    }

    public void setName(String name){
        put(NAME, name);
    }

    public String getCompanyName(){
        return (String) get(COMPANYNAME);
    }
    public void setCompanytName(String value){
        put(COMPANYNAME, value);
    }

    public String getCity(){ return (String) get(CITY);  }
    public void setCity(String value) {   put(CITY, value);  }

    public String getAddress(){  return (String) get(ADDRESS);  }
    public void setAddress(String value) {   put(ADDRESS, value);  }

    public String getCountry(){
        return (String) get(COUNTRY);
    }
    public void setCountry(String value){
        put(COUNTRY, value);
    }

    public void addPosition(ParseRelation<Position> relationPositions, Position value){
        relationPositions.add(value);
    }


    //getRelationFavoriteStudents

    public ParseRelation<Student> getRelationFavoriteStudents(){
        return getRelation(FAVOURITESTUDENTS);
    }

    public void addFavoriteStudents(ParseRelation relation, Student student){
        relation.add(student);
    }

    public ParseRelation<Position> getRelationPositions() {
        return getRelation(POSITIONS);
    }


}