package com.example.dynamic.login;

import androidx.features.FeatureFromModule;
import androidx.lifecycle.LiveData;

import com.example.dynamic.login.data.User;

@FeatureFromModule("login")
public interface LoginDataFeature {
    String MODULE_NAME = "login";

    LiveData<User> getUser();

    LiveData<String> getToken();

    LiveData<Boolean> isAuthenticated();
}
