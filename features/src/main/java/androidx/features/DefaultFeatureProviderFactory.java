package androidx.features;

import androidx.annotation.NonNull;

/**
 * Interface that marks a {@link FeatureProvider} as having a default
 * {@link FeatureProvider.Factory}
 */
public interface DefaultFeatureProviderFactory {
    /**
     * Returns the default {@link FeatureProvider.Factory} that should be
     * used when no custom {@code Factory} is provided to the
     * {@link FeatureProvider} constructors.
     *
     * @return a {@code ViewModelProvider.Factory}
     */
    @NonNull
    FeatureProvider.Factory getDefaultFeatureProviderFactory();
}