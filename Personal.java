package com.example.foodwastage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;

public class Personal extends AppCompatActivity {

    private TextView personalName, personalEmail, liveLocationText;
    private EditText editAge, editLocation;
    private RadioGroup radioGroupGender;
    private Button saveDetailsButton, uploadImageButton, getLocationButton;
    private ImageView profileImageView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int STORAGE_PERMISSION_CODE = 102;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 104;

    private Uri imageUri;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        personalName = findViewById(R.id.personal_name);
        personalEmail = findViewById(R.id.personal_email);
        editAge = findViewById(R.id.edit_age);
        radioGroupGender = findViewById(R.id.radio_group_gender);
        editLocation = findViewById(R.id.edit_location);
        saveDetailsButton = findViewById(R.id.btn_save_details);
        uploadImageButton = findViewById(R.id.btn_upload_image);
        profileImageView = findViewById(R.id.profile_image_view);
        liveLocationText = findViewById(R.id.live_location_text);
        getLocationButton = findViewById(R.id.btn_get_location);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String name = sharedPreferences.getString("name", "User Name");
        String email = sharedPreferences.getString("email", "user@example.com");
        personalName.setText("Name: " + name);
        personalEmail.setText("Email: " + email);



        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve and display the saved details
        String age = sharedPreferences.getString("age", "");
        String gender = sharedPreferences.getString("gender", "");
        String location = sharedPreferences.getString("location", "");
        String profileImagePath = sharedPreferences.getString("profileImage", "");

        editAge.setText(age);
        if (gender.equals("Male")) {
            RadioButton radioMale = findViewById(R.id.radio_male);
            radioMale.setChecked(true);
        } else if (gender.equals("Female")) {
            RadioButton radioFemale = findViewById(R.id.radio_female);
            radioFemale.setChecked(true);
        } else if (gender.equals("Other")) {
            RadioButton radioOther = findViewById(R.id.radio_other);
            radioOther.setChecked(true);
        }
        editLocation.setText(location);

        // Load and display the profile image
        if (!profileImagePath.isEmpty()) {
            profileImageView.setImageURI(Uri.parse(profileImagePath));
        }

        // Handle image upload (camera or gallery)
        uploadImageButton.setOnClickListener(v -> showImagePickerDialog());

        // Handle getting live location
        getLocationButton.setOnClickListener(v -> fetchLiveLocation());

        saveDetailsButton.setOnClickListener(v -> saveDetails());
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Personal.this);
        builder.setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermission();
                    } else {
                        openGallery();
                    }
                });
        builder.create().show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLiveLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLiveLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                liveLocationText.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                                // Optionally use Geocoder to get the city name from coordinates
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                profileImageView.setImageURI(imageUri);
                editor.putString("profileImage", imageUri.toString());
                editor.apply();
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                profileImageView.setImageURI(selectedImageUri);
                editor.putString("profileImage", selectedImageUri.toString());
                editor.apply();
            }
        }
    }

    private void saveDetails() {
        String age = editAge.getText().toString();
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton.getText().toString();
        String location = editLocation.getText().toString();

        // Validate inputs
        if (age.isEmpty() || gender.isEmpty() || location.isEmpty()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Personal.this);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder.setMessage("Please enter all details");
            alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            // Save additional details in SharedPreferences
            editor.putString("age", age);
            editor.putString("gender", gender);
            editor.putString("location", location);
            editor.apply();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Personal.this);
            alertDialogBuilder.setTitle("Success");
            alertDialogBuilder.setMessage("Details saved successfully");
            alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                Intent intent = new Intent(Personal.this, ProfilePage.class);
                startActivity(intent);
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}