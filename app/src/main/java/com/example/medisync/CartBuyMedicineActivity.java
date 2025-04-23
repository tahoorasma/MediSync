package com.example.medisync;

import android.app.DatePickerDialog;
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
    Button dateButton, timeButton, btnCheckout, btnBack;
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
        String username = sharedPreferences.getString("username","").toString();

        Database db = new Database(getApplicationContext());
        float totalAmount = 0;
        ArrayList dbData = db.getCartData(username,"medicine");
        //Toast.makeText(this, ""+dbData, Toast.LENGTH_LONG).show();

        packages = new String[dbData.size()][];
        for (int i = 0;i<packages.length;i++){
            packages[i]= new String[5];
        }

        for (int i=0;i<dbData.size();i++){
            String arrData = dbData.get(i).toString();
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            packages[i][0]= strData[0];
            packages[i][4]= "Cost : "+strData[1]+"/-";
            totalAmount = totalAmount + Float.parseFloat(strData[1]);
        }
        tvTotal.setText("Total Cost : "+totalAmount);

        list = new ArrayList();
        for(int i = 0;i<packages.length;i++ ){
            item= new HashMap<String,String>();
            item.put("line1",packages[i][0]);
            item.put("line2",packages[i][1]);
            item.put("line3",packages[i][2]);
            item.put("line4",packages[i][3]);
            item.put("line5",packages[i][4]);
            list.add(item);
        }
        sa = new SimpleAdapter(this, list, R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e,});
        lst.setAdapter(sa);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartBuyMedicineActivity.this,BuyMedicineActivity.class));

            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(new Intent(CartBuyMedicineActivity.this,BuyMedicineBookActivity.class));
                it.putExtra("price",tvTotal.getText());
                it.putExtra("date",dateButton.getText());
                startActivity(it);
            }
        });

        initDatePicker();
        setCurrentDate();
    }
    private void setCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(cal.getTime());
        dateButton.setText(currentDate); // Set the current date to the date button
    }

    private void initDatePicker() {
        // Set up the date picker
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(CartBuyMedicineActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Update the button text with the selected date
                                dateButton.setText(day + "/" + (month + 1) + "/" + year); // Month is 0-based
                            }
                        }, year, month, day);

                // Disable previous dates
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()); // Set minimum date to today
                datePickerDialog.show();
            }
        });
    }
}
