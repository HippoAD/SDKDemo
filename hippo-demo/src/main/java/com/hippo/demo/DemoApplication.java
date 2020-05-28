package com.hippo.demo;

import android.app.Application;

import com.transsion.core.CoreUtil;
import com.zero.ta.api.config.HippoAd;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CoreUtil.init(this);
        HippoAd.init(new HippoAd.AdConfigBuilder()
                .setAppId(20093)
                .setAppToken("e2cceb572c79d5ed7e523752a0ed234b9adfcc05")
                .setDebug(true)
                .testServer(true)
                .build());
    }
}
