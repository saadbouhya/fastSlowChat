package com.example.slowvf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gameActivityIntent = new Intent(MainActivity.this, Identification.class);
        startActivity(gameActivityIntent);
        //setContentView(R.layout.activity_main);
    }
}