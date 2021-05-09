package com.foa.pos.model;

public interface IResultCallback {
    void onSuccess(boolean value);
    void onError();
}
