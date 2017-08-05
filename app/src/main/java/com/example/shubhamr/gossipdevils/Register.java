package com.example.shubhamr.gossipdevils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {


    String[] gender_array, countries_array;
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2;
   private Button register;
   private EditText gender, country;
   private String genderdata, countrydata;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.register);

        // AutoCompleteTextField for Gender
        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.gender);
        gender_array = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gender_array);
        autoCompleteTextView1.setAdapter(adapter);

        // AutoCompleteTextField for Countries Names
        country = (AutoCompleteTextView) findViewById(R.id.country);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.country);
        countries_array = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries_array);
        autoCompleteTextView1.setAdapter(adapter2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                           Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Not Successfull",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                genderdata = gender.getText().toString();
                countrydata = country.getText().toString();

                register_user(genderdata,countrydata);
            }
        });
    }

    private void register_user(String genderdata, String countrydata) {

/*mAuth.createUserWithEmailAndPassword(genderdata,countrydata).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful())
        {

            Intent intend = new Intent(Register.this,MainActivity.class);
            startActivity(intend);
            finish();
        }

        else {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }

    }
});

*/

    }

}