package com.example.dynamic.login;

import com.example.dynamic.login.data.User;

import io.reactivex.Flowable;

public interface LoginDataFeature {
    String MODULE_NAME = "login";

    Flowable<User> getUser();

    Flowable<String> getToken();
}
