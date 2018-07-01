package com.example.android.journalapp.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.journalapp.Adapters.JournalListAdapter;
import com.example.android.journalapp.AddJournalActivity;
import com.example.android.journalapp.JournalEntry;
import com.example.android.journalapp.R;
import com.example.android.journalapp.ViewModel.JournalViewModel;

import java.util.List;

public class EntryListFragment extends Fragment implements JournalListAdapter.OnClick {

    LiveData<List<JournalEntry>> mEntryList;
    Observer<List<JournalEntry>> mObserver = new Observer<List<JournalEntry>>() {
        @Override
        public void onChanged(@Nullable List<JournalEntry> journalEntries) {
            adapter = new JournalListAdapter(journalEntries);
            adapter.setCallback(EntryListFragment.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };

    RecyclerView recyclerView;
    JournalViewModel mViewModel;

    JournalListAdapter adapter;

    public EntryListFragment(){
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_list_fragment,container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int index = viewHolder.getAdapterPosition();
                final JournalEntry entry = mEntryList.getValue().get(index);
                mViewModel.deleteEntry(entry.id); // TODO CORRECT
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void clicked(int position) {
        JournalEntry entry = mEntryList.getValue().get(position);
        Intent intent = new Intent(getContext(), AddJournalActivity.class);
        intent.putExtra(AddJournalActivity.ID_KEY, entry.id);
        intent.putExtra(AddJournalActivity.TITLE_KEY, entry.title);
        intent.putExtra(AddJournalActivity.ENTRY_KEY, entry.entry);
        intent.putExtra(AddJournalActivity.DATE_KEY, entry.dateCreated);
        intent.putExtra(AddJournalActivity.DATE_UPDATED_KEY, entry.dateUpdated);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void setViewModelData(JournalViewModel model){
        mViewModel = model;
        mEntryList = mViewModel.getEntries();
        mEntryList.observe(this, mObserver);
    }
}
