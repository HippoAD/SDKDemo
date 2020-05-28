package com.hippo.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.transsion.core.utils.ToastUtil;
import com.zero.ta.api.adx.AInterstitial;
import com.zero.ta.api.listener.AdListener;
import com.zero.ta.api.listener.TaRequest;
import com.zero.ta.common.constant.TaErrorCode;

import java.lang.ref.WeakReference;

/**
 * Example of Interstitial Ad
 */
public class InterstitialActivity extends AppCompatActivity {

    private AInterstitial interstitial;
    private TextView loadingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        loadingTv = findViewById(R.id.loading);

        createInterstitial();
    }

    private void createInterstitial() {
        interstitial = new AInterstitial("1207186650155089920");
        TaRequest tAdRequest = new TaRequest.TaRequestBuild()
                .setListener(new TadListener(this))
                .build();
        interstitial.setAdRequest(tAdRequest);
    }

    public void loadInterstitial(View view) {
        interstitial.loadAd();
        loadingVisible(true);
    }

    public void showAd() {
        interstitial.show();
    }

    private void loadingVisible(boolean load) {
        loadingTv.setVisibility(load ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitial != null) {
            interstitial.destroy();
            interstitial = null;
        }
    }

    private static class TadListener extends AdListener {
        WeakReference<InterstitialActivity> weakReference;

        TadListener(InterstitialActivity activity) {
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
                weakReference.get().showAd();
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

        @Override
        public void onAdClosed() {
            // Called when an ad is closed
            ToastUtil.showToast("ad closed");
        }
    }
}
