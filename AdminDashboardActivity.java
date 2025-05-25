package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminDashboardActivity extends AppCompatActivity {

    private static final String TAG = "AdminDashboardActivity";
    private Button viewBorrowRequestsButton;
    private Button viewBorrowHistoryButton;
    private TextView pendingRequestsCountTextView;
    private TextView borrowHistoryCountTextView; // إضافة TextView لعدد سجلات الإعارة
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();
        pendingRequestsCountTextView = findViewById(R.id.pendingRequestsCountTextView);
        borrowHistoryCountTextView = findViewById(R.id.borrowHistoryCountTextView); // ربط TextView الجديد
        viewBorrowRequestsButton = findViewById(R.id.viewBorrowRequestsButton);
        viewBorrowHistoryButton = findViewById(R.id.viewBorrowHistoryButton);

        // جلب وعرض عدد طلبات الإعارة المعلقة
        fetchPendingRequestsCount();
        // جلب وعرض عدد سجلات الإعارة
        fetchBorrowHistoryCount(); // <--- تم التأكيد على استدعاء الدالة هنا

        viewBorrowRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminBorrowRequestsActivity.class));
        });

        viewBorrowHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, BorrowHistoryActivity.class));
        });
    }

    private void fetchPendingRequestsCount() {
        Query query = db.collection("borrowedBooks").whereEqualTo("status", "pending");
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            int pendingCount = queryDocumentSnapshots.size();
            pendingRequestsCountTextView.setText(String.valueOf(pendingCount));
        }).addOnFailureListener(e -> {
            Log.e(TAG, "فشل في جلب عدد طلبات الإعارة المعلقة: ", e);
            pendingRequestsCountTextView.setText("خطأ في التحميل");
        });
    }

    private void fetchBorrowHistoryCount() {
        db.collection("borrowedHistory").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int historyCount = queryDocumentSnapshots.size();
            borrowHistoryCountTextView.setText(String.valueOf(historyCount));
        }).addOnFailureListener(e -> {
            Log.e(TAG, "فشل في جلب عدد سجلات الإعارة: ", e);
            borrowHistoryCountTextView.setText("خطأ في التحميل");
        });
    }
}