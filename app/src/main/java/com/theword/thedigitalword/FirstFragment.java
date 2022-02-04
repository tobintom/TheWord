package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.theword.thedigitalword.adapter.BookCardArrayAdapter;
import com.theword.thedigitalword.model.Book;
import com.theword.thedigitalword.model.Card;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private BookCardArrayAdapter cardArrayAdapter;
    private ListView listView;
    SharedPreferences sharedPreferences = null;

    public FirstFragment(){

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        int pos = 0;
       // this.getActivity().setContentView(R.layout.fragment_first);
        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this.getActivity(),SharedPreferencesUtil.getTheme(sharedPreferences,this.getContext()));
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) this.getActivity().findViewById(R.id.book_listView);
        Context context = this.getContext();
        cardArrayAdapter = new BookCardArrayAdapter(this.getContext(),R.layout.book_item_card);
        String selectedBook = SharedPreferencesUtil.getBook(this.getActivity().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),this.getContext());
        if(Util.getSelectedBook()==null || Util.getSelectedBook().trim().length()==0) {
            Util.setSelectedBook(selectedBook);
        }

        //Process on separate Thread
        new Thread(){
            @Override
            public void run() {
                try {
                    String jsonData = TheWordMetaService.getBooks(sharedPreferences,
                            context);
                    if(jsonData!=null && jsonData.trim().length()>0) {
                        JSONObject obj = new JSONObject(jsonData);
                        String dir = obj.getString("direction");
                        String lang = obj.getString("id");
                        JSONArray arr = obj.getJSONArray("books");
                        for(int i=0;i<arr.length();i++){
                            JSONObject o = (JSONObject) arr.get(i);
                            String name = o.getString("name");
                            String english = o.getString("english");
                            String id = o.getString("number");
                            boolean isSelected = selectedBook.equalsIgnoreCase(id);
                            Book card = new Book(id,name,english,dir,lang);
                            cardArrayAdapter.add(card);
                            if(isSelected){
                                cardArrayAdapter.setSelectedBook(id);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Update UI Components
                            listView.setAdapter(cardArrayAdapter);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();




        listView.setOnItemClickListener((parent, viewa, position, id) ->
        {
            String book = cardArrayAdapter.getItem(position).getBookIcon();
            listView.setItemChecked(position,true);
            Util.setSelectedBook(book);
            TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabhost.getTabAt(1).select();

            //SharedPreferencesUtil.setBook(sharedPreferences,this.getContext(),book);
//            Intent i = new Intent(this,HomeActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
        });
    }

}