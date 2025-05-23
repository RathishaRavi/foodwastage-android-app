package com.example.foodwastage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryVolunteer extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private TextView locationTextView;
    private Button requestVolunteerButton;
    private ListView volunteerListView;
    private List<Volunteer> volunteers = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_volunteer);

        nameEditText = findViewById(R.id.name);
        phoneNumberEditText = findViewById(R.id.phone_number);
        locationTextView = findViewById(R.id.location);
        requestVolunteerButton = findViewById(R.id.request_volunteer);
        volunteerListView = findViewById(R.id.volunteer_list);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestVolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String location = locationTextView.getText().toString();

                if (ContextCompat.checkSelfPermission(DeliveryVolunteer.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeliveryVolunteer.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    fetchLocation();
                }
                // Validate inputs
                if (name.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
                    // Create an AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryVolunteer.this);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage("Please enter all details");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Volunteer volunteer = new Volunteer(name, phoneNumber, location);
                    volunteers.add(volunteer);
                    VolunteerAdapter adapter = new VolunteerAdapter(DeliveryVolunteer.this, volunteers);
                    volunteerListView.setAdapter(adapter);

                    // Display the details
                    String details = "Name: " + name + "\nPhone Number: " + phoneNumber + "\nLocation: " + location;
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DeliveryVolunteer.this);
                    alertDialogBuilder.setTitle("Volunteer Details");
                    alertDialogBuilder.setMessage(details);
                    alertDialogBuilder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(DeliveryVolunteer.this, Feedback.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String locationText = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                            locationTextView.setText(locationText);
                        } else {
                            Toast.makeText(DeliveryVolunteer.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Volunteer {
        private String name;
        private String phoneNumber;
        private String location;

        public Volunteer(String name, String phoneNumber, String location) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLocation() {
            return location;
        }
    }

    class VolunteerAdapter extends ArrayAdapter<Volunteer> {
        private Context context;
        private List<Volunteer> volunteers;

        public VolunteerAdapter(Context context, List<Volunteer> volunteers) {
            super(context, 0, volunteers);
            this.context = context;
            this.volunteers = volunteers;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.activity_delivery_volunteer, parent, false);
            }

            Volunteer volunteer = getItem(position);
            TextView nameTextView = view.findViewById(R.id.name);
            TextView phoneNumberTextView = view.findViewById(R.id.phone_number);
            TextView locationTextView = view.findViewById(R.id.location);

            nameTextView.setText(volunteer.getName());
            phoneNumberTextView.setText(volunteer.getPhoneNumber());
            locationTextView.setText(volunteer.getLocation());

            return view;
        }
    }
}