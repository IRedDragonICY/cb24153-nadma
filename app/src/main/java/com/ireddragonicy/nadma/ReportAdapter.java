package com.ireddragonicy.nadma;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final Context context;
    private final List<Report> reportList;
    private final OnEditClickListener onEditClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    public ReportAdapter(Context context, List<Report> reportList, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.reportList = reportList;
        this.onEditClickListener = editClickListener;
        this.onDeleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.incidentTypeTextView.setText(report.getIncidentType());
        String dateTimeText = report.getDate() + " " + report.getTime();
        holder.dateTextView.setText(dateTimeText);
        holder.locationTextView.setText(report.getLocation());

        if (report.getBase64Image() != null && !report.getBase64Image().isEmpty()) {
            Bitmap bitmap = base64ToBitmap(report.getBase64Image());
            holder.reportImageView.setImageBitmap(bitmap);
            holder.reportImageView.setVisibility(View.VISIBLE);
        } else {
            holder.reportImageView.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(report);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(report.getDocumentId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView incidentTypeTextView;
        TextView dateTextView;
        TextView locationTextView;
        ImageView reportImageView;
        MaterialButton editButton;
        MaterialButton deleteButton;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            incidentTypeTextView = itemView.findViewById(R.id.incidentTypeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            reportImageView = itemView.findViewById(R.id.reportImageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public interface OnEditClickListener {
        void onEditClick(Report report);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String documentId);
    }
}