package com.hippo.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.transsion.core.utils.ToastUtil;
import com.zero.ta.api.adx.ASplash;
import com.zero.ta.api.listener.AdListener;
import com.zero.ta.api.listener.OnSkipListener;
import com.zero.ta.api.listener.TaRequest;
import com.zero.ta.common.constant.TaErrorCode;

import java.lang.ref.WeakReference;

/**
 * Example of Splash Ad
 */
public class SplashAdActivity extends AppCompatActivity {

    private ASplash splashView;
    private TextView loadingTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingTv = findViewById(R.id.loading);

        loadSplashAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadSplashAd() {
        splashView = findViewById(R.id.splash_ad);
        splashView.setPlacementId("1207186654622072832");

        TaRequest tAdRequest = new TaRequest.TaRequestBuild()
                .setListener(new TadListener(this))
                .build();
        splashView.setAdRequest(tAdRequest);

        splashView.setSkipListener(new TskipListener());

        splashView.loadAd();
        loadingVisible(true);
    }

    private void showSplash() {
        FrameLayout logo = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.logo_layout, null);
        splashView.show(logo);

//        splashView.setFlag(ASplash.FLAG_NO_LOGO_LAYOUT);
//        splashView.show(null);
    }

    private void loadingVisible(boolean load) {
        loadingTv.setVisibility(load ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashView != null) {
            splashView.destroy();
            splashView = null;
        }
    }

    private class TskipListener implements OnSkipListener {

        @Override
        public void onClick() {
            ToastUtil.showLongToast("go to Main");
            SplashAdActivity.this.finish();
        }

        @Override
        public void onTimeEnd() {
            ToastUtil.showLongToast("go to Main");
            SplashAdActivity.this.finish();
        }
    }

    private static class TadListener extends AdListener {
        WeakReference<SplashAdActivity> weakReference;

        TadListener(SplashAdActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onError(TaErrorCode taErrorCode) {
            if (weakReference.get() != null) {
                weakReference.get().loadingVisible(false);
            }
            // Request failed
            ToastUtil.showToast("load error : " + taErrorCode.getErrorMessage());
        }

        @Override
        public void onAdLoaded() {
            if (weakReference.get() != null) {
                weakReference.get().loadingVisible(false);
                weakReference.get().showSplash();
            }
            // Request success
            ToastUtil.showToast("ad load");
        }

        @Override
        public void onAdShow() {
            // Called when an ad is displayed
            ToastUtil.showToast("ad show");
        }

        @Override
        public void onAdClicked() {
            // Called when an ad is clicked
            ToastUtil.showToast("ad clicked");
        }
    }
}
