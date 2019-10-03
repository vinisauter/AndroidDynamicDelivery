package com.example.login.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.data.LoginDataSource;
import com.example.login.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            LoginRepository loginRepository = LoginRepository.getInstance();
            if (loginRepository == null) {
                loginRepository = LoginRepository.createInstance(new LoginDataSource());
            }
            return (T) new LoginViewModel(loginRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
