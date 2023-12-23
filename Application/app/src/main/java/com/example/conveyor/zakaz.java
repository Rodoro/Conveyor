package com.example.conveyor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class zakaz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakaz);
    }
    public void startNewActivityvinti(View vi){
        Intent intentvinti = new Intent(this, vinti.class);
        startActivity(intentvinti);
    }

}