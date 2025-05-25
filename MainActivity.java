package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static boolean isGuest = false; // مؤشر وضع الزائر

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // ✅ تهيئة Firebase بشكل صحيح
        FirebaseApp.initializeApp(this);

        // لا تتصل بـ Firebase إذا المستخدم زائر
        if (!isGuest) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference messageRef = database.getReference("welcome_message");
            messageRef.setValue("مرحبًا بك في تطبيق مكتبة منارات");
            Toast.makeText(this, "أهلاً بك في منارة المكتبات المدرسية", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "أنت الآن في وضع الزائر – بعض الميزات غير مفعّلة", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    // الدوال التسعة لفتح الأقسام
    public void openLibrary(View view) {
        startActivity(new Intent(this, LibraryActivity.class));
    }

    public void openCultural(View view) {
        startActivity(new Intent(this, CulturalActivity.class));
    }

    public void openBorrow(View view) {
        startActivity(new Intent(this, BorrowBooksActivity.class));
    }

    public void openFriends(View view) {
        startActivity(new Intent(this, FriendsActivity.class));
    }

    public void openSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void openGuide(View view) {
        startActivity(new Intent(this, GuideActivity.class));
    }

    public void openSkills(View view) {
        startActivity(new Intent(this, SkillsActivity.class));
    }

    public void openSupport(View view) {
        startActivity(new Intent(this, SupportActivity.class));
    }

    public void openContact(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }

    // دالة فتح شاشة لوحة المشرف (تم نقلها لشاشة الإعدادات)
    // public void openAdminPanel(View view) {
    //     startActivity(new Intent(this, AdminBorrowRequestsActivity.class));
    // }
}