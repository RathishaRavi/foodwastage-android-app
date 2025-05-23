package com.example.foodwastage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Newuser extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput;
    private Button signUpButton;
    private TextView signInRedirect;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> detailsCollection;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.btn_signup);
        signInRedirect = findViewById(R.id.btn_signin_redirect);
        initializeMongoDB();

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Validate inputs
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Newuser.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new Document to insert
                    Document detailsDoc = new Document("name", name)
                            .append("email", email)
                            .append("password", password);
                    // Save user details in SharedPreferences
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(Newuser.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    // Redirect to Sign-In activity after successful registration
                    Intent intent = new Intent(Newuser.this, Signin.class);
                    startActivity(intent);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean success = insertDetails(detailsDoc);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Toast.makeText(Newuser.this, "Details saved successfully", Toast.LENGTH_SHORT).show();
                                        nameInput.setText("");
                                        emailInput.setText("");
                                        passwordInput.setText("");

                                    } else {
                                        Toast.makeText(Newuser.this, "Error saving details", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    Toast.makeText(Newuser.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    // Redirect to Sign-In activity after successful registration
                    Intent intent1 = new Intent(Newuser.this, Signin.class);
                    startActivity(intent1);
                }
            }

            private boolean insertDetails(Document detailsDoc) {
                try {
                    detailsCollection.insertOne(detailsDoc);
                    return true;
                } catch (MongoException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        signInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Sign-In Activity
                Intent intent = new Intent(Newuser.this, Signin.class);
                startActivity(intent);
            }
        });
    }
    private void initializeMongoDB() {
        Toast.makeText(Newuser.this, "MongoDB entered", Toast.LENGTH_LONG).show();
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress("10.0.2.2", 27017))))
                .build());
        mongoDatabase = mongoClient.getDatabase("food_wastage_management");
        detailsCollection = mongoDatabase.getCollection("new_user");
        Toast.makeText(Newuser.this, "MongoDB initiated", Toast.LENGTH_SHORT).show();
    }
}

