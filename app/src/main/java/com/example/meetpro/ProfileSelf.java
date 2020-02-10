package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSelf extends Template {
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtMail;
    private TextView txtDesc;
    private TextView txtSector;
    private TextView txtJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addContent(R.layout.activity_profile_self);

        txtName =  findViewById(R.id.name);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtSector = findViewById(R.id.sector);
        txtDesc = findViewById(R.id.description);
        txtJob = findViewById(R.id.proffesion);
        getUserInfo();
    }

    private void getUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                            if(mUser.getUid().equals(snap.getKey().toString())){
                                String name = snap.child("name").getValue().toString();
                                String surname = snap.child("surname").getValue().toString();
                                String phone = snap.child("phone").getValue().toString();
                                String description = snap.child("description").getValue().toString();
                                String email = snap.child("email").getValue().toString();
                                String sector = snap.child("sector").getValue().toString();
                                String job = snap.child("job").getValue().toString();

                                txtName.setText(name + " " + surname);
                                txtPhone.setText(phone);
                                txtMail.setText(email);
                                txtSector.setText(sector);
                                txtDesc.setText(description);
                                txtJob.setText(job);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void onClickEdit(View v){
        Intent answer = new Intent(ProfileSelf.this, Edit.class);
        startActivity(answer);
    }

}
