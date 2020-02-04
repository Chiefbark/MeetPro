package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.meetpro.R;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void onClickRegister(View v){
        Intent answer = new Intent(Register.this, Register.class);
        startActivity(answer);
    }
}
