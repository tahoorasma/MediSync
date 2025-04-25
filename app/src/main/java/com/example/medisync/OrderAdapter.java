package com.example.medisync;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    Database db;
    String username;

    public OrderAdapter(Context context, ArrayList<HashMap<String, String>> data, String username) {
        this.context = context;
        this.data = data;
        this.username = username;
        inflater = LayoutInflater.from(context);
        db = new Database(context);
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return data.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.multi_lines_detail, null);

        TextView line1 = convertView.findViewById(R.id.line_a);
        TextView line2 = convertView.findViewById(R.id.line_b);
        TextView line3 = convertView.findViewById(R.id.line_c);
        TextView line4 = convertView.findViewById(R.id.line_d);
        TextView line5 = convertView.findViewById(R.id.line_e);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        HashMap<String, String> item = data.get(position);

        line1.setText(item.get("line1"));
        line2.setText(item.get("line2"));
        line3.setText(item.get("line3"));
        line4.setText(item.get("line4"));
        line5.setText(item.get("line5"));

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteOrder(username, item.get("line4"), item.get("line5"));
                        data.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("NO", null)
                    .show();
        });
        return convertView;
    }
}

