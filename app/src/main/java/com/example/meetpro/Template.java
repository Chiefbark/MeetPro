package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Template extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);
    }

    protected void addContent(int idLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(idLayout, null);
        ((LinearLayout) findViewById(R.id.content)).addView(v);
    }
}
