package com.example.dynamic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.features.DynamicViewModel;
import androidx.features.FeatureProvider;
import androidx.features.InstallStatus;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.example.dynamic.login.LoginFeatureProvider;
import com.example.dynamic.payments.PaymentFlowFeature;

@SuppressWarnings("WeakerAccess")
public class SplashViewModel extends DynamicViewModel {
    private FeatureProvider featureProvider = new FeatureProvider(this);

    private MediatorLiveData<InstallStatus> loginModuleState;
    private MediatorLiveData<InstallStatus> paymentsModuleState;
    private MediatorLiveData<LoginFeatureProvider> loginFeatureProvider;
    private MediatorLiveData<PaymentFlowFeature> paymentFlowFeature;
    private LiveData<Boolean> isAuthenticated;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        installModules("login");
    }

    public LiveData<InstallStatus> getLoginModuleState() {
        if (loginModuleState == null) {
            loginModuleState = new MediatorLiveData<>();
//            if (isModuleInstalled("login")) {
//                loginModuleState.setValue(InstallStatus.INSTALLED);
//            } else
            {
                loginModuleState.addSource(getInstallStatus(), installStatus -> {
                    if (installStatus.getModules().contains("login") && installStatus != InstallStatus.UNKNOWN) {
                        loginModuleState.setValue(installStatus);
                    }
                });
            }
        }
        return loginModuleState;
    }

    public LiveData<InstallStatus> getPaymentsModuleState() {
        if (paymentsModuleState == null) {
            paymentsModuleState = new MediatorLiveData<>();
//            //
//            if (isModuleInstalled("payments")) {
//                paymentsModuleState.setValue(InstallStatus.INSTALLED);
//            } else
            {
                paymentsModuleState.addSource(getInstallStatus(), installStatus -> {
                    if (installStatus.getModules().contains("payments") && installStatus != InstallStatus.UNKNOWN) {
                        paymentsModuleState.setValue(installStatus);
                    }
                });
            }
        }
        return paymentsModuleState;
    }

    public LiveData<PaymentFlowFeature> getPaymentFlowFeature() {
        if (paymentFlowFeature == null) {
            paymentFlowFeature = new MediatorLiveData<>();

            paymentFlowFeature.addSource(installModules(PaymentFlowFeature.MODULE_NAME), installStatus -> {
                if (paymentFlowFeature.getValue() == null && installStatus.isInstalled()) {
                    paymentFlowFeature.setValue(featureProvider.get(PaymentFlowFeature.class));
                }
            });
        }
        return paymentFlowFeature;
    }

    public LiveData<LoginFeatureProvider> getLoginFeatureProvider() {
        if (loginFeatureProvider == null) {
            loginFeatureProvider = new MediatorLiveData<>();

            loginFeatureProvider.addSource(installFeatures(LoginFeatureProvider.class), installStatus -> {
                if (loginFeatureProvider.getValue() == null && installStatus.isInstalled()) {
                    loginFeatureProvider.setValue(featureProvider.get(LoginFeatureProvider.class));
                }
            });
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
