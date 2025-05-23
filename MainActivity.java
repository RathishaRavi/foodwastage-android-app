package com.example.foodwastage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private Button newUserButton, signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initialize buttons
        newUserButton = findViewById(R.id.newUserButton);
        signInButton = findViewById(R.id.signInButton);

        // OnClickListener for "Sign In" button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SecondActivity
                Intent intent = new Intent(MainActivity.this, Signin.class);
                startActivity(intent);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SecondActivity
                Intent intent = new Intent(MainActivity.this, Newuser.class);
                startActivity(intent);
            }
        });


    }
}
