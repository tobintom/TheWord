package com.theword.thedigitalword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theword.thedigitalword.adapter.BibleNoteAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    TextView back = null;
    TextView favs = null;
    TextView filter = null;
    private RecyclerView srchView = null;
    private List<BibleDBContent> srchList = null;
    Context context = null;
    private static final int MY_REQUEST_CODE = 203;
    BibleNoteAdapter srchAdapter = null;
    private String FILTER = "NO_FILTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        context = this.getApplicationContext();
        srchList = new ArrayList<>();

        srchView = findViewById(R.id.idbnotes);
        favs = findViewById(R.id.bnotes);
        filter = findViewById(R.id.notefilter);

        //Go Back
        TextView back = findViewById(R.id.bnoteBack);
        back.setOnClickListener(v -> {
            finish();
        });

        filter.setOnClickListener(v -> {
            if(FILTER.equals("NO_FILTER")) {
                BibleHelper _helper = new BibleHelper(this);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NotesActivity.this, android.R.layout.simple_selectable_list_item);
                List result = _helper.getAllBibleNoteLinks();
                result.remove("");
                if(result!=null && result.size()>0) {
                    arrayAdapter.addAll(result);

                    View rowList = getLayoutInflater().inflate(R.layout.link_row, null);
                    AlertDialog builderSingle = new AlertDialog.Builder(NotesActivity.this)
                            .setView(rowList)
                            .create();
                    builderSingle.setIcon(R.drawable.ic_baseline_link_24);
                    builderSingle.setTitle("Select Link");
                    Button b = rowList.findViewById(R.id.linkCancel);
                    b.setOnClickListener(v1 -> {
                        builderSingle.dismiss();
                    });

                    ListView listView = rowList.findViewById(R.id.linkList);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        String link = (String) parent.getAdapter().getItem(position);
                        filterByLink(link);
                        builderSingle.dismiss();
                    });
                    builderSingle.show();
                }else{
                    Toast.makeText(context,
                            "There are no Bible Notes with Links to filter", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else{
                FILTER = "NO_FILTER";
                filter.setText("Filter by Link");
                filter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_link_24, 0, 0, 0);
                //refresh
                Intent data = new Intent(context, NotesActivity.class);
                // Request MainActivity refresh its ListView (or not).
                data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Set Result
                startActivity(data);
            }
        });

        //Get all data
        BibleHelper _helper = new BibleHelper(this);
        srchList = _helper.getAllBibleNotes();
        if(srchList==null || srchList.size()==0){
            favs.setText("No Bible Notes found..");
        }else {
            favs.setText("Click on list icon to read full chapter.");
            srchAdapter = new BibleNoteAdapter(NotesActivity.this,srchList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }
    }

    public void filterByLink(String link){
        FILTER = "FILTER";
        filter.setText("Cancel Filter");
        filter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_link_off_24, 0, 0, 0);
        //Get all data
        BibleHelper _helper = new BibleHelper(this);
        srchList = _helper.getAllBibleNotesByLink(link);
        if(srchList==null || srchList.size()==0){
            favs.setText("No Bible Notes found..");
        }else {
            favs.setText("Click on list icon to read full chapter.");
            srchAdapter = new BibleNoteAdapter(NotesActivity.this,srchList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }
    }
}