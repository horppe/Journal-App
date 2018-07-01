package com.example.android.journalapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intercepting.SingleActivityFactory;

import com.example.android.journalapp.Database.JournalDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class JournalSQLiteDatabaseTest {
    Context mActivity;
    JournalEntry mEntry;

    @Before public void setupContext(){
        mActivity = InstrumentationRegistry.getTargetContext();
    }
    @Before public void setmEntry(){
        String id = "id";
        String title = "title";
        String entryText = "entryText";
        long dateCreated = Calendar.getInstance().getTime().getTime();
        long dateUpdated = Calendar.getInstance().getTime().getTime();
        mEntry = new JournalEntry(id, title, entryText, dateCreated, dateUpdated);
    }

    @Test
    public void createDatabase(){
        JournalDatabase db = new JournalDatabase(mActivity);
        assertEquals(true, db.getReadableDatabase().isOpen());
    }

    @Test public void shouldAddToDatabase(){
        boolean expectedResult = true;
        JournalDatabase db = new JournalDatabase(mActivity);
        boolean result = db.addEntry(mEntry);
        assertEquals(expectedResult, result);
    }

    @Test public void  shouldUpdateDatabase(){
        boolean expectedResult = true;
        JournalDatabase db = new JournalDatabase(mActivity);
        mEntry.title = "Changed";
        boolean result = db.updateEntry(mEntry);
        assertEquals(expectedResult, result);
    }

    @Test public void shouldDeleteFromDatabase(){
        boolean expectedResult = true;
        JournalDatabase db = new JournalDatabase(mActivity);
        String id = mEntry.id;
        db.addEntry(mEntry);
        boolean result = db.deleteEntry(id);
        assertEquals(expectedResult, result);
    }
}
