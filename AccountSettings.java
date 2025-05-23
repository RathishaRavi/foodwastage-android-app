package com.example.foodwastage;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountSettings extends AppCompatActivity {

    private EditText newPasswordInput;
    private Button changePasswordButton, signOutButton, privacyPolicyButton, deleteAccountButton,button;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        newPasswordInput = findViewById(R.id.et_new_password);
        changePasswordButton = findViewById(R.id.btn_change_password);
        signOutButton = findViewById(R.id.btn_signout);
        privacyPolicyButton = findViewById(R.id.btn_privacy_policy);
        deleteAccountButton = findViewById(R.id.btn_delete_account);
        button=findViewById(R.id.button);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordInput.getText().toString();
                if (newPassword.isEmpty()) {
                    Toast.makeText(AccountSettings.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("password", newPassword);
                    editor.apply();
                    Toast.makeText(AccountSettings.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user data and redirect to sign-in activity
                editor.clear();
                editor.apply();
                Intent intent = new Intent(AccountSettings.this, Signin.class);
                startActivity(intent);
                finish(); // Close the AccountSettings activity
            }
        });

        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Privacy Policy activity or webpage

            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user data and notify user
                editor.clear();
                editor.apply();
                Toast.makeText(AccountSettings.this, "Account deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountSettings.this, Signin.class);
                startActivity(intent);
                finish(); // Close the AccountSettings activity
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccountSettings.this,ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}
