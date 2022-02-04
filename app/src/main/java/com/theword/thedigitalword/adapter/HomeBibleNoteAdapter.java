package com.theword.thedigitalword.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.HomeBookmarkActivity;
import com.theword.thedigitalword.HomeNotesActivity;
import com.theword.thedigitalword.NotesActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.component.EditBibleNoteDialog;
import com.theword.thedigitalword.component.EditHomeBibleNoteDialog;
import com.theword.thedigitalword.component.EditHomeBookmarkDialog;
import com.theword.thedigitalword.component.ViewBibleNoteDialog;
import com.theword.thedigitalword.component.ViewVerseDialog;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeBibleNoteAdapter extends RecyclerView.Adapter<HomeBibleNoteAdapter.Viewholder> {

    FragmentManager fm = null;
    private Context context;
    private List<BibleDBContent> noteList;
    SharedPreferences sharedPreferences = null;
    private String bookName = "";
    private String engName = "";
    private String verse = "";

    public HomeBibleNoteAdapter(Context context, List<BibleDBContent> noteList,String bookName, String engName,String verse) {
        this.context = context;
        this.bookName = bookName;
        this.engName = engName;
        this.noteList = noteList;
        this.verse = verse;
        sharedPreferences = this.context.getSharedPreferences(this.context.getString(R.string.app_id), Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homebiblenotescard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        BibleDBContent model = noteList.get(position);

        String book = model.getBibleBook();
        String chapter = model.getBibleChapter();
        String verses = model.getBibleVerse();

        String title = "";
        String engTitle = "";

        String lang = SharedPreferencesUtil.getLanguage(sharedPreferences,context);
        String displayVerses = Util.ConvertToRanges(verses);

        if(lang!=null && !lang.equalsIgnoreCase("ENG")){
            title = bookName +" " +chapter+":"+displayVerses;
            engTitle = engName +" " +chapter+":"+displayVerses;
        }else{
            title = bookName +" " +chapter+":"+displayVerses;
        }
        long time = Long.valueOf(model.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy HH:mm a");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        String stringTime = sdf.format(c.getTime());

        holder.bookVerse.setText(title);
        if(lang!=null && !lang.equalsIgnoreCase("ENG")) {
            holder.bookEng.setVisibility(View.VISIBLE);
            holder.bookEng.setText(engTitle);
        }else{
            holder.bookEng.setVisibility(View.GONE);
        }
        String finalTitle = title;

        holder.noteTime.setText(stringTime);
        holder.bookTitle.setText(model.getTitle());
        holder.bookLink.setText(model.getLink());

        holder.bookVerse.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        holder.bookEng.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        holder.noteTime.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        holder.bookTitle.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        holder.bookLink.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        String content = model.getBibleNote();
        String displayedContent = "";
        String interimContent = content!=null?content:"";
        interimContent = interimContent.replaceAll(System.getProperty("line.separator")," ");
        if(interimContent!=null && interimContent.trim().length()>35) {
            displayedContent = interimContent.substring(0,35);
            displayedContent = displayedContent + "...";
        }else{
            displayedContent = interimContent;
        }

        holder.note.setText(displayedContent);
        holder.note.setOnClickListener(v -> {
            ViewBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                verseDialogFragment = ViewBibleNoteDialog.newInstance(model, finalTitle);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        //Read Verses
        holder.verse.setOnClickListener(v -> {
            ViewVerseDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();
                String code = model.getBibleBook() +" "+model.getBibleChapter()+":"+ Util.ConvertToRanges(model.getBibleVerse());
                verseDialogFragment = ViewVerseDialog.newInstance(code);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
        });

        //Delete
        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this Bible Note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BibleHelper _noteHelper = new BibleHelper(context);
                            _noteHelper.deleteBibleNote(model);
                            Intent data = new Intent(context, HomeNotesActivity.class);
                            data.putExtra("verse",verse);
                            data.putExtra("bookName",bookName);
                            data.putExtra("bookEng",engName);
                            // Request MainActivity refresh its ListView (or not).
                            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Set Result
                            context.startActivity(data);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        //Remove
        holder.remove.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to remove verse "+verse +" from this Bible Note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BibleHelper _noteHelper = new BibleHelper(context);
                            String verses = model.getBibleVerse();
                            List arr = new ArrayList();
                            arr.addAll(Arrays.asList((verses).split(",",-1)));
                            if(arr!=null){
                                arr.remove(verse);
                                if(arr.isEmpty()){
                                    _noteHelper.deleteBibleNote(model);
                                }else {
                                    String commaseparatedlist = arr.toString().replace("[", "")
                                            .replace("]", "")
                                            .replace(" ", "");
                                    model.setBibleVerse(commaseparatedlist);
                                    _noteHelper.updateBibleNoteVerses(model);
                                }
                            }
                            Intent data = new Intent(context, HomeNotesActivity.class);
                            data.putExtra("verse",verse);
                            data.putExtra("bookName",bookName);
                            data.putExtra("bookEng",engName);
                            // Request MainActivity refresh its ListView (or not).
                            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Set Result
                            context.startActivity(data);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        //Edit
        holder.edit.setOnClickListener(v -> {
            EditHomeBibleNoteDialog verseDialogFragment = null;
            try {
                fm = ((AppCompatActivity)context).getSupportFragmentManager();

                verseDialogFragment = EditHomeBibleNoteDialog.newInstance(model, finalTitle,bookName,engName,verse);
                verseDialogFragment.show(fm, ViewVerseDialog.TAG);
            }catch (Exception e){
                verseDialogFragment.dismiss();
            }
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

        private TextView bookTitle, bookLink, bookEng, bookVerse, noteTime, verse, note;
        private ImageView  edit, delete, remove;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.hnbooktitle);
            bookLink = itemView.findViewById(R.id.hnbooklink);
            bookEng = itemView.findViewById(R.id.hnbookEng);
            bookVerse = itemView.findViewById(R.id.hnbookVerses);
            noteTime = itemView.findViewById(R.id.hnbookTime);
            verse = itemView.findViewById(R.id.hnbookverse);
            note = itemView.findViewById(R.id.hnbookNote);
            edit = itemView.findViewById(R.id.hnbookEdit);
            delete = itemView.findViewById(R.id.hnbookDelete);
            remove = itemView.findViewById(R.id.hnoteRemove);
        }
    }
}
