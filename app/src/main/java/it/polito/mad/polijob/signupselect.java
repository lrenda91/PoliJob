package it.polito.mad.polijob;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import it.polito.mad.polijob.CompanyActivities.reg_c_1;
import it.polito.mad.polijob.StudentActivities.reg_s_1;


public class signupselect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup_userselect_page);

        Button button = (Button) findViewById(R.id.company_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(signupselect.this, reg_c_1.class));
                finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.student_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(signupselect.this, reg_s_1.class));
                finish();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/facebook-letter-faces.ttf");
        TextView user = (TextView) findViewById(R.id.userselector);
        user.setTypeface(custom_font);
    }


}
