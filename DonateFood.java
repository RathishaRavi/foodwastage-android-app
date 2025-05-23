package com.example.foodwastage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DonateFood extends AppCompatActivity {

    private TextView nameTextView;
    private EditText addressInput, phoneNumberInput, purposeInput, dateInput, timeInput, quantityInput;
    private RadioGroup foodTypeGroup;
    private Spinner supplySourceSpinner;
    private Button donateButton;

    private SharedPreferences sharedPreferences;
    private Calendar calendar;
    // MongoDB variables
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> donationCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_food);

        // Initialize views
        nameTextView = findViewById(R.id.textview_name);
        addressInput = findViewById(R.id.edittext_address);
        phoneNumberInput = findViewById(R.id.edittext_phone_number);
        purposeInput = findViewById(R.id.edittext_purpose);
        dateInput = findViewById(R.id.edittext_date);
        timeInput = findViewById(R.id.edittext_time);
        quantityInput = findViewById(R.id.edittext_quantity);
        foodTypeGroup = findViewById(R.id.radiogroup_food_type);
        supplySourceSpinner = findViewById(R.id.spinner_supply_source);
        donateButton = findViewById(R.id.button_donate);
        calendar = Calendar.getInstance();
        // Setup Spinner for source of supply
        String[] supplySources = {"Select Source", "Restaurant", "Event", "Local Farm", "Supermarket", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, supplySources);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplySourceSpinner.setAdapter(spinnerAdapter);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        // Display the user's name
        nameTextView.setText("Name: " + name);
        // Initialize MongoDB
        initialiseMongoDB();
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DonateFood.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Update the selected date
                            dateInput.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(DonateFood.this,
                        (view, selectedHour, selectedMinute) -> {
                            // Format the time for 24-hour or AM/PM based on requirements
                            String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                            timeInput.setText(formattedTime);
                        }, hour, minute, true); // Set true for 24-hour format, false for 12-hour format
                timePickerDialog.show();
            }
        });
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                String phoneNumber = phoneNumberInput.getText().toString();
                String purpose = purposeInput.getText().toString();
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();
                String quantity = quantityInput.getText().toString();
                String selectedFoodType = ((RadioButton) findViewById(foodTypeGroup.getCheckedRadioButtonId())).getText().toString();
                String selectedSupplySource = supplySourceSpinner.getSelectedItem().toString();

                // Check if all required fields are filled
                if (address.isEmpty() || phoneNumber.isEmpty() || purpose.isEmpty() || date.isEmpty() || time.isEmpty() || quantity.isEmpty() ||
                        foodTypeGroup.getCheckedRadioButtonId() == -1 || selectedSupplySource.equals("Select Source")) {
                    // Show error message using AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DonateFood.this);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage("Please fill all the details");
                    alertDialogBuilder.setPositiveButton("OK", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    // Create a new Document to insert
                    Document donationDoc = new Document("address", address)
                            .append("phoneNumber", phoneNumber)
                            .append("purpose", purpose)
                            .append("date", date)
                            .append("time", time)
                            .append("quantity", quantity)
                            .append("foodType", selectedFoodType)
                            .append("supplySource", selectedSupplySource);

                    // Use ExecutorService for background task
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {
                        boolean success = insertDonation(donationDoc);
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(DonateFood.this, "Donation details saved successfully", Toast.LENGTH_SHORT).show();
                                // Clear inputs after successful donation
                                addressInput.setText("");
                                phoneNumberInput.setText("");
                                purposeInput.setText("");
                                dateInput.setText("");
                                timeInput.setText("");
                                quantityInput.setText("");
                                foodTypeGroup.clearCheck();
                                supplySourceSpinner.setSelection(0);
                            } else {
                                Toast.makeText(DonateFood.this, "Error saving donation details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                    // Confirm donation using AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DonateFood.this);
                    alertDialogBuilder.setTitle("Donation Confirmation");
                    alertDialogBuilder.setMessage("Food is donated. Delivery person will be on time.");
                    alertDialogBuilder.setPositiveButton("OK", (dialog,which)->{
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                Intent intent = new Intent(DonateFood.this, BottomNavigationActivity.class);
                intent.putExtra("address", address);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("purpose", purpose);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("quantity", quantity);
                intent.putExtra("selectedFoodType", selectedFoodType);
                intent.putExtra("selectedSupplySource", selectedSupplySource);
                startActivity(intent);
            }

        });
    }
    private void initialiseMongoDB() {
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress("10.0.2.2", 27017))))
                .build());
        mongoDatabase = mongoClient.getDatabase("food_wastage_management");
        donationCollection = mongoDatabase.getCollection("donations");
    }
    private boolean insertDonation(Document donationDoc) {
        try {
            donationCollection.insertOne(donationDoc);
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }
}



