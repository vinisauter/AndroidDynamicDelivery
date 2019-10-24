package com.example.login;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.CompatActivity;
import androidx.activity.OnActivityResultCallback;
import androidx.annotation.Nullable;

import com.example.dynamic.login.LoginFlowFeature;
import com.example.dynamic.login.data.LoginStatus;
import com.example.login.ui.login.LoginActivity;

import java.util.concurrent.atomic.AtomicInteger;

public class LoginFlowImpl implements LoginFlowFeature {
    private CompatActivity activity;
    private AtomicInteger atomicRequestCode = new AtomicInteger(1000);

    LoginFlowImpl(CompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void startLogin(LoginCallback callback) {
        Intent intent = new Intent(activity, LoginActivity.class);
        int requestCode = atomicRequestCode.getAndIncrement();
        activity.startActivityForResult(intent, new OnActivityResultCallback(requestCode, true) {
            @Override
            public void handleOnActivityResult(int resultCode, @Nullable Intent data) {
                if (Activity.RESULT_OK == resultCode) {
                    callback.onLoginStatusChanged(LoginStatus.AUTHENTICATED);
                }
            }
        });
    }
}
