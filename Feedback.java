package com.example.foodwastage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity {

    Button sub,home;
    EditText food,su;
    RatingBar rating;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        sub=findViewById(R.id.b2);
        home=findViewById(R.id.back);
        food=findViewById(R.id.suggestion);
        su=findViewById(R.id.suggest);
        rating=findViewById(R.id.ratingBar);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String rat=String.valueOf(rating.getRating());
                Toast.makeText(getApplicationContext(),rat,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Thanks for Rating :)",Toast.LENGTH_LONG).show();

                Intent intent=new Intent(Feedback.this,ProfilePage.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Feedback.this,ProfilePage.class);
                startActivity(intent);
            }
        });

    }
}
