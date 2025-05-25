package com.manarat.manaratlibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {

    private List<Book> resultsList; // استخدم كلاس الـ Book أو أي كلاس يمثل بيانات نتائج البحث

    public ResultsAdapter(List<Book> resultsList) {
        this.resultsList = resultsList;
    }

    public void updateList(List<Book> newList) {
        resultsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false); // تحتاج لإنشاء item_result.xml
        return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Book result = resultsList.get(position);
        holder.resultTitleTextView.setText(result.getTitle());
        holder.resultAuthorTextView.setText(result.getAuthor());
        // قم بربط أي بيانات أخرى بنتيجة البحث هنا
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public TextView resultTitleTextView;
        public TextView resultAuthorTextView;
        // قم بتعريف أي Views أخرى موجودة في item_result.xml هنا

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultTitleTextView = itemView.findViewById(R.id.result_title); // تحتاج لتعديل الـ ID ليناسب item_result.xml
            resultAuthorTextView = itemView.findViewById(R.id.result_author); // تحتاج لتعديل الـ ID ليناسب item_result.xml
            // قم بتهيئة أي Views أخرى هنا
        }
    }
}