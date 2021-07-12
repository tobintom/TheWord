package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.theword.thedigitalword.adapter.ChapterAdapter;
import com.theword.thedigitalword.model.Book;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class First2Fragment extends Fragment {


    SharedPreferences sharedPreferences = null;

    public First2Fragment(){

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first2, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView)this.getActivity().findViewById(R.id.chaptergrid);
        TextView text = (TextView)this.getActivity().findViewById(R.id.chapterbook);
        ChapterAdapter chapterAdapter = new ChapterAdapter(this.getContext(), new ArrayList());

        //this.getActivity().setContentView(R.layout.fragment_first2);
        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        try {
            String jsonData = TheWordMetaService.getChapters(this.getActivity().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                    this.getContext(),Util.getSelectedBook());
            if(jsonData!=null && jsonData.trim().length()>0) {
                JSONObject obj = new JSONObject(jsonData);
                String lang = obj.getString("id");
                String bname= obj.getString("name");
                text.setText(bname);
                text.setTypeface(null,Typeface.BOLD);
                JSONArray arr = obj.getJSONArray("chapters");
                for(int i=0;i<arr.length();i++){
                    String chapter =   arr.getString(i);
                    chapterAdapter.addChapter(chapter);
                }
            }
        }catch (Exception e){

        }
        gridView.setAdapter(chapterAdapter);
        gridView.setOnItemClickListener((parent, view1, position, id) ->
        {
            String selectedChapter = chapterAdapter.getItem(position);
            String selectedBook = Util.getSelectedBook();
            SharedPreferencesUtil.setBook(sharedPreferences,this.getContext(), selectedBook);
            SharedPreferencesUtil.setChapter(sharedPreferences,this.getContext(),selectedChapter);
            Util.setSelectedBook(null);
            Intent i = new Intent(this.getContext(),HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }

    //Used to recreate view when tab is selected
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            FragmentTransaction ftr = getFragmentManager().beginTransaction();
            ftr.detach(this).attach(this).commit();
        }
    }


}