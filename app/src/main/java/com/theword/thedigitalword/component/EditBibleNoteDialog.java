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

public class EditBibleNoteDialog extends DialogFragment {
    public static String TAG = "EditBibleNoteDialog";

    Context context = null;
    String dir = "";
    String id = "";

    TextView cross = null;
    TextView selectedVerses = null;
    EditText title, note = null;
    EditText link = null;
    Button save = null;
    Button cancel = null;

    public EditBibleNoteDialog(){
        super();
    }

    public static EditBibleNoteDialog newInstance(BibleDBContent referenceCode, String displayVerses) {
        EditBibleNoteDialog frag = new EditBibleNoteDialog();
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
        return inflater.inflate(R.layout.fragment_editbiblenote, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            context = getContext();
            //Dialog Components
            cross = view.findViewById(R.id.nebview);
            selectedVerses = view.findViewById(R.id.nebversetext);
            title = view.findViewById(R.id.nebTitle_E);
            link = view.findViewById(R.id.nebLink_E);
            note = view.findViewById(R.id.nebiblenote_E);
            save = view.findViewById(R.id.nebook_save);
            cancel = view.findViewById(R.id.nebook_cancel);
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

            //Save
            save.setOnClickListener(v -> {
                String titleString = title.getText().toString();
                String noteString = note.getText().toString();
                if(titleString==null||titleString.trim().equals("") ) {
                    Toast.makeText(context,
                            "Please enter a Note Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(noteString==null||noteString.trim().equals("") ) {
                    Toast.makeText(context,
                            "Please enter a Note", Toast.LENGTH_SHORT).show();
                    return;
                }
                String linkString = link.getText().toString();
                model.setTitle(titleString);
                model.setLink(linkString);
                model.setBibleNote(noteString);
                BibleHelper helper = new BibleHelper(context);
                //Add new
                helper.updateBibleNote(model);
                Toast.makeText(context, "Bible Note successfully updated.",
                        Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                //refresh
                Intent data = new Intent(context, NotesActivity.class);
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
