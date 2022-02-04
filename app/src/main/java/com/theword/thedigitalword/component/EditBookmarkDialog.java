package com.theword.thedigitalword.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.theword.thedigitalword.BookmarkActivity;
import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.Calendar;

public class EditBookmarkDialog extends DialogFragment {
    public static String TAG = "EditBookmarkDialog";

    Context context = null;
    String dir = "";
    String id = "";

    TextView cross = null;
    TextView selectedVerses = null;
    EditText title = null;
    EditText link = null;
    Button save = null;
    Button cancel = null;

    public EditBookmarkDialog(){
        super();
    }

    public static EditBookmarkDialog newInstance(BibleDBContent referenceCode, String displayVerses) {
        EditBookmarkDialog frag = new EditBookmarkDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putSerializable("model", referenceCode);
        args.putString("displayVerses", displayVerses);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editbookmark, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            context = getContext();
            //Dialog Components
            cross = view.findViewById(R.id.ebview);
            selectedVerses = view.findViewById(R.id.ebversetext);
            title = view.findViewById(R.id.ebTitle_E);
            link = view.findViewById(R.id.ebLink_E);
            save = view.findViewById(R.id.ebook_save);
            cancel = view.findViewById(R.id.ebook_cancel);
            //Set Text Direction
            selectedVerses.setTextDirection((direction!=null && direction.trim().equalsIgnoreCase("RTL"))?View.TEXT_DIRECTION_RTL:View.TEXT_DIRECTION_LTR);

            // Fetch arguments from bundle and set title
            BibleDBContent model = (BibleDBContent)getArguments().getSerializable("model");
            String displayText = getArguments().getString("displayVerses", "");

            selectedVerses.setText(displayText);
            title.setText(model.getTitle());
            link.setText(model.getLink());

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
                model.setTitle(titleString);
                model.setLink(linkString);
                BibleHelper helper = new BibleHelper(context);
                //Add new
                helper.updateBookmark(model);
                Toast.makeText(context, "Bookmark successfully updated.",
                        Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                //refresh
                Intent data = new Intent(context, BookmarkActivity.class);
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
