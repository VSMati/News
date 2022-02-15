package com.example.news.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsList {
    @SerializedName("articles")
    private List<NewsDTO> mNewsDTOList;

    public List<NewsDTO> getNewsDTOList() {
        return mNewsDTOList;
    }

    public void setNewsDTOList(List<NewsDTO> newsDTOList) {
        mNewsDTOList = newsDTOList;
    }
}
