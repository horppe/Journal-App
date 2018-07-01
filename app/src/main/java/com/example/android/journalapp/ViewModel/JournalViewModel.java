package com.example.android.journalapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.journalapp.Database.FireBaseJournalDatabase;
import com.example.android.journalapp.JournalEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.List;


public class JournalViewModel extends AndroidViewModel {

    private MutableLiveData<List<JournalEntry>> mEntries = new MutableLiveData<>();
    private FireBaseJournalDatabase journalDatabase;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            JournalEntry entry = dataSnapshot.getValue(JournalEntry.class);
            if (entry == null) return;
            List<JournalEntry> newEntryList = new ArrayList<>(mEntries.getValue());
            newEntryList.add(entry);
            mEntries.setValue(newEntryList);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            JournalEntry changedEntry = dataSnapshot.getValue(JournalEntry.class);
            if (changedEntry == null) return;

            List<JournalEntry> newEntryList = new ArrayList<>(mEntries.getValue());

            for(int i = 0; i < newEntryList.size(); i++){
                JournalEntry entry = newEntryList.get(i);
                if (entry.id.equals(changedEntry.id)){
                    newEntryList.set(i, changedEntry);
                }
            }
            mEntries.setValue(newEntryList);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            JournalEntry removedEntry = dataSnapshot.getValue(JournalEntry.class);
            if (removedEntry == null) return;
            List<JournalEntry> newEntryList = new ArrayList<>(mEntries.getValue());

            for(int i = 0; i < newEntryList.size(); i++){
                JournalEntry entry = newEntryList.get(i);
                if (entry.id.equals(removedEntry.id)) {
                    newEntryList.remove(i);
                }
            }
            mEntries.setValue(newEntryList);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("Database erorr", "An error occured during databse operations",databaseError.toException());
        }
    };

    public JournalViewModel(@NonNull Application application) {
        super(application);
        Log.d("View model", "in model");

        journalDatabase = FireBaseJournalDatabase.getInstance();
        journalDatabase.getReference().addChildEventListener(childEventListener);
        mEntries.setValue(new ArrayList<JournalEntry>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        journalDatabase.getReference().removeEventListener(childEventListener);

    }

    public LiveData<List<JournalEntry>> getEntries(){
        return mEntries;
    }

    public void addEntry(JournalEntry entry){
        journalDatabase.addEntry(entry);
    }

    public void updateEntry(JournalEntry entry){
        journalDatabase.updateEntry(entry);
    }

    public void deleteEntry(String id){
        journalDatabase.deleteEntry(id);
    }

    public void destroy(){
        this.onCleared();

    }

}
