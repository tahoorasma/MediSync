package com.example.medisync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {
    private String[][] physician_details = {
            {"Dr. Ahsan Malik", "Jinnah Hospital, Lahore", "Exp: 7yrs", "Mobile No: 03001234567", "1500"},
            {"Dr. Sana Iqbal", "Civil Hospital, Karachi", "Exp: 5yrs", "Mobile No: 03019876543", "1200"},
            {"Dr. Usman Tariq", "Shifa Hospital, Islamabad", "Exp: 10yrs", "Mobile No: 03111234567", "2000"},
            {"Dr. Mehwish Khan", "Services Hospital, Lahore", "Exp: 6yrs", "Mobile No: 03451239876", "1300"},
            {"Dr. Hamza Shah", "Aga Khan Hospital, Karachi", "Exp: 8yrs", "Mobile No: 03345678901", "1800"}
    };

    private String[][] nutritionist_details = {
            {"Dr. Ayesha Rizvi", "Kulsum Hospital, Islamabad", "Exp: 4yrs", "Mobile No: 03214567890", "1100"},
            {"Dr. Bilal Haider", "Mayo Hospital, Lahore", "Exp: 9yrs", "Mobile No: 03007654321", "1700"},
            {"Dr. Zoya Tariq", "Liaquat Hospital, Hyderabad", "Exp: 6yrs", "Mobile No: 03450123987", "1400"},
            {"Dr. Moiz Ahmed", "PIMS, Islamabad", "Exp: 11yrs", "Mobile No: 03336549812", "2100"},
            {"Dr. Hira Siddiqui", "Indus Hospital, Karachi", "Exp: 5yrs", "Mobile No: 03041239876", "1600"}
    };

    private String[][] dentist_details = {
            {"Dr. Taimoor Aslam", "Punjab Institute of Cardiology, Lahore", "Exp: 12yrs", "Mobile No: 03461234567", "2500"},
            {"Dr. Anum Javed", "Ziauddin Hospital, Karachi", "Exp: 7yrs", "Mobile No: 03334321098", "1900"},
            {"Dr. Fawad Riaz", "DHQ Hospital, Faisalabad", "Exp: 6yrs", "Mobile No: 03009345678", "1500"},
            {"Dr. Amina Farooq", "CMH, Rawalpindi", "Exp: 8yrs", "Mobile No: 03215678904", "1700"},
            {"Dr. Shahzeb Ali", "NICVD, Karachi", "Exp: 10yrs", "Mobile No: 03112349876", "2200"}
    };

    private String[][] surgeon_details = {
            {"Dr. Rabia Noor", "Lahore General Hospital", "Exp: 5yrs", "Mobile No: 03127654321", "1300"},
            {"Dr. Imran Saeed", "General Hospital, Lahore", "Exp: 14yrs", "Mobile No: 03417894561", "2400"},
            {"Dr. Saba Nisar", "JPMC, Karachi", "Exp: 9yrs", "Mobile No: 03033445566", "1600"},
            {"Dr. Hassan Rauf", "Fatima Memorial Hospital, Lahore", "Exp: 7yrs", "Mobile No: 03216549873", "1800"},
            {"Dr. Nadia Qureshi", "Medicare Hospital, Karachi", "Exp: 6yrs", "Mobile No: 03149988776", "1500"}
    };

    private String[][] cardiologist_details = {
            {"Dr. Asim Mehmood", "Islamabad Medical Complex", "Exp: 8yrs", "Mobile No: 03465554433", "1700"},
            {"Dr. Kiran Abbas", "Children’s Hospital, Lahore", "Exp: 5yrs", "Mobile No: 03024446688", "1200"},
            {"Dr. Saad Rehman", "Karachi Medical Centre", "Exp: 9yrs", "Mobile No: 03234445566", "2000"},
            {"Dr. Nida Faisal", "Ali Medical Center, Islamabad", "Exp: 6yrs", "Mobile No: 03121234567", "1600"},
            {"Dr. Faisal Shah", "Hamdard Hospital, Karachi", "Exp: 7yrs", "Mobile No: 03011223344", "1500"}
    };

    TextView tv;
    Button btn;
    String[][] doctor_details ={};
    HashMap<String,String> item;
    ArrayList list ;
    SimpleAdapter sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tv =findViewById(R.id.textViewODTitle);
        btn=findViewById(R.id.buttonBMBack);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);

        if (title.compareTo("Family Physician")==0) {
            doctor_details = physician_details;
        } else
        if (title.compareTo("Nutritionist")==0) {
            doctor_details = nutritionist_details;
        } else
        if (title.compareTo("Dentist")==0) {
            doctor_details = dentist_details;
        } else
        if (title.compareTo("Surgeon")==0) {
            doctor_details = surgeon_details;
        } else
            doctor_details = cardiologist_details;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDetailsActivity.this,FindDoctorActivity.class));
            }
        });
        list = new ArrayList();
        for (int i =0;i<doctor_details.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", doctor_details[i][0]);
            item.put("line2", doctor_details[i][1]);
            item.put("line3", doctor_details[i][2]);
            item.put("line4", doctor_details[i][3]);
            item.put("line5", "Consultation Fees: PKR "+doctor_details[i][4]+"/-");
            list.add(item);
        }
        sa = new SimpleAdapter(this,list,R.layout.multi_lines,new String[]{"line1","line2","line3","line4","line5",},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e,}
        );
        ListView lst = findViewById(R.id.listViewDD);
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(DoctorDetailsActivity.this,BookAppointmentActivity.class);
                it.putExtra("Text1", title);
                it.putExtra("doctor_name", doctor_details[i][0]); // Doctor Name
                it.putExtra("hospital_address", doctor_details[i][1]); // Hospital Address
                it.putExtra("contact_number", doctor_details[i][3]); // Mobile No
                it.putExtra("fees", doctor_details[i][4]); // Fees
                startActivity(it);
            }
        });
    }
}
