package androidx.features;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class UIFeatureProvider extends FeatureProvider {

    public UIFeatureProvider(@NonNull FragmentActivity owner) {
        super(owner);
    }

    public UIFeatureProvider(@NonNull FragmentActivity owner, @NonNull Factory factory) {
        super(owner, factory);
    }
}
