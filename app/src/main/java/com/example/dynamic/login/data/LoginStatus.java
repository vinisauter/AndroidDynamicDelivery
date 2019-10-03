package com.example.dynamic.login.data;

public enum LoginStatus {
    ERROR,
    AUTHENTICATION_REQUIRED,
    AUTHENTICATED,
    NEW_USER;

    private Throwable error;

    public LoginStatus putError(Throwable error) {
        this.error = error;
        return this;
    }

    public Throwable getError() {
        return error;
    }

}
