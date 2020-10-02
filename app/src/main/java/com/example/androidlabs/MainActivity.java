package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.app.ProgressDialog.show;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_linear);

Button btn = findViewById(R.id.button3);

btn.setOnClickListener(e -> Toast.makeText(this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show());

Switch s =findViewById(R.id.switch1);
s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton cb, boolean b) {
       Snackbar.make(s,"the switch is on",Snackbar.LENGTH_LONG ).
               setAction("undo",click ->cb.
                       setChecked(!b)).show();

    }
});

//





    }
}