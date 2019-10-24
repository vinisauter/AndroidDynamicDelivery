package androidx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class CompatActivity extends AppCompatActivity implements OnActivityResultDispatcherOwner {
    private final OnActivityResultDispatcher mOnActivityResultDispatcher = new OnActivityResultDispatcher(this);

    /**
     * Retrieve the {@link OnActivityResultDispatcher} that will be triggered when
     * {@link #onActivityResult(int, int, Intent)} is called.
     *
     * @return The {@link OnActivityResultDispatcher} associated with this ComponentActivity.
     */
    @NonNull
    @Override
    public OnActivityResultDispatcher getOnActivityResultDispatcher() {
        return mOnActivityResultDispatcher;
    }

    @NonNull
    @Override
    public CompatActivity getActivity() {
        return this;
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mOnActivityResultDispatcher.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, OnActivityResultCallback callback) {
        mOnActivityResultDispatcher.addCallback(callback);
        super.startActivityForResult(intent, callback.getRequestCode());
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityResultCallback callback) {
        mOnActivityResultDispatcher.addCallback(callback);
        super.startActivityForResult(intent, callback.getRequestCode(), options);
    }

}
