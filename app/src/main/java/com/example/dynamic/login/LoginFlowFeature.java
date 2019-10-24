package com.example.dynamic.login;

import androidx.features.FeatureFromModule;

import com.example.dynamic.login.data.LoginStatus;

@FeatureFromModule("login")
public interface LoginFlowFeature {
    String MODULE_NAME = "login";

    interface LoginCallback {
        void onLoginStatusChanged(LoginStatus loginStatusResult);
    }

    void startLogin(LoginCallback callback);
}
