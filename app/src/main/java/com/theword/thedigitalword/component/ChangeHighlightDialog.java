package com.theword.thedigitalword.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.HightlightActivity;
import com.theword.thedigitalword.MeActivity;
import com.theword.thedigitalword.R;
import com.theword.thedigitalword.adapter.CrossReferenceAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ChangeHighlightDialog extends DialogFragment {
    public static String TAG = "ChangeHighlightDialog";
    private TextView cross = null;
    private ImageView c1,c2,c3,c4,c5,c6;
    private TextView crossText = null;
    private TextView crossHint = null;
    private RecyclerView srchView = null;
    private ArrayList srchList = null;
    Context context = null;
    boolean hasData = false;
    String dir = "";
    String id = "";

    public ChangeHighlightDialog(){

    }

    public static ChangeHighlightDialog newInstance(BibleDBContent referenceCode, String title) {
        ChangeHighlightDialog frag = new ChangeHighlightDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("code", referenceCode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_changehighlight, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            cross = (TextView) view.findViewById(R.id.changeHightitle);
            crossHint = (TextView) view.findViewById(R.id.changeText);
            BibleDBContent model = (BibleDBContent)getArguments().getSerializable("code");

            c1 = (ImageView)view.findViewById(R.id.ch1);
            c2 = (ImageView)view.findViewById(R.id.ch2);
            c3 = (ImageView)view.findViewById(R.id.ch3);
            c4 = (ImageView)view.findViewById(R.id.ch4);
            c5 = (ImageView)view.findViewById(R.id.ch5);
            c6 = (ImageView)view.findViewById(R.id.ch6);

            c1.setOnClickListener(v -> {
                updateHighlight("#FFFF2E",model);
            });
            c2.setOnClickListener(v -> {
                updateHighlight("#ffa726",model);
            });
            c3.setOnClickListener(v -> {
                updateHighlight("#97E47E",model);
            });
            c4.setOnClickListener(v -> {
                updateHighlight("#B7D0E1",model);
            });
            c5.setOnClickListener(v -> {
                updateHighlight("#CE9DD9",model);
            });
            c6.setOnClickListener(v -> {
                updateHighlight("#E5DB9C",model);
            });

            context = getContext();

            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
            String direction = SharedPreferencesUtil.getDirection(sharedPreferences, this.getContext());
            // Fetch arguments from bundle and set title
            String title = getArguments().getString("title", "");
            crossHint.setText(title);
            crossHint.setTextDirection((direction!=null&&direction.trim().equalsIgnoreCase("RTL")?TextView.TEXT_DIRECTION_RTL:TextView.TEXT_DIRECTION_LTR));

            cross.setOnClickListener(v -> {
                        getDialog().dismiss();
                    });

        }catch(Exception e){

        }
    }

    public void updateHighlight(String color,BibleDBContent model){
        if(model!=null){
            model.setColor(color);
            model.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            BibleHelper _helper = new BibleHelper(context);
            _helper.updateHighlight(model);
            Toast.makeText(context, "The verse has been updated.",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, HightlightActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
