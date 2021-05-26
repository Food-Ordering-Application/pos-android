package com.foa.smartpos.model;

public interface IDataResultCallback<T> {
    void onSuccess(boolean success,T data);
}
