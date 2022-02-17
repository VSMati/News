package com.example.news.api;

import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.NewsList;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("top-headlines")
    Call<NewsList> getBasedOnLanguage(@Query("language") String lang,
                                      @Query("country") String country,
                                      @Query("apiKey") String key);

    @GET("top-headlines")
    Call<NewsList> getBasedOnCategory(@Query("language") String lang,
                                      @Query("country") String country,
                                      @Query("category") String category,
                                      @Query("apiKey") String key);

    @GET("top-headlines")
    Call<NewsList> getTrendingArticles(@Query("country") String country,
                                       @Query("sortBy") String sort,
                                       @Query("apiKey") String key);

    @GET("everything")
    Call<NewsList> getBySearch(@Query("lang") String language,
                               @Query("q") String search,
                               @Query("apiKey") String key);

}
