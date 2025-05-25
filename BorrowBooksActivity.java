package com.manarat.manaratlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class BorrowBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // تم إضافة هذا السطر لإخفاء شريط الإجراءات بالكامل
        setContentView(R.layout.activity_borrow_books);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        bookList = new ArrayList<>();
        adapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Check for internet connection and load data accordingly
        if (isConnectedToInternet()) {
            loadBooksFromFirestore();
        } else {
            initializeBooks();
        }
    }

    private void loadBooksFromFirestore() {
        db.collection("books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            if (book != null) {
                                Log.d("FirestoreData", "Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", ImageUrl: " + book.getImageUrl());
                                bookList.add(book);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(BorrowBooksActivity.this, "فشل في تحميل البيانات من الإنترنت", Toast.LENGTH_SHORT).show();
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        // If loading from Firestore fails, you might want to try loading local data as a fallback
                        initializeBooks();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initializeBooks() {
        bookList.clear();
        bookList.add(new Book("الكتاب الأول", "أحمد خالد", R.drawable.ic_book1, true));
        bookList.add(new Book("الرحلة", "سارة محمود", R.drawable.ic_book2, false));
        bookList.add(new Book("أغصان الخريف", "ياسر عبد الله", R.drawable.ic_book3, true));
        bookList.add(new Book("الضوء المخفي", "هبة سعيد", R.drawable.ic_book4, false));
        bookList.add(new Book("الحكاية الدولية", "محمد عبد العزيز", R.drawable.ic_book5, true));
        bookList.add(new Book("بحر الأسرار", "دينا حسين", R.drawable.ic_book6, true));
        bookList.add(new Book("رسائل لم تصل", "ريم حسان", R.drawable.ic_book7, true));
        bookList.add(new Book("صوت الصمت", "ليلى سمير", R.drawable.ic_book8, true));
        bookList.add(new Book("خلف الجدار", "فؤاد عبد الله", R.drawable.ic_book9, false));
        bookList.add(new Book("النجوم التائهة", "إيمان يوسف", R.drawable.ic_book10, true));
        bookList.add(new Book("موعد مع الأبدية", "نادر فوزي", R.drawable.ic_book11, true));
        adapter.notifyDataSetChanged();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}