package com.example.moracmoracsignintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
//추가


public class CEOMainActivity extends AppCompatActivity {

    //TextView read_name; //선언

    Button fab2;

    ImageButton note;
    ImageButton menu;

    ImageButton gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceomain);



        //인텐드
        /*TextView mv = findViewById(R.id.textView);
        Intent secondIntent = getIntent();
        String information = secondIntent.getStringExtra("info");
        mv.setText(information);*/

        //id 매핑
        //read_name = (TextView)findViewById(R.id.read_name);
        //텍스트 바꾸기
        //read_name.setText(information);

        setContentView(R.layout.activity_ceomain);

        note = findViewById(R.id.note_button);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CEOMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        menu = findViewById(R.id.menu_button);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CEOMainActivity.this, menuActivity.class);
                startActivity(intent);
            }
        });

        gps = findViewById(R.id.gps_button);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CEOMainActivity.this, MapsEditActivity.class);
                startActivity(intent);
            }
        });


        fab2 = findViewById(R.id.fab2);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CEOMainActivity.this, CEOUploadActivity.class);
                startActivity(intent);
            }
        });
    }
}