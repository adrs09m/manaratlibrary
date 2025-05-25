package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private TextView profileSettings;
    private TextView notificationsSettings;
    private TextView aboutUs;
    private TextView contactUs;
    private TextView logout;
    private TextView adminPanel; // العنصر الجديد للوصول إلى لوحة المشرف
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        profileSettings = findViewById(R.id.profileSettingsTextView);
        notificationsSettings = findViewById(R.id.notificationsSettingsTextView);
        aboutUs = findViewById(R.id.aboutUsTextView);
        contactUs = findViewById(R.id.contactUsTextView);
        logout = findViewById(R.id.logoutTextView);
        adminPanel = findViewById(R.id.adminPanelTextView); // ربط العنصر الجديد

        profileSettings.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        });

        notificationsSettings.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "إعدادات الإشعارات قيد التطوير", Toast.LENGTH_SHORT).show();
        });

        aboutUs.setOnClickListener(v -> {
            Intent aboutUsIntent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(aboutUsIntent);
        });

        contactUs.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ContactActivity.class));
        });

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finish();
        });

        // مستمع النقر لعنصر لوحة المشرف
        adminPanel.setOnClickListener(v -> {
            openAdminPanel();
        });

        // التحقق من دور المستخدم لإظهار/إخفاء خيار لوحة المشرف
        checkAdminRole();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                currentUser = user;
                Log.d("SettingsActivity", "AuthStateListener: currentUser updated = " + currentUser.getUid());
                checkAdminRole(); // استدعاء checkAdminRole عند تحديث حالة المستخدم
            } else {
                currentUser = null;
                Log.d("SettingsActivity", "AuthStateListener: currentUser is null");
                adminPanel.setVisibility(View.GONE); // إخفاء لوحة المشرف عند تسجيل الخروج
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void checkAdminRole() {
        if (currentUser != null) {
            Log.d("SettingsActivity", "checkAdminRole: currentUser UID = " + currentUser.getUid());
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d("SettingsActivity", "checkAdminRole: documentSnapshot exists = " + documentSnapshot.exists());
                        if (documentSnapshot.exists()) {
                            String role = documentSnapshot.getString("role");
                            Log.d("SettingsActivity", "checkAdminRole: user role = " + role);
                            if ("admin".equals(role)) {
                                adminPanel.setVisibility(View.VISIBLE); // إظهار خيار لوحة المشرف للمسؤولين
                            } else {
                                adminPanel.setVisibility(View.GONE); // إخفاء خيار لوحة المشرف للمستخدمين العاديين
                            }
                        } else {
                            adminPanel.setVisibility(View.GONE); // إخفاء الخيار إذا لم يتم العثور على الوثيقة
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("SettingsActivity", "checkAdminRole: فشل في التحقق من دور المستخدم: ", e);
                        adminPanel.setVisibility(View.GONE); // إخفاء الخيار في حالة الفشل
                    });
        } else {
            Log.d("SettingsActivity", "checkAdminRole: currentUser is null");
            adminPanel.setVisibility(View.GONE); // إخفاء الخيار إذا لم يكن هناك مستخدم مسجل للدخول
        }
    }

    private void openAdminPanel() {
        Log.d("SettingsActivity", "openAdminPanel: بدء الدالة");
        Log.d("SettingsActivity", "openAdminPanel: قيمة currentUser قبل التحقق: " + currentUser);
        if (currentUser != null) {
            Log.d("SettingsActivity", "openAdminPanel: currentUser UID = " + currentUser.getUid());
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d("SettingsActivity", "openAdminPanel: نجح استرداد بيانات المستخدم");
                        Log.d("SettingsActivity", "openAdminPanel: documentSnapshot موجود؟ " + documentSnapshot.exists());
                        if (documentSnapshot.exists()) {
                            String role = documentSnapshot.getString("role");
                            Log.d("SettingsActivity", "openAdminPanel: دور المستخدم: " + role);
                            if ("admin".equals(role)) {
                                // تم التعديل هنا لفتح AdminDashboardActivity
                                startActivity(new Intent(SettingsActivity.this, AdminDashboardActivity.class));
                            } else {
                                Toast.makeText(SettingsActivity.this, "لا تملك صلاحيات المسؤول.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SettingsActivity.this, "لم يتم العثور على معلومات المستخدم.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("SettingsActivity", "openAdminPanel: فشل في استرداد بيانات المستخدم: ", e);
                        Toast.makeText(SettingsActivity.this, "حدث خطأ أثناء التحقق من صلاحيات المسؤول.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.d("SettingsActivity", "openAdminPanel: currentUser فارغ!");
            Toast.makeText(SettingsActivity.this, "تم فقدان حالة تسجيل الدخول. يرجى تسجيل الدخول مرة أخرى.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finish();
        }
    }
}