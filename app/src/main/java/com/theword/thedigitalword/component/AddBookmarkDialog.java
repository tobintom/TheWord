package com.theword.thedigitalword.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.adapter.CrossReferenceAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBookmarkDialog extends DialogFragment {
    public static String TAG = "AddBookmarkDialog";

    Context context = null;
    String dir = "";
    String id = "";

    TextView cross = null;
    TextView selectedVerses = null;
    EditText title = null;
    EditText link = null;
    Button save = null;
    Button cancel = null;

    public AddBookmarkDialog(){

    }

    public static AddBookmarkDialog newInstance(String bookChapter, String verses) {
        AddBookmarkDialog frag = new AddBookmarkDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putString("bookChapter", bookChapter);
        args.putString("verses", verses);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addbookmark, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            context = getContext();
            //Dialog Components
            cross = view.findViewById(R.id.bview);
            selectedVerses = view.findViewById(R.id.bversetext);
            title = view.findViewById(R.id.bTitle_E);
            link = view.findViewById(R.id.bLink_E);
            save = view.findViewById(R.id.book_save);
            cancel = view.findViewById(R.id.book_cancel);
            //Set Text Direction
            selectedVerses.setTextDirection((direction!=null && direction.trim().equalsIgnoreCase("RTL"))?View.TEXT_DIRECTION_RTL:View.TEXT_DIRECTION_LTR);

            // Fetch arguments from bundle and set title
            String bookChapter = getArguments().getString("bookChapter", "");
            String verses = getArguments().getString("verses", "");
            String displayText = bookChapter + Util.ConvertToRanges(verses);
            selectedVerses.setText(displayText);

            //Close
            cross.setOnClickListener(v -> {
                        getDialog().dismiss();
                    });

            //Cancel
            cancel.setOnClickListener(v -> {
                getDialog().dismiss();
            });

            //Save
            save.setOnClickListener(v -> {
                String titleString = title.getText().toString();
                if(titleString==null||titleString.trim().equals("") ) {
                    Toast.makeText(context,
                            "Please enter a Bookmark Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                String linkString = link.getText().toString();
                BibleDBContent model = new BibleDBContent();
                model.setTitle(titleString);
                model.setLink(linkString);
                model.setBibleVerse(verses);
                model.setBibleChapter(SharedPreferencesUtil.getChapter(sharedPreferences,context));
                model.setBibleBook(SharedPreferencesUtil.getBook(sharedPreferences,context));
                model.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                BibleHelper helper = new BibleHelper(context);
                //Add new
                helper.addBookMark(model);
                Toast.makeText(context, "Bookmark successfully saved.",
                        Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                //refresh
                Intent data = new Intent(context,HomeActivity.class);
                // Request MainActivity refresh its ListView (or not).
                data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Set Result
                startActivity(data);
            });

        }catch(Exception e){
            getDialog().dismiss();
        }
    }




}
