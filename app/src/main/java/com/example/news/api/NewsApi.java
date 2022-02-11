package com.example.news.api;

import com.example.news.api.models.NewsDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApi {
    @GET("everything?language={lang}")
    Single<List<NewsDTO>> getBasedOnLanguage(@Path("lang") String lang);

    @GET("top-headlines?country={country}")
    Single<List<NewsDTO>> getTrendingArticles(@Path("country") String country);

    @GET("everything?language={lang}&q={search}")
    Single<List<NewsDTO>> getBySearch(@Path("lang") String language, @Path("search") String search);

}
