package com.example.news.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourceList {
    @SerializedName("source")
    private List<SourceDTO> mSourceDTOList;

    public List<SourceDTO> getSourceDTOList() {
        return mSourceDTOList;
    }

    public void setSourceDTOList(List<SourceDTO> sourceDTOList) {
        mSourceDTOList = sourceDTOList;
    }
}
