package com.example.medisync;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class CartLabActivity extends AppCompatActivity {

    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    CartAdapter adapter;
    TextView tvTotal;
    ListView lst;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnCheckout, btnBack;
    private String[][] packages = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab);

        dateButton = findViewById(R.id.buttonCartDate);
        timeButton = findViewById(R.id.buttonCartTime);
        btnCheckout = findViewById(R.id.buttonCartCheckout);
        btnBack = findViewById(R.id.buttonCartBack);
        tvTotal = findViewById(R.id.textViewCartTotalCost);
        lst = findViewById(R.id.listViewCartLabTextMultiline);
        lst.setBackgroundColor(Color.parseColor("#E0F3FB"));

        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "");

        Database db = new Database(getApplicationContext());
        float totalAmount = 0;
        ArrayList<String> dbData = db.getCartData(username, "lab");

        packages = new String[dbData.size()][];
        for (int i = 0; i < packages.length; i++) {
            packages[i] = new String[5];
        }

        for (int i = 0; i < dbData.size(); i++) {
            String arrData = dbData.get(i);
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            packages[i][0] = strData[0];
            packages[i][4] = "Cost: PKR " + strData[1] + "/-";
            totalAmount = totalAmount + Float.parseFloat(strData[1]);
        }

        tvTotal.setText("Total Cost: PKR " + totalAmount);

        list = new ArrayList<>();
        for (int i = 0; i < packages.length; i++) {
            item = new HashMap<>();
            item.put("line1", packages[i][0]); // Product name
            item.put("line5", packages[i][4]); // Cost
            list.add(item);
        }
        adapter = new CartAdapter(this, list, username, tvTotal);
        lst.setAdapter(adapter);

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(CartLabActivity.this, LabTestActivity.class));
        });

        btnCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartLabActivity.this, LabTestBookActivity.class);
            it.putExtra("price", tvTotal.getText());
            it.putExtra("date", dateButton.getText());
            it.putExtra("time", timeButton.getText());
            startActivity(it);
        });

        initDatePicker();
        initTimePicker();
        setCurrentDate();
    }

    private void setCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());
        dateButton.setText(currentDate);
    }

    private void initDatePicker() {
        dateButton.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CartLabActivity.this,
                    (view, year1, month1, dayOfMonth) ->
                            dateButton.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                    year, month, day);

            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void initTimePicker() {
        timeButton.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int hrs = cal.get(Calendar.HOUR_OF_DAY);
            int mins = cal.get(Calendar.MINUTE);
            int style = AlertDialog.THEME_HOLO_DARK;

            TimePickerDialog timePickerDialog = new TimePickerDialog(CartLabActivity.this,
                    style, (view, hourOfDay, minute) ->
                    timeButton.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    hrs, mins, true);
            timePickerDialog.show();
        });
    }
}
