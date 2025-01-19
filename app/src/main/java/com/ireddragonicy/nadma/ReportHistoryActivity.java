package com.ireddragonicy.nadma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private final List<Report> reportList = new ArrayList<>();
    private FirebaseFirestore db;
    private TextView emptyView;
    private SessionManager sessionManager;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReportAdapter(this, reportList, this::editReport, this::deleteReport);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);
        userId = sessionManager.getSignedInAccount() != null ? sessionManager.getSignedInAccount().getId() : null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchReports();
    }

    private void fetchReports() {
        if (userId != null) {
            db.collection("reports")
                    .whereEqualTo("userId", userId)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        reportList.clear();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Report report = document.toObject(Report.class);
                                report.setDocumentId(document.getId());
                                reportList.add(report);
                            }
                            if (reportList.isEmpty()) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Error getting reports: " + (task.getException() != null ? task.getException().getMessage() : "unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    public void editReport(Report report) {
        Intent intent = new Intent(this, ReportIncidentActivity.class);
        intent.putExtra("report_id", report.getDocumentId());
        intent.putExtra("incidentType", report.getIncidentType());
        intent.putExtra("date", report.getDate());
        intent.putExtra("time", report.getTime());
        intent.putExtra("location", report.getLocation());
        intent.putExtra("description", report.getDescription());
        intent.putExtra("name", report.getName());
        intent.putExtra("phone", report.getPhone());
        intent.putExtra("email", report.getEmail());
        intent.putExtra("base64Image", report.getBase64Image());
        startActivity(intent);
    }

    public void deleteReport(String documentId) {
        db.collection("reports").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Report deleted successfully.", Toast.LENGTH_SHORT).show();
                    fetchReports();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}