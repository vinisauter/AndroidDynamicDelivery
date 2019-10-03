package com.example.login;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveEvent;

import com.example.dynamic.login.LoginFlowFeature;
import com.example.dynamic.login.data.LoginStatus;
import com.example.login.ui.login.LoginActivity;

public class LoginFlowImpl implements LoginFlowFeature {
    // TODO: get it from ViewModel
    public static final LiveEvent<LoginStatus> loginStatusLiveEvent = new LiveEvent<LoginStatus>() {{
        setValue(LoginStatus.AUTHENTICATION_REQUIRED);
    }};

    @Override
    public void start(FragmentActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public LiveEvent<LoginStatus> loginState() {
        return loginStatusLiveEvent;
    }
}
