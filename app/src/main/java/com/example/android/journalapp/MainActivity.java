package com.example.android.journalapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.journalapp.FireBaseAuth.SignInActivity;
import com.example.android.journalapp.Fragments.EntryListFragment;
import com.example.android.journalapp.ViewModel.JournalViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static String USER_ID;
    public static String USER_NAME;


    JournalViewModel viewModel;
    FloatingActionButton addNewBtn;
    EntryListFragment entryListFragment;


    FirebaseAuth mFireBaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setUpAuth sets event listener that calls the setUpUi method that upaates MainActivity's UI
        setUpAuth();

    }
    @Override
    protected void onStart() {
        super.onStart();
        // Add Authentication state listener that triggers UI updates when a user is logged in
        mFireBaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove Authentication state listener
        mFireBaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu: mFireBaseAuth.signOut();
                return true;
            default: break;
        }
        return false;
    }



    void gotoAddNewPage(){
        Intent intent = new Intent(this, AddJournalActivity.class);
        startActivity(intent);
    }

    void setUpAuth(){
        mFireBaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                else{
                    USER_ID = firebaseAuth.getUid();
                    USER_NAME = firebaseAuth.getCurrentUser().getDisplayName();
                    setUpUi();
                }
            }
        };
    }

    void setUpUi(){
        // Database and common operations have been setup
        viewModel = ViewModelProviders.of(MainActivity.this).get(JournalViewModel.class);

        addNewBtn = (FloatingActionButton) findViewById(R.id.addNewEntry);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddNewPage();
            }
        });

        entryListFragment = new EntryListFragment();
        entryListFragment.setViewModelData(viewModel);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_container, entryListFragment)
                .commit();
    }

}
