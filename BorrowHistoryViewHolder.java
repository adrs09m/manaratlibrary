package com.manarat.manaratlibrary; // تأكد أن هذا هو اسم الحزمة الصحيح لتطبيقك

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BorrowHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView historyBookTitleTextView;
    public TextView historyUserNameTextView;
    public TextView historyUserEmailTextView;
    public TextView historyApprovalRejectionDateTextView;
    public TextView historyStatusTextView;
    public TextView historyBorrowDurationTextView;
    public TextView historyBorrowerRatingTextView;
    public TextView historyReturnDateTextView;

    public BorrowHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        historyBookTitleTextView = itemView.findViewById(R.id.historyBookTitleTextView);
        historyUserNameTextView = itemView.findViewById(R.id.historyUserNameTextView);
        historyUserEmailTextView = itemView.findViewById(R.id.historyUserEmailTextView);
        historyApprovalRejectionDateTextView = itemView.findViewById(R.id.historyApprovalRejectionDateTextView);
        historyStatusTextView = itemView.findViewById(R.id.historyStatusTextView);
        historyBorrowDurationTextView = itemView.findViewById(R.id.historyBorrowDurationTextView);
        historyBorrowerRatingTextView = itemView.findViewById(R.id.historyBorrowerRatingTextView);
        historyReturnDateTextView = itemView.findViewById(R.id.historyReturnDateTextView);
    }
}