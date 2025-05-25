package com.manarat.manaratlibrary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// اسم الجدول في قاعدة البيانات
@Entity(tableName = "books")
public class Book implements Serializable {
    // مفتاح أساسي لـ Room، يولد تلقائياً
    @PrimaryKey(autoGenerate = true)
    private int id;

    // معرف الكتاب الأصلي من Firebase، إذا كنت تستخدمه للمزامنة
    @ColumnInfo(name = "book_id_firebase")
    private String bookIdFirebase;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "status")
    private String status; // مثلاً: "متاح", "معار"

    @ColumnInfo(name = "pages")
    private int pages;

    @ColumnInfo(name = "publish_date")
    private String publishDate;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "category")
    private String category; // الفئة: "الآداب", "الفنون", إلخ.

    // إضافة حقل لتخزين معرف المورد القابل للرسم (drawable resource ID)
    // هذا مفيد للصور الافتراضية أو التي يتم تحميلها محليًا
    private int drawableResId;

    // مُنشئ فارغ مطلوب من Room و Firestore
    public Book() {
        // تهيئة القيم الافتراضية
        this.id = 0;
        this.bookIdFirebase = null;
        this.title = null;
        this.author = null;
        this.description = null;
        this.imageUrl = null;
        this.status = "غير متاح"; // افتراضياً غير متاح
        this.pages = 0;
        this.publishDate = null;
        this.publisher = null;
        this.category = null;
        this.drawableResId = 0;
    }

    // مُنشئ للراحة عند إنشاء كائنات Book جديدة من Firestore أو يدويًا
    public Book(String bookIdFirebase, String title, String author, String description, String imageUrl, String status, int pages, String publishDate, String publisher, String category) {
        this.bookIdFirebase = bookIdFirebase;
        this.title = title;
        this.author = author;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.pages = pages;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.category = category;
        this.drawableResId = 0; // قيمة افتراضية، يمكن تعيينها لاحقًا إذا لزم الأمر
    }

    // مُنشئ إضافي لـ BorrowBooksActivity الذي يستخدم int drawableResId و boolean available
    // (يجب أن يكون هذا المنشئ موجودًا إذا كنت تستخدمه في BorrowBooksActivity)
    public Book(String title, String author, int drawableResId, boolean available) {
        this.title = title;
        this.author = author;
        this.drawableResId = drawableResId;
        // تعيين الحالة بناءً على التوفر
        this.status = available ? "متاح" : "معار";
        // تعيين قيم افتراضية للحقول الأخرى
        this.bookIdFirebase = null;
        this.description = null;
        this.imageUrl = null;
        this.pages = 0;
        this.publishDate = null;
        this.publisher = null;
        this.category = null;
    }


    // Getters
    public int getId() { return id; }
    public String getBookIdFirebase() { return bookIdFirebase; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getStatus() { return status; }
    public int getPages() { return pages; }
    public String getPublishDate() { return publishDate; }
    public String getPublisher() { return publisher; }
    public String getCategory() { return category; }
    // Getter لـ drawableResId
    public int getDrawableResId() { return drawableResId; }
    // Getter لـ isAvailable بناءً على حقل status
    public boolean isAvailable() {
        return "متاح".equalsIgnoreCase(status); // استخدام equalsIgnoreCase لمقارنة غير حساسة لحالة الأحرف
    }


    // Setters
    public void setId(int id) { this.id = id; }
    public void setBookIdFirebase(String bookIdFirebase) { this.bookIdFirebase = bookIdFirebase; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setStatus(String status) { this.status = status; }
    public void setPages(int pages) { this.pages = pages; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setCategory(String category) { this.category = category; }
    // Setter لـ drawableResId
    public void setDrawableResId(int drawableResId) { this.drawableResId = drawableResId; }
}
