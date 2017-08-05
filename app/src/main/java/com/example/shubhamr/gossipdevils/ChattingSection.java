package com.example.shubhamr.gossipdevils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChattingSection extends AppCompatActivity {

    EditText message;
    ImageButton send,upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_section);

        message = (EditText) findViewById(R.id.messagetext);
        send = (ImageButton) findViewById(R.id.send);
        upload = (ImageButton) findViewById(R.id.upload);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}