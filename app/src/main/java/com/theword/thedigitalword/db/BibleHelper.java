 package com.theword.thedigitalword.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.model.PersonalNote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibleHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TDW_DBB";

    // Table names
    private static final String TABLE_HIGHLIGHTS = "Highlights";
    private static final String TABLE_FAVORITES = "Favorites";
    private static final String TABLE_BOOKMARKS = "Bookmarks";
    private static final String TABLE_NOTES = "VerseNotes";

    private static final String COLUMN_ID ="Id";
    private static final String COLUMN_TITLE ="Title";
    private static final String COLUMN_LINK ="Link";
    private static final String COLUMN_BOOK ="Bible_Book";
    private static final String COLUMN_CHAPTER = "Bible_Chapter";
    private static final String COLUMN_VERSE = "Bible_Verse";
    private static final String COLUMN_NOTE = "Bible_Note";
    private static final String COLUMN_COLOR = "Verse_Color";
    private static final String COLUMN_TIMESTAMP = "Timestamp";

    public BibleHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Scripts.
        String hightlight_script = "CREATE TABLE " + TABLE_HIGHLIGHTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TITLE + " TEXT, "
                + COLUMN_BOOK + " TEXT, " + COLUMN_CHAPTER + " TEXT, "
                + COLUMN_VERSE+ " TEXT, " + COLUMN_COLOR + " TEXT, " + COLUMN_TIMESTAMP + " TEXT" + " )";

        String favorite_script = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TITLE + " TEXT, "
                + COLUMN_BOOK + " TEXT, " + COLUMN_CHAPTER + " TEXT, "
                + COLUMN_VERSE+ " TEXT, " + COLUMN_TIMESTAMP + " TEXT" + " )";

        String bookmark_script = "CREATE TABLE " + TABLE_BOOKMARKS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TITLE + " TEXT, " + COLUMN_LINK + " TEXT, "
                + COLUMN_BOOK + " TEXT, " + COLUMN_CHAPTER + " TEXT, "
                + COLUMN_VERSE+ " TEXT, " + COLUMN_TIMESTAMP + " TEXT" + " )";

        String note_script = "CREATE TABLE " + TABLE_NOTES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TITLE + " TEXT, " + COLUMN_LINK + " TEXT, "
                + COLUMN_BOOK + " TEXT, " + COLUMN_CHAPTER + " TEXT, "
                + COLUMN_VERSE+ " TEXT, " + COLUMN_NOTE + " TEXT, " + COLUMN_TIMESTAMP + " TEXT" + " )";

        // Execute Scripts.
        db.execSQL(hightlight_script);
        db.execSQL(favorite_script);
        db.execSQL(bookmark_script);
        db.execSQL(note_script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHLIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        // Create tables again
        onCreate(db);
    }

    public void addHighlights(List<BibleDBContent> contents) {
        SQLiteDatabase db = null;
        try {
            if (contents != null && contents.size() > 0) {
                db = this.getWritableDatabase();
                db.beginTransaction();
                for (BibleDBContent content : contents) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_TIMESTAMP, content.getTimestamp());
                    values.put(COLUMN_BOOK, content.getBibleBook());
                    values.put(COLUMN_CHAPTER, content.getBibleChapter());
                    values.put(COLUMN_VERSE, content.getBibleVerse());
                    values.put(COLUMN_COLOR, content.getColor());
                    // Inserting Row
                    db.insert(TABLE_HIGHLIGHTS, null, values);
                }
            }
        }finally{
            if(db!=null) {
            // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }

    public Map<String, BibleDBContent> getBookHighlightsMap(String book, String chapter) {
        Map<String, BibleDBContent> noteList = new HashMap<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_HIGHLIGHTS + " WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter + "' " +
                    " ORDER BY Id DESC ";
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setBibleBook(cursor.getString(2));
                    content.setBibleChapter(cursor.getString(3));
                    content.setBibleVerse(cursor.getString(4));
                    content.setColor(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.put(content.getBibleVerse(), content);
                } while (cursor.moveToNext());
            }
        }finally{
                if(cursor!=null && !cursor.isClosed()) {
                    cursor.close();
                }
                if(db!=null) {
                    // Closing database connection

                    db.close();
                }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getBookHighlights(String book, String chapter) {
        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_HIGHLIGHTS +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' "+
                " ORDER BY Id DESC ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setBibleBook(cursor.getString(2));
                    content.setBibleChapter(cursor.getString(3));
                    content.setBibleVerse(cursor.getString(4));
                    content.setColor(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }


    public List<BibleDBContent> getAllHighlights() {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_HIGHLIGHTS +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setBibleBook(cursor.getString(2));
                    content.setBibleChapter(cursor.getString(3));
                    content.setBibleVerse(cursor.getString(4));
                    content.setColor(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public int updateHighlight(BibleDBContent note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, note.getTitle());
            values.put(COLUMN_COLOR, note.getColor());
            values.put(COLUMN_TIMESTAMP, note.getTimestamp());

            // updating row
            ret = db.update(TABLE_HIGHLIGHTS, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }

    public void deleteHighlight(BibleDBContent note) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_HIGHLIGHTS, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }

    public void deleteHighlights(String inclause) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_HIGHLIGHTS, COLUMN_VERSE + " IN " + inclause,
                    null);
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }

    public void addFavorites(List<BibleDBContent> contents) {
        if(contents!=null && contents.size()>0) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                for (BibleDBContent content : contents) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_TIMESTAMP, content.getTimestamp());
                    values.put(COLUMN_BOOK, content.getBibleBook());
                    values.put(COLUMN_CHAPTER, content.getBibleChapter());
                    values.put(COLUMN_VERSE, content.getBibleVerse());
                    // Inserting Row
                    db.insert(TABLE_FAVORITES, null, values);
                }
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public Map<String, BibleDBContent> getBookFavoritesMap(String book, String chapter) {
        Map<String, BibleDBContent> noteList = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' "+
                " ORDER BY Id DESC ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setBibleBook(cursor.getString(2));
                    content.setBibleChapter(cursor.getString(3));
                    content.setBibleVerse(cursor.getString(4));
                    content.setTimestamp(cursor.getString(5));
                    // Adding note to list
                    noteList.put(content.getBibleVerse(), content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getAllFavorites() {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setBibleBook(cursor.getString(2));
                    content.setBibleChapter(cursor.getString(3));
                    content.setBibleVerse(cursor.getString(4));
                    content.setTimestamp(cursor.getString(5));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public void deleteFavorite(BibleDBContent note) {
        if(note!=null) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                db.delete(TABLE_FAVORITES, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(note.getNoteId())});
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public void deleteFavorites(String inclause) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_FAVORITES, COLUMN_VERSE + " IN " + inclause,
                    null);
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }

    public void addBookMark(BibleDBContent bookmark) {
        if(bookmark!=null) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                //Save Bookmark
                ContentValues values = new ContentValues();
                values.put(COLUMN_TIMESTAMP, bookmark.getTimestamp());
                values.put(COLUMN_BOOK, bookmark.getBibleBook());
                values.put(COLUMN_CHAPTER, bookmark.getBibleChapter());
                values.put(COLUMN_VERSE, bookmark.getBibleVerse());
                values.put(COLUMN_TITLE, (bookmark.getTitle() != null ? bookmark.getTitle().trim() : ""));
                values.put(COLUMN_LINK, (bookmark.getLink() != null ? bookmark.getLink().trim() : ""));
                // Inserting Row
                db.insert(TABLE_BOOKMARKS, null, values);
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public Map<String, BibleDBContent> getBookmarksMap(String book, String chapter) {
        Map<String, BibleDBContent> noteList = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' "+
                " ORDER BY Id DESC ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.put(content.getBibleVerse(), content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getAllBookmarks() {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<String> getAllBookmarkLinks(){
        List<String> bookmarkLinks = new ArrayList<String>();
        //Query
        String selectQuery = "SELECT DISTINCT Link FROM " + TABLE_BOOKMARKS +" LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String value = cursor.getString(0);
                    // Adding note to list
                    bookmarkLinks.add(value);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        return bookmarkLinks;
    }

    public List<BibleDBContent> getAllBookmarksByLink(String link) {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        link = link!=null?link.trim():"";
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS +" WHERE Link LIKE '" + link + "'" +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor= null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getAllBookmarksByVerse(String book, String chapter, String verse) {
        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' AND " +
                " ( " +
                COLUMN_VERSE + " like '" + verse +"' OR " +
                COLUMN_VERSE + " like '%," + verse +"' OR " +
                COLUMN_VERSE + " like '" + verse +",%' OR " +
                COLUMN_VERSE + " like '%," + verse +",%'  " +
                " ) " +
                " ORDER BY Id DESC ";
        SQLiteDatabase db = null;
        Cursor cursor= null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setTimestamp(cursor.getString(6));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection
                db.close();
            }
        }
        // return note list
        return noteList;
    }


    public void deleteBookmark(BibleDBContent note) {
        if(note!=null) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                db.delete(TABLE_BOOKMARKS, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(note.getNoteId())});
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public int updateBookmark(BibleDBContent note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, (note.getTitle() != null ? note.getTitle().trim() : ""));
            values.put(COLUMN_LINK, (note.getLink() != null ? note.getLink().trim() : ""));
            values.put(COLUMN_TIMESTAMP, String.valueOf(Calendar.getInstance().getTimeInMillis()));

            // updating row
            ret = db.update(TABLE_BOOKMARKS, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }

    public int updateBookmarkVerses(BibleDBContent note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_VERSE, (note.getBibleVerse()));
            values.put(COLUMN_TIMESTAMP, String.valueOf(Calendar.getInstance().getTimeInMillis()));

            // updating row
            ret = db.update(TABLE_BOOKMARKS, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }
    public void addBibleNote(BibleDBContent bookmark) {
        if(bookmark!=null) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                //Save Bookmark
                ContentValues values = new ContentValues();
                values.put(COLUMN_TIMESTAMP, bookmark.getTimestamp());
                values.put(COLUMN_BOOK, bookmark.getBibleBook());
                values.put(COLUMN_CHAPTER, bookmark.getBibleChapter());
                values.put(COLUMN_VERSE, bookmark.getBibleVerse());
                values.put(COLUMN_TITLE, (bookmark.getTitle() != null ? bookmark.getTitle().trim() : ""));
                values.put(COLUMN_LINK, (bookmark.getLink() != null ? bookmark.getLink().trim() : ""));
                values.put(COLUMN_NOTE, (bookmark.getBibleNote() != null ? bookmark.getBibleNote().trim() : ""));
                // Inserting Row
                db.insert(TABLE_NOTES, null, values);
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public Map<String, BibleDBContent> getBibleNotesMap(String book, String chapter) {
        Map<String, BibleDBContent> noteList = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' "+
                " ORDER BY Id DESC ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setBibleNote(cursor.getString(6));
                    content.setTimestamp(cursor.getString(7));
                    // Adding note to list
                    noteList.put(content.getBibleVerse(), content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getAllBibleNotes() {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setBibleNote(cursor.getString(6));
                    content.setTimestamp(cursor.getString(7));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public List<BibleDBContent> getAllBibleNotesByVerse(String book, String chapter, String verse) {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES +" WHERE Bible_Book = '" + book + "' AND Bible_Chapter = '" + chapter +"' AND " +
                " ( " +
                COLUMN_VERSE + " like '" + verse +"' OR " +
                COLUMN_VERSE + " like '%," + verse +"' OR " +
                COLUMN_VERSE + " like '" + verse +",%' OR " +
                COLUMN_VERSE + " like '%," + verse +",%'  " +
                " ) " +
                " ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setBibleNote(cursor.getString(6));
                    content.setTimestamp(cursor.getString(7));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }


    public List<String> getAllBibleNoteLinks(){
        List<String> bookmarkLinks = new ArrayList<String>();
        //Query
        String selectQuery = "SELECT DISTINCT Link FROM " + TABLE_NOTES +" LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String value = cursor.getString(0);
                    // Adding note to list
                    bookmarkLinks.add(value);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        return bookmarkLinks;
    }

    public List<BibleDBContent> getAllBibleNotesByLink(String link) {

        List<BibleDBContent> noteList = new ArrayList<BibleDBContent>();
        link = link!=null?link.trim():"";
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES +" WHERE Link LIKE '" + link + "'" +" ORDER BY Id DESC LIMIT 500 ";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BibleDBContent content = new BibleDBContent();
                    content.setNoteId(Integer.parseInt(cursor.getString(0)));
                    content.setTitle(cursor.getString(1));
                    content.setLink(cursor.getString(2));
                    content.setBibleBook(cursor.getString(3));
                    content.setBibleChapter(cursor.getString(4));
                    content.setBibleVerse(cursor.getString(5));
                    content.setBibleNote(cursor.getString(6));
                    content.setTimestamp(cursor.getString(7));
                    // Adding note to list
                    noteList.add(content);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            if(db!=null) {
                // Closing database connection

                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public void deleteBibleNote(BibleDBContent note) {
        if(note!=null) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                db.delete(TABLE_NOTES, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(note.getNoteId())});
            }finally {
                if(db!=null) {
                    // Closing database connection
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    public int updateBibleNote(BibleDBContent note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, (note.getTitle() != null ? note.getTitle().trim() : ""));
            values.put(COLUMN_LINK, (note.getLink() != null ? note.getLink().trim() : ""));
            values.put(COLUMN_NOTE, (note.getBibleNote() != null ? note.getBibleNote().trim() : ""));
            values.put(COLUMN_TIMESTAMP, String.valueOf(Calendar.getInstance().getTimeInMillis()));

            // updating row
            ret = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }

    public int updateBibleNoteVerses(BibleDBContent note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_VERSE, (note.getBibleVerse()));
            values.put(COLUMN_TIMESTAMP, String.valueOf(Calendar.getInstance().getTimeInMillis()));

            // updating row
            ret = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null) {
                // Closing database connection
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }

}
