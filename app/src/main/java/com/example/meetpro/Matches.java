package com.example.meetpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetpro.model.User;
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

import java.util.ArrayList;

public class Matches extends AppCompatActivity {
    private ListView matchList;
    private ArrayList<String> userKey;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        matchList = findViewById(R.id.matchList);
        userKey = new ArrayList<>();
        userList = new ArrayList<>();

        final boolean hasConfirmation = getIntent().getBooleanExtra("hasConfirmation", false);

        findViewById(R.id.match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(Matches.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Matches.this, Matches.class);
                    startActivity(n);
                    finish();
                }
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Matches.this, NearYou.class);
                    startActivity(n);
                    finish();
                }
            }
        });

        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Matches.this, ProfileSelf.class);
                    startActivity(n);
                    finish();
                }
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
                Intent n = new Intent(Matches.this, Main.class);
                startActivity(n);
                finish();
            }
        });

        fillList();
    }

    /**
     * Fills the ArrayList with the uids stored in the match node of the user
     */
    private void fillList() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.
                getInstance().
                getReference().
                child("matches").
                child(mUser.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            userKey.add(data.getKey());
                        }
                        listUsers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Fill the ArrayList of User according to the uids stored in uids ArrayList
     */
    private void listUsers() {
        FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (userKey.contains(data.getKey())) {
                                User usr = new User();
                                usr.setId(data.getKey());
                                usr.setName(data.child("name").getValue().toString());
                                usr.setSurname(data.child("surname").getValue().toString());
                                usr.setJob(data.child("job").getValue().toString());
                                usr.setSector(data.child("sector").getValue().toString());
                                userList.add(usr);
                                Log.d("user", usr.getName());
                            }
                        }
                        matchList.setAdapter(new matchAdapter(Matches.this, R.layout.row_match, userList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Prrivate class, custom ArrayAdapter
     */
    private class matchAdapter extends ArrayAdapter<User> {
        private ArrayList<User> users;

        public matchAdapter(@NonNull Context context, int resource, ArrayList<User> users) {
            super(context, resource, users);
            this.users = users;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            View view = convertview;
            final User user = users.get(position);

            if (view == null) {
                view = LayoutInflater.from(Matches.this).inflate(R.layout.row_match, parent, false);
            }

            // Cogemos las referencias del layout que le hemos puesto para los items en objetos
            // del tipo TextView
            TextView userName = view.findViewById(R.id.name);
            TextView userSector = view.findViewById(R.id.sector);
            TextView userProfesion = view.findViewById(R.id.profesion);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Matches.this, ProfileMatched.class);
                    intent.putExtra("uID", user.getId());
                    startActivity(intent);
                }
            });

            // Asignamos su valor mediante setText
            userName.setText(user.getName() + " " + user.getSurname());
            userSector.setText(" " + user.getSector());
            userProfesion.setText(user.getJob());

            DownloadImage(user, (ImageView) view.findViewById(R.id.profilePic));
            return view;
        }

        private void DownloadImage(User user, final ImageView profilePic) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://crudandroid-77e06.appspot.com");
            StorageReference photoReference = storageReference.child(user.getId() + ".jpg");

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
    private void showConfirmDialog(final Class target) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Descartar cambios")
                .setMessage("Los cambios realizados no se guardarán")
                .setCancelable(false)
                .setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent n = new Intent(Matches.this, target);
                        startActivity(n);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
