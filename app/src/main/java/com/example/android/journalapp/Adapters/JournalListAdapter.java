package com.example.android.journalapp.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.journalapp.AddJournalActivity;
import com.example.android.journalapp.JournalEntry;
import com.example.android.journalapp.MainActivity;
import com.example.android.journalapp.R;
import com.example.android.journalapp.ViewModel.JournalViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder> {
    // TODO Implement Click on the recyclerView Items with this Interface
    public interface OnClick{
        void clicked(int position);
    }
    OnClick callback;
    private List<JournalEntry> entryList;

    public JournalListAdapter(List<JournalEntry> entryList){
        this.entryList = entryList;
    }

    public void setCallback(OnClick callback){
        this.callback = callback;
    }

    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_list_item, parent, false);


        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        JournalEntry entry = entryList.get(position);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date created = new Date();
        created.setTime(entry.dateCreated);

        // Set the views value
        Date date = new Date(entryList.get(position).dateUpdated);
        DateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        holder.singleDayView.setText(dayFormat.format(date));
        holder.dateCreated.setText(dateFormat.format(created));
        holder.description.setText(entry.title);


    }


    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView singleDayView;
        TextView dateCreated;
        TextView description;

        JournalViewHolder(View itemView){
            super(itemView);
            singleDayView = (TextView) itemView.findViewById(R.id.singleDay);
            dateCreated = (TextView)itemView.findViewById(R.id.dateView);
            description = (TextView)itemView.findViewById(R.id.descView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            callback.clicked(position);
        }

    }

}
