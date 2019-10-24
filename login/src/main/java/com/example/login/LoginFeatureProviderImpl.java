package com.example.login;

import androidx.activity.CompatActivity;

import com.example.dynamic.login.LoginDataFeature;
import com.example.dynamic.login.LoginFeatureProvider;
import com.example.dynamic.login.LoginFlowFeature;
import com.google.auto.service.AutoService;

@AutoService(LoginFeatureProvider.class)
public class LoginFeatureProviderImpl implements LoginFeatureProvider {

    @Override
    public LoginFlowFeature getFlow(CompatActivity compatActivity) {
        return new LoginFlowImpl(compatActivity);
    }

    @Override
    public LoginDataFeature getData() {
        return LoginDataImpl.getInstance();
    }
}
