package it.polito.mad.polijob.model;

import com.parse.*;
import com.parse.ParseException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.polito.mad.polijob.PoliJobApplication;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.company.FavoriteStudentsFragment;

/**
 * Created by luigi on 07/05/15.
 */
public class PoliJobDB {

    public interface LogoCallback {
        //void onLogoException(ParseException pe);
        void onLogoSuccess();
    }

    public interface ParseObjectObserver<T extends ParseObject> {
        void updateContent(T t);
    }

    public interface LogInCallback {
        void onLogInException(ParseException pe);
        void onLogInSuccess(ParseObject user);
    }

    public interface UploadedCallback {
        void onUploadPhotoException(ParseException pe);
        void onUploadPhotoInSuccess();
    }

    public interface CompaniesFoundCallback {
        void onListFound(List<Company> list);
    }

    public interface PositionFetchCallback {
        void onPositionFound(Position position);
        void onPositionNotFound();
        void onPositionFetchException(ParseException pe);
    }

    public interface StudentsFoundCallback {
        void onListFound(List<Student> list);
    }

    public interface PositionsFoundCallback {
        void onListFound(List<Position> list);
        void onFindPositionException(ParseException pe);
    }

    public interface CompanyFetchedCallback {
        void onCompanyFetchedException(ParseException pe);
        void onCompanyFetchedSuccess(CompanyStruct company);
    }

    public interface LogOutCallback {
        void onLogOutException(ParseException pe);
        void onLogOutSuccess();
    }

    public interface SignUpCallback {
        void onSignUpException(ParseException pe);
        void onSignUpSuccess();
    }

    public interface GetPositionsCallback {
        void onGetPositionsException(ParseException pe);
        void onGetPositionsSuccess(List<Position> result);
    }

    public interface AppliedPositionSearchCallback {
        void onAppliedPositionSearchResult(boolean found);
    }

    private static final String APPLICATION_ID = "uQyfGzmjJhLFvS3pRFEKVmy9a3E7SOYvfEj7YzE3";
    //private static final String APPLICATION_ID = "D1XKWrxtqaFmHqR2r8zBktG8qov5GEdh7S3Er761";
    private static final String CLIENT_KEY = "y8wu1Cjexh5rfxsRAEgxaYnHHDOoNwqTiEL6uFoW";
    //private static final String CLIENT_KEY = "RQ4suqmhVWpNfHy6lpOLQxgPtDmVmthRy0oxwGy7";

    public static final String SPECIFIC_USER = "relation";
    //public static final String CLASS_POSITION = "Position";
    //public static final String CLASS_STUDENT = "Student";
    public static final String SPECIFIC_COMPANY = "relationC";

    private PoliJobDB() {
    }

