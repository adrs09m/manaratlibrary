package com.manarat.manaratlibrary;

public class SuggestionItem {
    private String title;
    private String author;

    public SuggestionItem(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // ممكن تضيف هنا setters لو محتاج تعدل البيانات بعد الإنشاء
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}