package com.manarat.manaratlibrary;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class BorrowHistoryAdapter extends FirestoreRecyclerAdapter<BorrowRequest, BorrowHistoryAdapter.BorrowHistoryViewHolder> {

    private static final String TAG = "BorrowHistoryAdapter";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BorrowHistoryAdapter(@NonNull FirestoreRecyclerOptions<BorrowRequest> options) {
        super(options);
    }

    public static class BorrowHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView historyBookTitleTextView;
        TextView historyUserNameTextView;
        TextView historyUserEmailTextView;
        TextView historyApprovalRejectionDateTextView;
        TextView historyStatusTextView;
        TextView historyBorrowDurationTextView;
        TextView historyBorrowerRatingTextView;
        TextView historyReturnDateTextView;
        TextView historyReturnStatusTextView;
        Button returnBookButton;

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
            historyReturnStatusTextView = itemView.findViewById(R.id.historyReturnStatusTextView);
            returnBookButton = itemView.findViewById(R.id.returnBookButton);
        }
    }

    @NonNull
    @Override
    public BorrowHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_history, parent, false);
        return new BorrowHistoryViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BorrowHistoryViewHolder holder, int position, @NonNull BorrowRequest model) {
        holder.historyBookTitleTextView.setText(model.getBookTitle());
        holder.historyUserNameTextView.setText("اسم المستخدم: " + model.getUserName());
        holder.historyUserEmailTextView.setText("الإيميل: " + model.getUserEmail());

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("ar"));
        if (model.getApprovalDate() != null) {
            holder.historyApprovalRejectionDateTextView.setText("تاريخ الموافقة: " + df.format(model.getApprovalDate()));
        } else if (model.getRejectionDate() != null) {
            holder.historyApprovalRejectionDateTextView.setText("تاريخ الرفض: " + df.format(model.getRejectionDate()));
        } else {
            holder.historyApprovalRejectionDateTextView.setText("تاريخ غير محدد");
        }

        holder.historyStatusTextView.setText("الحالة: " + model.getStatus());

        if (model.getBorrowDuration() != null) {
            holder.historyBorrowDurationTextView.setText("مدة الاستعارة: " + model.getBorrowDuration() + " أيام");
        } else {
            holder.historyBorrowDurationTextView.setText("مدة الاستعارة: غير محدد");
        }

        if (model.getBorrowerRating() != null && !model.getBorrowerRating().isEmpty()) {
            holder.historyBorrowerRatingTextView.setText("تقييم المستعير: " + model.getBorrowerRating());
        } else {
            holder.historyBorrowerRatingTextView.setText("تقييم المستعير: غير متوفر");
        }

        if (model.getReturnDate() != null) {
            holder.historyReturnDateTextView.setText("تاريخ الإرجاع: " + df.format(model.getReturnDate()));
        } else {
            holder.historyReturnDateTextView.setText("تاريخ الإرجاع: لم يتم الإرجاع");
        }

        if (model.getReturnStatus() != null && !model.getReturnStatus().isEmpty()) {
            holder.historyReturnStatusTextView.setText("حالة الإرجاع: " + model.getReturnStatus());
            if (model.getReturnStatus().equals("تم الإرجاع")) {
                holder.historyReturnStatusTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.returned_color));
            } else {
                holder.historyReturnStatusTextView.setTextColor(Color.BLACK);
            }
        } else {
            holder.historyReturnStatusTextView.setText("حالة الإرجاع: لم يتم الإرجاع");
            holder.historyReturnStatusTextView.setTextColor(Color.RED);
        }

        Log.d(TAG, "Binding item with Return Button - Book ID: " + model.getBookId() + ", User ID: " + model.getUserId());

        if (model.getStatus().equals("approved")) {
            holder.returnBookButton.setVisibility(View.VISIBLE);
            holder.returnBookButton.setOnClickListener(v -> {
                returnBook(model.getBookId(), model.getUserId(), getSnapshots().getSnapshot(position).getId(), holder.itemView);
            });
        } else {
            holder.returnBookButton.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            BorrowRequest selectedHistoryItem = model;
            Intent detailsIntent = new Intent(holder.itemView.getContext(), BorrowHistoryDetailsActivity.class);
            detailsIntent.putExtra("borrowHistoryItem", selectedHistoryItem);
            holder.itemView.getContext().startActivity(detailsIntent);
        });
    }

    private void returnBook(String bookId, String userId, String historyDocId, View itemView) {
        Log.d(TAG, "returnBook called with - Book ID: " + bookId + ", User ID: " + userId + ", History Doc ID: " + historyDocId);

        db.collection("borrowedBooks")
                .whereEqualTo("bookId", bookId)
                .whereEqualTo("userId", userId)
                // .whereEqualTo("status", "approved") // <-- تم تعليق هذا الشرط مؤقتًا
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "تم العثور على " + queryDocumentSnapshots.size() + " كتاب (أو أكثر) مطابق بالـ Book ID والمستخدم ID.");
                    if (!queryDocumentSnapshots.isEmpty()) {
                        boolean foundApproved = false;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            BorrowRequest borrowedBook = document.toObject(BorrowRequest.class);
                            // تم تعليق هذا الشرط مؤقتًا
                            // if (borrowedBook != null && borrowedBook.getStatus().equals("approved")) {
                            foundApproved = true;
                            DocumentReference borrowedBookRef = document.getReference();
                            borrowedBookRef.update("status", "returned", "returnDate", new Date())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "تم تحديث حالة الكتاب في borrowedBooks إلى 'returned' وتاريخ الإرجاع.");
                                        updateHistoryAndBookAvailability(bookId, historyDocId, itemView);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "فشل في تحديث حالة الكتاب في borrowedBooks: ", e);
                                        Toast.makeText(itemView.getContext(), "فشل في تحديث حالة الكتاب المعار.", Toast.LENGTH_SHORT).show();
                                    });
                            break;
                            // }
                        }
                        if (!foundApproved) {
                            Log.w(TAG, "لم يتم العثور على كتاب معار بحالة 'approved' بالـ Book ID: " + bookId + " والمستخدم ID: " + userId);
                            Toast.makeText(itemView.getContext(), "لم يتم العثور على كتاب معار نشط.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "لم يتم العثور على كتاب مطابق بالـ Book ID: " + bookId + " والمستخدم ID: " + userId + " في borrowedBooks.");
                        Toast.makeText(itemView.getContext(), "لم يتم العثور على الكتاب المعار.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في البحث عن الكتاب المعار: ", e);
                    Toast.makeText(itemView.getContext(), "حدث خطأ أثناء البحث عن الكتاب المعار.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateHistoryAndBookAvailability(String bookId, String historyDocId, View itemView) {
        DocumentReference historyRef = db.collection("borrowedHistory").document(historyDocId);
        historyRef.update("returnDate", new Date(), "status", "returned", "returnStatus", "تم الإرجاع")
                .addOnSuccessListener(aVoid1 -> {
                    Log.d(TAG, "تم تحديث سجل الإعارة في borrowedHistory.");
                    db.collection("books").document(bookId)
                            .update("available", true)
                            .addOnSuccessListener(aVoid2 -> {
                                Log.d(TAG, "تم تحديث حالة توفر الكتاب في books إلى true.");
                                Toast.makeText(itemView.getContext(), "تم تسجيل إرجاع الكتاب بنجاح.", Toast.LENGTH_SHORT).show();
                                // تحديث واجهة المستخدم لإظهار حالة الإرجاع
                                // يمكنك استدعاء notifyDataSetChanged() هنا أو طريقة أخرى لتحديث العنصر المرئي
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "فشل في تحديث حالة توفر الكتاب: ", e);
                                Toast.makeText(itemView.getContext(), "فشل في تحديث حالة توفر الكتاب.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في تحديث سجل الإعارة: ", e);
                    Toast.makeText(itemView.getContext(), "فشل في تحديث سجل الإعارة.", Toast.LENGTH_SHORT).show();
                });
    }
}