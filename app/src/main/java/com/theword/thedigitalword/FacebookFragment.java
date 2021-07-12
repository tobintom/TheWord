package com.theword.thedigitalword;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class FacebookFragment extends Fragment {

    WebView facebookView = null;
    MaterialButton button = null;
    private static final String fbaseURl = "https://www.facebook.com";
    private static final String fwidgetURL =
            "<!DOCTYPE html>\n" + "<html>\n" + "<head></head>\n"
                    + "<body>\n" +
                    "<div id=\"fb-root\"></div>\n" +
                    "<script async defer crossorigin=\"anonymous\" src=\"https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v10.0\" nonce=\"se3bYZ2D\"></script> " +
                    "<div class=\"fb-page\" href=\"https://www.facebook.com/theworddigital\" tabs=\"timeline\" width=\"\" height=\"1600\" small-header=\"true\" lazy=\"true\" adapt-container-width=\"true\" hide-cover=\"true\" show-facepile=\"false\"><blockquote cite=\"https://www.facebook.com/theworddigital\" class=\"fb-xfbml-parse-ignore\"><a href=\"https://www.facebook.com/theworddigital\">The Word</a></blockquote></div>"
                    + "</body>\n" + "</html>";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (MaterialButton)this.getActivity().findViewById(R.id.postButton);

        button.setOnClickListener(v -> {
            facebookView = (WebView) this.getActivity().findViewById(R.id.timeline_facebook);
            setupWebView();
            facebookView.loadDataWithBaseURL(fbaseURl, fwidgetURL, "text/html", "UTF-8", null);
        });
    }

    private void setupWebView() {
        facebookView.setBackgroundColor(0);
        facebookView.setWebViewClient(new UriWebViewClient());
        facebookView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = facebookView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);

    }

    private class UriWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}