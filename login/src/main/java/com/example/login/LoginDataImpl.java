package com.example.login;

import com.example.dynamic.login.LoginDataFeature;
import com.example.dynamic.login.data.User;
import com.example.login.data.LoginRepository;
import com.example.login.data.model.LoggedInUser;
import com.google.auto.service.AutoService;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

@AutoService(LoginDataFeature.class)
public class LoginDataImpl implements LoginDataFeature {
    private LoginRepository loginRepository = LoginRepository.getInstance();

    @Override
    public Flowable<User> getUser() {
        return Observable.just(loginRepository.getUser()).map(new Function<LoggedInUser, User>() {
            @Override
            public User apply(LoggedInUser loggedInUser) throws Exception {
                User user = new User();
                user.setUserId(loggedInUser.getUserId());
                user.setNome(loggedInUser.getDisplayName());
                return user;
            }
        }).toFlowable(BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<String> getToken() {
        return Observable.just(loginRepository.getUser()).map(new Function<LoggedInUser, String>() {
            @Override
            public String apply(LoggedInUser loggedInUser) throws Exception {
                return loggedInUser.getUserId();
            }
        }).toFlowable(BackpressureStrategy.LATEST);
    }
}
