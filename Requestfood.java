package com.example.foodwastage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

public class Requestfood extends AppCompatActivity{

    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> detailsCollection;
    String[] places ={"chennai","trichy","vellore","thiruvannamali","arcot","thiruvalam","vilupuram","arani","ambur"};

    Button request;
    RadioButton Veg, NonVeg;
    RadioGroup Group;
    EditText name, no, add, date,  food;
    AutoCompleteTextView actv;
    CheckBox resta, event,ot;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_requestfood);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,places);
        actv =  (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLUE);

        request = findViewById(R.id.requestbutton);
        Veg = findViewById(R.id.vegButton);
        NonVeg = findViewById(R.id.nonButton);
        Group = findViewById(R.id.grp);
        name = findViewById(R.id.name);
        no = findViewById(R.id.num);
        add = findViewById(R.id.addr);
        date = findViewById(R.id.editTextText4);
        food = findViewById(R.id.editTextText6);
        resta = findViewById(R.id.checkBox4);
        event = findViewById(R.id.checkBox5);
        ot = findViewById(R.id.other);
        initialiseMongoDB();

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = "", type = "";
                if (resta.isChecked()) {
                    value = resta.getText().toString();
                }

                if (event.isChecked()) {
                    value = event.getText().toString();
                }
                if (ot.isChecked()) {
                    value = event.getText().toString();
                }
                int Membership = Group.getCheckedRadioButtonId();
                if (Membership == R.id.vegButton) {
                    type = Veg.getText().toString();
                }
                if (Membership == R.id.nonButton) {
                    type = NonVeg.getText().toString();
                }

                String name1 = name.getText().toString();
                String number = no.getText().toString();
                String address = add.getText().toString();
                String dat = date.getText().toString();
                String loc = actv.getText().toString();
                String fod = food.getText().toString();

                if (name1.isEmpty() || number.isEmpty() || address.isEmpty() || dat.isEmpty() || loc.isEmpty() || fod.isEmpty()) {
                    Toast.makeText(Requestfood.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Requestfood.this, Result.class);
                    intent.putExtra("na", name1);
                    intent.putExtra("na1", number);
                    intent.putExtra("na2", address);
                    intent.putExtra("na3", dat);
                    intent.putExtra("na4", loc);
                    intent.putExtra("na5", value);
                    intent.putExtra("na6", type);
                    intent.putExtra("na7", fod);
                    startActivity(intent);
                    Toast.makeText(Requestfood.this, "Saving details...", Toast.LENGTH_SHORT).show();

                    // Create a new Document to insert
                    Document detailsDoc = new Document("name", name1)
                            .append("number", number)
                            .append("address", address)
                            .append("date", dat)
                            .append("location", loc).append("value", value)
                            .append("type", type).append("fod", fod);

                    // Use ExecutorService for background task
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean success = insertDetails(detailsDoc);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Toast.makeText(Requestfood.this, "Details saved successfully", Toast.LENGTH_SHORT).show();
                                        name.setText("");
                                        no.setText("");
                                        add.setText("");
                                        date.setText("");
                                        actv.setText("Location");
                                        food.setText("");
                                        resta.setText("");
                                        event.setText("");
                                        ot.setText("");

                                    } else {
                                        Toast.makeText(Requestfood.this, "Error saving details", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                Intent intent = new Intent(Requestfood.this, BottomNavigationActivity.class);
                intent.putExtra("na", name1);
                intent.putExtra("na1", number);
                intent.putExtra("na2", address);
                intent.putExtra("na3", dat);
                intent.putExtra("na4", loc);
                intent.putExtra("na5", value);
                intent.putExtra("na6", type);
                intent.putExtra("na7", fod);
                startActivity(intent);
            }
        });
    }
    private void initialiseMongoDB() {
        Toast.makeText(Requestfood.this, "MongoDB entered", Toast.LENGTH_LONG).show();
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress("10.0.2.2", 27017))))
                .build());
        mongoDatabase = mongoClient.getDatabase("food_wastage_management");
        detailsCollection = mongoDatabase.getCollection("Request_food");
        Toast.makeText(Requestfood.this, "MongoDB initiated", Toast.LENGTH_SHORT).show();
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
}