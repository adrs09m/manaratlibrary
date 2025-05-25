package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword, etFullName, etStudyLevel, etClassNumber;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // إضافة مثيل FirebaseFirestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.fullNameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        etStudyLevel = findViewById(R.id.studyLevelEditText);
        etClassNumber = findViewById(R.id.classNumberEditText);
        Button btnRegister = findViewById(R.id.registerButton);
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // تهيئة مثيل FirebaseFirestore

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String studyLevel = etStudyLevel.getText().toString().trim();
            String classNumber = etClassNumber.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || studyLevel.isEmpty() || classNumber.isEmpty()) {
                Toast.makeText(this, "يرجى ملء كل الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "كلمتا المرور غير متطابقتين", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("RegisterActivity", "Attempting to create user with email: " + email);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Log.d("RegisterActivity", "createUserWithEmailAndPassword:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            Log.d("RegisterActivity", "User UID obtained: " + userId);

                            // إنشاء خريطة لتخزين بيانات المستخدم في Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email); // تخزين البريد الإلكتروني
                            userData.put("displayName", fullName); // تخزين اسم المستخدم
                            userData.put("studyLevel", studyLevel); // تخزين المرحلة الدراسية
                            userData.put("classNumber", classNumber); // تخزين الفصل الدراسي
                            Log.d("RegisterActivity", "Attempting to save user data to Firestore: " + userData.toString());

                            // إنشاء مستند جديد في مجموعة "users" باستخدام الـ userId كمُعرّف
                            db.collection("users").document(userId)
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("RegisterActivity", "Firestore user document created successfully for: " + userId);
                                        Toast.makeText(this, "تم إنشاء الحساب وبيانات المستخدم بنجاح", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("RegisterActivity", "Error creating Firestore user document", e);
                                        Toast.makeText(this, "فشل حفظ بيانات المستخدم: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        // يمكنك هنا حذف حساب Authentication الذي تم إنشاؤه إذا فشل حفظ البيانات
                                    });
                        } else {
                            Log.w("RegisterActivity", "createUserWithEmailAndPassword: success, but user is null");
                            Toast.makeText(this, "حدث خطأ في الحصول على معلومات المستخدم بعد التسجيل.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("RegisterActivity", "createUserWithEmailAndPassword:failure", e);
                        Toast.makeText(this, "فشل إنشاء الحساب: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        alreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}