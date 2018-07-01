package com.example.android.journalapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.journalapp.JournalEntry;

import java.util.ArrayList;
import java.util.List;

//        This is the SQLite database alternative
//         I make use of an Int Id as the Primary key here
//         as opposed to a String key in the FirebaseDatabase Version

public class JournalDatabase extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "journal-database";
    final static int DATABASE_VERSION = 1;
    final static String TABLE_NAME = "journal";
    final static String ID = "id";
    final static String ENTRY_TITLE = "entry-title";
    final static String ENTRY = "entry";
    final static String DATE_CREATED = "dateCreated";
    final static String DATE_UPDATED = "dateUpdated";



    public JournalDatabase(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
        Log.d("Database", "Creating database");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" ("+ ID +" text primary key" +
                ", "  + ENTRY_TITLE +" text," + ENTRY + " text, "+ DATE_CREATED + " INTEGER, " + DATE_UPDATED + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addEntry(JournalEntry entry){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY, entry.entry);
        contentValues.put(DATE_CREATED, entry.dateCreated);
        contentValues.put(DATE_UPDATED, entry.dateUpdated);
        SQLiteDatabase db = this.getWritableDatabase();
        long succeed = db.insert(TABLE_NAME,null, contentValues);

        return succeed != -1;
    }

    public List<JournalEntry> getEntries(){
        Cursor result = this.getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // If there is nothing in the database table, return null
        if (result == null) return null;

        List<JournalEntry> entries = new ArrayList<>();

        if (result.moveToFirst()){
            do{
                String id;
                String entryTitle;
                String entry = null;
                long dateCreated;
                long dateUpdated;
                id = result.getString(0);
                entryTitle = result.getString(1);
                entry = result.getString(2);
                dateCreated = result.getLong(3);
                dateUpdated = result.getLong(4);

                JournalEntry journalEntry = new JournalEntry(id, entryTitle, entry, dateCreated,dateUpdated);
                entries.add(journalEntry);
            } while(result.moveToNext());
        }

        result.close();
        return entries;
    }

 //SQLite implementation
    public JournalEntry getEntry(String id){
        Cursor result = this.getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id, null);

        if (result.moveToFirst()){
            String entryTitle = result.getString(1);
            String entryText = result.getString(2);
            long dateCreated = result.getLong(3);
            long dateUpdated = result.getLong(4);
            result.close();
            return new JournalEntry( id,entryTitle, entryText, dateCreated, dateUpdated);
        }

        return null;
    }
 //SQLite implementation
    public boolean updateEntry(JournalEntry entry){
        String id = entry.id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY, entry.entry);
        contentValues.put(DATE_CREATED, entry.dateCreated);
        contentValues.put(DATE_UPDATED, entry.dateUpdated);
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update(TABLE_NAME,contentValues,ID +" = ?", new String[]{String.valueOf(id)});
        if (result < 1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteEntry(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int succeed = db.delete(TABLE_NAME,ID +" = ?", new String[]{String.valueOf(id)});

        return succeed != -1;
    }
}
