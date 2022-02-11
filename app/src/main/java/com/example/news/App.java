package com.example.news;

import androidx.multidex.MultiDexApplication;

import com.example.news.api.NewsService;

public class App extends MultiDexApplication {
    private NewsService mNewsService;
    @Override
    public void onCreate() {
        super.onCreate();
        mNewsService = new NewsService();
    }

    public NewsService getNewsService() {
        return mNewsService;
    }
}
