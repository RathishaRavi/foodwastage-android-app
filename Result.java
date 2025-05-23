package com.example.foodwastage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.content.DialogInterface;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {

    TextView n, number, address, date, location, source, foodtype, no;
    Button but1;
    AlertDialog.Builder builder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        builder = new AlertDialog.Builder(this);

        but1 = findViewById(R.id.but2);
        n = findViewById(R.id.name);
        number = findViewById(R.id.num);
        address = findViewById(R.id.textView1);
        date = findViewById(R.id.textView2);
        location = findViewById(R.id.textView3);
        source = findViewById(R.id.textView4);
        foodtype = findViewById(R.id.textView5);
        no = findViewById(R.id.textView25);


        String name1 = getIntent().getStringExtra("na");
        String number1 = getIntent().getStringExtra("na1");
        String address1 = getIntent().getStringExtra("na2");
        String date1 = getIntent().getStringExtra("na3");
        String location1 = getIntent().getStringExtra("na4");
        String source1 = getIntent().getStringExtra("na5");
        String foodtype1 = getIntent().getStringExtra("na6");
        String person = getIntent().getStringExtra("na7");
        if (name1 != null && !name1.isEmpty()) {
            n.setText(name1);
        } else {
            n.setText("No name provided");
        }

        if (number1 != null && !number1.isEmpty()) {
            number.setText(number1);
        } else {
            number.setText("No number provided");
        }

        if (address1 != null && !address1.isEmpty()) {
            address.setText(address1);
        } else {
            address.setText("No address provided");
        }

        if (date1 != null && !date1.isEmpty()) {
            date.setText(date1);
        } else {
            date.setText("No date provided");
        }

        if (location1 != null && !location1.isEmpty()) {
            location.setText(location1);
        } else {
            location.setText("No location provided");
        }

        if (source1 != null && !source1.isEmpty()) {
            source.setText(source1);
        } else {
            source.setText("No source provided");
        }

        if (foodtype1 != null && !foodtype1.isEmpty()) {
            foodtype.setText(foodtype1);
        } else {
            foodtype.setText("No food type provided");
        }

        if (person != null && !person.isEmpty()) {
            no.setText(person);
        } else {
            no.setText("No person provided");
        }

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Food Requested!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Result.this, ProfilePage.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}