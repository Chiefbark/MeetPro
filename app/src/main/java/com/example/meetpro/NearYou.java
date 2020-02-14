package com.example.meetpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.meetpro.model.User;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NearYou extends AppCompatActivity {
    private ListView searchview;
    private User myUser;
    private Switch myLocation;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private FusedLocationProviderClient fusedLocationClient;
    private Double userLatitude, userLongitude;    private ArrayList<String> userKey;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_you);
        searchview = findViewById(R.id.searchList);
        myUser = new User();
        userKey = new ArrayList<>();
        userList = new ArrayList<>();
        myLocation = findViewById(R.id.currentLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationSetInfo();
            }
        });
        myLocation.setChecked(false);
        final boolean hasConfirmation = getIntent().getBooleanExtra("hasConfirmation", false);

        findViewById(R.id.match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(NearYou.this, Matches.class);
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
                    Intent n = new Intent(NearYou.this, NearYou.class);
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
                    Intent n = new Intent(NearYou.this, ProfileSelf.class);
                    startActivity(n);
                    finish();
                }
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent n = new Intent(NearYou.this, Main.class);
                startActivity(n);
                finish();
            }
        });
        df2.setRoundingMode(RoundingMode.UP);
        setLocationSetInfo();

    }


    /**
     * Sets the location if it is empty and sets the user information into the database after
     */
    private void setLocationSetInfo() {

        fusedLocationClient.getLastLocation().addOnSuccessListener(NearYou.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    ArrayList<Address> addresses;
                    Geocoder geocoder1 = new Geocoder(NearYou.this);
                    try {
                        addresses = (ArrayList<Address>) geocoder1.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            myUser.setLatitude(""+address.getLatitude());
                            myUser.setLongitude(""+address.getLongitude());
                            fillMyUser();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }

            }
        });
    }

    /**
     * Gets the distance between the user's location and the location given
     *
     * @param latitude  - latitude of the given location
     * @param longitude - logintude of the given location
     * @return distance in kilometers
     */
    private double getDistance(double latitude, double longitude) {
        double disKm;

        Location location1 = new Location("punto A");
        location1.setLatitude(Double.parseDouble(myUser.getLatitude()));
        location1.setLongitude(Double.parseDouble(myUser.getLongitude()));

        Location location2 = new Location("punto B");
        location2.setLatitude(latitude);
        location2.setLongitude(longitude);
        disKm = location1.distanceTo(location2) / 1000;

        return disKm;
    }

    /**
     * Gets the user longitude and latitude and stores it into variable
     */
    private void fillMyUser() {
        if(!myLocation.isChecked()) {
            final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().
                    getReference().
                    child("users").
                    child(mUser.getUid()).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myUser = new User();
                            myUser.setLatitude(dataSnapshot.child("latitude").getValue().toString());
                            myUser.setLongitude(dataSnapshot.child("longitude").getValue().toString());
                            fillList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else{
            fillList();
        }
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
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     *
     */
    private void showConfirmDialog(final Class target) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Descartar cambios")
                .setMessage("Los cambios realizados no se guardarán")
                .setCancelable(false)
                .setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent n = new Intent(NearYou.this, target);
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

    /**
     * Fills the ArrayList with the uids stored in the match node of the user
     */
    private void fillList() {
        userKey.clear();
        Log.d("onclick", "fillMyUser: ");

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
        userList.clear();
        FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (!userKey.contains(data.getKey()) && !data.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                User usr = new User();
                                usr.setId(data.getKey());
                                usr.setName(data.child("name").getValue().toString());
                                usr.setSurname(data.child("surname").getValue().toString());
                                usr.setJob(data.child("job").getValue().toString());
                                usr.setSector(data.child("sector").getValue().toString());
                                usr.setLongitude(data.child("longitude").getValue().toString());
                                usr.setLatitude(data.child("latitude").getValue().toString());
                                userList.add(usr);
                                Log.d("user", usr.getName());
                            }
                        }
                        searchview.setAdapter(new NearYou.searchAdapter(NearYou.this, R.layout.row_search, userList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    /**
     * Prrivate class, custom ArrayAdapter
     */
    private class searchAdapter extends ArrayAdapter<User> {
        private ArrayList<User> users;

        public searchAdapter(@NonNull Context context, int resource, ArrayList<User> users) {
            super(context, resource, users);
            this.users = users;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            View view = convertview;
            final User user = users.get(position);
            double longitude = Double.parseDouble(user.getLongitude());
            double latitude = Double.parseDouble(user.getLatitude());

            if (view == null) {
                view = LayoutInflater.from(NearYou.this).inflate(R.layout.row_search, parent, false);
            }

            // Cogemos las referencias del layout que le hemos puesto para los items en objetos
            // del tipo TextView
            TextView userName = view.findViewById(R.id.name);
            TextView userSector = view.findViewById(R.id.sector);
            TextView userProfesion = view.findViewById(R.id.profesion);
            TextView userDistance = view.findViewById(R.id.distance);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NearYou.this, ProfileUnmatched.class);
                    intent.putExtra("uID", user.getId());
                    startActivity(intent);
                }
            });

            // Asignamos su valor mediante setText
            userName.setText(user.getName() + " " + user.getSurname());
            userSector.setText(" " + user.getSector());
            userProfesion.setText(user.getJob());
            userDistance.setText("a " + df2.format(getDistance(latitude, longitude)) + " km");

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

}
