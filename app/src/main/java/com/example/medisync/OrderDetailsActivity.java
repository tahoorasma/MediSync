package com.example.medisync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {
    private String[][] order_details = {};

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btn = findViewById(R.id.buttonBMBack);
        lst = findViewById(R.id.listViewOD);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDetailsActivity.this,HomeActivity.class));
            }
        });
        Database db = new Database(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","").toString();
        ArrayList dbData = db.getOrderData(username);

        order_details = new String[dbData.size()][];
        for (int i=0;i<order_details.length;i++) {
            order_details[i] = new String[5];
            String arrData = dbData.get(i).toString();
            Log.d("arrData", arrData);
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            Log.d("strData", Arrays.toString(strData));
            if (strData[7].compareTo("appointment") == 0) {
                order_details[i][0] = strData[0];
            } else if (strData[7].compareTo("lab") == 0) {
                order_details[i][0] = "Home Sample Collection";
            } else {order_details[i][0] = "Home Delivery";}
            if (strData[7].compareTo("appointment") == 0) {
                order_details[i][1] = strData[1];
            } else {order_details[i][1] = "Address: " + strData[1] + ", " + strData[3];}
            order_details[i][2]= "Rs."+strData[6];
            if (strData[7].compareTo("medicine") == 0) {
                order_details[i][3] = strData[4];
            } else {order_details[i][3] = strData[4] + " " + strData[5];}
            if (strData[7].compareTo("lab") == 0) {
                order_details[i][4]= "Lab Tests";
            } else if (strData[7].compareTo("appointment") == 0) {
                order_details[i][4]= "Appointment";
            } else {order_details[i][4]= "Medicine";}
        }
        list = new ArrayList<>();
        for (int i = 0;i<order_details.length;i++){
            item = new HashMap<String , String>();
            item.put("line1",order_details[i][0]);
            item.put("line2",order_details[i][1]);
            item.put("line3",order_details[i][2]);
            item.put("line4",order_details[i][3]);
            item.put("line5",order_details[i][4]);
            list.add(item);
        }
        OrderAdapter adapter = new OrderAdapter(this, list, username);
        lst.setAdapter(adapter);
    }
}
