package com.example.foodwastage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button signInButton;
    private TextView signUpRedirect;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        emailInput = findViewById(R.id.signin_email);
        passwordInput = findViewById(R.id.signin_password);
        signInButton = findViewById(R.id.btn_signin);
        signUpRedirect = findViewById(R.id.btn_signup_redirect);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Validate email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signin.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user credentials are stored in SharedPreferences
                    String savedEmail = sharedPreferences.getString("email", null);
                    String savedPassword = sharedPreferences.getString("password", null);

                    if (email.equals(savedEmail) && password.equals(savedPassword)) {
                        Toast.makeText(Signin.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        // Proceed to another activity (e.g., dashboard)
                        Intent intent = new Intent(Signin.this, ProfilePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Signin.this, "Invalid credentials or user not found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Sign-Up Activity
                Intent intent = new Intent(Signin.this, Newuser.class);
                startActivity(intent);
            }
        });
    }
}
