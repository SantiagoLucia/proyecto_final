package com.example.proyecto_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class EstacionarActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int PERMISSION_CAMERA = 100;
    private static final int CAPTURE_CODE = 101;
    TextView tv_address;

    ImageButton img_estacionam;
    Uri image_uri;

    Button btn_waypoint, btn_waypoint_photo, btn_showMap;
    Location currentLocation;
    double saved_lat, saved_lon;

    // location request es un archivo de configuracion de fused
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    // API para sevicios de ubicacion
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionar);

        SharedPreferences prefs = getSharedPreferences("ubicacion_auto", Context.MODE_PRIVATE);
        saved_lat = Double.parseDouble(prefs.getString("latitud", "0"));
        saved_lon = Double.parseDouble(prefs.getString("longitud", "0"));
        image_uri = Uri.parse(prefs.getString("foto",""));

        tv_address = findViewById(R.id.tv_address);

        img_estacionam = findViewById(R.id.iv_estacionam);
        img_estacionam.setImageURI(image_uri);

        btn_waypoint = findViewById(R.id.guardarUbicacion);
        btn_waypoint_photo = findViewById(R.id.guardarUbicacion2);
        btn_showMap = findViewById(R.id.btn_showMap);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // guardar la ubicacion
                currentLocation = locationResult.getLastLocation();
                updateUIValues(currentLocation);
            }
        };

        img_estacionam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), ExpandirImagenActivity.class);
                    startActivity(i);
            }
        });

        btn_waypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved_lat = currentLocation.getLatitude();
                saved_lon = currentLocation.getLongitude();
                SharedPreferences prefs = getSharedPreferences("ubicacion_auto", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("latitud", String.valueOf(saved_lat));
                editor.putString("longitud", String.valueOf(saved_lon));
                editor.commit();
                img_estacionam.setImageResource(R.drawable.ic_baseline_image_24);
                Toast.makeText(getApplicationContext(), "Ubicación guardada con éxito.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_waypoint_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CAMERA);

                    } else {
                        openCamera();

                        saved_lat = currentLocation.getLatitude();
                        saved_lon = currentLocation.getLongitude();
                        SharedPreferences prefs = getSharedPreferences("ubicacion_auto", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("latitud", String.valueOf(saved_lat));
                        editor.putString("longitud", String.valueOf(saved_lon));
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Ubicación guardada con éxito.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saved_lat != 0) {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    i.putExtra("locLat", saved_lat);
                    i.putExtra("locLon", saved_lon);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Debe guardar la ubicación.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateGPS();
        startLocationUpdates();

    } // fin del metodo onCreate

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"nuevo estacionamiento");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Nueva ubicación de estacionamiento");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent camintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camintent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camintent,CAPTURE_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            img_estacionam.setImageURI(image_uri);
            SharedPreferences prefs = getSharedPreferences("ubicacion_auto", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("foto", image_uri.toString());
            editor.commit();
            Toast.makeText(getApplicationContext(), image_uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   updateGPS();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Esta app requiere permisos para ejecutarse correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            break;

            case PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Esta app requiere permisos para ejecutarse correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

    }

    private void updateGPS() {
        // obtener permisos del usuario para trackear ubicacion
        // obtener la ubicacion actual desde el cliente fused
        // actualizar la interfaz de usuario

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                getApplicationContext()
        );

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // el usuario acepto usar el gps
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // pongo los valores de location en los componetes de la UI
                    updateUIValues(location);
                }
            });
        }
        else {
            // no garantizo los permisos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {
        // actualiza todos los text views

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch ( Exception e ) {
            tv_address.setText("No se puede obtener la dirección");

        }

    }


}