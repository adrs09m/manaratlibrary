package com.manarat.manaratlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class StudentArtAdapter extends RecyclerView.Adapter<StudentArtAdapter.ViewHolder> {

    private Context context;
    private List<StudentArtItem> studentArtList;

    public StudentArtAdapter(Context context, List<StudentArtItem> studentArtList) {
        this.context = context;
        this.studentArtList = studentArtList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_art, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentArtItem currentItem = studentArtList.get(position);
        Picasso.get().load(currentItem.getImageUri()).into(holder.artImageView);
        if (currentItem.getStudentName() != null && !currentItem.getStudentName().isEmpty()) {
            holder.studentNameTextView.setVisibility(View.VISIBLE);
            holder.studentNameTextView.setText(currentItem.getStudentName());
        } else {
            holder.studentNameTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return studentArtList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView artImageView;
        TextView studentNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artImageView = itemView.findViewById(R.id.artImageView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
        }
    }
}