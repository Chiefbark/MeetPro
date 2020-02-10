package com.example.meetpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.meetpro.model.User;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class NearYou extends Template {
    private ListView searchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_near_you);

        searchview = findViewById(R.id.searchList);

        listUsers();
    }

    private void listUsers() {
        // Le ponemos de contexto a Firebase la actividad
        Firebase.setAndroidContext(this);
        // Creamos un objeto Firebase al que le pasamos la URL de la base de datos
        Firebase mDatabase = new Firebase("https://crudandroid-77e06.firebaseio.com/");
        // Inicializamos la clase anónima FireBaseListAdapter pasando como parámetros la actividad, la clase
        // del modelo, el layout que tendran los items de la lista y por último la referencia de la
        // base de datos
        FirebaseListAdapter myAdapter = new FirebaseListAdapter<User>(this, User.class,R.layout.row_search,mDatabase.child("users")) {

            @Override
            protected void populateView(View view, User user, int i) {

                // Cogemos las referencias del layout que le hemos puesto para los items en objetos
                // del tipo TextView
                TextView userName = (TextView)view.findViewById(R.id.name);
                TextView userSector = (TextView)view.findViewById(R.id.sector);

                // Asignamos su valor mediante setText
                userName.setText(user.getName()+" "+user.getSurname());
                userSector.setText(user.getSector());

            }
        };
        // Asignamos el adapter a la lista
        searchview.setAdapter(myAdapter);
    }


}
