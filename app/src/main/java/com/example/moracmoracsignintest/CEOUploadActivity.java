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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CEOUploadActivity extends AppCompatActivity {

    //name,  phone, store, pay, category
    EditText writeEdite;

    TextView readText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceoupload);

        //writeEdite = findViewById(R.id.editTextText_name);

        //readText = findViewById(R.id.read);

        Button readBtn = findViewById(R.id.read_btn);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                //myRef.setValue(writeEdite.getText().toString());

                //읽기
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);


                        //readText.setText("이름: "+ value);

                        //변환
                        String values = value;

                        Intent intent = new Intent(CEOUploadActivity.this, CEOMainActivity.class);

                        //내보내기
                        intent.putExtra("info",values);

                        startActivity(intent);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        //실패시
                        readText.setText("error: "+ error.toException());
                    }
                });

            }
        });
    }
}