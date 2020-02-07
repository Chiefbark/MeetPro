package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }

    public void onClickRegister(View v) {
        Intent n = new Intent(Main.this, Register.class);
        startActivity(n);
    }

    public void onClickLogin(View v) {
        Intent n = new Intent(Main.this, Login.class);
        startActivity(n);
    }
}
