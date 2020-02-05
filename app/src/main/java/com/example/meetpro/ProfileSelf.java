package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

public class ProfileSelf extends Template {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addContent(R.layout.activity_profile_self);
    }
}
