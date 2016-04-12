package com.androidtitan.hotspots.main.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidtitan.hotspots.R;

import java.util.ArrayList;

/**
 * Created by amohnacs on 4/6/16.
 */
public class SimpleWikiAdapter extends RecyclerView.Adapter<SimpleWikiAdapter.ViewHolder>{
    private ArrayList<String> list = new ArrayList<>();

    private String urlString;

    public SimpleWikiAdapter(Context context, String webString) {
        urlString = webString;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wiki_row_simplest, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        holder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        holder.webView.getSettings().setLoadsImagesAutomatically(true);
        holder.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setBuiltInZoomControls(true);
        holder.webView.loadUrl(urlString);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        WebView webView;

        public ViewHolder(View itemView) {
            super(itemView);
            webView = (WebView) itemView.findViewById(R.id.wikiWebView);
        }
    }
}
