package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {

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
        FirebaseAuth.getInstance().signOut();

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
