package com.example.meetpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtPhone;
    private EditText txtMail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Le atribuimos los componentes de la vista a las variables
        txtName =  findViewById(R.id.nombre);
        txtSurname = findViewById(R.id.apellido);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);


    }

    public void onClickRegister(View v){
        realizarRegistro();
    }

    /**
     * Makes the register with Firebase authentication service, it also creates an empty node into
     * the realtime database
     */
    private void realizarRegistro() {
        // Recogemos todos los datos del formulario
        final String name = txtName.getText().toString();
        final String surname = txtSurname.getText().toString();
        final String phone = txtPhone.getText().toString();
        final String email = txtMail.getText().toString();
        final String password = txtPassword.getText().toString();

        // Comprobamos que los campos no estén vacíos
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Introduce un valor en email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Introduce un valor en contraseña",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Introduce un valor en nombre",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(surname)){
            Toast.makeText(this,"Introduce un valor en apellido",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Introduce un valor en teléfono",Toast.LENGTH_LONG).show();
            return;
        }

        // Realizamos el registro en la base de datos
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Si ha sido exitoso el registro
                        if(task.isSuccessful()){
                            // Logueamos al usuario, si se completa nueva entrada en la base de datos con sus datos
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).
                                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // Obtenemos el usuario activo en firebase
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                                            // Llenamos el hashmap del usuario
                                            Map<String,String> userMap = new HashMap<>();
                                            userMap.put("name",name);
                                            userMap.put("surname",surname);
                                            userMap.put("phone",phone);
                                            userMap.put("email",email);
                                            userMap.put("latitude","0.00");
                                            userMap.put("longitude","0.00");
                                            userMap.put("description","");
                                            userMap.put("job","");
                                            userMap.put("sector","");
                                            userMap.put("profilepic","");
                                            // Mandamos el userMap a la base de datos al nodo usuarios, al nodo userid
                                            // Dejamos constancia de los campos vacíos
                                            FirebaseDatabase.getInstance().
                                                    getReference().
                                                    child("users").
                                                    child(mUser.getUid()).
                                                    setValue(userMap);
                                            // Cargamos un intent y lanzamos la nueva activity
                                            Intent answer = new Intent(Register.this, Edit.class);
                                            startActivity(answer);
                                        }
                                    });
                        }else {
                            Toast.makeText(Register.this,
                                    "Ha ocurrido un error en el registro",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
