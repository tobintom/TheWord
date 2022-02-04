package com.theword.thedigitalword.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.adapter.CrossReferenceAdapter;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewVerseDialog extends DialogFragment {
    public static String TAG = "ViewVerseDialog";
    private TextView cross = null;
    private TextView crossText = null;
    private TextView crossHint = null;
    private RecyclerView srchView = null;
    private ArrayList srchList = null;
    Context context = null;
    boolean hasData = false;
    String dir = "";
    String id = "";

    public ViewVerseDialog(){

    }

    public static ViewVerseDialog newInstance(String referenceCode) {
        ViewVerseDialog frag = new ViewVerseDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putString("code", referenceCode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewverse, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            cross = (TextView) view.findViewById(R.id.view);
            crossHint = (TextView) view.findViewById(R.id.versetxthint);
            srchView = view.findViewById(R.id.idverses);
            srchList = new ArrayList();
            context = getContext();

            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            // Fetch arguments from bundle and set title
            String code = getArguments().getString("code", "");

            cross.setOnClickListener(v -> {
                        getDialog().dismiss();
                    });
            //Search for Cross References
            if (code != null && code.trim().length() > 0) {
                String bookCode = code.split(" ")[0];
                String chapterCode = code.split(" ")[1].split(":")[0];
                String verseCode = code.split(" ")[1].split(":")[1];
                //Service call to get references
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                String searchData = TheWordContentService.getPassages(sharedPreferences,getContext(),code);
                                if(searchData!=null && searchData.trim().length()>0) {
                                    String bookName = "";
                                    String englishName = "";
                                    String bookNumber = "";
                                    String chapter = "";
                                    String chapterText = "";
                                    JSONObject searchObject = new JSONObject(searchData);
                                    String searchResult = searchObject.getString("status");
                                    if (searchResult != null && searchResult.trim().equalsIgnoreCase("SUCCESS")) {
                                        hasData = true;
                                        //Parse Search Results
                                        dir = searchObject.getString("dir");
                                        id = searchObject.getString("id");
                                        JSONArray oArray = searchObject.getJSONArray("passages");
                                        for (int i = 0; i < oArray.length(); i++) {
                                            JSONObject ol = (JSONObject) oArray.get(i);
                                            bookName = ol.getString("name");
                                            bookNumber = ol.getString("book");
                                            englishName = ol.getString("english");
                                            //Get all the Chapters
                                            JSONArray chapters = ol.getJSONArray("content");
                                            for (int c = 0; c < chapters.length(); c++) {
                                                JSONObject content = (JSONObject) chapters.get(c);
                                                chapter = content.getString("chapter");
                                                chapterText = "";
                                                JSONArray verses = content.getJSONArray("verses");
                                                for (int v = 0; v < verses.length(); v++) {
                                                    JSONObject verse = (JSONObject) verses.get(v);
                                                    chapterText = chapterText + verse.getString("verse") + ".  " + verse.getString("text") + System.getProperty("line.separator");
                                                }
                                                //Build Adapter
                                                srchList.add(new SearchModel("", bookName, bookNumber, englishName, chapter, chapterText, dir, id));
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //Update UI
                             getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(hasData && !srchList.isEmpty()){
                                        crossHint.setText("Click on the list icon to read full chapter.");
                                        //Set Adapter
                                        CrossReferenceAdapter srchAdapter = new CrossReferenceAdapter(getContext(), srchList);
                                        // below line is for setting a layout manager for our recycler view.
                                        // here we are creating vertical list so we will provide orientation as vertical
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        // in below two lines we are setting layoutmanager and adapter to our recycler view.
                                        srchView.setLayoutManager(linearLayoutManager);
                                        srchView.setAdapter(srchAdapter);
                                    }else{
                                        crossHint.setText("Verse not Found...");
                                    }
                                }
                            });
                        }
                    }.start();
            } else {
                crossHint.setText("Verse not Found..");
            }
        }catch(Exception e){

        }
    }

}
