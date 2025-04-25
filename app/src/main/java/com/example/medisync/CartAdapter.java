package com.example.medisync;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    String username;
    Database db;
    TextView tvTotal;

    public CartAdapter(Context context, ArrayList<HashMap<String, String>> data, String username, TextView tvTotal) {
        this.context = context;
        this.data = data;
        this.username = username;
        this.tvTotal = tvTotal;
        inflater = LayoutInflater.from(context);
        db = new Database(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.double_lines, null);

        TextView lineA = convertView.findViewById(R.id.line_a);
        TextView lineE = convertView.findViewById(R.id.line_e);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        HashMap<String, String> item = data.get(position);
        String product = item.get("line1");
        String cost = item.get("line5");

        lineA.setText(product);
        lineE.setText(cost);

        btnDelete.setOnClickListener(v -> {
            db.removeItemFromCart(username, product);
            data.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
            float totalCost = 0;
            for (HashMap<String, String> i : data) {
                String costText = i.get("line5"); // e.g., "Cost: PKR 1200/-"
                if (costText != null && costText.contains("PKR")) {
                    String priceStr = costText.replaceAll("[^\\d.]", ""); // Extract number
                    totalCost += Float.parseFloat(priceStr);
                }
            }
            tvTotal.setText("Total Cost: PKR " + totalCost);
        });

        return convertView;
    }
}

