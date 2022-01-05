package com.esri.arcgisruntime.sample.setinitialmaplocation;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class DemoApp extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        com.secneo.sdk.Helper.install(this);
    }
}
