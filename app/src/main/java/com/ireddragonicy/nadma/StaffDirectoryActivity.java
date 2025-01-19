package com.ireddragonicy.nadma;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StaffDirectoryActivity extends AppCompatActivity {

    private StaffAdapter staffAdapter;
    private final List<Staff> allStaffList = new ArrayList<>();
    private final List<Staff> currentStaffList = new ArrayList<>();
    private int currentPage = 0;
    private final int itemsPerPage = 5;
    private TextView pageTextView;
    private MaterialButton prevButton;
    private MaterialButton nextButton;
    private EditText searchEditText;
    private final List<Staff> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_directory);

        RecyclerView recyclerView = findViewById(R.id.staffRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        staffAdapter = new StaffAdapter(currentStaffList);
        recyclerView.setAdapter(staffAdapter);

        pageTextView = findViewById(R.id.pageTextView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        searchEditText = findViewById(R.id.searchEditText);

        setupPaginationButtons();
        configureActionBar();
        setupSearch();
        fetchDataFromUrl();
    }

    public void navigateBack(View view) {
        Intent intent = new Intent(this, EmergencyContactActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void fetchDataFromUrl() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://www.nadma.gov.my/bm/hubungi-kami/direktori-kakitangan").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(StaffDirectoryActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("StaffDirectoryActivity", "onFailure: ", e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    parseHtml(response.body().string());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(StaffDirectoryActivity.this, "Response failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseHtml(String html) {
        Document document = Jsoup.parse(html);
        Elements tableRows = document.select("table.sppb-addon-table-main tbody tr");
        List<Staff> tempList = new ArrayList<>();
        for (Element row : tableRows) {
            if (row.hasAttr("style") && row.attr("style").contains("background")) {
                Elements header = row.select("td div.sppb-addon-content");
                if (!header.isEmpty()) {
                    String headerText = Objects.requireNonNull(header.first()).text().trim();
                    Staff staffHeader = new Staff(null, null, headerText, null, null);
                    staffHeader.setHeader(true);
                    tempList.add(staffHeader);
                }
            } else {
                Elements cells = row.select("td");
                if (cells.size() == 5) {
                    tempList.add(new Staff(cells.get(0).text().trim(),
                            cells.get(1).text().trim(),
                            cells.get(2).text().trim(),
                            cells.get(3).text().trim(),
                            cells.get(4).text().trim()));
                }
            }
        }
        runOnUiThread(() -> {
            allStaffList.addAll(tempList);
            filteredList.addAll(allStaffList);
            updatePage();
        });
    }

    private void setupPaginationButtons() {
        prevButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });

        nextButton.setOnClickListener(v -> {
            int maxPage = (int) Math.ceil((double) filteredList.size() / itemsPerPage) - 1;
            if (currentPage < maxPage) {
                currentPage++;
                updatePage();
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
                currentPage = 0;
                updatePage();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(allStaffList);
        } else {
            String lowerCaseText = text.toLowerCase(Locale.getDefault());
            for (Staff staff : allStaffList) {
                if ((staff.getNama() != null && staff.getNama().toLowerCase(Locale.getDefault()).contains(lowerCaseText)) ||
                        (staff.getJawatan() != null && staff.getJawatan().toLowerCase(Locale.getDefault()).contains(lowerCaseText))) {
                    filteredList.add(staff);
                }
            }
        }
    }

    private void updatePage() {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, filteredList.size());
        currentStaffList.clear();
        if (startIndex < filteredList.size()) {
            currentStaffList.addAll(filteredList.subList(startIndex, endIndex));
        }
        staffAdapter.notifyDataSetChanged();
        updatePaginationView();
    }

    private void updatePaginationView() {
        int maxPage = (int) Math.ceil((double) filteredList.size() / itemsPerPage);
        pageTextView.setText(String.format("Page %d of %d", currentPage + 1, maxPage));
        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < maxPage - 1);
    }

    private void configureActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateBack(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}