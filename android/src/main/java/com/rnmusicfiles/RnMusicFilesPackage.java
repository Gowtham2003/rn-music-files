package com.rnmusicfiles;

import androidx.annotation.NonNull;

import com.rnmusicfiles.Module.CoverImage;
import com.rnmusicfiles.Module.rnmusicfilesModule;
import com.rnmusicfiles.Utils.SerialExecutor;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RnMusicFilesPackage implements ReactPackage {
  private SerialExecutor executor = new SerialExecutor();

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
    return Arrays.<NativeModule>asList(new rnmusicfilesModule(reactContext, executor));
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return null;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
    return Arrays.asList(
                new CoverImage(executor)
        );
    }
}
