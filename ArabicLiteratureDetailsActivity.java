package com.manarat.manaratlibrary;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ArabicLiteratureDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arabic_literature_details);

        LinearLayout contentContainer = findViewById(R.id.content_container);
        ArrayList<ContentItem> contentList = (ArrayList<ContentItem>) getIntent().getSerializableExtra("contentList");

        if (contentList != null) {
            for (ContentItem item : contentList) {
                if (item.getText() != null) {
                    TextView textView = new TextView(this);
                    textView.setText(item.getText());
                    textView.setTextSize(16);
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 16); // إضافة مسافة بين النصوص
                    textView.setLayoutParams(params);
                    contentContainer.addView(textView);
                } else if (item.getImageResourceId() != 0) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(item.getImageResourceId());
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 16); // إضافة مسافة أسفل الصور
                    imageView.setLayoutParams(params);
                    contentContainer.addView(imageView);
                }
            }
        }
    }
}