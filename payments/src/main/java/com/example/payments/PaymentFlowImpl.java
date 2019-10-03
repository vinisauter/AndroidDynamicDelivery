package com.example.payments;

import android.app.Activity;
import android.content.Intent;

import com.example.dynamic.payments.PaymentFlowFeature;
import com.example.payments.ui.PaymentsActivity;

public class PaymentFlowImpl implements PaymentFlowFeature {

    @Override
    public void start(Activity activity) {
        activity.startActivity(new Intent(activity, PaymentsActivity.class));
    }

}
