package androidx.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

/**
 * A class that has an {@link OnActivityResultDispatcher} that allows you to register a
 * {@link OnActivityResultCallback} for handling the system back button.
 * <p>
 * It is expected that classes that implement this interface route the system back button
 * to the dispatcher
 *
 * @see OnActivityResultDispatcher
 */
public interface OnActivityResultDispatcherOwner extends LifecycleOwner {

    /**
     * Retrieve the {@link OnActivityResultDispatcher} that should handle the system back button.
     *
     * @return The {@link OnActivityResultDispatcher}.
     */
    @NonNull
    OnActivityResultDispatcher getOnActivityResultDispatcher();

    @NonNull
    FragmentActivity getActivity();
}
