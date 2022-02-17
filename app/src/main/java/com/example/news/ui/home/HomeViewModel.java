package com.example.news.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.config.ConfigurationUtil;
import com.example.news.api.NewsRepository;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;

import java.util.List;

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

    public MutableLiveData<Wrapper<List<NewsDTO>>> getList(String category) {
        return mRepository.getListBasedOnCategory(lang,country,category);
    }
}