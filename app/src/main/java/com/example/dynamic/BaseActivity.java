package com.example.dynamic;

import androidx.activity.CompatActivity;

public abstract class BaseActivity extends CompatActivity {
//    static final class NonConfigurationInstances {
//        Object custom;
//        FeatureStore viewModelStore;
//    }
//
//    // Lazily recreated from NonConfigurationInstances by getFeatureStore()
//    private FeatureStore mFeatureStore;
//
//    @NonNull
//    @Override
//    public FeatureStore getFeatureStore() {
//        if (getApplication() == null) {
//            throw new IllegalStateException("Your activity is not yet attached to the "
//                    + "Application instance. You can't request Feature before onCreate call.");
//        }
//        if (mFeatureStore == null) {
//            NonConfigurationInstances nc =
//                    (NonConfigurationInstances) getLastNonConfigurationInstance();
//            if (nc != null) {
//                // Restore the FeatureStore from NonConfigurationInstances
//                mFeatureStore = nc.viewModelStore;
//            }
//            if (mFeatureStore == null) {
//                mFeatureStore = new FeatureStore();
//            }
//        }
//        return mFeatureStore;
//    }
}
