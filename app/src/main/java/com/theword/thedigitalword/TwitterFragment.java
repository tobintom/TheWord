package com.theword.thedigitalword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class TwitterFragment extends Fragment {

    WebView webView = null;
    MaterialButton button = null;

    private static final String baseURl = "https://twitter.com";
    private static final String widgetInfo = "<a class=\"twitter-timeline\" data-width=\"350\" data-tweet-limit=\"3\" href=\"https://twitter.com/digitaltheword?ref_src=twsrc%5Etfw\">Latest Tweets by digitaltheword</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (MaterialButton)this.getActivity().findViewById(R.id.tweetButton);

        button.setOnClickListener(v -> {
            webView = (WebView) this.getActivity().findViewById(R.id.timeline_twitter);
            webView.setBackgroundColor(0);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL(baseURl, widgetInfo, "text/html", "UTF-8", null);
        });


    }
}
