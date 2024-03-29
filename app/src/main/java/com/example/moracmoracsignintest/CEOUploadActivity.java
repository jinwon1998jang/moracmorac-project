package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CEOUploadActivity extends AppCompatActivity {

    EditText signupCeoname, signupPhonenum, signupStorename, signupHtpay, signupCategory;
    // TextView loginRedirectText 필요없는 코드
    //Button signupButton => Button storeButton
    Button storeButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceoupload);

        signupCeoname = findViewById(R.id.signup_ceoname);
        signupPhonenum = findViewById(R.id.signup_phonenum);
        signupStorename = findViewById(R.id.signup_storename);
        signupHtpay = findViewById(R.id.signup_htpay);
        signupCategory = findViewById(R.id.signup_category);
        //loginRedirectText = findViewById(R.id.loginRedirectText) 필요없는 코드
        storeButton = findViewById(R.id.store_btn);

        //리얼타임 파이어베이스에 데이터 전송
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재 로그인된 사용자의 ID 가져오기
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userID = user.getEmail();
                    // userId를 사용하여 필요한 작업 수행
                    String id = userID;

                    String ceoname = signupCeoname.getText().toString();
                    String phonenum = signupPhonenum.getText().toString();
                    String storename = signupStorename.getText().toString();
                    String htpay = signupHtpay.getText().toString();
                    String category = signupCategory.getText().toString();

                    // users = store data 데이터베이스 레퍼런스 설정
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("store data");

                    //하위 경로
                    //String childPath = id + "/" + storename;

                    //데이터 객체 생성

                    id = id.replace(".com", "_com");
                    HelperClass helperClass = new HelperClass(ceoname, phonenum, storename, htpay, category, id);
                    //데이터 업로드
                    reference.child(id).setValue(helperClass);


                    Toast.makeText(CEOUploadActivity.this, "저장 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CEOUploadActivity.this, CEOMainActivity.class);
                    startActivity(intent);
                }



            }
        });
    }

}