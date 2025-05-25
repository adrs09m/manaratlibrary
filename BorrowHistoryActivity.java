package com.manarat.manaratlibrary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BorrowHistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private BorrowHistoryAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_history);

        db = FirebaseFirestore.getInstance();
        historyRecyclerView = findViewById(R.id.borrowHistoryRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = db.collection("borrowedHistory"); // استعلام عن مجموعة سجلات الإعارة

        FirestoreRecyclerOptions<BorrowRequest> options = new FirestoreRecyclerOptions.Builder<BorrowRequest>()
                .setQuery(query, BorrowRequest.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new BorrowHistoryAdapter(options);
        historyRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}