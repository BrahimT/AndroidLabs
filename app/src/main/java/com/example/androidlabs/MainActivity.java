package com.example.androidlabs;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;




public class MainActivity extends AppCompatActivity {

    SharedPreferences share;
    String p ,s;
    EditText emailAddress;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3_linear);
        share = this.getSharedPreferences(
                "", Context.MODE_PRIVATE);
       s=share.getString("ReserveName","");

        emailAddress=(EditText) findViewById(R.id.editText2);
        emailAddress.setHint(s);
        btn=(Button) findViewById(R.id.button2);

        btn.setOnClickListener(e->{
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
         goToProfile.putExtra("Email",emailAddress.getText());
            startActivity(goToProfile);
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit =  share .edit();
        p=emailAddress.getText().toString();
        edit.putString("",p);
        edit.commit();
    }
}