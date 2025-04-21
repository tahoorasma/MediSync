package com.example.medisync;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class BuyMedicineActivity extends AppCompatActivity {

    private String[][] packages = {
            {"Panadol (Paracetamol) 500mg", "", "", "", "50"},
            {"Brufen (Ibuprofen) 400mg", "", "", "", "120"},
            {"Rigix (Cetirizine) 10mg", "", "", "", "80"},
            {"Augmentin (Amoxicillin + Clavulanate) 625mg", "", "", "", "350"},
            {"Flagyl (Metronidazole) 400mg", "", "", "", "100"},
            {"Loprin (Aspirin) 75mg", "", "", "", "25"},
            {"Ventolin (Salbutamol) Inhaler", "", "", "", "450"},
            {"Omeprazole 20mg", "", "", "", "150"},
            {"Gaviscon (Antacid) Liquid", "", "", "", "200"},
            {"Ciprofloxacin 500mg", "", "", "", "180"}
    };

    private String[] package_details = {
            "Used to relieve mild to moderate pain and fever",
            "Reduces pain, inflammation, and fever",
            "Treats allergies, hay fever, and skin itching",
            "Antibiotic for bacterial infections like sinusitis, pneumonia, and urinary tract infections",
            "Treats infections caused by bacteria and parasites (e.g., dental infections, amoebiasis)",
            "Prevents heart attacks and strokes by thinning blood",
            "Relieves asthma and breathing problems by opening airways",
            "Reduces stomach acid and treats heartburn or GERD",
            "Neutralizes stomach acid and relieves indigestion or acid reflux",
            "Antibiotic for urinary tract infections, diarrhea, and skin infections"
    };

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;
    Button btnBack, btnGoToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);
        lst = findViewById(R.id.listViewDD);
        btnBack = findViewById(R.id.buttonBMBack);
        btnGoToCart = findViewById(R.id.buttonLTDAddToCart);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyMedicineActivity.this,HomeActivity.class));
            }
        });
        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyMedicineActivity.this,CartBuyMedicineActivity.class));
            }
        });

        list = new ArrayList<>();
        for (int i = 0;i<packages.length;i++){
            item = new HashMap<String , String>();
            item.put("line1",packages[i][0]);
            item.put("line2",packages[i][1]);
            item.put("line3",packages[i][2]);
            item.put("line4",packages[i][3]);
            item.put("line5","Total Cost: "+packages[i][4]+"/-");
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e,});
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(BuyMedicineActivity.this,BuyMedicineDetailsActivity.class);
                it.putExtra("text1",packages[i][0]);
                it.putExtra("text2",package_details[i]);
                it.putExtra("text3",packages[i][4]);
                startActivity(it);
            }
        });
    }
}