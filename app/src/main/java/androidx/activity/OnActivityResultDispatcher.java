package androidx.activity;

import android.content.Intent;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayDeque;
import java.util.Iterator;

@SuppressWarnings({"WeakerAccess", "unused"})
public class OnActivityResultDispatcher implements LifecycleEventObserver, Cancellable {

    private final ArrayDeque<OnActivityResultCallback> mOnActivityResultCallbacks = new ArrayDeque<>();
    private final Lifecycle mLifecycle;

    public OnActivityResultDispatcher(@NonNull LifecycleOwner owner) {
        mLifecycle = owner.getLifecycle();
        mLifecycle.addObserver(this);
    }

    @MainThread
    public void addCallback(@NonNull OnActivityResultCallback onActivityResultCallback) {
        if (mLifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        mOnActivityResultCallbacks.add(onActivityResultCallback);
    }

    @MainThread
    public boolean hasEnabledCallbacks() {
        Iterator<OnActivityResultCallback> iterator = mOnActivityResultCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            if (iterator.next().isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @MainThread
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Iterator<OnActivityResultCallback> iterator = mOnActivityResultCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            OnActivityResultCallback callback = iterator.next();
            if (requestCode == callback.getRequestCode()) {
                callback.setActivityResult(resultCode, data);
                if (callback.isEnabled()) {
                    callback.handleOnActivityResult();
                    mOnActivityResultCallbacks.remove(callback);
                }
                return;
            }
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source,
                               @NonNull Lifecycle.Event event) {
        Iterator<OnActivityResultCallback> iterator = mOnActivityResultCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            OnActivityResultCallback callback = iterator.next();
            if (event == Lifecycle.Event.ON_RESUME) {
                callback.setEnabled(true);
                if (callback.hasResult()) {
                    callback.handleOnActivityResult();
                    mOnActivityResultCallbacks.remove(callback);
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                callback.setEnabled(false);
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                cancel();
            }
        }
    }

    @Override
    public void cancel() {
        Iterator<OnActivityResultCallback> iterator = mOnActivityResultCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            OnActivityResultCallback mOnActivityResultCallback = iterator.next();
            mOnActivityResultCallbacks.remove(mOnActivityResultCallback);
            mLifecycle.removeObserver(this);
        }
    }
}
