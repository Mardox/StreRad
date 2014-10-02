package com.example.mark.streamradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class NewsDetailActivity extends ActionBarActivity {

    WebView newsBrowser;
    NewsItemsData newsItemsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);


        newsItemsData = new NewsItemsData();
        setTitle(newsItemsData.titles[id]);
        newsBrowser = (WebView) findViewById(R.id.newsBrowser);
        WebSettings webSettings = newsBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        newsBrowser.loadUrl(newsItemsData.urls[id]);
        newsBrowser.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && newsBrowser.canGoBack()) {
            newsBrowser.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            newsBrowser.reload();
        }

        return super.onOptionsItemSelected(item);
    }
}
