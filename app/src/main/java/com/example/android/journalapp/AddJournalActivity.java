package com.example.android.journalapp;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;

import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.journalapp.ViewModel.JournalViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddJournalActivity extends AppCompatActivity {
    public final static String DATE_KEY = "date-key";
    public final static String DATE_UPDATED_KEY = "date-updated-key";
    public final static String TITLE_KEY = "title-key";
    public final static String ENTRY_KEY = "entry-key";
    public final static String ID_KEY = "id-key";
    static int toog = 1; // start with visible state when clicked

    JournalViewModel mViewModel;

    CalendarView calendarView;

    ViewGroup updatedToogleBtn;
    ViewGroup createdToogleBtn;

    TextView createdText;
    TextView createdDate;

    TextView updatedText;
    TextView updatedDate;
    Button editBtn;
    View.OnClickListener editBtnSwtichListener;

    EditText mTitleText;
    EditText editText;

    // Journal entry variables
    String id;
    long dateCreated;
    long dateUpdated;
    boolean isNewEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_journal);

        mViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);



        setupViews();

        setUpDateAndEntryText();

        addTimeText();

        setListeners();

        // If this is a new Entry allow editing the EditText
        //
        if(isNewEntry){
            allowEdit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_journal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_btn: saveEntry();
                return true;
            case R.id.delete_btn:
                mViewModel.deleteEntry(id);
                finish();
                return true;
                default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setupViews(){
        updatedToogleBtn = (ViewGroup) findViewById(R.id.updated_toogleButton);
        createdToogleBtn = (ViewGroup) findViewById(R.id.created_toogleButton);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        mTitleText = (EditText) findViewById(R.id.title_text);
        editText = (EditText) findViewById(R.id.editNoteView);
        editBtn = (Button) findViewById(R.id.edit_btn);

        createdText = (TextView) findViewById(R.id.createdText);
        createdDate = (TextView) findViewById(R.id.createdDate);
        updatedText = (TextView) findViewById(R.id.updatedText);
        updatedDate = (TextView) findViewById(R.id.updatedDate);

    }

    void saveEntry(){
        String titleText = mTitleText.getText().toString();
        String entryText = editText.getText().toString();

        // if entryText is empty, show a toast and toogle the input keyboard
        if (TextUtils.isEmpty(entryText)){
            Toast.makeText(AddJournalActivity.this, "No entry in Journal", Toast.LENGTH_SHORT).show();
            toggleInput();
            allowEdit();
            return;
        }
        // If this is a new entry, get current time. Else use previous time for dateCreated
        long createdOn = isNewEntry ? Calendar.getInstance().getTime().getTime(): dateCreated;
        long updatedOn = Calendar.getInstance().getTime().getTime();
        JournalEntry entry = new JournalEntry("",titleText,entryText,createdOn, updatedOn );

        // If saving a new entry, Add it to the database, if not, Update the existing entry
        if (isNewEntry){
            mViewModel.addEntry(entry);
        }else {
            //Get the id from the Calling intent and set it in the entry object
            entry.id = getIntent().getStringExtra(ID_KEY);
            if((!entry.id.isEmpty())){
                mViewModel.updateEntry(entry);
                Toast.makeText(AddJournalActivity.this, "Entry updated", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    void toggleInput(){
        editText.requestFocus();
        InputMethodManager mInputMethodManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mInputMethodManger == null) return;

        mInputMethodManger.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    void addTimeText(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date updated = new Date();
        updated.setTime(dateUpdated);
        updatedDate.setText(dateFormat.format(updated));

        Date created = new Date();
        created.setTime(dateCreated);
        createdDate.setText(dateFormat.format(created));

    }

    void setUpDateAndEntryText(){
        Intent intent = getIntent();

        // Check if there is a dateCreated already;
        boolean hasDate = intent
                .getLongExtra(DATE_KEY, -1) != -1;

        if(hasDate){
            dateCreated = intent.getLongExtra(DATE_KEY, -1);
            id = intent.getStringExtra(ID_KEY);
        }
        else{
            // Create a new Date
            dateCreated = Calendar.getInstance().getTime().getTime();
        }

        // Check if there is a dateUpdated already
        boolean hasDateUpdated = intent
                .getLongExtra(DATE_UPDATED_KEY, -1) != -1;

        if(hasDateUpdated) dateUpdated = intent.getLongExtra(DATE_UPDATED_KEY, -1);
        else dateUpdated = Calendar.getInstance().getTime().getTime();

        String receivedText = intent.getStringExtra(ENTRY_KEY);
        boolean hasText = receivedText != null;
        if (hasText){
            editText.setText(receivedText);
        }

        // Determine if this entry is a new entry with the conditions above
        if(hasDate && hasDateUpdated && hasText){
            isNewEntry = false;
        }
        else{
            isNewEntry = true;
        }
        // Set title text from intent
        String tempTitleText = intent.getStringExtra(TITLE_KEY);
        if (tempTitleText != null){
            mTitleText.setText(tempTitleText);
        }
    }

    void setListeners(){
        updatedToogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDate(dateUpdated);
                toogleCalender();

            }});

        createdToogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDate(dateCreated);
                toogleCalender();
            }
        });

        setEditTextSwitch();

    }

    void setEditTextSwitch(){
        editBtnSwtichListener = new View.OnClickListener() {
            private int swt = 0;
            @Override
            public void onClick(View v) {
                if (swt == 0){
                    allowEdit();
                    swt = 1;
                } else if (swt == 1){
                    disallowEdit();
                    swt = 0;
                }

            }
        };

        editBtn.setOnClickListener(editBtnSwtichListener);
    }

    void allowEdit(){
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);

        mTitleText.setInputType(InputType.TYPE_CLASS_TEXT);
        mTitleText.setFocusable(true);
        mTitleText.setFocusableInTouchMode(true);
        toggleInput();
    }

    void disallowEdit(){
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);

        mTitleText.setInputType(InputType.TYPE_NULL);
        mTitleText.setFocusable(false);
        mTitleText.setFocusableInTouchMode(false);
    }

    @TargetApi(19)
    void toogleCalender(){
        TransitionManager.beginDelayedTransition(calendarView);
        if (toog == 1){
            calendarView.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 1));
            toog = 0;
        } else{
            calendarView.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0));
            toog = 1;
        }
        TransitionManager.beginDelayedTransition(calendarView);
    }
}
