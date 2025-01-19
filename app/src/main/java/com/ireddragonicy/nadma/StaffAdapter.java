package com.ireddragonicy.nadma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Staff> staffList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public StaffAdapter(List<Staff> staffList) {
        this.staffList = staffList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER){
            View view = inflater.inflate(R.layout.item_staff_header,parent,false);
            return new HeaderViewHolder(view);
        }else {
            View view = inflater.inflate(R.layout.item_staff, parent, false);
            return new StaffViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Staff staff = staffList.get(position);

        if (getItemViewType(position) == TYPE_HEADER){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.headerTextView.setText(staff.getJawatan());

        }else {
            StaffViewHolder staffViewHolder = (StaffViewHolder) holder;
            staffViewHolder.bilTextView.setText(staff.getBil());
            staffViewHolder.namaTextView.setText(staff.getNama());
            staffViewHolder.jawatanTextView.setText(staff.getJawatan());
            staffViewHolder.noTeleponTextView.setText(staff.getNoTelefon());
            staffViewHolder.emailTextView.setText(staff.getEmail());
        }

    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @Override
    public int getItemViewType(int position) {
       if (staffList.get(position).isHeader()){
           return TYPE_HEADER;
       }else{
           return TYPE_ITEM;
       }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView headerTextView;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
             headerTextView = itemView.findViewById(R.id.staffHeaderTextView);
        }
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {

        TextView bilTextView;
        TextView namaTextView;
        TextView jawatanTextView;
        TextView noTeleponTextView;
        TextView emailTextView;


        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            bilTextView = itemView.findViewById(R.id.bilTextView);
            namaTextView = itemView.findViewById(R.id.namaTextView);
            jawatanTextView = itemView.findViewById(R.id.jawatanTextView);
            noTeleponTextView = itemView.findViewById(R.id.noTeleponTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }
}