package com.manarat.manaratlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocalLibrary.db";
    private static final int DATABASE_VERSION = 1;

    // جدول الاستعارات المحلية
    public static final String TABLE_BORROWED_OFFLINE = "borrowed_offline";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_BOOK_TITLE = "book_title";
    public static final String COLUMN_BORROW_DATE = "borrow_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_SYNCED = "synced"; // 0 لم تتم المزامنة، 1 تمت المزامنة

    private static final String CREATE_TABLE_BORROWED_OFFLINE =
            "CREATE TABLE " + TABLE_BORROWED_OFFLINE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_ID + " TEXT NOT NULL," +
                    COLUMN_BOOK_ID + " TEXT NOT NULL," +
                    COLUMN_BOOK_TITLE + " TEXT NOT NULL," +
                    COLUMN_BORROW_DATE + " INTEGER NOT NULL," +
                    COLUMN_STATUS + " TEXT NOT NULL," +
                    COLUMN_SYNCED + " INTEGER NOT NULL);";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("LocalDatabaseHelper", "Constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BORROWED_OFFLINE);
        Log.d("LocalDatabaseHelper", "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROWED_OFFLINE);
        onCreate(db);
        Log.d("LocalDatabaseHelper", "Database upgraded");
    }

    public long addBorrowedBook(String userId, String bookId, String bookTitle, long borrowDate, String status, int synced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_BOOK_ID, bookId);
        values.put(COLUMN_BOOK_TITLE, bookTitle);
        values.put(COLUMN_BORROW_DATE, borrowDate);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_SYNCED, synced);
        long result = db.insert(TABLE_BORROWED_OFFLINE, null, values);
        db.close();
        Log.d("LocalDatabaseHelper", "Borrowed book added locally: " + result);
        return result;
    }

    // **ستحتاج هنا دوال لجلب البيانات التي لم تتم مزامنتها وتحديث حالتها بعد المزامنة**
    // مثال لدالة لجلب البيانات غير المزامنة:
    // public Cursor getUnsyncedBorrowedBooks() { ... }
    // مثال لدالة لتحديث حالة المزامنة:
    // public int updateBorrowedBookSyncStatus(long id, int synced) { ... }
}