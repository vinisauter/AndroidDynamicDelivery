package androidx.features;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;

import java.util.Arrays;


@SuppressWarnings({"unused", "WeakerAccess"})
public class DynamicViewModel extends AndroidViewModel {
    private static final String TAG = "DynamicViewModel";
    private final SplitInstallManager splitInstallManager = SplitInstallManagerFactory.create(getApplication());
    private Integer sessionId = 0;

    private MutableLiveData<InstallStatus> installStatus = new MutableLiveData<>();

    private final SplitInstallStateUpdatedListener listener = state -> {
        if (state.sessionId() == sessionId) {
            installStatus.postValue(InstallStatus.valueOf(splitInstallManager, state));
        }
    };

    public DynamicViewModel(@NonNull Application application) {
        super(application);
        splitInstallManager.registerListener(listener);
        installStatus.postValue(InstallStatus.UNKNOWN);
    }

    @Override
    protected void onCleared() {
        splitInstallManager.unregisterListener(listener);
        super.onCleared();
    }

    public boolean isModuleInstalled(String module) {
        return splitInstallManager.getInstalledModules().contains(module);
    }

    public <T> LiveData<T> getFeatureLiveData(Class<T> featureClass, String... modules) {
        boolean isModuleInstalled = true;
        for (String module : modules) {
            if (!isModuleInstalled(module)) {
                isModuleInstalled = false;
            }
        }
        if (isModuleInstalled) {
            MutableLiveData<T> featureLiveData = new MutableLiveData<>();
            featureLiveData.setValue(new FeatureProvider(this).get(featureClass));
            return featureLiveData;
        } else {
            return Transformations.switchMap(installModules(modules), new Function<InstallStatus, LiveData<T>>() {
                MutableLiveData<T> featureLiveData = new MutableLiveData<>();

                @Override
                public LiveData<T> apply(InstallStatus input) {
                    if (input == InstallStatus.INSTALLED) {
                        featureLiveData.setValue(new FeatureProvider(this).get(featureClass));
                    }
                    return featureLiveData;
                }
            });
        }
    }

    public LiveData<Long> getDownloadingPercentage() {
        return Transformations.switchMap(installStatus, new Function<InstallStatus, LiveData<Long>>() {
            MutableLiveData<Long> percentageCounter = new MutableLiveData<Long>() {{
                setValue(0L);
            }};

            @Override
            public LiveData<Long> apply(InstallStatus status) {
                if (status == null) {
                    percentageCounter.postValue(0L);
                } else {
                    percentageCounter.postValue(status.getDownloadPercentage());
                }
                return percentageCounter;
            }
        });
    }

    public LiveData<InstallStatus> installModules(String... modules) {
        SplitInstallRequest.Builder builder = SplitInstallRequest.newBuilder();
        for (String module : modules) {
            builder.addModule(module);
        }
        SplitInstallRequest request = builder.build();

        splitInstallManager.startInstall(request)
                .addOnFailureListener(exception -> installStatus.setValue(InstallStatus.FAILED.setError(exception)))
                .addOnSuccessListener(sessionId -> this.sessionId = sessionId);
        return installStatus;
    }

    public void uninstallModules(String... modules) {
        splitInstallManager.deferredUninstall(Arrays.asList(modules));
    }

    public void deferredInstallModules(String... modules) {
        splitInstallManager.deferredInstall(Arrays.asList(modules));
    }
}
