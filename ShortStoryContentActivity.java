package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ShortStoryContentActivity extends AppCompatActivity {

    private Button writeStoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_story_content);

        writeStoryButton = findViewById(R.id.writeStoryButton);
        writeStoryButton.setOnClickListener(v -> {
            // عند النقر على زر "اكتب قصتك الآن"، انتقل إلى نشاط كتابة القصة
            Intent intent = new Intent(ShortStoryContentActivity.this, WriteShortStoryActivity.class);
            startActivity(intent);
        });

        // يمكنك هنا إضافة أي منطق إضافي لتحميل بيانات المسابقة ديناميكيًا
        // مثل الموعد النهائي والجوائز من مصدر بيانات.
    }
}