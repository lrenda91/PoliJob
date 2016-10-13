package it.polito.mad.polijob.StudentActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;

import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.model.StudentStruct;

public class addDetails extends Activity {

    private EditText editTxt;
    private Button btn;
    private Button back;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_add_skills);
        editTxt = (EditText) findViewById(R.id.addSkillSEdit);
        btn = (Button) findViewById(R.id.addSkillSButton);
        list = (ListView) findViewById(R.id.skillS_list);
        arrayList = new ArrayList<String>();

        Student po = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);
        if(po!=null)   arrayList = (ArrayList<String>) po.getSkills();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        final StudentStruct studentStruct = (StudentStruct)getIntent().getSerializableExtra("studentStruct");
        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this line adds the data of your EditText and puts in your array
                arrayList.add(editTxt.getText().toString());
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();
            }
        });
        studentStruct.skills = arrayList;

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(addDetails.this, reg_s_3.class);
                intent.putExtra("studentStruct", studentStruct);
                startActivity(intent);
                finish();
            }
        });


    }


}
