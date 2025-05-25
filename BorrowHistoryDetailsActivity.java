package com.manarat.manaratlibrary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Locale;

public class BorrowHistoryDetailsActivity extends AppCompatActivity {

    TextView bookTitleTextViewDetails;
    TextView userNameTextViewDetails;
    TextView userEmailTextViewDetails;
    TextView requestDateTextViewDetails;
    TextView approvalRejectionDateTextViewDetails;
    TextView statusTextViewDetails;
    TextView borrowDurationTextViewDetails;
    TextView borrowerRatingTextViewDetails;
    TextView returnDateTextViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_history_details);

        // تهيئة عناصر الواجهة المستخدمة
        bookTitleTextViewDetails = findViewById(R.id.bookTitleTextViewDetails);
        userNameTextViewDetails = findViewById(R.id.userNameTextViewDetails);
        userEmailTextViewDetails = findViewById(R.id.userEmailTextViewDetails);
        requestDateTextViewDetails = findViewById(R.id.requestDateTextViewDetails);
        approvalRejectionDateTextViewDetails = findViewById(R.id.approvalRejectionDateTextViewDetails);
        statusTextViewDetails = findViewById(R.id.statusTextViewDetails);
        borrowDurationTextViewDetails = findViewById(R.id.borrowDurationTextViewDetails);
        borrowerRatingTextViewDetails = findViewById(R.id.borrowerRatingTextViewDetails);
        returnDateTextViewDetails = findViewById(R.id.returnDateTextViewDetails);

        // استعادة بيانات BorrowRequest
        BorrowRequest historyItem = (BorrowRequest) getIntent().getSerializableExtra("borrowHistoryItem");

        if (historyItem != null) {
            // عرض البيانات
            bookTitleTextViewDetails.setText(historyItem.getBookTitle());
            userNameTextViewDetails.setText(historyItem.getUserName());
            userEmailTextViewDetails.setText(historyItem.getUserEmail());

            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("ar"));

            if (historyItem.getRequestDate() != null) {
                requestDateTextViewDetails.setText("تاريخ الطلب: " + df.format(historyItem.getRequestDate()));
            } else {
                requestDateTextViewDetails.setText("تاريخ الطلب: غير محدد");
            }

            if (historyItem.getApprovalDate() != null) {
                approvalRejectionDateTextViewDetails.setText("تاريخ الموافقة: " + df.format(historyItem.getApprovalDate()));
            } else if (historyItem.getRejectionDate() != null) {
                approvalRejectionDateTextViewDetails.setText("تاريخ الرفض: " + df.format(historyItem.getRejectionDate()));
            } else {
                approvalRejectionDateTextViewDetails.setText("تاريخ الموافقة/الرفض: غير محدد");
            }

            statusTextViewDetails.setText("الحالة: " + historyItem.getStatus());

            // قم بتعيين باقي البيانات هنا إذا كانت موجودة في فئة BorrowRequest
            // مثال:
            // borrowDurationTextViewDetails.setText("مدة الاستعارة: " + historyItem.getBorrowDuration() + " أيام");
            // returnDateTextViewDetails.setText("تاريخ الإرجاع: " + (historyItem.getReturnDate() != null ? df.format(historyItem.getReturnDate()) : "غير محدد"));
            // borrowerRatingTextViewDetails.setText("تقييم المستعير: " + historyItem.getBorrowerRating());
        }
    }
}