package com.theword.thedigitalword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.model.Book;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<String> chapterList = null;


    public ChapterAdapter(Context context, ArrayList chapters){
        this.chapterList = chapters;
        this.mContext = context;
    }

    public void addChapter(String chapter){
        chapterList.add(chapter);
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public String getItem(int position) {
        return chapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_item, parent, false);
        }
        String chapter = getItem(position);
        final TextView name = (TextView) convertView.findViewById(R.id.chapter);
        name.setText(chapter);
        return convertView;
    }
}
