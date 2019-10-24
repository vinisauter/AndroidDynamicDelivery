package com.example.dynamic.login;

import androidx.activity.CompatActivity;
import androidx.features.FeatureFromModule;

@FeatureFromModule("login")
public interface LoginFeatureProvider {

    LoginFlowFeature getFlow(CompatActivity compatActivity);

    LoginDataFeature getData();

}
