package com.example.shubhamr.gossipdevils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChattingSection extends AppCompatActivity {

    EditText message;
    ImageButton send,upload;
    MenuItem item;
    // Our created menu to use
    private Menu mymenu;
    String randomid;
    TextView random;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_section);

        Bundle bundle=getIntent().getExtras();
        randomid = bundle.getString("randomid");

        mAuth = FirebaseAuth.getInstance();
        random = (TextView) findViewById(R.id.random);
        random.setText(randomid);




        message = (EditText) findViewById(R.id.messagetext);
        send = (ImageButton) findViewById(R.id.send);
        upload = (ImageButton) findViewById(R.id.upload);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendmessage();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Do animation start
      /*  LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        item.setActionView(iv);*/

    }

    private void sendmessage() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String realmessage = message.getText().toString();

        String current_user_ref = "messages/" +uid + "/" +randomid;
        String chat_user_ref = "messages/" +randomid + "/" +uid;

        DatabaseReference user_message_push = mDatabase.child("messages")
                .child(uid).child(randomid).push();
        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message",realmessage);


        Map messageUserMAP = new HashMap();
        messageUserMAP.put(current_user_ref+"/" +push_id,messageMap);
        messageUserMAP.put(chat_user_ref+"/" +push_id,messageMap);

        mDatabase.updateChildren(messageUserMAP, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Log.d("mainactivity",databaseError.getMessage().toString());
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add our menu
        getMenuInflater().inflate(R.menu.main, menu);

        // We should save our menu so we can use it to reset our updater.
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new UpdateTask(this).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void resetUpdating() {
        // Get our refresh item from the menu
        MenuItem m = mymenu.findItem(R.id.action_refresh);
        if (m.getActionView() != null) {
            // Remove the animation.
            m.getActionView().clearAnimation();
            m.setActionView(null);
        }
    }
}