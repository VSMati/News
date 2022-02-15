package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.news.databinding.ActivityWebNewsBinding;
/**Show the webpage of news' source*/
public class WebActivity extends AppCompatActivity {
    private ActivityWebNewsBinding binding;
    private WebView mWebView;
    private String url;
    private static final String EXTRA_URL = "com.example.news.WebActivity.EXTRA_URL";

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebNewsBinding.inflate(getLayoutInflater());
        url = getIntent().getStringExtra(EXTRA_URL);
        setContentView(binding.getRoot());
        mWebView = binding.web;

        mWebView.loadUrl(url);
        binding.progressBar.setMax(100); binding.progressBar.setProgress(1);
        /*Set bar's progress according to url load*/
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                binding.progressBar.setProgress(newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.error_web, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}