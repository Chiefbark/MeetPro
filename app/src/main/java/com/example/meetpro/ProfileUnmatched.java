package com.example.meetpro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ProfileUnmatched extends Template implements View.OnClickListener {
    // Private values of the class
    private TextView txtName;
    private TextView txtDesc;
    private TextView txtSector;
    private TextView txtJob;
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

        Button likeButton = findViewById(R.id.match);
        likeButton.setOnClickListener(this);

        getUserInfo();
    }

    /**
     * Gets the information about the user and sets the textviews in the layout
     */
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
                        txtSector.setText("" + sector);
                        txtDesc.setText("" + description);
                        txtJob.setText("" + job);

                        DownloadImage((ImageView) findViewById(R.id.profilePic));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Onclick
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.match:
                isPending();
                break;
        }
    }

    /**
     * Likes a profile, which means it is added to the pending node
     */
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
                    matchMap.put(data.getKey(), true);
                }
                matchMap.put(mUser.getUid(), true);
                addPending(matchMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     * Adds a match into the node matches
     *
     * @param uID      - UserId
     * @param matchMap - Other userID
     */
    private void addMatch(final String uID, HashMap<String, Boolean> matchMap) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.
                getInstance().
                getReference().
                child("matches").
                child(uID).
                setValue(matchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileUnmatched.this,
                            "ITS A MATCH",
                            Toast.LENGTH_LONG).show();
                    if (!mUser.getUid().equals(uID)) {
                        Intent intent = new Intent(ProfileUnmatched.this, ProfileMatched.class);
                        intent.putExtra("uID", uID);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * Checks if the user that have been liked is in the pending list
     * if it is, it makes the match, otherwise it adds the user to the pending list
     */
    private void isPending() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.
                getInstance().
                getReference().
                child("pending").
                child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> matchMap = new HashMap<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals(uID)) {
                        matchProfile(uID, mUser.getUid());
                        matchProfile(mUser.getUid(), uID);
                        checkPending = true;
                    }
                }
                if (!checkPending) {
                    likeProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Gathers all the matches in one hashmap and adds the new match
     *
     * @param uID      - UserID
     * @param otherUID - Second userID
     */
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
                    matchMap.put(data.getKey(), true);
                }
                matchMap.put(otherUID, true);
                addMatch(uID, matchMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    /**
     * Adds a new pending match to the user's node
     *
     * @param matchMap - Map of the user's pending matches
     */
    private void addPending(HashMap<String, Boolean> matchMap) {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("pending").
                child(uID).
                setValue(matchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileUnmatched.this,
                            "Like realizado con éxito",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void DownloadImage(final ImageView profilePic) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://crudandroid-77e06.appspot.com");
        StorageReference photoReference = storageReference.child(uID + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 20;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
}
