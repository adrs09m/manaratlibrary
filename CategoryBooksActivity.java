package com.manarat.manaratlibrary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager; // Change this import

import java.util.ArrayList;
import java.util.List;

public class CategoryBooksActivity extends AppCompatActivity {

    public static final String CATEGORY_NAME_KEY = "category_name";

    private RecyclerView categoryBooksRecyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_books);

        Toolbar toolbar = findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        String categoryName = getIntent().getStringExtra(CATEGORY_NAME_KEY);
        if (categoryName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("الكتب");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // إعداد RecyclerView لتنسيق شبكي (كتابين بجانب بعض)
        categoryBooksRecyclerView = findViewById(R.id.categoryBooksRecyclerView);
        // Change this line:
        categoryBooksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        List<Book> books = getBooksForCategory(categoryName);

        bookAdapter = new BookAdapter(this, books);
        categoryBooksRecyclerView.setAdapter(bookAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<Book> getBooksForCategory(String category) {
        List<Book> books = new ArrayList<>();
        // Your dummy data logic remains the same for now
        if ("الآداب".equals(category)) {
            books.add(new Book(
                    "firebase_id_1",
                    "مائة عام من العزلة",
                    "جابرييل جارسيا ماركيز",
                    "رواية رائعة من الأدب اللاتيني الأمريكي.",
                    "",
                    "متاح",
                    450,
                    "1967",
                    "دار التنوير",
                    "الآداب"
            ));
            books.get(0).setDrawableResId(R.drawable.ic_book_default);

            books.add(new Book(
                    "firebase_id_2",
                    "البؤساء",
                    "فيكتور هوجو",
                    "رواية تاريخية فرنسية تتناول قضايا العدالة والظلم.",
                    "",
                    "معار",
                    1400,
                    "1862",
                    "دار الشروق",
                    "الآداب"
            ));
            books.get(1).setDrawableResId(R.drawable.ic_book_placeholder);

            books.add(new Book(
                    "firebase_id_3",
                    "الأمير الصغير",
                    "أنطوان دو سانت إكزوبيري",
                    "قصة فلسفية وشاعرية للأطفال والكبار.",
                    "",
                    "متاح",
                    96,
                    "1943",
                    "دار الساقي",
                    "الآداب"
            ));
            books.get(2).setDrawableResId(R.drawable.ic_book_default);

            // Add another dummy book to better visualize two columns
            books.add(new Book(
                    "firebase_id_5",
                    "الجريمة والعقاب",
                    "دوستويفسكي",
                    "رواية نفسية عميقة.",
                    "",
                    "متاح",
                    600,
                    "1866",
                    "دار التنوير",
                    "الآداب"
            ));
            books.get(3).setDrawableResId(R.drawable.ic_book_placeholder);


        } else if ("الفنون".equals(category)) {
            books.add(new Book(
                    "firebase_id_4",
                    "تاريخ الفن",
                    "إرنست غومبريتش",
                    "مرجع شامل لتاريخ الفن من العصور الأولى حتى الآن.",
                    "",
                    "متاح",
                    688,
                    "1950",
                    "دار المنى",
                    "الفنون"
            ));
            books.get(0).setDrawableResId(R.drawable.ic_book_default);
            books.add(new Book(
                    "firebase_id_6",
                    "الفن الحديث",
                    "اسم المؤلف",
                    "وصف عن الفن الحديث.",
                    "",
                    "متاح",
                    300,
                    "2000",
                    "دار النشر",
                    "الفنون"
            ));
            books.get(1).setDrawableResId(R.drawable.ic_book_placeholder);
        }
        return books;
    }
}