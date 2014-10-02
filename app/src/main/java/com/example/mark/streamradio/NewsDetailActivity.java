package com.example.mark.streamradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;


public class NewsDetailActivity extends ActionBarActivity {

    WebView newsBrowser;
    NewsItemsData newsItemsData;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // create new ProgressBar and style it
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 24));
        //progressBar.setProgress(65);

        // retrieve the top view of our application
        final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(progressBar);

        // Here we try to position the ProgressBar to the correct position by looking
        // at the position where content area starts. But during creating time, sizes
        // of the components are not set yet, so we have to wait until the components
        // has been laid out
        // Also note that doing progressBar.setY(136) will not work, because of different
        // screen densities and different sizes of actionBar
        ViewTreeObserver observer = progressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View contentView = decorView.findViewById(android.R.id.content);
                progressBar.setY(contentView.getY() - 10);

                ViewTreeObserver observer = progressBar.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(this);
            }
        });
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal_holo_no_background_light));



        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        //progressBar = (ProgressBar) findViewById(R.id.browserProgress);


        newsItemsData = new NewsItemsData();
        setTitle(newsItemsData.titles[id]);
        newsBrowser = (WebView) findViewById(R.id.newsBrowser);
        WebSettings webSettings = newsBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        newsBrowser.loadUrl(newsItemsData.urls[id]);
        webSettings.setSupportMultipleWindows(true);
        //newsBrowser.setWebViewClient(new MyBrowser());
        newsBrowser.setWebChromeClient(new MyBrowser());
        newsBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    private class MyBrowser extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if(newProgress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
            progressBar.setProgress(newProgress);
            if(newProgress == 100) {
                progressBar.setVisibility(ProgressBar.GONE);
            }
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
