package com.example.meetpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileMatched extends Template {
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtMail;
    private TextView txtDesc;
    private TextView txtSector;
    private TextView txtJob;
    private String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_profile_matched);

        txtName = findViewById(R.id.name);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtSector = findViewById(R.id.sector);
        txtDesc = findViewById(R.id.description);
        txtJob = findViewById(R.id.proffesion);
        uID = getIntent().getStringExtra("uID");
        getUserInfo();

    }

    private void getUserInfo() {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                child(uID).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String surname = dataSnapshot.child("surname").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String description = dataSnapshot.child("description").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String sector = dataSnapshot.child("sector").getValue().toString();
                        String job = dataSnapshot.child("job").getValue().toString();

                        txtName.setText(name + " " + surname);
                        txtPhone.setText(phone);
                        txtMail.setText(email);
                        txtSector.setText(sector);
                        txtDesc.setText(description);
                        txtJob.setText(job);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
