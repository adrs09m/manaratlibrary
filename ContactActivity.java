package com.manarat.manaratlibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    EditText nameInput, emailInput, subjectInput, messageInput;
    Button sendButton, emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact); // ← اسم الملف XML

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        subjectInput = findViewById(R.id.subjectInput);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        emailButton = findViewById(R.id.emailButton);

        sendButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String subject = subjectInput.getText().toString();
            String message = messageInput.getText().toString();

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "تم إرسال رسالتك بنجاح!", Toast.LENGTH_LONG).show();
                // يمكن لاحقًا ربطها بـ Firebase أو خدمة إرسال
            }
        });

        emailButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:info@example.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "رسالة من التطبيق");
            startActivity(Intent.createChooser(emailIntent, "إرسال عبر البريد"));
        });
    }
}
