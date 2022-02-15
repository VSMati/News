package com.example.news.ui.home;

import android.content.res.Resources;
import android.os.Build;
import android.widget.Toast;

import androidx.core.os.ConfigurationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.App;
import com.example.news.ConfigurationUtil;
import com.example.news.R;
import com.example.news.api.NewsRepository;
import com.example.news.api.NewsService;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private final NewsRepository mRepository = NewsRepository.getInstance();
    private String lang;
    private String country;

    public HomeViewModel() {
        lang = ConfigurationUtil.getLang();
        country = ConfigurationUtil.getCountry();
    }

    public MutableLiveData<Wrapper<List<NewsDTO>>> getList() {
        return mRepository.getListBasedOnLanguage(lang,country);
    }
}