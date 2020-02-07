package com.example.meetpro.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.meetpro.model.User;
import com.example.meetpro.model.UserList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DAOUser {
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static FirebaseUser mUser;
    /**
     * Constructor of the DAOUser
     */
    private DAOUser() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
    }

    /**
     * Returns an instance of the DAOUser
     *
     * @return An instance of the DAOUser
     */
    public static DAOUser getInstance() {
        return new DAOUser();
    }

    /**
     * Inserts a new User into the database
     *
     * @param user The User to insert
     */
    public static void insert(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getMail(), user.getPassword());
        mAuth.signInWithEmailAndPassword(user.getMail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    update(user);
                }
            }
        });
    }
    public static void SignIn(User user){
        mAuth.signInWithEmailAndPassword(user.getMail(),user.getPassword());
    }
    /**
     * Selects all the Users from the database
     *
     * @return The UserList of Users from the database
     */
    public static UserList select() {
        return null;
    }

    /**
     * Selects the User with the specific id from the database
     *
     * @param uid The id of the User to select
     * @return The User selected
     */
    public static User select(final String uid) {
        final User[] user = new User[1];
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                            if(uid.equals(snap.getKey().toString())){
                                String name = snap.child("name").getValue().toString();
                                String surname = snap.child("surname").getValue().toString();
                                String phone = snap.child("phone").getValue().toString();
                                String description = snap.child("description").getValue().toString();

                                user[0] = new User(name,surname,phone,description);
                                Log.d("usrobj", user.toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return user[0];
    }

    /**
     * Updates an User into the database
     * @param user The User to update
     */
    public static void update(User user) {
        Map<String,String> map = new HashMap<>(); // HashMap para enviar a la bd
        map.put("name",user.getName());
        map.put("surname",user.getSurname());
        map.put("mail",user.getMail());
        map.put("description",user.getDescription());
        map.put("phone",user.getPhone());
        map.put("location","");

        mDatabase.child("users") // Accedemos al nodo que nos interesa
                .child(mAuth.getCurrentUser().getUid()) //Accedemos al nodo usuario
                .setValue(map); // Le mandamos al nodo el mapa
    }

    public static void signOut(){
        mAuth.signOut();
    }

}
