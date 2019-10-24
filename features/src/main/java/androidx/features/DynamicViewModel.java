package androidx.features;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DynamicViewModel extends AndroidViewModel {
    private final SplitInstallManager splitInstallManager = SplitInstallManagerFactory.create(getApplication());
    private Integer sessionId = 0;

    private MutableLiveData<InstallStatus> installStatus = new MutableLiveData<>();

    private final SplitInstallStateUpdatedListener listener = state -> {
        // if (state.sessionId() == sessionId)
        installStatus.postValue(InstallStatus.valueOf(splitInstallManager, state));
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

    public LiveData<InstallStatus> getInstallStatus() {
        return installStatus;
    }

    public LiveData<InstallStatus> installModules(String... modules) {
        Set<String> modulesToInstall = new HashSet<>(Arrays.asList(modules));
        startInstall(modulesToInstall);
        return installStatus;
    }

    public LiveData<InstallStatus> installFeatures(Class<?>... featureClasses) {
        Set<String> modulesToInstall = new HashSet<>();
        for (Class<?> featureClass : featureClasses) {
            FeatureFromModule featureFromModule = featureClass.getAnnotation(FeatureFromModule.class);
            if (featureFromModule == null) {
                throw new IllegalArgumentException("Feature where not found. " +
                        "Please use @FeatureFromModule (\"the_module_name\") to define which module this feature implemented.");
            } else {
                String module = featureFromModule.value();
                modulesToInstall.add(module);

            }
        }
        startInstall(modulesToInstall);
        return installStatus;
    }

    private void startInstall(Set<String> modules) {
        SplitInstallRequest.Builder builder = SplitInstallRequest.newBuilder();
        for (String module : modules) {
            builder.addModule(module);
        }
        SplitInstallRequest request = builder.build();

        splitInstallManager.startInstall(request)
                .addOnFailureListener(exception -> installStatus.setValue(InstallStatus.FAILED.setError(exception)))
                .addOnSuccessListener(sessionId -> this.sessionId = sessionId);
    }

    public void uninstallModules(String... modules) {
        splitInstallManager.deferredUninstall(Arrays.asList(modules));
    }

    public void deferredInstallModules(String... modules) {
        splitInstallManager.deferredInstall(Arrays.asList(modules));
    }
}
