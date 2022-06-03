package com.example.proyecto_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class EstacionarActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    Switch sw_locationupdates, sw_gps;

    Button btn_waypoint, btn_showMap;
    Location currentLocation, savedLocation;

    // location request es un archivo de configuracion de fused
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    // API para sevicios de ubicacion
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionar);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        btn_waypoint = findViewById(R.id.guardarUbicacion);
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
                // Location location = locationResult.getLastLocation();
                currentLocation = locationResult.getLastLocation();
                updateUIValues(currentLocation);
            }
        };


        btn_waypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedLocation = currentLocation;
                Toast.makeText(getApplicationContext(), "Ubicación guardada con éxito.", Toast.LENGTH_SHORT).show();

            }
        });


        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("locLat", savedLocation.getLatitude());
                i.putExtra("locLon", savedLocation.getLongitude());
                startActivity(i);

            }
        });

        updateGPS();
        startLocationUpdates();

    } // fin del metodo onCreate


    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        tv_updates.setText("Actualizando la ubicación.");
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
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }
        else {
            tv_altitude.setText("No disponible");
        }

        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }
        else {
            tv_speed.setText("No disponible");
        }

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