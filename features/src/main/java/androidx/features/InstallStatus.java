package androidx.features;

import android.app.Activity;
import android.content.IntentSender;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public enum InstallStatus {
    UNKNOWN(SplitInstallSessionStatus.UNKNOWN),
    PENDING(SplitInstallSessionStatus.PENDING),
    REQUIRES_USER_CONFIRMATION(SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION),
    DOWNLOADING(SplitInstallSessionStatus.DOWNLOADING),
    DOWNLOADED(SplitInstallSessionStatus.DOWNLOADED),
    INSTALLING(SplitInstallSessionStatus.INSTALLING),
    INSTALLED(SplitInstallSessionStatus.INSTALLED),
    FAILED(SplitInstallSessionStatus.FAILED),
    CANCELING(SplitInstallSessionStatus.CANCELING),
    CANCELED(SplitInstallSessionStatus.CANCELED);

    private final int installStatus;

    InstallStatus(@SplitInstallSessionStatus int installStatus) {
        this.installStatus = installStatus;
    }

    private SplitInstallSessionState state;
    private SplitInstallManager splitInstallManager;
    private Exception error;

    public boolean startConfirmationDialogForResult(Activity activity, int requestCode) {
        try {
            return splitInstallManager.startConfirmationDialogForResult(state, activity, requestCode);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static InstallStatus valueOf(SplitInstallManager manager, SplitInstallSessionState state) {
        return valueOf(state.status()).setState(state).setManager(manager);
    }

    public SplitInstallSessionState getState() {
        return state;
    }

    private InstallStatus setState(SplitInstallSessionState state) {
        this.state = state;
        return this;
    }

    private InstallStatus setManager(SplitInstallManager splitInstallManager) {
        this.splitInstallManager = splitInstallManager;
        return this;
    }

    private static InstallStatus valueOf(int status) {
        switch (status) {
            case SplitInstallSessionStatus.PENDING:
                return PENDING;
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                return REQUIRES_USER_CONFIRMATION;
            case SplitInstallSessionStatus.DOWNLOADING:
                return DOWNLOADING;
            case SplitInstallSessionStatus.DOWNLOADED:
                return DOWNLOADED;
            case SplitInstallSessionStatus.INSTALLING:
                return INSTALLING;
            case SplitInstallSessionStatus.INSTALLED:
                return INSTALLED;
            case SplitInstallSessionStatus.FAILED:
                return FAILED;
            case SplitInstallSessionStatus.CANCELING:
                return CANCELING;
            case SplitInstallSessionStatus.CANCELED:
                return CANCELED;
            case SplitInstallSessionStatus.UNKNOWN:
            default:
                return UNKNOWN;
        }
    }

    public InstallStatus setError(Exception exception) {
        this.error = exception;
        return this;
    }

    public Exception getError() {
        return error;
    }

    public <T> T getFeature(Class<T> featureClass) {
        return new FeatureProvider(this).get(featureClass);
    }

    public long getDownloadPercentage() {
        long percentageCounter = 0;
        switch (installStatus) {
            case SplitInstallSessionStatus.UNKNOWN:
            case SplitInstallSessionStatus.FAILED:
            case SplitInstallSessionStatus.CANCELED:
            case SplitInstallSessionStatus.PENDING:
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                percentageCounter = 0L;
                break;
            case SplitInstallSessionStatus.DOWNLOADING:
                if (getState() != null) {
                    long totalBytes = getState().totalBytesToDownload();
                    long downloadedBytes = getState().bytesDownloaded();
                    percentageCounter = (downloadedBytes / totalBytes) * 100;
                }
                break;
            case SplitInstallSessionStatus.DOWNLOADED:
            case SplitInstallSessionStatus.INSTALLING:
            case SplitInstallSessionStatus.INSTALLED:
                percentageCounter = 100L;
                break;
        }
        return percentageCounter;
    }
}
