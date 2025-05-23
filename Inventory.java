package com.example.foodwastage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Inventory extends Activity {

    private EditText foodNameEditText;
    private EditText quantityEditText;
    private EditText expiryDateEditText;
    private Button addButton;
    private ListView inventoryListView;

    private ArrayList<String> inventoryList;
    private ArrayAdapter<String> inventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize UI components
        foodNameEditText = findViewById(R.id.food_name);
        quantityEditText = findViewById(R.id.quantity);
        expiryDateEditText = findViewById(R.id.expiry_date);
        addButton = findViewById(R.id.add_button);
        inventoryListView = findViewById(R.id.inventory_list);

        // Initialize inventory list and adapter
        inventoryList = new ArrayList<>();
        inventoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,inventoryList);
        inventoryListView.setAdapter(inventoryAdapter);

        // Set button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToInventory();
                Intent intent = new Intent(Inventory.this, ProfilePage.class);
                startActivity(intent);

            }
        });
        // Set list item click listener (optional)
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });
    }

    private void addToInventory() {
        // Get input values
        String foodName = foodNameEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();

        // Validate inputs
        if (foodName.isEmpty() || quantityStr.isEmpty() || expiryDate.isEmpty()) {
            // Optionally show a message to the user
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            // Optionally show a message to the user
            return;
        }

        // Add item to the list
        String inventoryItem = "Food: " + foodName + ", Quantity: " + quantity + ", Expiry: " + expiryDate;
        inventoryList.add(inventoryItem);
        inventoryAdapter.notifyDataSetChanged();

        // Clear input fields
        foodNameEditText.setText("");
        quantityEditText.setText("");
        expiryDateEditText.setText("");

    }
}

