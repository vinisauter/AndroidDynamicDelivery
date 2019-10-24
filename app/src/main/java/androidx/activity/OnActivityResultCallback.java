package androidx.activity;

import android.content.Intent;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

/**
 * Class for handling {@link OnActivityResultDispatcher#onActivityResult(int, int, Intent)}  callbacks without
 * strongly coupling that implementation to a subclass of {@link ComponentActivity}.
 * <p>
 * This class maintains its own {@link #isEnabled() enabled state}. Only when this callback
 * is enabled will it receive callbacks to {@link #handleOnActivityResult(int, Intent)} .
 * <p>
 * Note that the enabled state is an additional layer on top of the
 * {@link androidx.lifecycle.LifecycleOwner} passed to
 * {@link OnActivityResultDispatcher#addCallback(OnActivityResultCallback)}
 * which controls when the callback is added and removed to the dispatcher.
 * <p>
 *
 * @see ComponentActivity#getOnActivityResultDispatcher()
 */
public abstract class OnActivityResultCallback {

    private final int mRequestCode;
    private boolean mEnabled;
    private Integer mResultCode;
    private Intent mData;

    /**
     * Create a {@link OnActivityResultCallback}.
     *
     * @param enabled The default enabled state for this callback.
     * @see #setEnabled(boolean)
     */
    public OnActivityResultCallback(int requestCode, boolean enabled) {
        mRequestCode = requestCode;
        mEnabled = enabled;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    /**
     * Set the enabled state of the callback. Only when this callback
     * is enabled will it receive callbacks to {@link #handleOnActivityResult(int, Intent)} .
     * <p>
     * Note that the enabled state is an additional layer on top of the
     * {@link androidx.lifecycle.LifecycleOwner} passed to
     * {@link OnActivityResultDispatcher#addCallback(OnActivityResultCallback)}
     * which controls when the callback is added and removed to the dispatcher.
     *
     * @param enabled whether the callback should be considered enabled
     */
    @MainThread
    public final void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    /**
     * Checks whether this callback should be considered enabled. Only when this callback
     * is enabled will it receive callbacks to {@link #handleOnActivityResult(int, Intent)} .
     *
     * @return Whether this callback should be considered enabled.
     */
    @MainThread
    public final boolean isEnabled() {
        return mEnabled;
    }

    /**
     * Callback for handling the {@link OnActivityResultDispatcher#onActivityResult(int, int, Intent)} event.
     */
    @MainThread
    public abstract void handleOnActivityResult(int resultCode, @Nullable Intent data);


    public void setActivityResult(int resultCode, Intent data) {
        this.mResultCode = resultCode;
        this.mData = data;
    }

    public boolean hasResult() {
        return mEnabled && mResultCode != null;
    }

    @MainThread
    void handleOnActivityResult() {
        handleOnActivityResult(mResultCode, mData);
    }
}
