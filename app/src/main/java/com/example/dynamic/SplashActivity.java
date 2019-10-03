package com.example.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.features.DynamicViewModel;
import androidx.features.InstallStatus;
import androidx.lifecycle.ViewModelProvider;

import com.example.dynamic.login.LoginFlowFeature;
import com.example.dynamic.login.data.LoginStatus;
import com.example.dynamic.payments.PaymentFlowFeature;

public class SplashActivity extends BaseActivity {

    private static final int INSTALL_USER_CONFIRMATION = 10;
    DynamicViewModel dynamicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dynamicViewModel = new ViewModelProvider(this).get(DynamicViewModel.class);

        dynamicViewModel.getFeatureLiveData(LoginFlowFeature.class, LoginFlowFeature.MODULE_NAME)
                .observe(this, this::onFeatureLoaded);
    }

    private void onFeatureLoaded(LoginFlowFeature loginFlowFeature) {
        loginFlowFeature.loginState().observe(this, loginStatus -> {
            if (loginStatus != null) {
                if (loginStatus == LoginStatus.AUTHENTICATED) {
                    startOrDownloadPayments();
                } else {
                    loginFlowFeature.start(this);
                }
            }
        });
    }

    private void startOrDownloadPayments() {
        dynamicViewModel.installModules(PaymentFlowFeature.MODULE_NAME).observe(this, installStatus -> {
            if (installStatus == InstallStatus.REQUIRES_USER_CONFIRMATION) {
                installStatus.startConfirmationDialogForResult(this, INSTALL_USER_CONFIRMATION);
                Log.i(installStatus.name(), PaymentFlowFeature.MODULE_NAME);
            } else if (installStatus == InstallStatus.DOWNLOADING) {
                // TODO: show progress
                Log.i(installStatus.name(), PaymentFlowFeature.MODULE_NAME + " -> " + installStatus.getDownloadPercentage());
            } else if (installStatus == InstallStatus.INSTALLING) {
                // TODO: show progress indeterminate
                Log.i(installStatus.name(), PaymentFlowFeature.MODULE_NAME);
            } else if (installStatus == InstallStatus.INSTALLED) {
                PaymentFlowFeature paymentFlowFeature = installStatus.getFeature(PaymentFlowFeature.class);
                launchPaymentFlow(paymentFlowFeature);
            } else if (installStatus == InstallStatus.FAILED) {
                Log.i(installStatus.name(), PaymentFlowFeature.MODULE_NAME + " " + installStatus.getError());
            }
        });
    }

    void launchPaymentFlow(PaymentFlowFeature paymentFlow) {
        paymentFlow.start(this);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_USER_CONFIRMATION) {
            if (resultCode == RESULT_OK) {
                // User approved installation
            } else {
                // User declined installation
            }
        }
    }
}
