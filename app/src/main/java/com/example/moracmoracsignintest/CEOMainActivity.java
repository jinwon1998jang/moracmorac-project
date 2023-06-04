package com.example.moracmoracsignintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class CEOMainActivity extends AppCompatActivity {

    Button fab2;
    ImageButton note;
    ImageButton menuBtn;
    ImageButton gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceomain);

        menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener((v) -> showMenu());

        note = findViewById(R.id.note_button);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CEOMainActivity.this, MainActivity.class);
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

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(CEOMainActivity.this, menuBtn);
        popupMenu.getMenu().add("Menu");
        popupMenu.getMenu().add("Logout");

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Logout")) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(CEOMainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle().equals("Menu")) {
                    startActivity(new Intent(CEOMainActivity.this, menuActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
