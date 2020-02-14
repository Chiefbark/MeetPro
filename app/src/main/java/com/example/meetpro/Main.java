package com.example.meetpro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {
    private int permisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }

    /**
     * If the user is logged it sends him to the NearYou activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        // PERMISOS
        if ((ContextCompat.checkSelfPermission(Main.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(Main.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(Main.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, permisos);
        }
        // SESSION
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, NearYou.class));
        }
    }

    /**
     * Starts register activity
     *
     * @param v
     */
    public void onClickRegister(View v) {
        Intent n = new Intent(Main.this, Register.class);
        startActivity(n);
    }

    /**
     * Starts login activity
     *
     * @param v
     */
    public void onClickLogin(View v) {
        Intent n = new Intent(Main.this, Login.class);
        startActivity(n);
    }
}
