package com.manarat.manaratlibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CulturalActivityAdapter extends RecyclerView.Adapter<CulturalActivityAdapter.ViewHolder> {

    private List<CulturalActivityItem> culturalActivitiesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CulturalActivityAdapter(List<CulturalActivityItem> culturalActivitiesList) {
        this.culturalActivitiesList = culturalActivitiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cultural_activity, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CulturalActivityItem item = culturalActivitiesList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());
        holder.descriptionTextView.setText(item.getDescription());
        holder.thumbnailImageView.setImageResource(item.getImageResourceId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return culturalActivitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.activity_image);
            titleTextView = itemView.findViewById(R.id.activity_title);
            dateTextView = itemView.findViewById(R.id.activity_date);
            descriptionTextView = itemView.findViewById(R.id.activity_description);
        }
    }
}