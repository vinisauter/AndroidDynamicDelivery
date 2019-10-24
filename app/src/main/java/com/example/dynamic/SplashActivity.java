package com.example.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.features.InstallStatus;
import androidx.features.InstallStatusObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dynamic.login.LoginFeatureProvider;
import com.example.dynamic.login.LoginFlowFeature;
import com.example.dynamic.login.data.LoginStatus;
import com.example.dynamic.payments.PaymentFlowFeature;

public class SplashActivity extends BaseActivity {

    private static final int INSTALL_USER_CONFIRMATION = 10;
    private SplashViewModel splashVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashVM = new ViewModelProvider(this).get(SplashViewModel.class);

        splashVM.isAuthenticated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAuthenticated) {
                splashVM.isAuthenticated().removeObserver(this);
                if (isAuthenticated) {
                    startPayments();
                } else {
                    startLogin();
                }
            }
        });

        observModuleState();
    }

    private void startLogin() {
        splashVM.getLoginFeatureProvider().observe(this, new Observer<LoginFeatureProvider>() {
            @Override
            public void onChanged(LoginFeatureProvider loginFeatureProvider) {
                splashVM.getLoginFeatureProvider().removeObserver(this);
                LoginFlowFeature loginFlowFeature = loginFeatureProvider.getFlow(SplashActivity.this);
                loginFlowFeature.startLogin(loginStatus -> {
                    if (loginStatus == LoginStatus.AUTHENTICATED) {
                        startPayments();
                    }
                });
            }
        });
    }

    private void startPayments() {
        splashVM.getPaymentFlowFeature().observe(this, new Observer<PaymentFlowFeature>() {
            @Override
            public void onChanged(PaymentFlowFeature paymentFlowFeature) {
                splashVM.getPaymentFlowFeature().removeObserver(this);
                paymentFlowFeature.start(SplashActivity.this);
                SplashActivity.this.finish();
            }
        });
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
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

    private void observModuleState() {
        splashVM.getLoginModuleState().observe(this, new InstallStatusObserver() {

            @Override
            public void requiresUserConfirmation(InstallStatus installStatus) {
                installStatus.startConfirmationDialogForResult(getActivity(), INSTALL_USER_CONFIRMATION);
            }

            @Override
            public void installed(InstallStatus installStatus) {
                Toast.makeText(getApplicationContext(), "Login Module installed.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failed(Exception error) {
                Toast.makeText(getApplicationContext(), "Login Module failed. " + error.getCause(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        splashVM.getPaymentsModuleState().observe(this, new InstallStatusObserver() {

            @Override
            public void requiresUserConfirmation(InstallStatus installStatus) {
                Toast.makeText(getApplicationContext(),
                        "Payments Module requires user confirmation.",
                        Toast.LENGTH_SHORT).show();
                installStatus.startConfirmationDialogForResult(getActivity(), INSTALL_USER_CONFIRMATION);
            }

            @Override
            public void downloading(long downloadPercentage) {
                Toast.makeText(getApplicationContext(),
                        "Payments Module downloading: " + downloadPercentage,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void installing() {
                Toast.makeText(getApplicationContext(),
                        "Payments Module installing.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void installed(InstallStatus installStatus) {
                Toast.makeText(getApplicationContext(),
                        "Payments Module installed.",
                        Toast.LENGTH_LONG).show();
            }


            @Override
            public void failed(Exception error) {
                Toast.makeText(getApplicationContext(),
                        "Payments Module failed. " + error.getCause(),
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
