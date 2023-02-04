package com.example.moracmoracsignintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView username = (TextView) findViewById(R.id.Username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        //adimin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFULL", Toast.LENGTH_SHORT ).show();
                }else
                    //incrrorct
                    Toast.makeText(MainActivity.this, "LOGIN FAILLD !!!", Toast.LENGTH_SHORT ).show();
            }
        });
    }
}