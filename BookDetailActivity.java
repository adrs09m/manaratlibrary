package com.manarat.manaratlibrary;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView;
    private TextView authorView;
    private TextView statusView;
    private TextView descriptionView;
    private TextView pagesView;
    private TextView publishDateView;
    private TextView publisherView;
    private TextView categoryView;
    private Button borrowButton;

    private String bookId;
    private String bookTitle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LocalDatabaseHelper localDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        localDbHelper = new LocalDatabaseHelper(this);

        imageView = findViewById(R.id.detail_book_image);
        titleView = findViewById(R.id.detail_book_title);
        authorView = findViewById(R.id.detail_book_author);
        statusView = findViewById(R.id.detail_book_status);
        descriptionView = findViewById(R.id.detail_book_description);
        pagesView = findViewById(R.id.detail_book_pages);
        publishDateView = findViewById(R.id.detail_book_publish_date);
        publisherView = findViewById(R.id.detail_book_publisher);
        categoryView = findViewById(R.id.detail_book_category);
        borrowButton = findViewById(R.id.borrow_button);

        Intent intent = getIntent();
        bookTitle = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String imageUrl = intent.getStringExtra("imageUrl");
        int imageRes = intent.getIntExtra("image", R.drawable.ic_book_default);
        boolean available = intent.getBooleanExtra("available", true);
        String description = intent.getStringExtra("description");
        int pages = intent.getIntExtra("pages", 0);
        String publishDate = intent.getStringExtra("publishDate");
        String publisher = intent.getStringExtra("publisher");
        String category = intent.getStringExtra("category");
        bookId = intent.getStringExtra("bookId");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(imageView);
        } else {
            imageView.setImageResource(imageRes);
        }

        titleView.setText(bookTitle);
        authorView.setText(author);

        if (available) {
            statusView.setText("متاح");
            statusView.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        } else {
            statusView.setText("معار");
            statusView.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        descriptionView.setText(description != null ? description : "لا يوجد وصف لهذا الكتاب.");
        pagesView.setText("عدد الصفحات: " + pages);
        publishDateView.setText("تاريخ النشر: " + (publishDate != null ? publishDate : "-"));
        publisherView.setText("الناشر: " + (publisher != null ? publisher : "-"));
        categoryView.setText("التصنيف: " + (category != null ? category : "-"));

        borrowButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                if (isNetworkAvailable()) {
                    recordBorrowOnline(userId, bookId, bookTitle);
                } else {
                    recordBorrowOffline(userId, bookId, bookTitle);
                }
            } else {
                Toast.makeText(BookDetailActivity.this, "يجب عليك تسجيل الدخول أولاً للاستعارة", Toast.LENGTH_SHORT).show();
                // يمكنك هنا فتح صفحة تسجيل الدخول
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void recordBorrowOnline(String userId, String bookId, String bookTitle) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // **بافتراض أن اسم حقل البريد الإلكتروني هو "email"**
                            String userEmail = documentSnapshot.getString("email");
                            Log.d("BorrowRequest", "Retrieved User Email: " + userEmail);

                            if (userEmail != null) {
                                Map<String, Object> borrowRequest = new HashMap<>();
                                borrowRequest.put("userId", userId);
                                borrowRequest.put("bookId", bookId);
                                borrowRequest.put("bookTitle", bookTitle);
                                borrowRequest.put("requestDate", new Date());
                                borrowRequest.put("status", "pending");
                                borrowRequest.put("userName", userEmail); // استخدام البريد الإلكتروني هنا
                                db.collection("borrowedBooks")
                                        .add(borrowRequest)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(BookDetailActivity.this, "تم إرسال طلب الاستعارة بنجاح. سيتم إعلامك بعد مراجعة المشرف.", Toast.LENGTH_LONG).show();
                                            // يمكنك هنا تعطيل زر الاستعارة أو تغيير حالته لمنع تكرار الطلب
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("BorrowRequest", "Error adding borrow request: " + e.getMessage());
                                            Toast.makeText(BookDetailActivity.this, "حدث خطأ أثناء إرسال طلب الاستعارة: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Log.w("BorrowRequest", "User email is null for user ID: " + userId);
                                Toast.makeText(BookDetailActivity.this, "لم يتم العثور على البريد الإلكتروني للمستخدم.", Toast.LENGTH_SHORT).show();
                                // يمكنك هنا معالجة هذه الحالة بشكل مختلف إذا لزم الأمر
                            }
                        } else {
                            Log.w("BorrowRequest", "User document does not exist for user ID: " + userId);
                            Toast.makeText(BookDetailActivity.this, "لم يتم العثور على بيانات المستخدم.", Toast.LENGTH_SHORT).show();
                            // يمكنك هنا معالجة هذه الحالة بشكل مختلف إذا لزم الأمر
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BorrowRequest", "Error fetching user document: " + e.getMessage());
                        Toast.makeText(BookDetailActivity.this, "فشل في جلب بيانات المستخدم: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // يمكنك هنا معالجة هذا الخطأ بشكل مختلف إذا لزم الأمر
                    });
        } else {
            Toast.makeText(BookDetailActivity.this, "يجب تسجيل الدخول أولاً.", Toast.LENGTH_SHORT).show();
        }
    }

    private void recordBorrowOffline(String userId, String bookId, String bookTitle) {
        // في حالة عدم الاتصال بالإنترنت، مش هيكون عندنا اسم المستخدم بسهولة
        // ممكن نخزن الـ userId مؤقتًا ونحاول نجيب الاسم لما يحصل اتصال ونحدث قاعدة البيانات المحلية
        long result = localDbHelper.addBorrowedBook(userId, bookId, bookTitle, new Date().getTime(), "pending", 0); // 0 للدلالة على أنها لم تتم مزامنته بعد
        if (result > 0) {
            Toast.makeText(BookDetailActivity.this, "تم حفظ طلب الاستعارة محليًا. سيتم المزامنة عند الاتصال بالإنترنت.", Toast.LENGTH_LONG).show();
            // يمكنك هنا تغيير حالة الزر أو تعطيله مؤقتًا
        } else {
            Toast.makeText(BookDetailActivity.this, "حدث خطأ أثناء حفظ طلب الاستعارة محليًا.", Toast.LENGTH_SHORT).show();
        }
        // **تذكر:** ستحتاج إلى آلية منفصلة لمزامنة البيانات المحلية مع Firestore عند الاتصال بالإنترنت.
    }
}