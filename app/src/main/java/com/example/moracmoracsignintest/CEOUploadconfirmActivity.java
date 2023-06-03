package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CEOUploadconfirmActivity extends AppCompatActivity {

    EditText confirmCEOname, confirmPhonenum;

    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceouploadconfirm);

        //checkUsertest();
        confirmCEOname = findViewById(R.id.confirm_ceoname);
        confirmPhonenum = findViewById(R.id.confirm_phonenum);
        confirmButton = findViewById(R.id.conf_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateCEOname() | !validatePhonenum()) {

                } else {
                    //login부분 추가
                    checkUser();
                }
            }
        });


    }

    public Boolean validateCEOname() {
        String val = confirmCEOname.getText().toString();
        if (val.isEmpty()) {
            confirmCEOname.setError("가게 이름을 채워주세요");
            return false;
        }else {
            confirmCEOname.setError(null);
            return true;
        }
    }

    public Boolean validatePhonenum() {
        String val = confirmPhonenum.getText().toString();
        if (val.isEmpty()) {
            confirmPhonenum.setError("전화번호를 채워주세요");
            return false;
        }else {
            confirmPhonenum.setError(null);
            return true;
        }
    }

    //login부분 추가
    public void checkUser() {
        String ceoCEOname = confirmCEOname.getText().toString().trim();
        String ceoPhonenum = confirmPhonenum.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store data");
        Query checkUserDatabase = reference.orderByChild("storename").equalTo(ceoCEOname);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    confirmCEOname.setError(null);
                    String phonenumFromDB = snapshot.child(ceoCEOname).child("phonenum").getValue(String.class);

                    if (phonenumFromDB.equals(ceoPhonenum)) {
                        confirmCEOname.setError(null);

                        String storenameFromeDB = snapshot.child(ceoCEOname).child("storename").getValue(String.class);
                        String ceonameFromDB = snapshot.child(ceoCEOname).child("ceoname").getValue(String.class);
                        String htpayFromeDB = snapshot.child(ceoCEOname).child("htpay").getValue(String.class);
                        String categoryFromeDB = snapshot.child(ceoCEOname).child("category").getValue(String.class);

                        Intent intent = new Intent(CEOUploadconfirmActivity.this, CEOMainActivity.class);

                        intent.putExtra("ceoname", ceonameFromDB);
                        intent.putExtra("phonenum", phonenumFromDB);
                        intent.putExtra("storename", storenameFromeDB);
                        intent.putExtra("htpay", htpayFromeDB);
                        intent.putExtra("category", categoryFromeDB);

                        startActivity(intent);
                    }else {
                        confirmPhonenum.setError("전화번호가 유효하지 않습니다");
                        confirmPhonenum.requestFocus();
                    }
                } else {
                    confirmCEOname.setError("이름을 정확히 입력하지 않았습니다.");
                    confirmCEOname.requestFocus();
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store data");
            }
        });
    }

    public void checkUsertest() {

    }
}