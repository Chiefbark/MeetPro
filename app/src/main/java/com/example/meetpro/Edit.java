package com.example.meetpro;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit extends Template {
    private ImageView profilePic;
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtPhone;
    private EditText txtMail;
    private EditText txtDesc;
    private Spinner sectorSpinner;
    private Spinner profesionSpinner;
    private EditText txtLocation;
    private Double userLatitude, userLongitude;
    private ImageButton imgButGeo;
    private FusedLocationProviderClient fusedLocationClient;



    //---ATRIBUTOS PARA LA CARGA DE IMAGENES---//
    private ImageView imageView;
    int PICK_IMAGE_REQUEST=111;
    Uri filePath;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://crudandroid-77e06.appspot.com");
    private static final int GALLERY_INTENT=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_edit);

        profilePic = findViewById(R.id.profilePic);
        sectorSpinner = (Spinner) findViewById(R.id.sectorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sectoresArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorSpinner.setAdapter(adapter);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        txtName = findViewById(R.id.name);
        txtSurname = findViewById(R.id.lastName);
        txtPhone = findViewById(R.id.phone);
        txtMail = findViewById(R.id.email);
        txtDesc = findViewById(R.id.description);

        txtLocation = (EditText) findViewById(R.id.address);
        FloatingActionButton picButton = findViewById(R.id.editPhoto);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargaImagenes();
            }
        });
        imgButGeo = (ImageButton) findViewById(R.id.imageButton);
        imgButGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();

            }
        });
        // We change the value according to the selection
        sectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                profesionSpinner = (Spinner) findViewById(R.id.profesionSpinner);
                if (position == 0) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit.this,
                            R.array.profesionArray00, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    profesionSpinner.setAdapter(adapter);
                } else {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit.this,
                            R.array.profesionArray01, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    profesionSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DownloadImage();
        getUserInfo();

    }

    /**
     * Sets the current latitude and longitude
     */
    private void setLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(Edit.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    ArrayList<Address> addresses = new ArrayList<Address>();
                    Geocoder geocoder1 = new Geocoder(Edit.this);
                    try {
                        addresses = (ArrayList<Address>) geocoder1.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            userLatitude = address.getLatitude();
                            userLongitude = address.getLongitude();
                            // sending back first address line and locality
                            txtLocation.setText(address.getAddressLine(0) + ", " + address.getLocality());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * Sets the address with the longitude and latitude
     * @param latitude - longitude
     * @param longitude -  latitude
     */
    private void setAddress(final double latitude, final double longitude) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(Edit.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    ArrayList<Address> addresses = new ArrayList<Address>();
                    Geocoder geocoder1 = new Geocoder(Edit.this);
                    try {
                        addresses = (ArrayList<Address>) geocoder1.getFromLocation(latitude, longitude, 1);

                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            // sending back first address line and locality
                            txtLocation.setText(address.getAddressLine(0) + ", " + address.getLocality());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * Gets the user information stored in the database
     */
    private void getUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                child(mUser.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String surname = dataSnapshot.child("surname").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String description = dataSnapshot.child("description").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        Double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                        Double longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());

                        setAddress(latitude, longitude);
                        txtName.setText(name);
                        txtSurname.setText(surname);
                        txtPhone.setText(phone);
                        txtMail.setText(email);
                        txtDesc.setText("" + description);
                        userLatitude = latitude;
                        userLongitude = longitude;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Sets the new information gathered from all the editText in the layout
     */
    private void setUserInfo() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


        // Llenamos el hashmap del usuario
        Map<String, String> userMap = new HashMap<>();
        userMap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        userMap.put("name", txtName.getText().toString());
        userMap.put("surname", txtSurname.getText().toString());
        userMap.put("phone", txtPhone.getText().toString());
        userMap.put("email", txtMail.getText().toString());
        userMap.put("latitude", userLatitude.toString());
        userMap.put("longitude", userLongitude.toString());
        userMap.put("description", txtDesc.getText().toString());
        userMap.put("job", profesionSpinner.getSelectedItem().toString());
        userMap.put("sector", sectorSpinner.getSelectedItem().toString());


        FirebaseDatabase.getInstance().
                getReference().
                child("users").
                child(mUser.getUid()).
                setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(filePath!=null) {
                        subirImagen();
                    }else{
                        Intent answer = new Intent(Edit.this, ProfileSelf.class);
                        startActivity(answer);
                    }
                }
            }
        });
    }

    /**
     * OnClick method for confirm button
     * @param v - View
     */
    public void onConfirm(View v) {
        if(userLatitude == 0.00 || userLongitude == 0.00){
            setLocationSetInfo();
        }else {
            setUserInfo();
        }
    }

    /**
     * Sets the location if it is empty and sets the user information into the database after
     */
    private void setLocationSetInfo() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(Edit.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    ArrayList<Address> addresses = new ArrayList<Address>();
                    Geocoder geocoder1 = new Geocoder(Edit.this);
                    try {
                        addresses = (ArrayList<Address>) geocoder1.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            userLatitude = address.getLatitude();
                            userLongitude = address.getLongitude();
                            // sending back first address line and locality
                            txtLocation.setText(address.getAddressLine(0) + ", " + address.getLocality());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        setUserInfo();
                    }
                }

            }
        });
    }
    //setea el imagView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            // Log.d("entra",filePath.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //subir imagenes a firebse
    public void subirImagen(){

        if(filePath!=null){
            StorageReference chilRef=storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
            //actualizando la imagen

            UploadTask uploadTask=chilRef.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Intent answer = new Intent(Edit.this, ProfileSelf.class);
                    startActivity(answer);
                    Toast.makeText(Edit.this,"subida exitosa",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit.this,"problemas al subir"+e,Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(Edit.this,"Seleccionar Imagen",Toast.LENGTH_LONG).show();
        }
        Log.d("ruta de mi imagen ",getFileExtension(filePath).toString());
    }

    //carga imagenes de la galeria
    public void CargaImagenes(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE_REQUEST);
    }
    //obtener la ruta de la imagen
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void DownloadImage(){
        StorageReference storageReference = storage.getReferenceFromUrl("gs://crudandroid-77e06.appspot.com");
        StorageReference photoReference= storageReference.child(
                        FirebaseAuth.
                        getInstance().
                        getCurrentUser().
                        getUid()+".jpg");

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

}
