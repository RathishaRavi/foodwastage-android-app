package com.example.foodwastage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Button btn;
    private String name, number, address, date, location, value, type, food;
    private String addressDonate, phoneNumberDonate;
    private String purposeDonate, dateDonate, timeDonate, quantityDonate, selectedFoodTypeDonate, selectedSupplySourceDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage("saved your request!");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        btn=findViewById(R.id.back);

        Intent intent = getIntent();
        name = intent.getStringExtra("na");
        number = intent.getStringExtra("na1");
        address = intent.getStringExtra("na2");
        date = intent.getStringExtra("na3");
        location = intent.getStringExtra("na4");
        value = intent.getStringExtra("na5");
        type = intent.getStringExtra("na6");
        food = intent.getStringExtra("na7");

        addressDonate = intent.getStringExtra("address");
        phoneNumberDonate = intent.getStringExtra("phoneNumber");
        purposeDonate = intent.getStringExtra("purpose");
        dateDonate = intent.getStringExtra("date");
        timeDonate = intent.getStringExtra("time");
        quantityDonate = intent.getStringExtra("quantity");
        selectedFoodTypeDonate = intent.getStringExtra("selectedFoodType");
        selectedSupplySourceDonate = intent.getStringExtra("selectedSupplySource");

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.dashboard) {
                fragment = new DashboardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("number", number);
                bundle.putString("address", address);
                bundle.putString("date", date);
                bundle.putString("location", location);
                bundle.putString("value", value);
                bundle.putString("type", type);
                bundle.putString("food", food);
                fragment.setArguments(bundle);
            } else if (item.getItemId() == R.id.daily_food_waste) {
                fragment = new FoodWasteHistoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("addressDonate", addressDonate);
                bundle.putString("phoneNumberDonate", phoneNumberDonate);
                bundle.putString("purposeDonate", purposeDonate);
                bundle.putString("dateDonate", dateDonate);
                bundle.putString("timeDonate", timeDonate);
                bundle.putString("quantityDonate", quantityDonate);
                bundle.putString("selectedFoodTypeDonate", selectedFoodTypeDonate);
                bundle.putString("selectedSupplySourceDonate", selectedSupplySourceDonate);
                fragment.setArguments(bundle);
            } else if (item.getItemId() == R.id.food_waste_history) {
                fragment = new FoodWasteHistoryFragment();
            } else if (item.getItemId() == R.id.goal_setting) {
                fragment = new GoalSettingFragment();
            }

            if (fragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }

            return true;
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BottomNavigationActivity.this,ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}