package com.example.news.api;

import androidx.lifecycle.MutableLiveData;

import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.NewsList;
import com.example.news.api.models.Wrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private static NewsRepository sNewsRepository;
    private static NewsService sNewsService;
    private static NewsApi sNewsApi;
    private final MutableLiveData<Wrapper<List<NewsDTO>>> mHomeList = new MutableLiveData<>();

    public static NewsRepository getInstance() {
        if (sNewsRepository == null) sNewsRepository = new NewsRepository();
        return sNewsRepository;
    }

    private NewsRepository() {
        sNewsService = new NewsService();
        sNewsApi = sNewsService.getNewsApi();
    }

    public MutableLiveData<Wrapper<List<NewsDTO>>> getListBasedOnLanguage(String lang, String con) {
        Call<NewsList> listOfNews = sNewsApi.getBasedOnLanguage(lang,con,sNewsService.KEY);
        listOfNews.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(@NotNull Call<NewsList> call, @NotNull Response<NewsList> response) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setData(response.body().getNewsDTOList());
                mHomeList.setValue(wrapper);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setError(t);
                mHomeList.postValue(wrapper);
            }
        });
        return mHomeList;
    }
}
