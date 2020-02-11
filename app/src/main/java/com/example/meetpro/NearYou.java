package com.example.meetpro;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meetpro.model.User;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NearYou extends Template {
    private ListView searchview;
    private User myUser;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_near_you);
        searchview = findViewById(R.id.searchList);
        df2.setRoundingMode(RoundingMode.UP);
        fillMyUser();
    }

    private void listUsers() {
        // Le ponemos de contexto a Firebase la actividad
        Firebase.setAndroidContext(this);
        // Creamos un objeto Firebase al que le pasamos la URL de la base de datos
        Firebase mDatabase = new Firebase("https://crudandroid-77e06.firebaseio.com/");
        // Inicializamos la clase anónima FireBaseListAdapter pasando como parámetros la actividad, la clase
        // del modelo, el layout que tendran los items de la lista y por último la referencia de la
        // base de datos
        FirebaseListAdapter myAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.row_search, mDatabase.child("users")) {

            @Override
            protected void populateView(View view, final User user, int i) {

                // Cogemos las referencias del layout que le hemos puesto para los items en objetos
                // del tipo TextView
                TextView userName = (TextView) view.findViewById(R.id.name);
                TextView userSector = (TextView) view.findViewById(R.id.sector);
                TextView userProfesion = (TextView) view.findViewById(R.id.profesion);
                TextView userDistance = (TextView) view.findViewById(R.id.distance);
                Double latitude = Double.parseDouble(user.getLatitude());
                Double longitude = Double.parseDouble(user.getLongitude());

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
                userSector.setText(user.getSector());
                userProfesion.setText(user.getJob());
                userDistance.setText("a " + df2.format(getDistance(latitude, longitude)) + " km");
            }
        };
        // Asignamos el adapter a la lista
        searchview.setAdapter(myAdapter);
    }

    private double getDistance(double latitude, double longitude) {
        double disKm;
        Geocoder geocoder1 = new Geocoder(NearYou.this);
        Geocoder geocoder2 = new Geocoder(NearYou.this);

        Location location1 = new Location("punto A");
        location1.setLatitude(Double.parseDouble(myUser.getLatitude()));
        location1.setLongitude(Double.parseDouble(myUser.getLongitude()));

        Location location2 = new Location("punto B");
        location2.setLatitude(latitude);
        location2.setLongitude(longitude);
        disKm = location1.distanceTo(location2) / 1000;

        return disKm;
    }

    private void fillMyUser() {
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
                        listUsers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
