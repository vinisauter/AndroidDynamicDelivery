package androidx.features;

import android.util.Log;

import androidx.lifecycle.Observer;

public abstract class InstallStatusObserver implements Observer<InstallStatus> {
    private InstallStatus lastStatus = null;

    @Override
    public void onChanged(InstallStatus installStatus) {
        if (lastStatus == installStatus) {
            return;
        }
        Log.i("InstallStatus", installStatus + "" + installStatus.getModules());
        try {
            switch (installStatus) {
                case PENDING:
                    break;
                case REQUIRES_USER_CONFIRMATION:
                    requiresUserConfirmation(installStatus);
                    break;
                case DOWNLOADING:
                    downloading(installStatus.getDownloadPercentage());
                    break;
                case INSTALLING:
                    installing();
                    break;
                case INSTALLED:
                    installed(installStatus);
                    break;
                case FAILED:
                    failed(new InstallationException("Installation failed. " + installStatus.getState().errorCode()));
                    break;
                case CANCELED:
                    failed(new InstallationException("Installation canceled. " + installStatus.getState().errorCode()));
                    break;
            }
        } catch (Exception e) {
            failed(e);
        }
        lastStatus = installStatus;
    }

    public void requiresUserConfirmation(InstallStatus installStatus) {

    }

    public void downloading(long downloadPercentage) {

    }

    public void installing() {

    }

    public abstract void installed(InstallStatus installStatus);

    public abstract void failed(Exception error);
}
