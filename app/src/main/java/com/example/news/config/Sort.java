package com.example.news.config;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public enum Sort {
    RELEVANCY,
    POPULARITY,
    PUBLISHED_AT;


    @NonNull
    @NotNull
    @Override
    public String toString() {
        switch (this) {
            case RELEVANCY:
                return "relevancy";
            case POPULARITY:
                return "popularity";
            case PUBLISHED_AT:
                return "publishedAt";
        }
        return super.toString();
    }
}
