package com.example.android.journalapp;

public class JournalEntry {
    /*  For the SQLiteDatabse implementation

        public int id;
        //Constructor for SQLiteDatabse
        public JournalEntry(int id, String entry, long dateCreated, long dateUpdated) {
        this.id = id;
        this.entry = entry;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        }
    */

    public String id;
    public String title;
    public String entry;
    public long dateCreated;
    public long dateUpdated;
    public JournalEntry(){

    }

    public JournalEntry(String id, String title, String entry, long dateCreated, long dateUpdated) {
        this.id = id;
        this.title = title;
        this.entry = entry;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID = " + id);
        str.append("\nEntry = "+ entry);
        str.append("\nCreated on: " + dateCreated);
        str.append("\nUpdated on: " + dateUpdated);
        return str.toString();
    }
}
