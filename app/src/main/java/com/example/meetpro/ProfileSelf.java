package com.example.meetpro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileSelf extends Template {
    private ImageView profilePic;

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

        profilePic = findViewById(R.id.photo);
        txtName = findViewById(R.id.name);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtSector = findViewById(R.id.sector);
        txtDesc = findViewById(R.id.description);
        txtJob = findViewById(R.id.proffesion);
        getUserInfo();
    }

    /**
     * Gets the user information from the database
     */
    private void getUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                child(mUser.getUid()).
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

                        DownloadImage();
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

    /**
     * Starts edit activity onclick
     *
     * @param v - View
     */
    public void onClickEdit(View v) {
        Intent answer = new Intent(ProfileSelf.this, Edit.class);
        answer.putExtra("hasConfirmation", true);
        startActivity(answer);
    }
    private void DownloadImage(){
        StorageReference storageReference = FirebaseStorage.
                getInstance()
                .getReferenceFromUrl("gs://crudandroid-77e06.appspot.com");
        StorageReference photoReference= storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 10;
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
