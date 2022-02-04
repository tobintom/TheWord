package com.theword.thedigitalword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.theword.thedigitalword.adapter.PersonalNoteAdapter;
import com.theword.thedigitalword.adapter.SearchAdapter;
import com.theword.thedigitalword.db.PersonalNotesHelper;
import com.theword.thedigitalword.model.PersonalNote;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonalNoteActivity extends AppCompatActivity {

    private RecyclerView srchView = null;
    private List<PersonalNote> srchList = null;
    Context context = null;
    SharedPreferences sharedPreferences = null;
    private static final int MY_REQUEST_CODE = 1000;
    PersonalNoteAdapter srchAdapter = null;
    TextView resultsView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_note);
        Toolbar toolbar = findViewById(R.id.toolbarNotes);
        setSupportActionBar(toolbar);

        //Go Back
        TextView back = findViewById(R.id.noteBack);
        back.setOnClickListener(v -> {
            finish();
        });

        resultsView = findViewById(R.id.searchNotes);

        context = this.getApplicationContext();
        srchView = findViewById(R.id.idnotes);
        srchList = new ArrayList();
        //Find notes
        PersonalNotesHelper _noteHelper = new PersonalNotesHelper(this);
        srchList = _noteHelper.getAllNotes();
        if(srchList==null || srchList.size()==0){
            resultsView.setVisibility(View.VISIBLE);
        }else{
            //Set results
            resultsView.setVisibility(View.INVISIBLE);
            //Set Adapter
            srchAdapter = new PersonalNoteAdapter(PersonalNoteActivity.this, srchList);
            // below line is for setting a layout manager for our recycler view.
            // here we are creating vertical list so we will provide orientation as vertical
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }

        FloatingActionButton fab = findViewById(R.id.addNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEditNote.class);
                intent.putExtra("mode", "E");
                // Start AddEditNoteActivity, (with feedback).
                startActivity(intent);
            }
        });
    }
}