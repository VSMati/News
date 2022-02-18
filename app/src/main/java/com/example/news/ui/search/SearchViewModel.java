package com.example.news.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.api.NewsRepository;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;
import com.example.news.config.ConfigurationUtil;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final String language;
    private String q;

    public SearchViewModel() {
        language = ConfigurationUtil.getLang();
    }

    public MutableLiveData<Wrapper<List<NewsDTO>>> getList() {
        return NewsRepository.getInstance().getListBasedOnQuery(language, q);
    }

    public void setQuery(String q) {
        this.q = q;
    }
}