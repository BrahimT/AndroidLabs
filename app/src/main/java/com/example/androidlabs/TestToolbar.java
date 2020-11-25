package com.example.androidlabs;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
DrawerLayout drawer=findViewById(R.id.drawer_layout);
//ActionBarDrawerToggle toggle=ActionBarDrawerToggle(this,drawer,myToolbar,R.string.hello_blank_fragment,R.string.Navigation_drawer_close);
//drawer.addDrawerListener(toggle);
//toggle.syncState();
//NavigationView navigationView=findViewById(R.id.navigation_view);
   //     navigationView.setNavigationItemSelectedListener(this);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



public boolean onOptionsItemSelected(MenuItem item) {
        String message=null;

        switch (item.getItemId()){
            case R.id.bookmark:
                message="you click on item 1";
                break;
            case R.id.edit:
                message="you click on item 2";
                break;
            case R.id.delete:
                message="you click on item 3";
                break;
            case R.id.Overflow:
                message="you click on the overflow menu ";
                break;
        }
    Toast.makeText(this,message,Toast.LENGTH_LONG).show();
return true;
}

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        String message=null;

        switch (item.getItemId()){
            case R.id.gotochat:
                Intent intent=new Intent(TestToolbar.this,ChatRoomActivity.class) ;
                startActivityForResult(intent,500);
                break;
            case R.id.login:
              //  if statement
              finish();
                break;
            case R.id.weather:
                Intent intent2=new Intent(TestToolbar.this,WeatherForecast.class) ;
                startActivityForResult(intent2,500);
                break;

        }
        DrawerLayout p=findViewById(R.id.drawer_Layout);
        p.closeDrawer(GravityCompat.START);
        Toast.makeText(this,"NavigationDrawer"+message,Toast.LENGTH_LONG).show();
        return false;
    }
}

