package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Template extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);

        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(Template.this, ProfileSelf.class);
                startActivity(n);
            }
        });
    }

    protected void addContent(int idLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(idLayout, null);
        ((LinearLayout) findViewById(R.id.content)).addView(v);
    }
}
