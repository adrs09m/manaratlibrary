package com.manarat.manaratlibrary;

import android.content.Context;
import android.content.Intent; // تم إضافة هذا الاستيراد
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat; // تم إضافة هذا الاستيراد
import java.util.List;
import java.util.Locale; // تم إضافة هذا الاستيراد

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.BorrowedBookViewHolder> {

    private Context context;
    private List<BorrowRequest> borrowedBooksList; // **تم التغيير هنا: من Book إلى BorrowRequest**

    public BorrowedBooksAdapter(Context context, List<BorrowRequest> borrowedBooksList) { // **وتم التغيير هنا أيضاً**
        this.context = context;
        this.borrowedBooksList = borrowedBooksList;
    }

    @NonNull
    @Override
    public BorrowedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new BorrowedBookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedBookViewHolder holder, int position) {
        BorrowRequest borrowRequest = borrowedBooksList.get(position); // **تم التغيير هنا: من Book إلى BorrowRequest**

        holder.bookTitleTextView.setText(borrowRequest.getBookTitle()); // استخدام getBookTitle من BorrowRequest

        // تنسيق تاريخ الاستعارة (requestDate)
        if (borrowRequest.getRequestDate() != null) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("ar")); // تنسيق قصير وباللغة العربية
            holder.borrowDateTextView.setText("تاريخ الاستعارة: " + df.format(borrowRequest.getRequestDate()));
        } else {
            holder.borrowDateTextView.setText("تاريخ الاستعارة: غير محدد");
        }

        // حاليًا، لا يوجد imageUrl في BorrowRequest مباشرة.
        // إذا كنت تخطط لعرض صورة الكتاب هنا، ستحتاج إلى جلبها من Firestore باستخدام bookId من BorrowRequest،
        // أو إضافة حقل imageUrl إلى BorrowRequest نفسه.
        // لاستخدام الصورة الافتراضية:
        holder.bookCoverImageView.setImageResource(R.drawable.ic_book_placeholder); // استخدام صورة افتراضية مؤقتًا

        holder.itemView.setOnClickListener(v -> {
            // يمكنك هنا فتح شاشة تفاصيل طلب الإعارة أو الكتاب المرتبط
            // بما أن لديك كائن BorrowRequest كاملاً، يمكنك تمريره
            // تأكد من أن BorrowRequest يطبق واجهة Serializable أو Parcelable إذا كنت ستمرره عبر Intent
            // Intent detailsIntent = new Intent(context, BorrowHistoryDetailsActivity.class);
            // detailsIntent.putExtra("borrowHistoryItem", borrowRequest);
            // context.startActivity(detailsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return borrowedBooksList.size();
    }

    public static class BorrowedBookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;
        TextView borrowDateTextView;

        public BorrowedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCoverImageView = itemView.findViewById(R.id.book_cover);
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            borrowDateTextView = itemView.findViewById(R.id.borrow_date);
        }
    }
}
