package com.example.medisync;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity {
    Button btn, bookbtn;
    Button dateButton;
    Button timeButton;
    TextView tvFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        bookbtn = findViewById(R.id.buttonAppBook);
        btn = findViewById(R.id.buttonAppBack);
        dateButton = findViewById(R.id.buttonAppDate);
        timeButton = findViewById(R.id.buttonAppTime);
        tvFee = findViewById(R.id.tvConsFee);

        // Retrieve data from the intent
        Intent intent = getIntent();
        String doctorName = intent.getStringExtra("doctor_name");
        String hospitalAddress = intent.getStringExtra("hospital_address");
        String contactNumber = intent.getStringExtra("contact_number");
        String fees = intent.getStringExtra("fees");

        // Use the retrieved data as needed
        TextView textViewDoctorName = findViewById(R.id.editTextAppFullName);
        TextView textViewHospitalAddress = findViewById(R.id.editTextAppAddress);
        TextView textViewContactNumber = findViewById(R.id.editTextAppContactNumber);

        textViewDoctorName.setText(doctorName);
        textViewHospitalAddress.setText(hospitalAddress);
        textViewContactNumber.setText(contactNumber);
        tvFee.setText("Consultation Fee: " + fees);

        btn.setOnClickListener(view -> {
            startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class));
            finish();
        });

        bookbtn.setOnClickListener(view -> {
            SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedpreferences.getString("username", "");
            Database db = new Database(getApplicationContext());

            // Retrieve necessary data
            String title = "Appointment";
            String fullname = textViewDoctorName.getText().toString();
            String address = textViewHospitalAddress.getText().toString();
            String contactno = textViewContactNumber.getText().toString();
            String date = dateButton.getText().toString();
            String time = timeButton.getText().toString();

            // Parse fees
            float feeValue;
            try {
                feeValue = Float.parseFloat(fees.replace("Consultation Fee: ", "")); // Remove "Fees: " before parsing
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid fee value", Toast.LENGTH_LONG).show();
                return; // Exit if fees are invalid
            }

            try {
                // Check if the appointment already exists
                if (db.checkAppointmentExists(username, title + "=>" + fullname, address, contactno, date, time) == 1) {
                    Toast.makeText(getApplicationContext(), "Appointment already booked", Toast.LENGTH_LONG).show();
                } else {
                    // Book the appointment
                    db.addOrder(username, title + "=>" + fullname, address, contactno, 0, date, time, feeValue, "appointment");
                    Toast.makeText(getApplicationContext(), "Your Appointment has been booked Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log the error
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Initialize date picker, time picker, and set current date
        initDatePicker();
        initTimePicker();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookAppointmentActivity.this,
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

    private void initTimePicker() {
        // Set up the time picker
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hrs = cal.get(Calendar.HOUR_OF_DAY); // Use HOUR_OF_DAY for 24-hour format
                int mins = cal.get(Calendar.MINUTE);
                int style = AlertDialog.THEME_HOLO_DARK;


                TimePickerDialog timePickerDialog = new TimePickerDialog(BookAppointmentActivity.this,
                        style, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Update the button text with the selected time
                        timeButton.setText(String.format("%02d:%02d", hourOfDay, minute)); // Format time to HH:mm
                    }
                }, hrs, mins, true); // true for 24-hour format
                timePickerDialog.show();
            }
        });
    }
}
