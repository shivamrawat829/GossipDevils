package com.example.shubhamr.gossipdevils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button chat;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;
     public DataSnapshot f;
    private String TAG="MainActivity";

    @Override
    protected void onStart() {
        FirebaseUser currentuser =mAuth.getCurrentUser();

        if (currentuser == null)
        {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        chat = (Button) findViewById(R.id.chat);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int questionCount = (int) dataSnapshot.getChildrenCount();

                        Log.d(TAG, "onDataChange: "+questionCount);
                        Toast.makeText(getApplicationContext(),"valus is "+questionCount,Toast.LENGTH_LONG).show();
                      //  int rand = random.nextInt(questionCount);
                        Iterator itr = dataSnapshot.getChildren().iterator();

                       Random no = new Random();
                        int rndNum = no.nextInt(questionCount-1) + 1;

                        for(int i = 0; i < rndNum; i++) {
                            itr.next();
                        }
                        DataSnapshot childSnapshot = (DataSnapshot) itr.next();
                      //  Toast.makeText(getApplicationContext(),"value of childsnapshot "+childSnapshot.getKey(),Toast.LENGTH_LONG).show();
                       // String question = childSnapshot.getValue("Users");

                        Intent intent = new Intent(MainActivity.this,ChattingSection.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("randomid",childSnapshot.getKey());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }






                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}