package com.example.foodwastage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Recipe extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private TextView recipeTextView;
    private Button backButton,voiceSearchButton;
    private String[] foods = {"White Pasta", "Spaghetti Bolognese", "Chicken Curry", "Rice", "Biryani"};
    private String[] recipes = {
            "White Pasta:\n Boil pasta.\nCook garlic and cream. \nMix together.",
            "Spaghetti Bolognese: \nCook spaghetti. \nPrepare beef sauce.\n Combine.",
            "Chicken Curry: \nCook chicken.\n Add curry spices.\n Simmer with vegetables.",
            "Rice:\n Cook by your own.",
            "Biryani:\n Just get from Zomato or Swiggy."
    };

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        recipeTextView = findViewById(R.id.recipeTextView);
        backButton = findViewById(R.id.backButton);
        voiceSearchButton = findViewById(R.id.voicebtn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, foods);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFood = (String) parent.getItemAtPosition(position);
                displayRecipe(selectedFood);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTextView.setText(""); // Clear the recipe details
                autoCompleteTextView.setText("");
                Intent intent = new Intent(Recipe.this, ProfilePage.class);
                intent.putExtra("key", "value"); // Replace "key" and "value" with your actual data
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(Recipe.this, "ProfilePage activity not found", Toast.LENGTH_SHORT).show();
                }
               // Clear the input
            }
        });
        voiceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }
    // Method to start voice input
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the name of the recipe...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    String voiceInput = result.get(0);
                    Toast.makeText(this, "You said: " + voiceInput, Toast.LENGTH_SHORT).show();
                    displayRecipe(voiceInput);
                } else {
                    Toast.makeText(this, "Could not understand your input. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Voice recognition was cancelled or failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Method to display the recipe based on the food name
    private void displayRecipe(String food) {
        String recipe = "Recipe not found.";
        for (int i = 0; i < foods.length; i++) {
            if (foods[i].equalsIgnoreCase(food)) {
                recipe = recipes[i];
                break;
            }
        }
        recipeTextView.setText(recipe);
    }
}