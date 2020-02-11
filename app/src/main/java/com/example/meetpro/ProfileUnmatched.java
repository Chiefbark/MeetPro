package com.example.meetpro;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUnmatched extends Template {
    private TextView txtName;
    private TextView txtDesc;
    private TextView txtSector;
    private TextView txtJob;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_profile_unmatched);

        uID = getIntent().getStringExtra("uID");
        txtName = findViewById(R.id.name);
        txtSector = findViewById(R.id.sector);
        txtDesc = findViewById(R.id.description);
        txtJob = findViewById(R.id.proffesion);
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
                        String description = dataSnapshot.child("description").getValue().toString();
                        String sector = dataSnapshot.child("sector").getValue().toString();
                        String job = dataSnapshot.child("job").getValue().toString();

                        txtName.setText(name + " " + surname);
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
