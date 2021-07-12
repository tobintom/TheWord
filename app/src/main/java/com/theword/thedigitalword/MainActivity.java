package com.theword.thedigitalword;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theword.thedigitalword.adapter.AdaptorClass;
import com.theword.thedigitalword.model.ModelClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<ModelClass> list = new ArrayList<>();
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));
        list.add(new ModelClass(getString(R.string.read), getString(R.string.dsa_sample_article)));

        AdaptorClass adapter = new AdaptorClass(list, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


}