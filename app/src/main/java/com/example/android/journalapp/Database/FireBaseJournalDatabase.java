package com.example.android.journalapp.Database;

import com.example.android.journalapp.JournalEntry;
import com.example.android.journalapp.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseJournalDatabase {
    private final static String APP_NAME_ROOT_NODE = "com_example_android_journalapp";
    private final static String USERS_NODE = "users";
    private final static String ENTRIES_NODE = "entries";


    private static FireBaseJournalDatabase database;
    private DatabaseReference mDatabaseRef;

    private FireBaseJournalDatabase(){
        FirebaseDatabase configDatabase  = FirebaseDatabase.getInstance();
        configDatabase.setPersistenceEnabled(true);
        mDatabaseRef = configDatabase.getReference(APP_NAME_ROOT_NODE).child(USERS_NODE).child(MainActivity.USER_ID).child(ENTRIES_NODE);
    }
    public static FireBaseJournalDatabase getInstance(){
        if (database == null){
            database =  new FireBaseJournalDatabase();
            return database;
        }

        return database;
    }

    public DatabaseReference getReference(){
        return mDatabaseRef;
    }

    public void addEntry(JournalEntry entry){
        if (entry == null) return;
        String genEntryId = mDatabaseRef.push().getKey();
        entry.id = genEntryId;
        Task task = mDatabaseRef.child(genEntryId).setValue(entry);

    }

    public void deleteEntry(String id) {
        if (id == null) return;
        mDatabaseRef.child(id).removeValue();
    }

    public void updateEntry(JournalEntry entry){
        if (entry == null) return;
        mDatabaseRef.child(entry.id).setValue(entry);
    }
}
