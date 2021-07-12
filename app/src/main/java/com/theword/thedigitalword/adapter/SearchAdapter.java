package com.theword.thedigitalword.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Viewholder> {

    private Context context;
    private ArrayList<SearchModel> searchList;
    SharedPreferences sharedPreferences = null;

    public SearchAdapter(Context context, ArrayList<SearchModel> searchList) {
        this.context = context;
        this.searchList = searchList;
        sharedPreferences = this.context.getSharedPreferences(this.context.getString(R.string.app_id), Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.srchcard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        SearchModel model = searchList.get(position);
        //Set the view
        String chapter = model.getBookName()+" " +model.getChapter();
        String engChapter = model.getEnglishName()+" "+model.getChapter();
        String dir = model.getDirection();
        holder.srchChapter.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        holder.srchChapter.setText(chapter);
        if(model.getId()!=null && !model.getId().trim().equalsIgnoreCase("ENG")){
            holder.engChapter.setText(engChapter);
        }
        //Set the content
        holder.srchContent.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        holder.srchContent.setText(model.getContent());
        setHighLightedText(holder.srchContent,model.getQuery());

        //Set the click listener
        holder.srchChapter.setOnClickListener(v -> {
            SharedPreferencesUtil.setBook(sharedPreferences,this.context, model.getBookNumber());
            SharedPreferencesUtil.setChapter(sharedPreferences,this.context,model.getChapter());
            Intent i = new Intent(this.context, HomeActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public SearchModel getItem(int pos){
        return searchList.get(pos);
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView srchChapter, engChapter, srchContent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            srchChapter = itemView.findViewById(R.id.srchchapter);
            srchContent = itemView.findViewById(R.id.srchContent);
            engChapter = itemView.findViewById(R.id.srchEngchapter);
        }
    }


    public void setHighLightedText(TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString().trim().toUpperCase();
        int ofe = tvt.indexOf(textToHighlight.toUpperCase(), 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight.toUpperCase(), ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }
}
