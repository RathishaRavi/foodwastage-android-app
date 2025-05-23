package com.example.foodwastage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    private TextView profileName;
    private Button personalDetailsButton, accountSettingsButton, donateFoodButton;
    private Button deliveringVolunteerButton,MenuButton, feedbackButton;
    private Button InventoryButton, RequestfoodButton, RecipeButton;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileName = findViewById(R.id.profile_name);
        personalDetailsButton = findViewById(R.id.btn_personal_details);
        accountSettingsButton = findViewById(R.id.btn_account_settings);
        donateFoodButton = findViewById(R.id.btn_donate_food);
        deliveringVolunteerButton = findViewById(R.id.btn_delivering_volunteer);
        feedbackButton = findViewById(R.id.btn_feedback);

        InventoryButton=findViewById(R.id.btn_inventory);
        RecipeButton=findViewById(R.id.btn_recipe);
        RequestfoodButton=findViewById(R.id.btn_requestfood);
        MenuButton=findViewById(R.id.btn_menu);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        // Retrieve the user's name from SharedPreferences
        String name = sharedPreferences.getString("name", "User");
        profileName.setText(name);
        // Set onClick listeners for each button

        personalDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,Personal.class);
                startActivity(intent);
                // Handle Personal Details button click
            }
        });

        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,AccountSettings.class);
                startActivity(intent);
                // Handle Account Settings button click
            }
        });

        RequestfoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,Requestfood.class);
                startActivity(intent);
                // Handle Personal Details button click
            }
        });

        RecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,Recipe.class);
                startActivity(intent);
                // Handle Personal Details button click
            }
        });
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,BottomNavigationActivity.class);
                startActivity(intent);
                // Handle Personal Details button click
            }
        });

        InventoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,Inventory.class);
                startActivity(intent);
            }
        });

        donateFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,DonateFood.class);
                startActivity(intent);
                // Handle Donate Food button click
            }
        });

        deliveringVolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,DeliveryVolunteer.class);
                startActivity(intent);
                // Handle Delivering Volunteer button click
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilePage.this,Feedback.class);
                startActivity(intent);
                // Handle Analytics button click
            }
        });
    }
}
