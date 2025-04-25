package com.example.medisync;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LabTestBookActivity extends AppCompatActivity {
    EditText edname, edaddress, edcontact, edcity;
    Button btnBooking, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);
        edname = findViewById(R.id.editTextLTBFullname);
        edaddress = findViewById(R.id.editTextLTBAddress);
        edcontact = findViewById(R.id.editTextLTBContact);
        edcity = findViewById(R.id.editTextLTBPincode);
        btnBooking = findViewById(R.id.buttonLTBBooking);

        Intent intent=getIntent();
        String[] price = intent.getStringExtra("price").toString().split(java.util.regex.Pattern.quote(": PKR"));
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        btnBack = findViewById(R.id.buttonLabDetailBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabTestBookActivity.this, LabTestActivity.class));
                finish(); // Optionally finish this activity if you don't want to go back to it
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString().trim();
                String address = edaddress.getText().toString().trim();
                String contact = edcontact.getText().toString().trim();
                String city = edcity.getText().toString().trim();
                String selectedDate = date.toString();
                String selectedTime = time.toString();

                if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || city.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contact.length() < 11) {
                    Toast.makeText(getApplicationContext(), "Contact number should be 11 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedpreferences.getString("username", "");

                Database db = new Database(getApplicationContext());
                db.addOrder(username, name, address, contact, city, selectedDate, selectedTime,
                        Float.parseFloat(price[1].toString()), "lab");

                db.removeCart(username, "lab");
                Toast.makeText(getApplicationContext(), "Your order has been placed successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
            }
        });


    }
}
