package com.ireddragonicy.nadma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.button.MaterialButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.CustomZoomButtonsController;

public class EmergencyContactActivity extends AppCompatActivity {

    private MapView mapView;
    private TextView phoneTextView;
    private TextView faxTextView;
    private TextView emailTextView;

    private static final double LATITUDE = 2.937323;
    private static final double LONGITUDE = 101.704762;
    private static final double MAP_ZOOM_LEVEL = 16.0;
    private static final String NADMA_OFFICE_TITLE = "NADMA Office Location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        initializeOSMConfiguration();
        initializeMapView();
        setupContactInfo();
        setupBackButton();
        configureActionBar();
        setupStaffDirectoryButton();
    }

    private void initializeOSMConfiguration() {
        Configuration.getInstance().setUserAgentValue(getPackageName());
    }

    private void initializeMapView() {
        mapView = findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);
        setupMapCenterAndMarker();
    }

    private void setupMapCenterAndMarker() {
        IMapController mapController = mapView.getController();
        mapController.setZoom(MAP_ZOOM_LEVEL);
        GeoPoint startPoint = new GeoPoint(LATITUDE, LONGITUDE);
        mapController.setCenter(startPoint);
        addMarker(startPoint);
    }

    private void addMarker(GeoPoint position) {
        Marker marker = new Marker(mapView);
        marker.setPosition(position);
        marker.setTitle(NADMA_OFFICE_TITLE);
        mapView.getOverlays().add(marker);
    }

    private void setupContactInfo() {
        phoneTextView = findViewById(R.id.phone_number);
        faxTextView = findViewById(R.id.fax_number);
        emailTextView = findViewById(R.id.email_address);
        setupContactListeners();
    }

    private void setupContactListeners() {
        phoneTextView.setOnClickListener(v -> dialPhoneNumber(phoneTextView.getText().toString()));
        faxTextView.setOnClickListener(v -> sendFaxViaEmail(faxTextView.getText().toString()));
        emailTextView.setOnClickListener(v -> sendEmail(emailTextView.getText().toString()));
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber.replaceAll("\\s+", "")));
        startActivity(intent);
    }

    private void sendFaxViaEmail(String faxNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Fax Transmission");
        intent.putExtra(Intent.EXTRA_TEXT, "Please find the fax number: " + faxNumber);
        startActivity(Intent.createChooser(intent, "Send Fax via Email"));
    }

    private void sendEmail(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        startActivity(intent);
    }

    private void setupBackButton() {
        findViewById(R.id.backButton).setOnClickListener(v -> navigateUpToParent());
    }

    private void configureActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupStaffDirectoryButton(){
        MaterialButton staffDirectoryButton = findViewById(R.id.btn_search_staff_directory);
          staffDirectoryButton.setOnClickListener(view -> {
              Intent intent = new Intent(this, StaffDirectoryActivity.class);
              startActivity(intent);
          });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateUpToParent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateUpToParent() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        if (intent == null) {
            intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}