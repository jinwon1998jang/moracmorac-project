package com.example.moracmoracsignintest;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteListActivity";

    private ListView favoriteListView;
    private List<String> emailList;
    private ArrayAdapter<String> emailAdapter;

    private DatabaseReference favoritesRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        favoriteListView = findViewById(R.id.favorite_list_view);
        emailList = new ArrayList<>();
        emailAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emailList);
        favoriteListView.setAdapter(emailAdapter);

        favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        loadFavoriteEmails();
    }

    private void loadFavoriteEmails() {
        if (currentUser == null) {
            Log.e(TAG, "User not logged in.");
            return;
        }

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.getKey();

                    if (userEmail != null) {
                        emailList.add(userEmail);
                    }
                }

                emailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading favorite emails", databaseError.toException());
            }
        });
    }
}