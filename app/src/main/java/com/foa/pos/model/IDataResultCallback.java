package com.foa.pos.model;

public interface IDataResultCallback<T> {
    void onSuccess(boolean success,T Data);
    void onError();
}
