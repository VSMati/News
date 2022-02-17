package com.example.news.ui.trending;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.api.NewsRepository;
import com.example.news.config.ConfigurationUtil;
import com.example.news.config.Sort;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;

import java.util.List;

public class TrendingViewModel extends ViewModel {

    private MutableLiveData<Wrapper<List<NewsDTO>>> mList;
    private String country;
    private String sortBy;

    public TrendingViewModel() {
        mList = new MutableLiveData<>();
        country = ConfigurationUtil.getCountry();
        sortBy = ConfigurationUtil.getSort(Sort.POPULARITY);
    }

    public MutableLiveData<Wrapper<List<NewsDTO>>> getList() {
        return NewsRepository.getInstance().getListBasedOnCountry(country, sortBy);
    }
}