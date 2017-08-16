package com.example.shubhamr.gossipdevils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingSection extends AppCompatActivity {

    EditText message;
    ImageButton send,upload;
    MenuItem item;




    String mCurrentUserId;

    String randomid;

    TextView random;
    // Our created menu to use
    private Menu mymenu;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
 private RecyclerView mmessage_list;
    private final List<Messages> messagesList = new ArrayList<>();
 private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_section);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

random = (TextView) findViewById(R.id.randomid);


        checkonline();

      /*  try {
            loadMessages();
        }

        catch (NullPointerException e1){

            Log.d("Chatting","Null pionter exception");
            Toast.makeText(getApplicationContext(),"Null pointer",Toast.LENGTH_LONG).show();
        }*/




        Bundle bundle=getIntent().getExtras();
        randomid = bundle.getString("randomid");
        random.setText(randomid);

        mAuth = FirebaseAuth.getInstance();


        mCurrentUserId = mAuth.getCurrentUser().getUid();
        //random = (TextView) findViewById(R.id.random);
//        random.setText(randomid);



        mAdapter = new MessageAdapter(messagesList);
        mmessage_list = (RecyclerView) findViewById(R.id.message_list);

        mLinearLayout = new LinearLayoutManager(this);
        mmessage_list.setHasFixedSize(true);
        mmessage_list.setLayoutManager(mLinearLayout);



mmessage_list.setAdapter(mAdapter);


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

        loadMessages();


    }

    private void checkonline() {

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mRootRef.child("Online").setValue(true);

    }



    private void loadMessages() {

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("messages").child(mCurrentUserId).child(randomid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mmessage_list.scrollToPosition(messagesList.size()-1);
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendmessage() {

        mRootRef = FirebaseDatabase.getInstance().getReference();

        String realmessage = message.getText().toString();

        String current_user_ref = "messages/" +mCurrentUserId + "/" +randomid;
        String chat_user_ref = "messages/" +randomid + "/" +mCurrentUserId;

        DatabaseReference user_message_push = mRootRef.child("messages")
                .child(mCurrentUserId).child(randomid).push();
        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message",realmessage);
        messageMap.put("from",mCurrentUserId);


        Map messageUserMAP = new HashMap();
        messageUserMAP.put(current_user_ref+"/" +push_id,messageMap);
        messageUserMAP.put(chat_user_ref+"/" +push_id,messageMap);


        mRootRef.updateChildren(messageUserMAP, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Log.d("MainActivity",databaseError.getMessage().toString());
                }

            }
        });


        message.setText("");

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


    @Override
    public void onBackPressed() {
       // mRootRef.child("messages").child(mCurrentUserId).removeValue();
       // mRootRef.child("messages").child(randomid).removeValue();

        super.onBackPressed();
    }
}