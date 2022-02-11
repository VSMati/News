package com.example.news.ui.home;

import android.content.res.Resources;
import android.os.Build;

import androidx.core.os.ConfigurationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.App;
import com.example.news.api.models.NewsDTO;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<NewsDTO>> mList;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private String lang;

    public HomeViewModel(App app) {
        mList = new MutableLiveData<List<NewsDTO>>();

        //mDisposable.add(app.getNewsService().getNewsApi().getBasedOnLanguage())
    }

    public LiveData<List<NewsDTO>> getList() {
        return mList;
    }

    private String getLang() {
        lang = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration())
                .get(0).getLanguage();
        return lang;
    }
}