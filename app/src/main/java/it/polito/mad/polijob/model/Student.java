package it.polito.mad.polijob.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by luigi on 05/05/15.
 */
@ParseClassName("Student")
public class Student extends ParseObject {

    private ParseFile resumeFile;

    public Student() {
        // A default constructor is required.
    }
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String ADDRESS = "address";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String PHONE = "phoneNumber";
    private static final String SSN = "SSN";
    private static final String GENDER = "gender";
    private static final String NATIONALITY = "nationality";
    private static final String ABOUT_ME = "aboutMe";
    private static final String PROJECTS = "myProjects";
    private static final String CAREER = "myCareer";
    private static final String HOBBIES = "hobbies";
    private static final String LANGUAGES = "languages";
    private static final String AVAILABILITYSTART = "availabilityStart";
    private static final String AVAILABILITYEND = "availabilityEnd";
    private static final String PHOTO = "photo";
    private static final String FAVOURITE_COMPANIES = "favouriteCompanies";
    private static final String RESUMES = "resumes";
    private static final String APPLIED = "applied";
    private static final String SKILLS = "skills";
    private static final String FAVOURITE_OFFERS = "favouriteOffers";


    public String getHobbies(){
        return (String) get(HOBBIES);
    }

    public void setHobbies(String name){
        put(HOBBIES, name);
    }


    public String getProjects(){
        return (String) get(PROJECTS);
    }

    public void setProjects(String name){
        put(PROJECTS, name);
    }

    public String getFirstName(){
        return (String) get(FIRSTNAME);
    }

    public void setFirstName(String name){
        put(FIRSTNAME, name);
    }


    public Date getAvailableEnd(){
        return (Date) get(AVAILABILITYEND);
    }

    public void setAvailableEnd(Date name){
        put(AVAILABILITYEND, name);
    }

    public Date getAvailableStart(){
        return (Date) get(AVAILABILITYSTART);
    }

    public void setAvailableStart(Date name){
        put(AVAILABILITYSTART, name);
    }


    public String getLastName(){
        return (String) get(LASTNAME);
    }

    public void setLastName(String name){
        put(LASTNAME, name);
    }



    public String getCountry(){
        return (String) get(COUNTRY);
    }
    public void setCountry(String value){
        put(COUNTRY, value);
    }

    public String getCity(){ return (String) get(CITY);  }
    public void setCity(String value) {   put(CITY, value);  }

    public String getAddress(){  return (String) get(ADDRESS);  }
    public void setAddress(String value) {   put(ADDRESS, value);  }

    public Date getBirthDate(){  return (Date) get(DATE_OF_BIRTH);  }
    public void setBirhtDate(Date value) {   put(DATE_OF_BIRTH, value);  }

    public String getPhoneNumber(){  return (String) get(PHONE);  }
    public void setPhoneNumber(String value) {   put(PHONE, value);  }

    public String getCareer(){  return (String) get(CAREER);  }
    public void setCareer(String value) {   put(CAREER, value);  }

    public String getSSN(){  return (String) get(SSN);  }
    public void setSSN(String value) {   put(SSN, value);  }

    public String getAboutme(){  return (String) get(ABOUT_ME);  }
    public void setAboutme(String value) {   put(ABOUT_ME, value);  }

    public String getNationality(){  return (String) get(NATIONALITY); }
    public void setNationality(String value) {   put(NATIONALITY, value);  }

    public String getGender(){  return (String) get(GENDER);  }
    public void setGender(String value) {   put(GENDER, value);  }

    public String getEmail(){ return (String) get(EMAIL);  }
    public void setEmail(String value) {   put(EMAIL, value);  }

    public List<String> getSkills(){ return (List<String>) get("skills");  }
    public void setSkills(List<String> value) {   put("skills", value);  }

    public String getPassword(){ return (String) get(PASSWORD);  }
    public void setPassword(String value) {   put(PASSWORD, value);  }

    public void setResumeFile(ParseFile resumeFile) {
        put(RESUMES, resumeFile);
    }
    public ParseFile getResumeFile(ParseFile resumeFile) {
        return (ParseFile) get(RESUMES);

    }

    public void setPhotoFile(ParseFile resumeFile) {
        put(PHOTO, resumeFile);
    }
    public ParseFile getPhotoFile() {
        return (ParseFile) get(PHOTO);

    }

    public ParseRelation<Company> getRelationFavoriteCompanies(){
        return getRelation(FAVOURITE_COMPANIES);
    }

    public void addFavoriteCompanies(ParseRelation relation, Company company){
        relation.add(company);
    }

    public ParseRelation<Position> getRelationFavoritePositions(){
        return getRelation(FAVOURITE_OFFERS);
    }

    public ParseRelation<Position> getRelationAppliedPositions(){
        return getRelation(APPLIED);
    }
    public void addAppliedPositions(ParseRelation relation, Position position){
        relation.add(position);
    }
    public void removeAppliedPositions(ParseRelation relation, Position position){
        relation.remove(position);
    }


    public void addFavoritePositions(ParseRelation relation, Position position){
        relation.add(position);
    }

    public List<String> getLanguages(){ return (List<String>) get(LANGUAGES);  }
    public void setLanguages(List<String> value) {   put(LANGUAGES, value);  }
}
