package com.manarat.manaratlibrary;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ArtExhibitionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_exhibition_details);

        TextView titleTextView = findViewById(R.id.artExhibitionDetailsTitleTextView);
        TextView introTextView = findViewById(R.id.artExhibitionDetailsIntroTextView);
        RecyclerView studentArtRecyclerView = findViewById(R.id.studentArtRecyclerView);

        // يمكنك تعيين نص المقدمة هنا أو جلبه من strings.xml
        introTextView.setText("نرحب بكم في معرض الفنون الذي يعرض إبداعات طلابنا الموهوبين وأعمالهم الفنية المتميزة. استمتعوا بمشاهدة مواهبهم المتنوعة.");

        // استبدل هذه القائمة ببيانات حقيقية لأعمال الطلاب (مسارات صور URI وأسماء الطلاب والسنة والفصل)
        List<StudentArtItem> studentArtList = new ArrayList<>();
        studentArtList.add(new StudentArtItem(Uri.parse("content://com.android.providers.media.documents/document/image%3A10"), "الطالب أ", "السنة الدراسية أ", "الفصل أ"));
        studentArtList.add(new StudentArtItem(Uri.parse("content://com.android.providers.media.documents/document/image%3A11"), "الطالب ب", "السنة الدراسية ب", "الفصل ب"));
        studentArtList.add(new StudentArtItem(Uri.parse("content://com.android.providers.media.documents/document/image%3A12"), "الطالب ج", "السنة الدراسية ج", "الفصل ج"));
        studentArtList.add(new StudentArtItem(Uri.parse("content://com.android.providers.media.documents/document/image%3A13"), "الطالب د", "السنة الدراسية د", "الفصل د"));
        // ... أضف المزيد من أعمال الطلاب

        StudentArtAdapter adapter = new StudentArtAdapter(this, studentArtList);
        studentArtRecyclerView.setAdapter(adapter);
    }
}