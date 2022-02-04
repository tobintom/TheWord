package com.theword.thedigitalword.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theword.thedigitalword.model.PersonalNote;

import java.util.ArrayList;
import java.util.List;

public class PersonalNotesHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TDW_DB";

    // Table name: Note.
    private static final String TABLE_NOTE = "PersonalNotes";

    private static final String COLUMN_NOTE_ID ="Note_Id";
    private static final String COLUMN_NOTE_TITLE ="Note_Title";
    private static final String COLUMN_NOTE_CONTENT = "Note_Content";
    private static final String COLUMN_NOTE_TIMESTAMP = "Note_Timestamp";

    public PersonalNotesHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script.
        String script = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY," + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_CONTENT + " TEXT," + COLUMN_NOTE_TIMESTAMP + " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        // Create tables again
        onCreate(db);
    }

    public void addNote(PersonalNote note) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();

            values.put(COLUMN_NOTE_TIMESTAMP, note.getTimestamp());
            values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
            values.put(COLUMN_NOTE_CONTENT, note.getNoteContent());

            // Inserting Row
            db.insert(TABLE_NOTE, null, values);
        }finally {
            // Closing database connection
            if(db!=null) {
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }

    public PersonalNote getNote(int id) {
      SQLiteDatabase db = null;
      Cursor cursor = null;
      PersonalNote note = null;
      try {
          db = this.getReadableDatabase();
          cursor = db.query(TABLE_NOTE, new String[]{COLUMN_NOTE_ID,
                          COLUMN_NOTE_TITLE, COLUMN_NOTE_CONTENT, COLUMN_NOTE_TIMESTAMP}, COLUMN_NOTE_ID + "=?",
                  new String[]{String.valueOf(id)}, null, null, null, null);
          if (cursor != null) {
              cursor.moveToFirst();
          }

          note = new PersonalNote(Integer.parseInt(cursor.getString(0)),
                  cursor.getString(1), cursor.getString(2), cursor.getString(2));
      }finally {
          if(cursor!=null && !cursor.isClosed()) {
              cursor.close();
          }
          if(db!=null) {
              db.close();
          }
      }
        // return note
        return note;
    }

    public List<PersonalNote> getAllNotes() {

        List<PersonalNote> noteList = new ArrayList<PersonalNote>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTE;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    PersonalNote note = new PersonalNote();
                    note.setNoteId(Integer.parseInt(cursor.getString(0)));
                    note.setNoteTitle(cursor.getString(1));
                    note.setNoteContent(cursor.getString(2));
                    note.setTimestamp(cursor.getString(3));
                    // Adding note to list
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
        }finally {
            if(cursor!=null && !cursor.isClosed()) {
                cursor.close();
            }
            if(db!=null) {
                db.close();
            }
        }
        // return note list
        return noteList;
    }

    public int updateNote(PersonalNote note) {
        SQLiteDatabase db = null;
        int ret;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
            values.put(COLUMN_NOTE_CONTENT, note.getNoteContent());
            values.put(COLUMN_NOTE_TIMESTAMP, note.getTimestamp());

            // updating row
            ret = db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null){
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
        return ret;
    }

    public void deleteNote(PersonalNote note) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                    new String[]{String.valueOf(note.getNoteId())});
        }finally {
            if(db!=null){
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }
    }
}
