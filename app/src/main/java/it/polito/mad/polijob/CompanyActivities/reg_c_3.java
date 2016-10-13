package it.polito.mad.polijob.CompanyActivities;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;

import java.io.File;
import java.util.ArrayList;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.File;

import it.polito.mad.polijob.MainActivity;
import it.polito.mad.polijob.R;
import it.polito.mad.polijob.company.CompanyMainActivity;
import it.polito.mad.polijob.controller.SimpleFileDialog;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.PoliJobDB;


public class reg_c_3 extends Activity {

    private ArrayAdapter<String> adapter2;
    String m_chosen;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private ProgressBar progressBar;
    private EditText editTxt;
    private CompanyStruct companyStruct;
    private Button btn;
    private ArrayList<String> arrayList;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_company_page3);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

         companyStruct = (CompanyStruct)
                getIntent().getSerializableExtra("companyStruct");

        addFields();//here i init button listeners of register fields

        Button button = (Button) findViewById(R.id.register3_company_skip_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PoliJobDB.updateUserFieldsCompany(companyStruct);
                startActivity(new Intent(reg_c_3.this, CompanyMainActivity.class));
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.register3_company_update_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PoliJobDB.updateUserFieldsCompany(companyStruct);
                startActivity(new Intent(reg_c_3.this, CompanyMainActivity.class));
                finish();
            }
        });




        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }

    private void addFields() {

        final String [] items           = new String [] {"From Camera", "From SD Card"};
        final ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);

        ((Button) findViewById(R.id.addlogo_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(reg_c_3.this, R.style.myDialog));
                builder.setTitle("Select Image");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        if (item == 0) {
                            Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file        = new File(Environment.getExternalStorageDirectory(),
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
                } );
                builder.show();
            }
        });

        Button button2 = (Button) findViewById(R.id.aboutcompany_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_c_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("About me");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //studentStruct.aboutMe = editTxt.getText().toString();
                        companyStruct.aboutCompany = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });

        Button button3 = (Button) findViewById(R.id.missions_button);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_c_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Missions");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        companyStruct.missions = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });

        Button button4 = (Button) findViewById(R.id.awards_button);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_c_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_string_field, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Awards");
                editTxt = (EditText) convertView.findViewById(R.id.addStringEdit);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        companyStruct.awards = editTxt.getText().toString();
                    }
                });
                alertDialog.show();
            }
        });


        Button addSkills = (Button) findViewById(R.id.fields_button);
        addSkills.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(reg_c_3.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.register_add_skills, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Fields of Work");
                list = (ListView) convertView.findViewById(R.id.skillS_list);
                editTxt = (EditText) convertView.findViewById(R.id.addSkillSEdit);
                btn = (Button) convertView.findViewById(R.id.addSkillSButton);
                arrayList = new ArrayList<>();
                adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                list.setAdapter(adapter2);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.add(editTxt.getText().toString());
                        adapter2.notifyDataSetChanged();
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
                        if(arrayList!=null) companyStruct.fields=arrayList;
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
        PoliJobDB.encodeLogo(requestCode, resultCode, data, mImageCaptureUri, reg_c_3.this, new PoliJobDB.UploadedCallback() {
            @Override
            public void onUploadPhotoException(ParseException pe) {

            }
            @Override
            public void onUploadPhotoInSuccess() {

                setUserInteractionEnabled(true);
            }
        });

    }

    private void setUserInteractionEnabled(boolean enabled){
        progressBar.setVisibility(enabled ? View.INVISIBLE : View.VISIBLE);
    }
}
