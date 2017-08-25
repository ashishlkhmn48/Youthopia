package com.ashishlakhmani.youthopia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;

public class WebViewFragment extends Fragment {

    WebView detailsWebView;
    private String detailsLink;
    private String toolbarHeading;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ((Home) getActivity()).changeConstraintLayout(toolbarHeading, true);

        detailsWebView = (WebView) view.findViewById(R.id.details_webView);
        progressBar = (ProgressBar) view.findViewById(R.id.webViewProgress);

        WebSettings webSettings = detailsWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        detailsWebView.loadUrl(detailsLink);

        detailsWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }
        });

        return view;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    public void setToolbarHeading(String toolbarHeading) {
        this.toolbarHeading = toolbarHeading;
    }
}