    public static void initialize(Context context, Class<? extends ParseObject>... classesToRegister) {
        if (context == null) {
            throw new RuntimeException("context must be non null");
        }
        for (Class cl : classesToRegister) {
            ParseObject.registerSubclass(cl);
        }
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, APPLICATION_ID, CLIENT_KEY);
    }

    public static ParseObject tryLocalLogin() {
        ParseObject po = null;
        if (ParseUser.getCurrentUser() == null) {
            return null;
        }
        if (ParseUser.getCurrentUser().get("type").equals("student")) {
            po = (ParseObject) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        } else {
            po = (ParseObject) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
        }
        try {
            po.fetchFromLocalDatastore();
        } catch (ParseException e) {
            return null;
        }
        return po;
    }

    public static void loginUser(String username, String password, final LogInCallback listener) {
        ParseUser.logInInBackground(username, password, new com.parse.LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    ParseObject relatedUser;
                    if (parseUser.get("type").equals("student")) {
                        relatedUser = (ParseObject) parseUser.get(PoliJobDB.SPECIFIC_USER);
                    } else {
                        relatedUser = (ParseObject) parseUser.get(PoliJobDB.SPECIFIC_COMPANY);
                    }

                    if (relatedUser == null) {
                        if (listener != null) {
                            listener.onLogInException(new ParseException(-1, "Cannot resolve user"));
                        }
                        return;
                    }
                    try {
                        relatedUser.fetchIfNeeded();
                        String k = (relatedUser instanceof Student) ? "photo" : "logo";
                        ParseFile f = (ParseFile) relatedUser.get(k);
                        if (f != null) {
                            f.save();
                        }
                        relatedUser.pin();
                    } catch (ParseException ex) {
                        if (listener != null) {
                            listener.onLogInException(ex);
                        }
                        return;
                    }
                    if (listener != null) {
                        listener.onLogInSuccess(relatedUser);
                    }
                } else if (listener != null) {
                    listener.onLogInException(e);
                }
            }
        });
    }


    public static void logOut(final LogOutCallback listener) {
        ParseObject relatedUser;
        if (ParseUser.getCurrentUser().get("type").equals("student")) {
            relatedUser = (ParseObject) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        } else {
            relatedUser = (ParseObject) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
        }
        try {
            relatedUser.unpin();
        } catch (ParseException e) {
            if (listener != null) {
                listener.onLogOutException(e);
            }
        }
        ParseUser.logOutInBackground(new com.parse.LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (listener != null) {
                    if (e != null) {
                        listener.onLogOutException(e);
                    } else {
                        listener.onLogOutSuccess();
                    }
                }
            }
        });
    }

    public static void signUpUser(final ParseUser user, final SignUpCallback listener) {
        user.signUpInBackground(new com.parse.SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (listener != null) {
                    if (e == null) {
                        listener.onSignUpSuccess();
                    } else {
                        listener.onSignUpException(e);
                    }
                }
            }
        });
    }

    public static void updateUserFieldsCompany(final CompanyStruct companyStruct) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Company company = (Company) currentUser.get(PoliJobDB.SPECIFIC_COMPANY);

            if(companyStruct.aboutCompany!=null) company.setAboutMe(companyStruct.aboutCompany);
            if(companyStruct.missions!=null) company.setMission(companyStruct.missions);
            if(companyStruct.awards!=null) company.setAwards(companyStruct.awards);
            if(companyStruct.fields!=null) company.setFieldsOfWork(companyStruct.fields);
            company.setCompanytName(companyStruct.companyNametxt);
            company.setDepartment(companyStruct.departmenttxt);
            company.setCountry(companyStruct.countrytxt);
            company.setCity(companyStruct.citytxt);
            if(companyStruct.contactNametxt!=null) company.setContactName(companyStruct.contactNametxt);
            company.setPhoneNumber(companyStruct.phonetxt);
            company.setFaxNumber(companyStruct.faxtxt);
            company.setWebPage(companyStruct.webPagetxt);
            company.setNumOfWorkers(companyStruct.numberofWorkers);
            company.setAddress(companyStruct.addresstxt);
            currentUser.put("relationC", company);
            currentUser.saveEventually();
            ParseQuery<Company> q = ParseQuery.getQuery(Company.class);
        }
    }

    public static void updateUserFields(final StudentStruct studentStruct) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Student student = (Student) currentUser.get(PoliJobDB.SPECIFIC_USER);
            student.setFirstName(studentStruct.firstNametxt);
            student.setLastName(studentStruct.lastNametxt);
            if(studentStruct.aboutMe!=null) student.setAboutme(studentStruct.aboutMe);
            student.setNationality(studentStruct.Nationality);
            student.setGender(studentStruct.Gender);
            student.setCountry(studentStruct.countrytxt);
            student.setAddress(studentStruct.Address);
            if (studentStruct.projects != null) student.setProjects(studentStruct.projects);
            if (studentStruct.career != null) student.setCareer(studentStruct.career);
            if (studentStruct.hobbies != null) student.setHobbies(studentStruct.hobbies);
            if (studentStruct.skills != null) student.setSkills(studentStruct.skills);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if(!studentStruct.BirhtDate.equals("")) student.setBirhtDate(df.parse(studentStruct.BirhtDate));
                if (studentStruct.startDate != null) student.setAvailableStart(df.parse(studentStruct.startDate));
                if (studentStruct.endDate != null) student.setAvailableEnd(df.parse(studentStruct.endDate));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            student.setCity(studentStruct.citytxt);
            student.setPhoneNumber(studentStruct.PhoneNumber);
            student.setSSN(studentStruct.SSN);
            student.setGender(studentStruct.Gender);
            student.saveInBackground();
            ParseQuery<Student> q = ParseQuery.getQuery(Student.class);
            if (studentStruct.fileName != null) uploadResume(studentStruct.fileName);
            //if(studentStruct.parseFile!=null) encodePhoto(studentStruct.parseFile);
        }
    }

    public static void getSkillBasedPositions(Student user, final GetPositionsCallback listener) {
        List<Position> positionList = null;
        ParseQuery<Position> query = ParseQuery.getQuery(Position.class);
        List<String> userSkills = user.getSkills();
        if (userSkills == null) {
            userSkills = new ArrayList<>();
        } else {
            query.whereContainedIn("skills", userSkills);
        }
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                if (listener != null) {
                    if (e == null) {
                        listener.onGetPositionsSuccess(list);
                    } else {
                        listener.onGetPositionsException(e);
                    }
                }
            }
        });
    }

    public static List<Student> getSkillBasedStudents(List<String> skills) {
        List<Student> studentList = null;
        ParseQuery<Student> query = ParseQuery.getQuery(PoliJobDB.SPECIFIC_USER);
        query.whereContainedIn("skills", skills);
        query.orderByDescending("updatedAt");
        try {
            studentList = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public static void tryFindAppliedPosition(final String posID, final AppliedPositionSearchCallback listener){
        getAppliedPositions(new PositionsFoundCallback() {
            @Override
            public void onListFound(List<Position> list) {
                for (Position p : list){
                    if (p.getObjectId().equals(posID) && listener != null){
                        listener.onAppliedPositionSearchResult(true);
                        return;
                    }
                }
            }
            @Override
            public void onFindPositionException(ParseException pe) {
                if (listener != null){
                    listener.onAppliedPositionSearchResult(false);
                }
            }
        });
    }

    public static void uploadResume(final String file) {
        byte[] b = convertToByte(file);
        final ParseFile fileParse = new ParseFile("resume.pdf", b);
        fileParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("parse", " ->>>> " + e.getMessage());
                }
                Log.d("parse", " ->>>> " + "file saved");
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);

                student.setResumeFile(fileParse);
                student.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.d("parse", " student->>>> " + e.getMessage());
                        }
                    }
                });

            }
        });


    }


    public static byte[] convertToByte(String m_chosen) {
        File file = new File(m_chosen);

        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            /*for (int i = 0; i < b.length; i++) {
                System.out.print((char)b[i]);
            }*/
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }

    public static void convertByteToFile(String outputFilePath) {
        String strFilePath = "Your path";
        try {
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            String strContent = "Write File using Java ";

            fos.write(strContent.getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (IOException ioe) {
            System.out.println("IOException : " + ioe);
        }
    }


    public static void encodePhoto(int requestCode, int resultCode, Intent data, Uri mImageCaptureUri, Activity activity, final UploadedCallback listener) {
        Bitmap bitmap = null;
        String path = "";

        if (requestCode == 2) {
            mImageCaptureUri = data.getData();
            path = getRealPathFromURI(mImageCaptureUri, activity); //from Gallery

            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        } else {
            path = mImageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        final ParseFile fileParse = new ParseFile("image.jpg", image);

        fileParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("parse", " ->>>> " + e.getMessage());
                    listener.onUploadPhotoException(e);
                }
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                student.setPhotoFile(fileParse);
                student.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.d("parse", " pic->>>> " + e.getMessage());
                            listener.onUploadPhotoException(e);
                        }
                        listener.onUploadPhotoInSuccess();
                    }
                });

            }
        });
    }


    public static void encodeLogo(int requestCode, int resultCode, Intent data, Uri mImageCaptureUri, Activity activity, final UploadedCallback listener) {
        Bitmap bitmap = null;
        String path = "";

        if (requestCode == 2) {
            mImageCaptureUri = data.getData();
            path = getRealPathFromURI(mImageCaptureUri, activity); //from Gallery

            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        } else {
            path = mImageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        final ParseFile fileParse = new ParseFile("image.jpg", image);

        fileParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("parse", " ->>>> " + e.getMessage());
                    listener.onUploadPhotoException(e);
                }
                Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
                company.setLogo(fileParse);
                company.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.d("parse", " pic->>>> " + e.getMessage());
                            listener.onUploadPhotoException(e);
                        }
                        listener.onUploadPhotoInSuccess();
                    }
                });

            }
        });
    }



    private static String getRealPathFromURI(Uri contentUri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        //Cursor cursor       = managedQuery(contentUri, proj, null, null,null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    public static void getCompanyStruct(String companyID, final CompanyFetchedCallback listener) {

        final CompanyStruct company = new CompanyStruct();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.getInBackground(companyID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    company.companyNametxt = ((Company) object).getCompanyName();
                    //company.companyNametxt=parentCompany.getCity();

                    listener.onCompanyFetchedSuccess(company);
                } else {

                }
            }
        });

    }


    public static void addToFavoriteCompanies(String posID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.getInBackground(posID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                student.addFavoriteCompanies(student.getRelationFavoriteCompanies(), (Company) parseObject);
                student.saveInBackground();
            }
        });

    }


    public static void addToFavoritePositons(String posID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Position");
        query.getInBackground(posID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                student.addFavoritePositions(student.getRelationFavoritePositions(), (Position) parseObject);
                student.saveInBackground();
            }
        });
    }


    public static void addToAppliedPositons(String posID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Position");
        query.getInBackground(posID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);

                student.addAppliedPositions(student.getRelationAppliedPositions(), (Position) parseObject);
                student.saveInBackground();
                ((Position) parseObject).addAppliedStudents(((Position) parseObject).getRelationAppliedStudents(), student);
                ((Position) parseObject).saveInBackground();
            }
        });
    }

    public static void removeFromAppliedPositons(String posID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Position");
        query.getInBackground(posID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);

                student.removeAppliedPositions(student.getRelationAppliedPositions(), (Position) parseObject);
                student.saveInBackground();
                ((Position) parseObject).removeAppliedStudents(((Position) parseObject).getRelationAppliedStudents(), student);
                ((Position) parseObject).saveInBackground();
            }
        });
    }

    public static void getFavoritePositions(final PositionsFoundCallback listener) {
        Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        ParseRelation rel = student.getRelationFavoritePositions();
        ParseQuery<Position> query = rel.getQuery();
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                if (e == null) listener.onListFound(list);
                else Log.d("parse2", " " + e.getMessage());
            }
        });
    }

    public static void findCompanyPositionByID(final String ID, final PositionFetchCallback listener){
        Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
        ParseRelation<Position> rel = company.getRelationPositions();
        rel.getQuery().getInBackground(ID, new GetCallback<Position>() {
            @Override
            public void done(Position position, ParseException e) {
                if (listener != null) {
                    if (e == null) {
                        listener.onPositionFound(position);
                    } else {
                        listener.onPositionNotFound();
                    }
                }
            }
        });
    }

    public static void findFavoritePositionByID(final String ID, final PositionFetchCallback listener){
        Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        ParseRelation rel = student.getRelationFavoritePositions();
        ParseQuery<Position> query = rel.getQuery();
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                if (e != null) {
                    listener.onPositionFetchException(e);
                    return;
                }
                for (Position pos : list) {
                    if (pos.getObjectId().equals(ID)) {
                        if (listener != null) {
                            listener.onPositionFound(pos);
                            return;
                        }
                    }
                }
                if (listener != null) {
                    listener.onPositionNotFound();
                }
            }
        });
    }


    public static void getAppliedPositions(final PositionsFoundCallback listener) {
        Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        ParseRelation rel = student.getRelationAppliedPositions();
        ParseQuery<Position> query = rel.getQuery();
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                if (e == null) listener.onListFound(list);
                else Log.d("parse2", " " + e.getMessage());
            }
        });

    }

    public static void getFavoriteCompanies(final CompaniesFoundCallback listener) {
        Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        ParseRelation rel = student.getRelationFavoriteCompanies();
        ParseQuery<Company> query = rel.getQuery();
        query.findInBackground(new FindCallback<Company>() {
            @Override
            public void done(List<Company> list, ParseException e) {
                if (e == null) listener.onListFound(list);
                else Log.d("parse1", " " + e.getMessage());
            }
        });
    }


    public static void getAppliedStudents(String ID, final StudentsFoundCallback listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Position");
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                ParseRelation rel = ((Position) parseObject).getRelationAppliedStudents();
                ParseQuery<Student> query = rel.getQuery();
                query.findInBackground(new FindCallback<Student>() {
                    @Override
                    public void done(List<Student> list, ParseException e) {
                        listener.onListFound(list);
                    }
                });

            }
        });
    }

    public static void getFavoriteStudents(final StudentsFoundCallback listener) {
        Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
        ParseRelation rel = company.getRelationFavoriteStudents();
        ParseQuery<Student> query = rel.getQuery();
        query.findInBackground(new FindCallback<Student>() {
            @Override
            public void done(List<Student> list, ParseException e) {
                if (e == null) {
                    listener.onListFound(list);
                } else {
                    Log.d("parseQueryGetFavSTU", " " + e.getMessage());
                }
            }
        });
    }

    public static void addToFavoriteStudents(String stuID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Student");
        query.getInBackground(stuID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
                company.addFavoriteStudents(company.getRelationFavoriteStudents(), (Student) parseObject);
                company.saveInBackground();
            }
        });
    }


    public static void deletePositionFromList(String posID,final String flag) {
        ParseQuery<Position> query = ParseQuery.getQuery("Position");
        query.getInBackground(posID, new GetCallback<Position>() {
            @Override
            public void done(Position item, ParseException e) {
                if (flag.equals("favouriteOffers")) {
                    Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                    student.getRelationFavoritePositions().remove(item);
                    student.saveInBackground();
                } else if (flag.equals("appliedPosition")) {
                    Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                    student.getRelationAppliedPositions().remove(item);
                    student.saveInBackground();
                }else if(flag.equals("myposition")){
                    Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
                    company.getRelationPositions().remove(item);
                    company.saveInBackground();
                }
            }
        });


    }

    public static void deleteCompanyFromList(String comID) {
        ParseQuery<Company> query = ParseQuery.getQuery("Company");
        query.getInBackground(comID, new GetCallback<Company>() {
            @Override
            public void done(Company com, ParseException e) {
                Student student = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
                student.getRelationFavoriteCompanies().remove(com);
                student.saveInBackground();
            }
        });

    }

    public static void deleteStudentFromList(String posID,final Student stu, final SaveCallback listener) {
        ParseQuery<Position> query = ParseQuery.getQuery("Position");
        query.getInBackground(posID, new GetCallback<Position>() {
            @Override
            public void done(Position item, ParseException e) {
                 item.getRelationAppliedStudents().remove(stu);
                item.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        listener.done(e);
                    }
                });
            }
        });
    }


    public static void getPositionsList(final GetPositionsCallback listener) {
        Company company = (Company) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_COMPANY);
        ParseRelation rel = company.getRelationPositions();
        ParseQuery<Position> query = rel.getQuery();
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                if (listener != null) {
                    if (e == null) {
                        listener.onGetPositionsSuccess(list);
                    } else {
                        listener.onGetPositionsException(e);
                    }
                }
            }
        });

    }

    public static void filterStudents(FilterStruct filter, final StudentsFoundCallback listener){
        List<Student> positionList = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ParseQuery<Student> query = ParseQuery.getQuery(Student.class);
        //filter.careerDegree
        if(!filter.careerDegree.equals("Select Career")) {
            query.whereContains("myCareer", filter.careerDegree);
        }
        if(!filter.studentCountry.equals("Select Country")) {
            query.whereContains("country", filter.studentCountry);
        }
        if(!filter.studentCity.equals("")) {
            query.whereContains("city", filter.studentCity);
        }
        if(!filter.languages.isEmpty()) {
            query.whereContainedIn("languages", filter.languages);
        }
        if(!filter.studentAvailabilityStartDate.equals("")){
            //df.parse(filter.studentAvailabilityStartDate)
            try {
                query.whereGreaterThanOrEqualTo("availabilityStart", df.parse(filter.studentAvailabilityStartDate));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        if(!filter.studentAvailabilityEndDate.equals("")){
            //df.parse(filter.studentAvailabilityStartDate)
            try {
                query.whereLessThanOrEqualTo("availabilityEnd", df.parse(filter.studentAvailabilityEndDate));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Student>() {
            @Override
            public void done(List<Student> list, ParseException e) {
                listener.onListFound(list);
            }
        });
    }
    public static void filterCompany(FilterStruct filter, final CompaniesFoundCallback listener){
        ParseQuery<Company> query = ParseQuery.getQuery(Company.class);
        if(!filter.depName.equals("Select Department")) {
            query.whereContains("department", filter.depName);
        }
        if(!filter.comName.equals("")) {
            query.whereContains("companyName", filter.comName);
        }
        if(!filter.studentCountry.equals("Select Country")) {
            query.whereContains("country", filter.studentCountry);
        }
        if(!filter.studentCity.equals("")) {
            query.whereContains("city", filter.studentCity);
        }
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Company>() {
            @Override
            public void done(List<Company> list, ParseException e) {
                listener.onListFound(list);
            }
        });
    }


    public static void filterPositions(FilterStruct filter, final GetPositionsCallback listener){
        List<Position> positionList = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ParseQuery<Position> query = ParseQuery.getQuery(Position.class);
        //filter.careerDegree
        if(!filter.typeOfDegree.equals("Required Career")) {
            query.whereContains("typeOfDegree", filter.typeOfDegree);
        }
        if(!filter.typeOfJob.equals("Select Position")) {
            query.whereContains("typeOfJob", filter.typeOfJob);
        }
        if(!filter.typeOfContract.equals("Select Contract")) {
            query.whereContains("typeOfContract", filter.typeOfContract);
        }
        if(!filter.positionCountry.equals("Select Country")) {
            query.whereContains("country", filter.positionCountry);
        }
        if(!filter.positionCity.equals("")) {
            query.whereContains("city", filter.positionCity);
        }

        if(!filter.studentAvailabilityStartDate.equals("")){
            //df.parse(filter.studentAvailabilityStartDate)
            try {
                query.whereGreaterThanOrEqualTo("availabilityStart", df.parse(filter.studentAvailabilityStartDate));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        if(!filter.studentAvailabilityEndDate.equals("")){
            //df.parse(filter.studentAvailabilityStartDate)
            try {
                query.whereLessThanOrEqualTo("availabilityEnd", df.parse(filter.studentAvailabilityEndDate));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Position>() {
            @Override
            public void done(List<Position> list, ParseException e) {
                listener.onGetPositionsSuccess(list);
            }
        });
    }


    public static void getLogoCompany(final Company company,final LogoCallback listener) {

            company.getLogo().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        final ParseFile fileParse = new ParseFile("image.jpg", bytes);
                        company.setLogo(fileParse);
                        listener.onLogoSuccess();
                    } else {
                        listener.onLogoSuccess();
                    }
                }
            });

    }

    public static void getLogoStudent(final Student student,final LogoCallback listener) {
        student.getPhotoFile().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    final ParseFile fileParse = new ParseFile("image.jpg", bytes);
                    student.setPhotoFile(fileParse);
                    listener.onLogoSuccess();
                } else {
                    listener.onLogoSuccess();
                }
            }
        });
    }


}


