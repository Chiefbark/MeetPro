package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileUnmatched extends Template implements View.OnClickListener{
    private TextView txtName;
    private TextView txtDesc;
    private TextView txtSector;
    private TextView txtJob;
    private Button likeButton;
    private String uID;
    private boolean checkPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_profile_unmatched);

        uID = getIntent().getStringExtra("uID");
        txtName = findViewById(R.id.name);
        txtSector = findViewById(R.id.sector);
        txtDesc = findViewById(R.id.description);
        txtJob = findViewById(R.id.proffesion);

        likeButton = findViewById(R.id.match);
        likeButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.match:
                isPending();
                break;
        }
    }

    private void likeProfile() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

            FirebaseDatabase.
                    getInstance().
                    getReference().
                    child("pending").
                    child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Boolean> matchMap = new HashMap<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        matchMap.put(data.getKey().toString(), true);
                    }
                    matchMap.put(mUser.getUid(), true);
                    addPending(matchMap);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

    private void addMatch(final String uID, HashMap<String,Boolean>matchMap) {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("matches").
                child(uID).
                setValue(matchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileUnmatched.this,
                            "ITS A MATCH",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfileUnmatched.this,ProfileMatched.class);
                    intent.putExtra("uID",uID);
                    startActivity(intent);
                }
            }
        });
    }

    private void isPending() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.
                getInstance().
                getReference().
                child("pending").
                child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Boolean> matchMap = new HashMap<>();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals(uID)){
                        matchProfile(uID,mUser.getUid());
                        matchProfile(mUser.getUid(),uID);
                        checkPending = true;
                    }
                }
                if(!checkPending) {
                    likeProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void matchProfile(final String uID, final String otherUID) {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("matches").
                child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> matchMap = new HashMap<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    matchMap.put(data.getKey().toString(), true);
                }
                matchMap.put(otherUID, true);
                addMatch(uID,matchMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });

    }

    private void addPending(HashMap<String, Boolean> matchMap) {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("pending").
                child(uID).
                setValue(matchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileUnmatched.this,
                            "Like realiazdo con éxito",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
