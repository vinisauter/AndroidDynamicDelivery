package androidx.features;

public class InstallationException extends Exception {

    public InstallationException() {
    }

    public InstallationException(String message) {
        super(message);
    }

    public InstallationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstallationException(Throwable cause) {
        super(cause);
    }

    public InstallationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
