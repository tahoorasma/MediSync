package com.example.medisync;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LabTestActivity extends AppCompatActivity {
    Button btn, btntocart;
    LinearLayout linearLayoutDoctors; // Declare LinearLayout to hold CardViews


    // Correctly define the packages array
    private String[][] packages = {
            {"Package 1: Full Body Checkup", "2800"},
            {"Package 2: Blood Glucose Fasting", "850"},
            {"Package 3: Heart Health Package", "2500"},
            {"Package 4: Thyroid Check", "1400"},
            {"Package 5: Immunity Check", "2000"}
    };
    private String[] package_details = {
            "Includes CBC, Blood Sugar, Liver & Kidney Function Tests, Lipid Profile, and Urine Analysis",
            "Checks fasting blood sugar level to monitor for diabetes",
            "Includes ECG, Cholesterol Test, Blood Pressure Check, and Cardiac Risk Markers",
            "Includes T3, T4, and TSH tests to evaluate thyroid function",
            "Assesses vitamin D, B12 levels, and white blood cell count to evaluate immune system"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test); // Set content view first
        EdgeToEdge.enable(this); // Enable edge-to-edge after setting content view

        // Initialize the button
        btn = findViewById(R.id.buttonLabBack);
        btntocart = findViewById(R.id.buttonLabGoToCart);
        linearLayoutDoctors = findViewById(R.id.linearLayoutDoctors); // Initialize the LinearLayout

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set a click listener for the back button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabTestActivity.this, HomeActivity.class));
                finish();
            }
        });

        btntocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabTestActivity.this, CartLabActivity.class));
                finish();
            }
        });

        // Add CardViews for each package
        addPackageCards();
    }
    private void addPackageCards() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < packages.length; i++) {
            final int index = i;
            View cardView = inflater.inflate(R.layout.card_package, linearLayoutDoctors, false);
            TextView packageName = cardView.findViewById(R.id.package_name);
            TextView packagePrice = cardView.findViewById(R.id.package_price);
            packageName.setText(packages[i][0]); // Set package name
            packagePrice.setText("Price: PKR " + packages[i][1]); // Set package price
            cardView.setBackgroundColor(Color.parseColor("#E0F3FB"));
            // Set an OnClickListener for the card
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LabTestActivity.this, LabTestDetailsActivity.class);
                    intent.putExtra("text1", packages[index][0]); // Package name
                    intent.putExtra("text2", package_details[index]); // Package details
                    intent.putExtra("text3", packages[index][1]); // Package price
                    startActivity(intent);
                }
            });

            // Add the CardView to the LinearLayout
            linearLayoutDoctors.addView(cardView);
        }
    }
}
