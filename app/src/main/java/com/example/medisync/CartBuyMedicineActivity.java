package com.example.medisync;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CartBuyMedicineActivity extends AppCompatActivity {
    HashMap<String,String> item;
    ArrayList list;
    ListView lst;
    SimpleAdapter sa;
    TextView tvTotal;
    private String[][] packages = {};
    private DatePickerDialog datePickerDialog;
    Button dateButton, btnCheckout, btnBack;
    Database db;
    String username;
    float totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);

        dateButton = findViewById(R.id.buttonBMCartDate);
        btnCheckout = findViewById(R.id.buttonBMCartCheckout);
        btnBack = findViewById(R.id.buttonBMCartBack);
        tvTotal = findViewById(R.id.textViewBMCartTotalPrice);
        lst = findViewById(R.id.listViewBMCart);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","").toString();
        db = new Database(getApplicationContext());

        loadCartItems();

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(CartBuyMedicineActivity.this, BuyMedicineActivity.class));
        });

        btnCheckout.setOnClickListener(view -> {
            if (list == null || list.isEmpty()) {
                Toast.makeText(this, "Your cart is empty. Please add items before checkout.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent it = new Intent(CartBuyMedicineActivity.this, BuyMedicineBookActivity.class);
                it.putExtra("price", tvTotal.getText());
                it.putExtra("date", dateButton.getText());
                startActivity(it);
            }
        });

        initDatePicker();
        setCurrentDate();
    }

    private void loadCartItems() {
        try {
            totalAmount = 0;
            ArrayList<String> dbData = db.getCartData(username, "medicine");

            packages = new String[dbData.size()][];
            for (int i = 0; i < packages.length; i++) {
                packages[i] = new String[5];
            }

            for (int i = 0; i < dbData.size(); i++) {
                String arrData = dbData.get(i);
                String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                packages[i][0] = strData[0];
                packages[i][1] = strData[1];
                packages[i][4] = "Cost: " + strData[1] + "/-";
                totalAmount += Float.parseFloat(strData[1]);
            }
            tvTotal.setText("Total Cost: " + totalAmount);

            list = new ArrayList();
            for (int i = 0; i < packages.length; i++) {
                item = new HashMap<String, String>();
                item.put("line1", packages[i][0]);
                item.put("line5", packages[i][4]);
                list.add(item);
            }
            sa = new SimpleAdapter(this, list, R.layout.mutilines_add_delete,
                    new String[]{"line1", "line5"},
                    new int[]{R.id.line_a, R.id.line_b});
            lst.setAdapter(sa);

        } catch (Exception e) {
            Toast.makeText(this, "Error loading cart items", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private float extractPriceFromText(String costText) {
        try {
            String priceStr = costText.replaceAll("[^\\d.]", "");
            return Float.parseFloat(priceStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void onAddClick(View view) {
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        String costText = packages[position][4];
        float currentPrice = extractPriceFromText(costText);
        float unitPrice = getUnitPrice(packages[position][0]);
        float newPrice = currentPrice + unitPrice;
        packages[position][4] = "Cost: " + newPrice + "/-";
        packages[position][1] = String.valueOf(newPrice);
        TextView tvQuantity = parentRow.findViewById(R.id.tvQuantity);
        try {
            int currentQty = Integer.parseInt(tvQuantity.getText().toString());
            tvQuantity.setText(String.valueOf(currentQty + 1));
        } catch (NumberFormatException e) {
            tvQuantity.setText("1");
        }
        ContentValues cv = new ContentValues();
        cv.put("price", newPrice);
        db.getWritableDatabase().update("cart", cv,
                "username = ? AND product = ? AND otype = ?",
                new String[]{username, packages[position][0], "medicine"});

        updateTotalPrice();
    }

    public void onMinusClick(View view) {
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        TextView tvQuantity = parentRow.findViewById(R.id.tvQuantity);
        try {
            int currentQty = Integer.parseInt(tvQuantity.getText().toString());
            if (currentQty > 1) {
                String costText = packages[position][4];
                float currentPrice = extractPriceFromText(costText);
                float unitPrice = getUnitPrice(packages[position][0]);
                float newPrice = currentPrice - unitPrice;
                packages[position][4] = "Cost: " + newPrice + "/-";
                packages[position][1] = String.valueOf(newPrice);
                tvQuantity.setText(String.valueOf(currentQty - 1));
                ContentValues cv = new ContentValues();
                cv.put("price", newPrice);
                db.getWritableDatabase().update("cart", cv,
                        "username = ? AND product = ? AND otype = ?",
                        new String[]{username, packages[position][0], "medicine"});
                updateTotalPrice();
            } else {
                Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            tvQuantity.setText("1");
        }
    }
    private float getUnitPrice(String productName) {
        for (String[] medicine : BuyMedicineActivity.packages) {
            if (medicine[0].equals(productName)) {
                try {
                    return Float.parseFloat(medicine[4]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    private void updateTotalPrice() {
        float total = 0;
        for (String[] item : packages) {
            if (item != null && item.length > 1) {
                try {
                    total += Float.parseFloat(item[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        tvTotal.setText("Total Cost: " + total);
    }
    public void onRemoveClick(View view) {
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        String productName = packages[position][0];
        db.removeItemFromCart(username, productName);
        loadCartItems();
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(CartBuyMedicineActivity.this,
                    (datePicker, year1, month1, dayOfMonth) ->
                            dateButton.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            datePickerDialog.show();
        });
    }
}
