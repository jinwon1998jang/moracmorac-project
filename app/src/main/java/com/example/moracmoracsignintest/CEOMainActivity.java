package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
//추가


public class CEOMainActivity extends AppCompatActivity {
    public DatabaseReference mDatabase;
    TextView signupCeoname, signupPhonenum, signupStorename, signupHtpay, signupCategory;

    //TextView read_name; //선언

    Button fab2;

    ImageButton note;
    ImageButton menu;

    ImageButton gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceomain);

        signupCeoname = findViewById(R.id.signupceoname);
        signupPhonenum = findViewById(R.id.signupphonenum);
        signupStorename = findViewById(R.id.signupstorename);
        signupHtpay = findViewById(R.id.signuphtpay);
        signupCategory = findViewById(R.id.signupcategory);


        //setContentView(R.layout.activity_ceomain);

        //showUserData();
        showData();
        //인텐드
        /*TextView mv = findViewById(R.id.textView);
        Intent secondIntent = getIntent();
        String information = secondIntent.getStringExtra("info");
        mv.setText(information);*/

        //id 매핑
        //read_name = (TextView)findViewById(R.id.read_name);
        //텍스트 바꾸기
        //read_name.setText(information);

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
                Intent intent = new Intent(CEOMainActivity.this, MapsActivity.class);
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

    //des

    public void showUserData() {
        Intent intent = getIntent();

        String ceoName = intent.getStringExtra("ceoname");
        String phoneNum = intent.getStringExtra("phonenum");
        String storeName = intent.getStringExtra("storename");
        String htPay = intent.getStringExtra("htpay");
        String cateGory = intent.getStringExtra("category");

        // signupCeoname, signupPhonenum, signupStorename, signupHtpay, signupCategory
        signupCeoname.setText(ceoName);
        signupPhonenum.setText(phoneNum);
        signupStorename.setText(storeName);
        signupHtpay.setText(htPay);
        signupCategory.setText(cateGory);


    }

    public void showData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store data");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 데이터를 읽어오는 로직을 작성합니다.
                    String ceoName = snapshot.child("ceoname").getValue(String.class);
                    String phoneNum = snapshot.child("phonenum").getValue(String.class);
                    String storeName = snapshot.child("storename").getValue(String.class);
                    String htPay = snapshot.child("htpay").getValue(String.class);
                    String cateGory = snapshot.child("category").getValue(String.class);

                    // 읽어온 데이터를 활용하여 작업을 수행합니다.
                    signupCeoname.setText(ceoName);
                    signupPhonenum.setText(phoneNum);
                    signupStorename.setText(storeName);
                    signupHtpay.setText(htPay);
                    signupCategory.setText(cateGory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 작업이 취소된 경우의 처리를 수행합니다.
            }
        };

        reference.addValueEventListener(valueEventListener);
    }

}