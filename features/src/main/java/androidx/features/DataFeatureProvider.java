package androidx.features;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class DataFeatureProvider extends FeatureProvider {

    public DataFeatureProvider(@NonNull ViewModel owner) {
        super(owner);
    }

    public DataFeatureProvider(@NonNull ViewModel owner, @NonNull Factory factory) {
        super(owner, factory);
    }
}
