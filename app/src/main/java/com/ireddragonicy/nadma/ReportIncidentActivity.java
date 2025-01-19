package com.ireddragonicy.nadma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
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
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView uploadedImageView;
    private MaterialButton uploadImageButton;
    private MaterialButton historyReportButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        sessionManager = new SessionManager(this);
        GoogleSignInAccount account = sessionManager.getSignedInAccount();
        if (account != null) {
            accountId = account.getId();
        }

        initializeViews();
        setupClickListeners();
        setupLaunchers();
        setupBackPressHandler();
    }

    private void initializeViews() {
        TextView reportIncidentText = findViewById(R.id.reportIncidentText);
        incidentType = findViewById(R.id.incidentType);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        locationInput = findViewById(R.id.locationInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);

        uploadImageButton = findViewById(R.id.uploadImageButton);
        historyReportButton = findViewById(R.id.historyReportButton);
        MaterialButton removeAllButton = findViewById(R.id.removeAllButton);
        MaterialButton submitReportButton = findViewById(R.id.submitReportButton);
        uploadedImageView = findViewById(R.id.uploadedImageView);
        backButton = findViewById(R.id.backButton);
    }

    private void setupClickListeners() {
        dateInput.setOnClickListener(view -> showDatePicker());
        timeInput.setOnClickListener(view -> showTimePicker());
        uploadImageButton.setOnClickListener(view -> showImagePickerDialog());
        historyReportButton.setOnClickListener(view -> showReportHistory());
        backButton.setOnClickListener(view -> onBackPressed());

        MaterialButton removeAllButton = findViewById(R.id.removeAllButton);
        MaterialButton submitReportButton = findViewById(R.id.submitReportButton);

        removeAllButton.setOnClickListener(view -> clearAllFields());
        submitReportButton.setOnClickListener(view -> submitReport());
    }

    private void setupLaunchers() {
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        uploadedImageView.setImageBitmap(imageBitmap);
                        uploadedImageView.setVisibility(View.VISIBLE);
                        uploadImageButton.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        uploadedImageView.setImageURI(selectedImageUri);
                        uploadedImageView.setVisibility(View.VISIBLE);
                        uploadImageButton.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, "Failed to retrieve image from gallery.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    private void setupBackPressHandler() {
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

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        String[] options = {"Take Photo", "Choose from Gallery"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else if (which == 1) {
                openGallery();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhotoIntent);
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

    private void clearAllFields() {
        incidentType.setText("");
        dateInput.setText("");
        timeInput.setText("");
        locationInput.setText("");
        descriptionInput.setText("");
        nameInput.setText("");
        phoneInput.setText("");
        emailInput.setText("");
        uploadedImageView.setVisibility(View.GONE);
        uploadImageButton.setVisibility(View.VISIBLE);
    }

    private void submitReport() {
        if (!validateInputs()) {
            return;
        }

        String incidentTypeText = incidentType.getText().toString();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();
        String location = locationInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();

        if (accountId != null) {
            Toast.makeText(this, "Report Submitted by User: " + accountId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Report Submitted (User not logged in)", Toast.LENGTH_SHORT).show();
        }

        clearAllFields();
    }

    private boolean validateInputs() {
        if (incidentType.getText().toString().trim().isEmpty()) {
            incidentType.setError("Please select incident type");
            return false;
        }
        if (dateInput.getText().toString().trim().isEmpty()) {
            dateInput.setError("Please select date");
            return false;
        }
        if (timeInput.getText().toString().trim().isEmpty()) {
            timeInput.setError("Please select time");
            return false;
        }
        if (locationInput.getText().toString().trim().isEmpty()) {
            locationInput.setError("Please enter location");
            return false;
        }
        if (descriptionInput.getText().toString().trim().isEmpty()) {
            descriptionInput.setError("Please enter description");
            return false;
        }
        return true;
    }

    private void showReportHistory() {
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, ReportHistoryActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please log in to view report history.", Toast.LENGTH_SHORT).show();
        }
    }
}