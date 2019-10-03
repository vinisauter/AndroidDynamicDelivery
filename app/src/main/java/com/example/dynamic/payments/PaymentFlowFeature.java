package com.example.dynamic.payments;

import android.app.Activity;

public interface PaymentFlowFeature {
    String MODULE_NAME = "payments";

    void start(Activity activity);
}
