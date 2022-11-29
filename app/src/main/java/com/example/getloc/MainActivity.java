package com.example.getloc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView userLocation, city, country, longitude, latitude;
    Button b;
    public static final int REQUEST_CODE=100;
    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocation = findViewById(R.id.UserLocation);
        city = findViewById(R.id.City);
        country = findViewById(R.id.Country);
        longitude = findViewById(R.id.LongitudeTV);
        latitude = findViewById(R.id.Latitude);
        b=findViewById(R.id.button);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},PackageManager.PERMISSION_GRANTED);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });


    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        smsManager=SmsManager.getDefault();

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                            latitude.setText("Latitude :" + addresses.get(0).getLatitude());
                            longitude.setText("Longitude :" + addresses.get(0).getLongitude());
                            city.setText("City :" + addresses.get(0).getLocality());
                            userLocation.setText("Location :" + addresses.get(0).getAddressLine(0));
                            country.setText("Country :" + addresses.get(0).getCountryName());



                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                            smsManager.sendTextMessage("8295904531",null,
                                    "Location is :"+addresses.get(0).getAddressLine(0),null
                                                ,null);
                            smsManager.sendTextMessage("9350247596",null,
                                "Location is :"+addresses.get(0).getAddressLine(0),null
                                ,null);
                    }
                }
            });
        } else {
            askPermissions();

        }
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this,"Required permissions",Toast.LENGTH_SHORT).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}