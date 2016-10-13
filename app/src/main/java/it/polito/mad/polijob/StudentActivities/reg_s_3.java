package it.polito.mad.polijob.StudentActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.controller.SimpleFileDialog;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.StudentStruct;
import it.polito.mad.polijob.student.StudentMainActivity;

public class reg_s_3 extends Activity implements PoliJobDB.UploadedCallback, View.OnClickListener {

    final Context context = this;

    private EditText editTxt;
    private Button btn;
    private ArrayList<String> arrayList;
    private ListView list;
    private Button back;
    String m_chosen;

    private ArrayAdapter<String> adapter;

    private StudentStruct studentStruct;



    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private  Spinner element;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;


    private ImageView mImageView;
    private ProgressBar progressBar;
    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_student_page3);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
         studentStruct = (StudentStruct)
                getIntent().getSerializableExtra("studentStruct");

        addFields();//here i init button listeners of register fields
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Button button = (Button) findViewById(R.id.register3_student_skip_button);
        Button button4 = (Button) findViewById(R.id.aboutcompany_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Utility.networkIsUp(reg_s_3.this)){
                    Toast.makeText(getApplicationContext(),
                            "No network",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                PoliJobDB.updateUserFields(studentStruct);
                startActivity(new Intent(reg_s_3.this, StudentMainActivity.class));
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.register3_student_update_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Utility.networkIsUp(reg_s_3.this)){
                    Toast.makeText(getApplicationContext(),
                            "No network",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                PoliJobDB.updateUserFields(studentStruct);
                startActivity(new Intent(reg_s_3.this, StudentMainActivity.class));
                finish();
            }
        });
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);




    }


    private void addFields() {
        Button addSkills = (Button) findViewById(R.id.skills_button);
        addSkills.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_skills, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("AddSKills");
                list = (ListView) convertView.findViewById(R.id.skillS_list);
                editTxt = (EditText) convertView.findViewById(R.id.addSkillSEdit);
                btn = (Button) convertView.findViewById(R.id.addSkillSButton);
                arrayList = new ArrayList<>();
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                list.setAdapter(adapter);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.add(editTxt.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(arrayList!=null) studentStruct.skills=arrayList;
                    }
                });
                alertDialog.show();
            }
        });



        //Button1
        Button dirChooserButton1 = (Button) findViewById(R.id.uploadresume_button);
        dirChooserButton1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                /////////////////////////////////////////////////////////////////////////////////////////////////
                //Create FileOpenDialog and register a callback
                /////////////////////////////////////////////////////////////////////////////////////////////////
                SimpleFileDialog FileOpenDialog =  new SimpleFileDialog(reg_s_3.this, "FileOpen",
                        new SimpleFileDialog.SimpleFileDialogListener()
                        {
                            @Override
                            public void onChosenDir(String chosenDir)
                            {
                                // The code in this function will be executed when the dialog OK button is pushed
                                m_chosen = chosenDir;
                                Toast.makeText(reg_s_3.this, "Chosen FileOpenDialog File: " +
                                        m_chosen, Toast.LENGTH_LONG).show();
                                studentStruct.fileName=m_chosen;
                            }
                        });

                //You can change the default filename using the public variable "Default_File_Name"
                FileOpenDialog.Default_File_Name = "";
                FileOpenDialog.chooseFile_or_Dir();

            }
        });


        final String [] items           = new String [] {"From Camera", "From SD Card"};
        final ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);


        ((Button) findViewById(R.id.addphoto_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                builder.setTitle("Select Image");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory(),
                                    "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                            mImageCaptureUri = Uri.fromFile(file);
                            try {
                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                                intent.putExtra("return-data", true);
                                startActivityForResult(intent, PICK_FROM_CAMERA);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                        }
                    }
                });
                builder.show();
            }
        });




        Button button2 = (Button) findViewById(R.id.aboutme_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("About me");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                arrayList = new ArrayList<>();
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        studentStruct.aboutMe = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });


        Button button9 = (Button) findViewById(R.id.linksto_button);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Links to Projects");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        studentStruct.projects = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });


        Button button8 = (Button) findViewById(R.id.hobbies_button);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Hobbies");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        studentStruct.hobbies = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });

        Button button7 = (Button) findViewById(R.id.career_button);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_career, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("University Career");
                //editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                 element = (Spinner) convertView.findViewById(R.id.spinner_career);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        studentStruct.career = element.getSelectedItem().toString();
                    }
                });
                alertDialog.show();
            }
        });


        Button button4 = (Button) findViewById(R.id.available_button);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_s_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_available_period, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Availability Date");
                findViewsById(convertView);
                setDateTimeField();
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        studentStruct.startDate = fromDateEtxt.getText().toString();
                        studentStruct.endDate= toDateEtxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        setUserInteractionEnabled(false);
        PoliJobDB.encodePhoto(requestCode, resultCode, data, mImageCaptureUri, reg_s_3.this, this);
        //TODO add progress bar
    }

    private void findViewsById(View convertView) {
        fromDateEtxt = (EditText) convertView.findViewById(R.id.etxt_fromdate);
        //fromDateEtxt.setInputType(InputType.TYPE_NULL);
        //fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) convertView.findViewById(R.id.etxt_todate);
        //toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setUserInteractionEnabled(boolean enabled){
        progressBar.setVisibility(enabled ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onUploadPhotoException(ParseException pe) {

    }

    @Override
    public void onUploadPhotoInSuccess() {
        setUserInteractionEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}