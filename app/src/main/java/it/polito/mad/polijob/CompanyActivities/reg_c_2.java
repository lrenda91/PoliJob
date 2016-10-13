package it.polito.mad.polijob.CompanyActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.StudentActivities.reg_s_3;
import it.polito.mad.polijob.model.CompanyStruct;
import it.polito.mad.polijob.model.StudentStruct;


public class reg_c_2 extends Activity {
    CompanyStruct companyStruct;

    EditText contactNameEdit;
    EditText phoneEdit;
    EditText faxEdit;
    EditText webEdit;
    EditText workersEdit;
    EditText address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_company_page2);

        companyStruct = (CompanyStruct) getIntent().getSerializableExtra("companyStruct");

        Button button = (Button) findViewById(R.id.register2_company_skip_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCompany();
                Intent intent = new Intent(reg_c_2.this, reg_c_3.class);
                intent.putExtra("companyStruct", companyStruct);
                startActivity(intent);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.register2_company_update_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCompany();
                Intent intent = new Intent(reg_c_2.this, reg_c_3.class);
                intent.putExtra("companyStruct", companyStruct);
                startActivity(intent);
                finish();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView register = (TextView) findViewById(R.id.register);
        register.setTypeface(custom_font);
    }

    private void updateCompany(){
        contactNameEdit= (EditText) findViewById(R.id.contactname_editText);
        phoneEdit= (EditText) findViewById(R.id.phoneN_company_editText);
        faxEdit= (EditText) findViewById(R.id.faxnumber_editText);
        webEdit= (EditText) findViewById(R.id.webpage_editText);
        workersEdit= (EditText) findViewById(R.id.workersn_editText);
        address= (EditText) findViewById(R.id.address_company_editText);

        companyStruct.contactNametxt=(contactNameEdit.getText().toString());
        companyStruct.phonetxt=(phoneEdit.getText().toString());
        companyStruct.faxtxt=(faxEdit.getText().toString());
        companyStruct.webPagetxt=(webEdit.getText().toString());
        companyStruct.addresstxt=(address.getText().toString());
        if(!workersEdit.getText().toString().equals("")) companyStruct.numberofWorkers=Integer.parseInt(workersEdit.getText().toString());
    }

}