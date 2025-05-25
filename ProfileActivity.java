package com.manarat.manaratlibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot; // تم إضافة هذا الاستيراد

import java.util.ArrayList;
import java.util.Date; // تم إضافة هذا الاستيراد
import java.util.List;
import java.util.Map; // تم إضافة هذا الاستيراد

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private RecyclerView borrowedBooksRecyclerView;
    private BorrowedBooksAdapter borrowedBooksAdapter;
    private List<BorrowRequest> borrowedBooksList; // **تم التغيير هنا: من Book إلى BorrowRequest**
    private TextView changePasswordTextView;
    private SwitchCompat notificationsSwitch;
    private TextView borrowedCountValueTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String USERS_COLLECTION = "users";
    private static final String FIELD_USERNAME = "displayName";
    private static final String FIELD_EMAIL = "email";
    private static final String PREFS_NAME = "AppSettings";
    private static final String PREF_NOTIFICATIONS_ENABLED = "notificationsEnabled";
    private ListenerRegistration borrowedBooksListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileNameTextView = findViewById(R.id.profile_name);
        profileEmailTextView = findViewById(R.id.profile_email);
        borrowedBooksRecyclerView = findViewById(R.id.borrowed_books_recycler);
        changePasswordTextView = findViewById(R.id.change_password);
        notificationsSwitch = findViewById(R.id.notifications_switch);
        borrowedCountValueTextView = findViewById(R.id.borrowed_count_value);

        changePasswordTextView.setOnClickListener(v -> {
            showCustomChangePasswordDialog();
        });

        // تهيئة القائمة والمحول قبل تحميل البيانات
        borrowedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        borrowedBooksList = new ArrayList<>();
        borrowedBooksAdapter = new BorrowedBooksAdapter(this, borrowedBooksList); // **الآن يطابق النوع**
        borrowedBooksRecyclerView.setAdapter(borrowedBooksAdapter);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            loadUserProfile(userId);
            loadBorrowedBooks(userId); // استدعاء تحميل الكتب بعد تهيئة المحول
        } else {
            profileNameTextView.setText("لم يتم تسجيل الدخول");
            profileEmailTextView.setText("");
            Toast.makeText(this, "يرجى تسجيل الدخول لعرض ملفك الشخصي", Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean areNotificationsEnabled = prefs.getBoolean(PREF_NOTIFICATIONS_ENABLED, true);
        notificationsSwitch.setChecked(areNotificationsEnabled);

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(PREF_NOTIFICATIONS_ENABLED, isChecked);
            editor.apply();

            if (isChecked) {
                Toast.makeText(ProfileActivity.this, "تم تفعيل الإشعارات", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "تم إيقاف الإشعارات", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile(String userId) {
        DocumentReference userRef = db.collection(USERS_COLLECTION).document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String username = documentSnapshot.getString(FIELD_USERNAME);
                String email = documentSnapshot.getString(FIELD_EMAIL);
                profileNameTextView.setText(username != null ? username : "اسم المستخدم غير متوفر");
                profileEmailTextView.setText(email != null ? email : "البريد الإلكتروني غير متوفر");
            } else {
                profileNameTextView.setText("معلومات المستخدم غير متوفرة");
                profileEmailTextView.setText("");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ProfileActivity.this, "حدث خطأ أثناء استرداد معلومات المستخدم", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Error loading user profile", e);
            profileNameTextView.setText("خطأ في التحميل");
            profileEmailTextView.setText("");
        });
    }

    private void loadBorrowedBooks(String userId) {
        Log.d("ProfileActivity", "loadBorrowedBooks called for user: " + userId);
        borrowedBooksListener = db.collection(USERS_COLLECTION)
                .document(userId)
                .collection("borrowed_books") // تأكد من أن هذا هو اسم المجموعة الفرعية الصحيح
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    Log.d("ProfileActivity", "borrowedBooksListener onEvent called");
                    if (e != null) {
                        Log.w("ProfileActivity", "Listen failed.", e);
                        return;
                    }

                    borrowedBooksList.clear();
                    Log.d("ProfileActivity", "borrowedBooksList cleared");
                    if (queryDocumentSnapshots != null) {
                        Log.d("ProfileActivity", "Number of documents in snapshot: " + queryDocumentSnapshots.size());
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            // **الآن نقوم بتحويل المستند إلى كائن BorrowRequest**
                            // تأكد أن حقول Firestore تتطابق مع أسماء حقول BorrowRequest
                            // أو قم بتعيينها يدوياً
                            BorrowRequest borrowRequest = doc.toObject(BorrowRequest.class);

                            // إذا كانت doc.toObject(BorrowRequest.class) لا تعمل بشكل صحيح
                            // بسبب عدم تطابق أسماء الحقول أو أنواعها، يمكنك تعيينها يدوياً:
                            /*
                            BorrowRequest borrowRequest = new BorrowRequest();
                            borrowRequest.setBookId(doc.getString("bookId"));
                            borrowRequest.setBookTitle(doc.getString("bookTitle"));
                            borrowRequest.setUserId(doc.getString("userId"));
                            borrowRequest.setUserName(doc.getString("userName"));
                            borrowRequest.setUserEmail(doc.getString("userEmail"));
                            // Firestore Timestamp to Java Date
                            if (doc.getTimestamp("requestDate") != null) {
                                borrowRequest.setRequestDate(doc.getTimestamp("requestDate").toDate());
                            }
                            borrowRequest.setStatus(doc.getString("status"));
                            // قم بتعيين الحقول الأخرى مثل approvalDate, rejectionDate, returnDate, etc.
                            // بنفس الطريقة إذا كانت موجودة في Firestore
                            */

                            Log.d("ProfileActivity", "Found borrow request: " + (borrowRequest != null ? borrowRequest.getBookTitle() : "BorrowRequest object is null"));
                            if (borrowRequest != null) {
                                borrowedBooksList.add(borrowRequest);
                                Log.d("ProfileActivity", "BorrowRequest added to list: " + borrowRequest.getBookTitle());
                            }
                        }
                        Log.d("ProfileActivity", "borrowedBooksList size: " + borrowedBooksList.size());
                        borrowedBooksAdapter.notifyDataSetChanged();
                        Log.d("ProfileActivity", "borrowedBooksAdapter notified");
                        borrowedCountValueTextView.setText(String.valueOf(borrowedBooksList.size()));
                        Log.d("ProfileActivity", "borrowedCountValueTextView updated: " + borrowedBooksList.size());
                    } else {
                        Log.d("ProfileActivity", "queryDocumentSnapshots is null (no borrowed books)");
                        borrowedCountValueTextView.setText("0");
                        borrowedBooksAdapter.notifyDataSetChanged();
                        Log.d("ProfileActivity", "borrowedCountValueTextView updated to 0");
                        Log.d("ProfileActivity", "borrowedBooksAdapter notified (empty list)");
                    }
                });
        Log.d("ProfileActivity", "borrowedBooksListener attached");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (borrowedBooksListener != null) {
            borrowedBooksListener.remove();
            Log.d("ProfileActivity", "borrowedBooksListener removed");
        }
    }

    private void showCustomChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.custom_change_password_dialog, null);
        final EditText newPasswordInput = viewInflated.findViewById(R.id.new_password_input);
        final EditText confirmNewPasswordInput = viewInflated.findViewById(R.id.confirm_new_password_input);
        Button changeButton = viewInflated.findViewById(R.id.change_button);
        Button cancelButton = viewInflated.findViewById(R.id.cancel_button);

        builder.setView(viewInflated);
        AlertDialog alertDialog = builder.create();

        changeButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmNewPasswordInput.getText().toString();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(ProfileActivity.this, "يرجى إدخال كلمة المرور الجديدة وتأكيدها", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ProfileActivity.this, "كلمة المرور الجديدة وتأكيد كلمة المرور غير متطابقتين", Toast.LENGTH_SHORT).show();
                return;
            }

            updatePassword(newPassword);
            alertDialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        alertDialog.show();
    }

    private void updatePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileActivity", "تم تحديث كلمة المرور بنجاح");
                            Toast.makeText(ProfileActivity.this, "تم تحديث كلمة المرور بنجاح", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("ProfileActivity", "فشل تحديث كلمة المرور", task.getException());
                            Toast.makeText(ProfileActivity.this, "فشل تحديث كلمة المرور: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(ProfileActivity.this, "لم يتم تسجيل الدخول. لا يمكن تحديث كلمة المرور.", Toast.LENGTH_SHORT).show();
        }
    }
}
