package com.theword.thedigitalword.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.HightlightActivity;
import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.component.ChangeHighlightDialog;
import com.theword.thedigitalword.component.CrossRefDialog;
import com.theword.thedigitalword.component.ViewVerseDialog;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.Viewholder> {

    FragmentManager fm = null;
    private Context context;
    private List<BibleDBContent> noteList;
    SharedPreferences sharedPreferences = null;

    public HighlightAdapter(Context context, List<BibleDBContent> noteList) {
        this.context = context;
        this.noteList = noteList;
        sharedPreferences = this.context.getSharedPreferences(this.context.getString(R.string.app_id), Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlightcard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        BibleDBContent model = noteList.get(position);

        //Set the view
        String color = model.getColor();
        String book = model.getBibleBook();
        String chapter = model.getBibleChapter();
        String verse = model.getBibleVerse();
        String title = "";
        String engTitle = "";

        String name = ApplicationData.getBook(book);
        String engName = ApplicationData.getEngBook(book);
        String lang = SharedPreferencesUtil.getLanguage(sharedPreferences,context);

        if(lang!=null && !lang.equalsIgnoreCase("ENG")){
            title = name +" " +chapter+":"+verse;
            engTitle = engName +" " +chapter+":"+verse;
        }else{
            title = name +" " +chapter+":"+verse;
        }
        long time = Long.valueOf(model.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy HH:mm a");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        String stringTime = sdf.format(c.getTime());

        holder.noteTitle.setText(title);
        if(lang!=null && !lang.equalsIgnoreCase("ENG")) {
            holder.engTitle.setVisibility(View.VISIBLE);
            holder.engTitle.setText(engTitle);
        }else{
            holder.engTitle.setVisibility(View.GONE);
        }
        holder.content.setBackgroundColor(Color.parseColor(color));
        holder.noteTime.setText(stringTime);

        //Title and Content takes to full page
        holder.noteTitle.setOnClickListener(v -> {
            SharedPreferencesUtil.setBook(sharedPreferences,this.context, model.getBibleBook());
            SharedPreferencesUtil.setChapter(sharedPreferences,this.context,model.getBibleChapter());
            Intent i = new Intent(this.context, HomeActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(i);
        });

        holder.verse.setOnClickListener(v -> {
            ViewVerseDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                String code = model.getBibleBook() +" "+model.getBibleChapter()+":"+model.getBibleVerse();
                verseDialogFragment = ViewVerseDialog.newInstance(code);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        //Set the click listener
        holder.edit.setOnClickListener(v -> {
            ChangeHighlightDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                String t = name +" "+model.getBibleChapter()+":"+model.getBibleVerse();
                verseDialogFragment = ChangeHighlightDialog.newInstance(model,t);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BibleHelper _noteHelper = new BibleHelper(context);
                            _noteHelper.deleteHighlight(model);
                            Intent data = new Intent(context, HightlightActivity.class);
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

    public BibleDBContent  getItem(int pos){
        return noteList.get(pos);
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView noteTitle, engTitle, noteTime,verse;
        private ImageView content, edit, delete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.highlightTitle);
            engTitle = itemView.findViewById(R.id.highlightEng);
            content = itemView.findViewById(R.id.highlightColor);
            noteTime = itemView.findViewById(R.id.highlightTime);
            edit = itemView.findViewById(R.id.highlightEdit);
            delete = itemView.findViewById(R.id.highlightDelete);
            verse = itemView.findViewById(R.id.highverse);
        }
    }
}
