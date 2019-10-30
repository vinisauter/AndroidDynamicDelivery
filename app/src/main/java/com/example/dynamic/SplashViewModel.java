package com.example.dynamic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.features.DynamicViewModel;
import androidx.features.InstallStatus;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.dynamic.login.LoginFeatureProvider;
import com.example.dynamic.payments.PaymentFlowFeature;

@SuppressWarnings("WeakerAccess")
public class SplashViewModel extends DynamicViewModel {
    private LiveData<InstallStatus> loginModuleState;
    private LiveData<InstallStatus> paymentsModuleState;
    private LiveData<LoginFeatureProvider> loginFeatureProvider;
    private LiveData<PaymentFlowFeature> paymentFlowFeature;
    private LiveData<Boolean> isAuthenticated;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        installModules("login");
    }

    public LiveData<InstallStatus> getLoginModuleState() {
        if (loginModuleState == null) {
            loginModuleState = getInstallStatus("login");
        }
        return loginModuleState;
    }

    public LiveData<InstallStatus> getPaymentsModuleState() {
        if (paymentsModuleState == null) {
            paymentsModuleState = getInstallStatus("payments");
        }
        return paymentsModuleState;
    }

    public LiveData<PaymentFlowFeature> getPaymentFlowFeature() {
        if (paymentFlowFeature == null) {
            paymentFlowFeature = installFeature(PaymentFlowFeature.MODULE_NAME, PaymentFlowFeature.class);
        }
        return paymentFlowFeature;
    }

    public LiveData<LoginFeatureProvider> getLoginFeatureProvider() {
        if (loginFeatureProvider == null) {
            loginFeatureProvider = installFeature(LoginFeatureProvider.class);
        }
        return loginFeatureProvider;
    }

    public LiveData<Boolean> isAuthenticated() {
        if (isAuthenticated == null) {
            isAuthenticated = Transformations.switchMap(getLoginFeatureProvider(),
                    loginFeatureProvider -> loginFeatureProvider.getData().isAuthenticated()
            );
        }
        return isAuthenticated;
    }
}
