package com.theword.thedigitalword.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.AddEditNote;
import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.PersonalNoteActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.db.PersonalNotesHelper;
import com.theword.thedigitalword.model.PersonalNote;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PersonalNoteAdapter extends RecyclerView.Adapter<PersonalNoteAdapter.Viewholder> {

    private Context context;
    private List<PersonalNote> noteList;
    SharedPreferences sharedPreferences = null;

    public PersonalNoteAdapter(Context context, List<PersonalNote> noteList) {
        this.context = context;
        this.noteList = noteList;
        sharedPreferences = this.context.getSharedPreferences(this.context.getString(R.string.app_id), Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notecard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        PersonalNote model = noteList.get(position);

        //Set the view
        String title = model.getNoteTitle();
        String content = model.getNoteContent();
        long time = Long.valueOf(model.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy HH:mm a");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        String stringTime = sdf.format(c.getTime());

        String displayedContent = "";
        String interimContent = content!=null?content:"";
        interimContent = interimContent.replaceAll(System.getProperty("line.separator")," ");
        if(interimContent!=null && interimContent.trim().length()>35) {
            displayedContent = interimContent.substring(0,35);
            displayedContent = displayedContent + "...";
        }else{
            displayedContent = interimContent;
        }
        holder.noteTitle.setText(title);
        holder.noteContent.setText(displayedContent);
        holder.noteTime.setText(stringTime);

        //Title and Content takes to full page
        holder.noteTitle.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditNote.class);
            intent.putExtra("mode", "V");
            intent.putExtra("note", model);
            context.startActivity(intent);
        });

        holder.noteContent.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditNote.class);
            intent.putExtra("mode", "V");
            intent.putExtra("note", model);
            context.startActivity(intent);
        });

        //Set the click listener
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditNote.class);
            intent.putExtra("mode", "E");
            intent.putExtra("note", model);
            context.startActivity(intent);
        });

        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PersonalNotesHelper _noteHelper = new PersonalNotesHelper(context);
                            _noteHelper.deleteNote(model);
                            Intent data = new Intent(context, PersonalNoteActivity.class);
                            // Request MainActivity refresh its ListView (or not).
                            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Set Result
                            context.startActivity(data);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public PersonalNote getItem(int pos){
        return noteList.get(pos);
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView noteTitle, noteContent, noteTime;
        private ImageView edit, delete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            noteTime = itemView.findViewById(R.id.noteTime);
            edit = itemView.findViewById(R.id.noteEdit);
            delete = itemView.findViewById(R.id.noteDelete);
        }
    }
}
