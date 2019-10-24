package androidx.features;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

@SuppressWarnings("WeakerAccess")
public class FeatureProvider {
    private static final String DEFAULT_KEY = "FeatureProvider.DefaultKey";

    @SuppressWarnings({"unused", "FieldCanBeLocal"})// TODO: replace with FeatureOwner
    private final Object owner;
    private final HashMap<String, Object> mFeatureStore = new HashMap<>();
    private final Factory factory;

    public FeatureProvider(@NonNull Object owner) {
        this(owner, owner instanceof DefaultFeatureProviderFactory
                ? ((DefaultFeatureProviderFactory) owner).getDefaultFeatureProviderFactory()
                : ServiceLoaderFactory.getInstance());
    }

    public FeatureProvider(@NonNull Object owner, @NonNull Factory factory) {
        this.factory = factory;
        this.owner = owner;
    }

    @NonNull
    @MainThread
    public <T> T get(@NonNull Class<T> featureClass) {
        String canonicalName = featureClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be Features");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, featureClass);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @MainThread
    private <T> T get(@NonNull String key, @NonNull Class<T> featureClass) {
        Object feature = mFeatureStore.get(key);
        if (featureClass.isInstance(feature)) {
            return (T) feature;
        } else {
            //noinspection StatementWithEmptyBody
            if (feature != null) {
                // TODO: log a warning.
            }
        }
        try {
            feature = factory.create(featureClass);
        } catch (NoSuchElementException throwable) {
            FeatureFromModule featureFromModule = featureClass.getAnnotation(FeatureFromModule.class);
            if (featureFromModule != null) {
                throw new IllegalArgumentException("Feature implementation where not found. \n" +
                        "Make sure you have downloaded the module \"" + featureFromModule.value() + "\" first, and that you have defined the service inplementation, via META-INF.service, or via @AutoService for class " + featureClass.getCanonicalName(), throwable);
            } else {
                throw new IllegalArgumentException("Feature implementation where not found. \n" +
                        "Please use @FeatureFromModule(\"the_module_name\") to define the model that this class is implemented", throwable);
            }
        }
        mFeatureStore.put(key, feature);
        return (T) feature;
    }

    /**
     * Simple factory, which finds the implementation of the provided interface and calls its empty constructor via ServiceLoader
     */
    public static class ServiceLoaderFactory implements Factory {

        private static ServiceLoaderFactory sInstance;

        /**
         * Retrieve a singleton instance of ServiceLoaderFactory.
         *
         * @return A valid {@link ServiceLoaderFactory}
         */
        @NonNull
        public static ServiceLoaderFactory getInstance() {
            if (sInstance == null) {
                sInstance = new ServiceLoaderFactory();
            }
            return sInstance;
        }

        @NonNull
        @Override
        public <T> T create(@NonNull Class<T> featureClass) {
            // Ask ServiceLoader for concrete implementations of StorageFeature.Provider
            // Explicitly use the 2-argument version of load to enable R8 optimization.
            ServiceLoader<T> serviceLoader = ServiceLoader.load(featureClass, featureClass.getClassLoader());

            // Explicitly ONLY use the .iterator() method on the returned ServiceLoader to enable R8 optimization.
            // When these two conditions are met, R8 replaces ServiceLoader calls with direct object instantiation.
            return serviceLoader.iterator().next();
        }
    }

    /**
     * Implementations of {@code Factory} interface are responsible to instantiate Features.
     */
    public interface Factory {
        /**
         * Creates a new instance of the given {@code Class}.
         * <p>
         *
         * @param featureClass a {@code Class} whose instance is requested
         * @param <T>          The type parameter for the Feature.
         * @return a newly created Feature
         */
        @NonNull
        <T> T create(@NonNull Class<T> featureClass);
    }
}
