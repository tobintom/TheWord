package com.theword.thedigitalword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theword.thedigitalword.db.PersonalNotesHelper;
import com.theword.thedigitalword.model.PersonalNote;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.Calendar;

public class AddEditNote extends AppCompatActivity {
    Context context = null;
    SharedPreferences sharedPreferences = null;

    private PersonalNote note;
    private String mode;
    private EditText noteTitleE;
    private EditText noteContentE;
    private TextView noteTitleV;
    private TextView noteContentV;
    private LinearLayout viewLayout;
    private LinearLayout editLayout;
    private Button viewCancel;
    private Button viewEdit;
    private Button viewDelete;
    private Button editSave;
    private Button editCancel;
    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        noteTitleE = findViewById(R.id.editTitle_E);
        noteContentE = findViewById(R.id.editText_E);
        noteTitleV = findViewById(R.id.viewTitle_V);
        noteContentV = findViewById(R.id.viewText_V);
        viewLayout = findViewById(R.id.linearLayoutV);
        editLayout = findViewById(R.id.linearLayoutE);

        viewCancel = findViewById(R.id.button_cancelV);
        viewEdit = findViewById(R.id.button_editV);
        viewDelete = findViewById(R.id.button_deleteV);
        editCancel = findViewById(R.id.button_cancel);
        editSave = findViewById(R.id.button_save);

        Intent intent = this.getIntent();
        mode = intent.getStringExtra("mode");
        this.note = (PersonalNote) intent.getSerializableExtra("note");

        //set the right view
        if(mode!=null && mode.trim().equalsIgnoreCase("E")){
            noteTitleV.setVisibility(View.INVISIBLE);
            noteContentV.setVisibility(View.INVISIBLE);
            viewLayout.setVisibility(View.INVISIBLE);
            noteTitleE.setVisibility(View.VISIBLE);
            noteContentE.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.VISIBLE);
        }else{
            noteTitleV.setVisibility(View.VISIBLE);
            noteContentV.setVisibility(View.VISIBLE);
            viewLayout.setVisibility(View.VISIBLE);
            noteTitleE.setVisibility(View.INVISIBLE);
            noteContentE.setVisibility(View.INVISIBLE);
            editLayout.setVisibility(View.INVISIBLE);
        }

        if(this.note!=null) {
            if (mode != null && mode.trim().equalsIgnoreCase("E")) {
                noteTitleE.setText(note.getNoteTitle());
                noteContentE.setText(note.getNoteContent());
            }else{
                noteTitleV.setText(note.getNoteTitle());
                noteContentV.setText(note.getNoteContent());
            }
        }

        //View Cancel
        viewCancel.setOnClickListener(v -> {
            this.onBackPressed();
        });

        //Edit Cancel
        editCancel.setOnClickListener(v -> {
            this.onBackPressed();
        });

        //Edit
        viewEdit.setOnClickListener(v -> {
            overridePendingTransition(0, 0);
            Intent i = new Intent(context, AddEditNote.class);
            i.putExtra("mode", "E");
            i.putExtra("note", note);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        });

        //Save
        editSave.setOnClickListener(v -> {
            String title = this.noteTitleE.getText().toString();
            String content = this.noteContentE.getText().toString();
            if(title.equals("") || content.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Please enter title & content", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonalNotesHelper _noteHelper = new PersonalNotesHelper(this);
            if(note!=null){
                note.setNoteContent(content);
                note.setNoteTitle(title);
                note.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                _noteHelper.updateNote(note);
                this.needRefresh = true;
            }else{
                PersonalNote newNote = new PersonalNote();
                newNote.setNoteTitle(title);
                newNote.setNoteContent(content);
                newNote.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                _noteHelper.addNote(newNote);
                this.needRefresh = true;
            }
            finishActivity();
        });

        //View Delete
        viewDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(AddEditNote.this)
                    .setMessage("Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PersonalNotesHelper _noteHelper = new PersonalNotesHelper(context);
                            _noteHelper.deleteNote(note);
                            finishActivity();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }

    public void finishActivity() {
        // Create Intent
        Intent data = new Intent(context,PersonalNoteActivity.class);
        // Request MainActivity refresh its ListView (or not).
        data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        data.putExtra("needRefresh", needRefresh);
        // Set Result
        startActivity(data);
        finish();
    }
}