package com.example.meetpro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    /**
     * Gets the information stored in the realtime database and sets the textview according to it
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
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String description = dataSnapshot.child("description").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String sector = dataSnapshot.child("sector").getValue().toString();
                        String job = dataSnapshot.child("job").getValue().toString();

                        txtName.setText(name + " " + surname);
                        txtPhone.setText("" + phone);
                        txtMail.setText("" + email);
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
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
