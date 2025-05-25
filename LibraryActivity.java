package com.manarat.manaratlibrary;

import android.content.Intent; // تأكد من استيراد Intent
import android.os.Bundle;
import android.view.View; // تأكد من استيراد View
import androidx.appcompat.app.AppCompatActivity;

public class LibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        // لا تنسَ إضافة أي منطق آخر خاص بـ LibraryActivity هنا
    }

    // هذه الدالة سيتم استدعاؤها عند النقر على أي بطاقة فئة
    public void openCategory(View view) {
        // الحصول على اسم الفئة من الـ tag الخاص بالـ CardView
        String categoryName = (String) view.getTag();

        // إنشاء Intent لفتح CategoryBooksActivity
        Intent intent = new Intent(this, CategoryBooksActivity.class);
        // تمرير اسم الفئة إلى الـ Activity الجديد
        intent.putExtra(CategoryBooksActivity.CATEGORY_NAME_KEY, categoryName);
        startActivity(intent); // بدء الـ Activity الجديد
    }
}