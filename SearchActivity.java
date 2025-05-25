package com.manarat.manaratlibrary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private ImageView clearSearchText;
    private RecyclerView suggestionsRecyclerView;
    private RecyclerView resultsRecyclerView;
    private LinearLayout advancedSearchOptions;
    private EditText authorInput;
    private EditText subjectInput;
    private EditText publisherInput;
    private TextView noResultsText;
    private BottomNavigationView bottomNavigation;
    private List<SuggestionItem> suggestionList; // استخدم List<SuggestionItem>
    private SuggestionsAdapter suggestionsAdapter;
    private ResultsAdapter resultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // إخفاء شريط الإجراءات بالكامل
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize UI elements
        searchInput = findViewById(R.id.search_input);
        clearSearchText = findViewById(R.id.clear_search_text);
        suggestionsRecyclerView = findViewById(R.id.suggestions_recycler_view);
        resultsRecyclerView = findViewById(R.id.results_recycler_view);
        advancedSearchOptions = findViewById(R.id.advanced_search_options);
        authorInput = findViewById(R.id.author_input);
        subjectInput = findViewById(R.id.subject_input);
        publisherInput = findViewById(R.id.publisher_input);
        noResultsText = findViewById(R.id.no_results_text);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Setup RecyclerView for suggestions
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionList = new ArrayList<>();
        // قم بملء suggestionList ببيانات الاقتراحات الفعلية من تطبيقك
        suggestionList.add(new SuggestionItem("المعجم الوسيط", "مجمع اللغة العربية"));
        suggestionList.add(new SuggestionItem("قصص الأنبياء", "ابن كثير"));
        suggestionList.add(new SuggestionItem("الأيام", "طه حسين"));
        suggestionList.add(new SuggestionItem("النحو الواضح", "علي الجارم ومصطفى أمين"));
        suggestionList.add(new SuggestionItem("موسوعة تاريخ", "حسين مؤنس"));
        suggestionList.add(new SuggestionItem("تاريخ مصر القومي", "عبد الرحمن الرافعي"));
        suggestionList.add(new SuggestionItem("الجغرافيا الطبيعية", "محمود إبراهيم"));
        suggestionsAdapter = new SuggestionsAdapter(suggestionList);
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);

        // Setup RecyclerView for results (initially hidden)
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsAdapter = new ResultsAdapter(new ArrayList<>());
        resultsRecyclerView.setAdapter(resultsAdapter);

        // TextWatcher for search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // لا تحتاج لفعل شيء هنا
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearchText.setVisibility(View.VISIBLE);
                    suggestionsRecyclerView.setVisibility(View.VISIBLE);
                    resultsRecyclerView.setVisibility(View.GONE);
                    noResultsText.setVisibility(View.GONE);
                    // قم بتحديث قائمة الاقتراحات بناءً على النص المدخل (إذا لزم الأمر)
                    filterSuggestions(s.toString());
                } else {
                    clearSearchText.setVisibility(View.GONE);
                    suggestionsRecyclerView.setVisibility(View.GONE);
                    resultsRecyclerView.setVisibility(View.GONE);
                    noResultsText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // لا تحتاج لفعل شيء هنا
            }
        });

        // Click listener for clear search text
        clearSearchText.setOnClickListener(v -> {
            searchInput.setText("");
            clearSearchText.setVisibility(View.GONE);
            suggestionsRecyclerView.setVisibility(View.GONE);
            resultsRecyclerView.setVisibility(View.GONE);
            noResultsText.setVisibility(View.GONE);
        });

        // Hide advanced search options initially
        advancedSearchOptions.setVisibility(View.GONE);

        // يمكنك إضافة Listener لـ TextView "بحث متقدم" هنا لإظهار advancedSearchOptions
        //TextView advancedSearchLink = findViewById(R.id.advanced_search_link);
        //if (advancedSearchLink != null) {
        //    advancedSearchLink.setOnClickListener(v -> {
        //        advancedSearchOptions.setVisibility(View.VISIBLE);
        //        suggestionsRecyclerView.setVisibility(View.GONE);
        //        resultsRecyclerView.setVisibility(View.GONE);
        //        noResultsText.setVisibility(View.GONE);
        //    });
        //}

        // يمكنك إضافة Listener لزر البحث المتقدم هنا
        //findViewById(R.id.advanced_search_button).setOnClickListener(v -> {
        //    performAdvancedSearch();
        //});

        // Setup BottomNavigationView listeners (if needed)
        bottomNavigation.setOnItemSelectedListener(item -> {
            // Handle bottom navigation item clicks
            return true;
        });
    }

    private void performSearch(String query) {
        // قم بتنفيذ عملية البحث هنا باستخدام الـ query
        // بعد الحصول على النتائج، قم بتحديث resultsRecyclerView وإظهاره وإخفاء suggestionsRecyclerView و noResultsText إذا لزم الأمر
        resultsRecyclerView.setVisibility(View.VISIBLE);
        suggestionsRecyclerView.setVisibility(View.GONE);
        noResultsText.setVisibility(View.GONE);
        // قم بتحديث resultsAdapter بالنتائج
    }

    private void performAdvancedSearch() {
        String author = authorInput.getText().toString().trim();
        String subject = subjectInput.getText().toString().trim();
        String publisher = publisherInput.getText().toString().trim();

        // قم بتنفيذ عملية البحث المتقدم هنا باستخدام البيانات المدخلة
        // بعد الحصول على النتائج، قم بتحديث resultsRecyclerView وإظهاره وإخفاء advancedSearchOptions و noResultsText إذا لزم الأمر
        resultsRecyclerView.setVisibility(View.VISIBLE);
        advancedSearchOptions.setVisibility(View.GONE);
        noResultsText.setVisibility(View.GONE);
        // قم بتحديث resultsAdapter بالنتائج
    }

    private void filterSuggestions(String text) {
        List<SuggestionItem> filteredList = new ArrayList<>();
        for (SuggestionItem suggestion : suggestionList) {
            if (suggestion.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    suggestion.getAuthor().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(suggestion);
            }
        }
        suggestionsAdapter.updateList(filteredList);
    }
}