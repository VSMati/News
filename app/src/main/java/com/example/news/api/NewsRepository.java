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
    private final MutableLiveData<Wrapper<List<NewsDTO>>> mTrendList = new MutableLiveData<>();
    private final MutableLiveData<Wrapper<List<NewsDTO>>> mSearchList = new MutableLiveData<>();

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
                if (response.body() != null) {
                    wrapper.setData(response.body().getNewsDTOList());
                }
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

    public MutableLiveData<Wrapper<List<NewsDTO>>> getListBasedOnCategory(String lang, String con, String cat) {
        Call<NewsList> listOfNews = sNewsApi.getBasedOnCategory(lang,con,cat,sNewsService.KEY);
        listOfNews.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(@NotNull Call<NewsList> call, @NotNull Response<NewsList> response) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                if (response.body() != null) {
                    wrapper.setData(response.body().getNewsDTOList());
                }
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

    public MutableLiveData<Wrapper<List<NewsDTO>>> getListBasedOnCountry(String con, String sort) {
        Call<NewsList> listOfNews = sNewsApi.getTrendingArticles(con, sort, sNewsService.KEY);
        listOfNews.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setData(response.body().getNewsDTOList());
                mTrendList.setValue(wrapper);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setError(t);
                mTrendList.postValue(wrapper);
            }
        });
        return mTrendList;
    }

    public MutableLiveData<Wrapper<List<NewsDTO>>> getListBasedOnQuery(String lang, String q) {
        Call<NewsList> listOfNews = sNewsApi.getBySearch(lang,q,NewsService.KEY);
        listOfNews.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setData(response.body().getNewsDTOList());
                mSearchList.setValue(wrapper);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                Wrapper<List<NewsDTO>> wrapper = new Wrapper<>();
                wrapper.setError(t);
                mSearchList.postValue(wrapper);
            }
        });
        return mSearchList;
    }
}
