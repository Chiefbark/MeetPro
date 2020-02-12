package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText txtMail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtMail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
    }

    /**
     * Logs a user into the app using Firebase Authentication service
     *
     * @param v - View
     */
    public void loginSession(View v) {
        final String email = txtMail.getText().toString();
        final String password = txtPassword.getText().toString();

        // Comprobamos que no estén vacíos
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Introduce un valor en email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Introduce un valor en contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth.
                getInstance().
                signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_LONG).show();
                            Intent n = new Intent(Login.this, NearYou.class);
                            startActivity(n);
                        } else {
                            Toast.makeText(Login.this, "Algo ha ido mal...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
