package com.example.meetpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetpro.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Edit extends Template {
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtPhone;
    private EditText txtMail;
    private EditText txtDesc;
    private Spinner sectorSpinner;
    private Spinner profesionSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_edit);

        sectorSpinner = (Spinner) findViewById(R.id.sectorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sectoresArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorSpinner.setAdapter(adapter);


        txtName =  findViewById(R.id.name);
        txtSurname = findViewById(R.id.lastName);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtDesc = findViewById(R.id.description);

        sectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spinner profesiones = (Spinner) findViewById(R.id.profesionSpinner);
                if (position==0){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit.this,
                            R.array.profesionArray00, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    profesiones.setAdapter(adapter);
                }else{
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit.this,
                            R.array.profesionArray01, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    profesiones.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getUserInfo();

    }

    public void setProfesion(View v){

    }

    private void getUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                            if(mUser.getUid().equals(snap.getKey().toString())){
                                String name = snap.child("name").getValue().toString();
                                String surname = snap.child("surname").getValue().toString();
                                String phone = snap.child("phone").getValue().toString();
                                String description = snap.child("description").getValue().toString();
                                String email = snap.child("email").getValue().toString();

                                txtName.setText(name);
                                txtSurname.setText(surname);
                                txtPhone.setText(phone);
                                txtMail.setText(email);
                                txtDesc.setText("" + description);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


        // Llenamos el hashmap del usuario
        Map<String,String> userMap = new HashMap<>();
        userMap.put("name",txtName.getText().toString());
        userMap.put("surname",txtSurname.getText().toString());
        userMap.put("phone",txtPhone.getText().toString());
        userMap.put("email",txtMail.getText().toString());
        userMap.put("latitude","");
        userMap.put("longitude","");
        userMap.put("description",txtDesc.getText().toString());
        userMap.put("job",profesionSpinner.getSelectedItem().toString());
        userMap.put("sector",sectorSpinner.getSelectedItem().toString());


        FirebaseDatabase.getInstance().
                getReference().
                child("users").
                child(mUser.getUid()).
                setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent answer = new Intent(Edit.this, ProfileSelf.class);
                    startActivity(answer);
                }
            }
        });
    }





    public void onConfirm(View v){
        setUserInfo();
    }


}
