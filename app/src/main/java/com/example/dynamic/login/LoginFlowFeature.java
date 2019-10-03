package com.example.dynamic.login;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveEvent;

import com.example.dynamic.login.data.LoginStatus;

public interface LoginFlowFeature {
    String MODULE_NAME = "login";

    void start(FragmentActivity activity);

    LiveEvent<LoginStatus> loginState();
}
