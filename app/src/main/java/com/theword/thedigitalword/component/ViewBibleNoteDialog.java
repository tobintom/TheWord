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

import com.theword.thedigitalword.NotesActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

public class ViewBibleNoteDialog extends DialogFragment {
    public static String TAG = "ViewBibleNoteDialog";

    Context context = null;
    String dir = "";
    String id = "";

    TextView cross = null;
    TextView selectedVerses = null;
    TextView title, note = null;
    TextView link = null;
    Button cancel = null;

    public ViewBibleNoteDialog(){
        super();
    }

    public static ViewBibleNoteDialog newInstance(BibleDBContent referenceCode, String displayVerses) {
        ViewBibleNoteDialog frag = new ViewBibleNoteDialog();
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
        return inflater.inflate(R.layout.fragment_viewbiblenote, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            context = getContext();
            //Dialog Components
            cross = view.findViewById(R.id.vebview);
            selectedVerses = view.findViewById(R.id.vebversetext);
            title = view.findViewById(R.id.vebTitle_E);
            link = view.findViewById(R.id.vebLink_E);
            note = view.findViewById(R.id.vebiblenote_E);
            cancel = view.findViewById(R.id.vebook_cancel);
            //Set Text Direction
            selectedVerses.setTextDirection((direction!=null && direction.trim().equalsIgnoreCase("RTL"))?View.TEXT_DIRECTION_RTL:View.TEXT_DIRECTION_LTR);

            // Fetch arguments from bundle and set title
            BibleDBContent model = (BibleDBContent)getArguments().getSerializable("model");
            String displayText = getArguments().getString("displayVerses", "");

            selectedVerses.setText(displayText);
            title.setText(model.getTitle());
            link.setText(model.getLink());
            note.setText(model.getBibleNote());

            //Close
            cross.setOnClickListener(v -> {
                        getDialog().dismiss();
                    });

            //Cancel
            cancel.setOnClickListener(v -> {
                getDialog().dismiss();
            });

        }catch(Exception e){
            getDialog().dismiss();
        }
    }
}
