package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProfileSelf extends Template {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addContent(R.layout.activity_profile_self);
    }

    public void onClickEdit(View v){
        Intent answer = new Intent(ProfileSelf.this, Edit.class);
        startActivity(answer);
    }
}
