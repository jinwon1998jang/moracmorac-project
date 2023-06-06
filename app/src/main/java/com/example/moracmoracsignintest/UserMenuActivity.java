package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMenuActivity extends AppCompatActivity {

    public DatabaseReference mDatabase;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapterUser adapter;

    TextView userCeoname, userPhonenum, userStorename, userHtpay, userCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        userCeoname = findViewById(R.id.userceoname);
        userPhonenum = findViewById(R.id.userphonenum);
        userStorename = findViewById(R.id.userstorename);
        userHtpay = findViewById(R.id.userhtpay);
        userCategory = findViewById(R.id.usercategory);

        showData();

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserMenuActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserMenuActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapterUser(UserMenuActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
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
                    userCeoname.setText(ceoName);
                    userPhonenum.setText(phoneNum);
                    userStorename.setText(storeName);
                    userHtpay.setText(htPay);
                    userCategory.setText(cateGory);
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