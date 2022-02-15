package com.example.news.api;

import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.NewsList;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("top-headlines")
    Call<NewsList> getBasedOnLanguage(@Query("language") String lang,
                                      @Query("country") String country,
                                      @Query("apiKey") String key);

    @GET("top-headlines?country={country}")
    Single<List<NewsDTO>> getTrendingArticles(@Path("country") String country);

    @GET("everything?language={lang}&q={search}")
    Single<List<NewsDTO>> getBySearch(@Path("lang") String language, @Path("search") String search);

}
