package com.example.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.example.dynamic.login.LoginDataFeature;
import com.example.dynamic.login.data.LoginStatus;
import com.example.dynamic.login.data.User;
import com.example.login.data.LoginRepository;
import com.example.login.data.model.LoggedInUser;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class LoginDataImpl implements LoginDataFeature {
    private static LoginDataFeature instance;
    private LoginRepository loginRepository = LoginRepository.getInstance();

    public static LoginDataFeature getInstance() {
        if (instance == null)
            instance = new LoginDataImpl();
        return instance;
    }

    @Override
    public LiveData<User> getUser() {
        return LiveDataReactiveStreams.fromPublisher(Observable.just(loginRepository.getUser())
                .map(new Function<LoggedInUser, User>() {
                    @Override
                    public User apply(LoggedInUser loggedInUser) throws Exception {
                        User user = new User();
                        user.setUserId(loggedInUser.getUserId());
                        user.setNome(loggedInUser.getDisplayName());
                        return user;
                    }
                }).toFlowable(BackpressureStrategy.LATEST)
        );
    }

    @Override
    public LiveData<String> getToken() {
        return LiveDataReactiveStreams.fromPublisher(Observable.just(loginRepository.getUser())
                .map(new Function<LoggedInUser, String>() {
                    @Override
                    public String apply(LoggedInUser loggedInUser) throws Exception {
                        return loggedInUser.getUserId();
                    }
                }).toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Boolean> isAuthenticated() {
        MediatorLiveData<Boolean> isAuthenticatedLiveData = new MediatorLiveData<>();
        if (loginRepository.getLoginState().getValue() == null) {
            isAuthenticatedLiveData.setValue(false);
        }
        isAuthenticatedLiveData.addSource(loginRepository.getLoginState(), loginStatus -> {
            Boolean oldvalue = isAuthenticatedLiveData.getValue();
            Boolean newValue = loginStatus == LoginStatus.AUTHENTICATED;
            if (newValue != oldvalue) {
                isAuthenticatedLiveData.setValue(newValue);
            }
        });
        return isAuthenticatedLiveData;
    }
}
