package com.example.news.api.models;

public class Wrapper<T> {
    private T data;
    private Throwable mError;

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return mError;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(Throwable error) {
        mError = error;
    }
}
