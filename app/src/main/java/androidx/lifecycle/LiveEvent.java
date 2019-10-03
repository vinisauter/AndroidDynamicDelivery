package androidx.lifecycle;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 *
 * @see <a href="https://github.com/googlesamples/android-architecture/tree/dev-todo-mvvm-live#live-events">Live events</a>
 */
@SuppressWarnings("unused")
public class LiveEvent<T> extends MediatorLiveData<T> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public LiveEvent() {
        super();
    }

    public <S> LiveEvent(@NonNull LiveData<S> source, @NonNull final Function<S, T> mapFunction) {
        super();
        super.addSource(source, x -> setValue(mapFunction.apply(x)));
    }

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
                super.setValue(null);
            }
        });
    }

    public <S> void addSource(@NonNull LiveData<S> source, @NonNull final Function<S, T> mapFunction) {
        super.addSource(source, x -> setValue(mapFunction.apply(x)));
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}