package com.ireddragonicy.nadma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.Locale;

public class ReportIncidentActivity extends AppCompatActivity {

    private AutoCompleteTextView incidentType;
    private EditText dateInput, timeInput, locationInput, descriptionInput, nameInput, phoneInput, emailInput;
    private SessionManager sessionManager;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        sessionManager = new SessionManager(this);
        GoogleSignInAccount account = sessionManager.getSignedInAccount();
        if (account != null) {
            accountId = account.getId();
        }

        TextView reportIncidentText = findViewById(R.id.reportIncidentText);
        incidentType = findViewById(R.id.incidentType);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        locationInput = findViewById(R.id.locationInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);

        MaterialButton uploadImageButton = findViewById(R.id.uploadImageButton);
        MaterialButton removeAllButton = findViewById(R.id.removeAllButton);
        MaterialButton submitReportButton = findViewById(R.id.submitReportButton);
        MaterialButton historyReportButtonBottom = findViewById(R.id.reportHistoryButtonBottom);

        dateInput.setOnClickListener(view -> showDatePicker());
        timeInput.setOnClickListener(view -> showTimePicker());

        uploadImageButton.setOnClickListener(view -> uploadImage());
        removeAllButton.setOnClickListener(view -> clearAllFields());
        submitReportButton.setOnClickListener(view -> submitReport());
        historyReportButtonBottom.setOnClickListener(view -> showReportHistory());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ReportIncidentActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
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
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timeInput.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void uploadImage() {
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
        String incidentTypeText = incidentType.getText().toString();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();
        String location = locationInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();

        if (accountId != null) {
            // Save the report along with the accountId
            Intent intent = new Intent();
            intent.putExtra("accountId", accountId);
            // Add other report details to the intent if needed
            Toast.makeText(this, "Report Submitted by User: " + accountId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Report Submitted (User not logged in)", Toast.LENGTH_SHORT).show();
        }
    }

    private void showReportHistory() {
        if (sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Opening Report History", Toast.LENGTH_SHORT).show();
            // Intent to Report History Activity
            // Pass the accountId if needed to filter reports
        } else {
            Toast.makeText(this, "Please log in to view report history.", Toast.LENGTH_SHORT).show();
        }
    }
}