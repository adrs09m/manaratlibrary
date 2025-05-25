package com.manarat.manaratlibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WriteShortStoryActivity extends AppCompatActivity {

    private EditText storyTitleEditText;
    private EditText storyContentEditText;
    private EditText participantNameEditText;
    private EditText participantEmailEditText;
    private Button submitStoryButton;
    private final String recipientEmail = "librarymanarat9@gmail.com"; // عنوان البريد الإلكتروني للمشرف (والمسابقة سابقًا)
    private final String emailSubjectPrefix = "قصة جديدة للمراجعة"; // تعديل موضوع البريد الإلكتروني

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_short_story);

        storyTitleEditText = findViewById(R.id.storyTitleEditText);
        storyContentEditText = findViewById(R.id.storyContentEditText);
        participantNameEditText = findViewById(R.id.participantNameEditText);
        participantEmailEditText = findViewById(R.id.participantEmailEditText);
        submitStoryButton = findViewById(R.id.submitStoryButton);

        submitStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = storyTitleEditText.getText().toString().trim();
                String content = storyContentEditText.getText().toString().trim();
                String participantName = participantNameEditText.getText().toString().trim();
                String participantEmail = participantEmailEditText.getText().toString().trim();

                if (participantName.isEmpty()) {
                    participantNameEditText.setError("يرجى إدخال اسمك");
                    return;
                }

                if (participantEmail.isEmpty()) {
                    participantEmailEditText.setError("يرجى إدخال بريدك الإلكتروني");
                    return;
                }

                if (title.isEmpty()) {
                    storyTitleEditText.setError("يرجى إدخال عنوان القصة");
                    return;
                }

                if (content.isEmpty()) {
                    storyContentEditText.setError("يرجى كتابة نص القصة");
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + recipientEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubjectPrefix + ": " + title + " - " + participantName); // موضوع للمراجعة
                intent.putExtra(Intent.EXTRA_TEXT, "اسم المشارك: " + participantName + "\n" +
                        "البريد الإلكتروني للمشارك: " + participantEmail + "\n\n" +
                        "عنوان القصة: " + title + "\n\n" +
                        "نص القصة:\n" + content);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(WriteShortStoryActivity.this, "لا يوجد تطبيق بريد إلكتروني مثبت.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}