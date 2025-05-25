package com.manarat.manaratlibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionViewHolder> {

    private List<SuggestionItem> suggestionList; // Changed to List<SuggestionItem>
    private OnSuggestionClickListener listener; // واجهة للتعامل مع نقرات الاقتراحات

    public interface OnSuggestionClickListener {
        void onSuggestionClick(SuggestionItem suggestion); // Changed to SuggestionItem
    }

    public SuggestionsAdapter(List<SuggestionItem> suggestionList) { // Changed to List<SuggestionItem>
        this.suggestionList = suggestionList;
    }

    public void setOnSuggestionClickListener(OnSuggestionClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<SuggestionItem> newList) { // Changed to List<SuggestionItem>
        suggestionList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        SuggestionItem currentItem = suggestionList.get(position); // Get SuggestionItem
        holder.suggestionTitleTextView.setText(currentItem.getTitle()); // Set title
        holder.suggestionAuthorTextView.setText(currentItem.getAuthor()); // Set author

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuggestionClick(currentItem); // Pass the SuggestionItem
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        public TextView suggestionTitleTextView;
        public TextView suggestionAuthorTextView;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionTitleTextView = itemView.findViewById(R.id.suggestion_title);
            suggestionAuthorTextView = itemView.findViewById(R.id.suggestion_author);
        }
    }
}