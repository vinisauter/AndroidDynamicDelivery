package com.example.payments.ui;

import androidx.features.DataFeatureProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.example.dynamic.login.LoginDataFeature;
import com.example.dynamic.login.data.User;

import io.reactivex.disposables.CompositeDisposable;

public class PaymentsViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final LiveData<User> userLiveData;
    private LoginDataFeature loginDataFeature;

    public PaymentsViewModel() {
        loginDataFeature = new DataFeatureProvider(this).get(LoginDataFeature.class);
        userLiveData = LiveDataReactiveStreams.fromPublisher(loginDataFeature.getUser());
        compositeDisposable.add(loginDataFeature.getToken().subscribe(token -> {

        }));
    }

}
