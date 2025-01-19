package com.ireddragonicy.nadma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import java.util.Calendar;

public class ReportIncidentActivity extends AppCompatActivity {

    private TextView reportIncidentText;
    private AutoCompleteTextView incidentType;
    private EditText dateInput, timeInput, locationInput, descriptionInput, nameInput, phoneInput, emailInput;
    private MaterialButton uploadImageButton, removeAllButton, submitReportButton, reportHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        reportIncidentText = findViewById(R.id.reportIncidentText);
        incidentType = findViewById(R.id.incidentType);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        locationInput = findViewById(R.id.locationInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);

        uploadImageButton = findViewById(R.id.uploadImageButton);
        removeAllButton = findViewById(R.id.removeAllButton);
        submitReportButton = findViewById(R.id.submitReportButton);
        reportHistoryButton = findViewById(R.id.reportHistoryButton);

        dateInput.setOnClickListener(view -> showDatePicker());
        timeInput.setOnClickListener(view -> showTimePicker());

        uploadImageButton.setOnClickListener(view -> uploadImage());
        removeAllButton.setOnClickListener(view -> clearAllFields());
        submitReportButton.setOnClickListener(view -> submitReport());
        reportHistoryButton.setOnClickListener(view -> showReportHistory());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dateInput.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                    timeInput.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void uploadImage() {
        // Implement your image upload logic here
        Toast.makeText(this, "Upload Image Clicked", Toast.LENGTH_SHORT).show();
    }

    private void clearAllFields() {
        incidentType.setText("");
        dateInput.setText("");
        timeInput.setText("");
        locationInput.setText("");
        descriptionInput.setText("");
        nameInput.setText("");
        phoneInput.setText("");
        emailInput.setText("");
        Toast.makeText(this, "All fields cleared", Toast.LENGTH_SHORT).show();
    }

    private void submitReport() {
        // Collect data from input fields and handle form submission
        String incidentTypeText = incidentType.getText().toString();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();
        String location = locationInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();

        // Add logic to validate and send data to the server or save it
        Toast.makeText(this, "Report Submitted", Toast.LENGTH_SHORT).show();
    }

    private void showReportHistory() {
        // Implement logic to show report history
        Toast.makeText(this, "Report History Clicked", Toast.LENGTH_SHORT).show();
    }
}
