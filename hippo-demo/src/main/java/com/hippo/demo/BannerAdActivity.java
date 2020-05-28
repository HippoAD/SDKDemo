package com.hippo.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.transsion.core.utils.ToastUtil;
import com.zero.ta.api.adx.ABannerView;
import com.zero.ta.api.listener.AdListener;
import com.zero.ta.api.listener.TaRequest;
import com.zero.ta.common.constant.TaErrorCode;

import java.lang.ref.WeakReference;

/**
 * Example of Banner Ad
 */
public class BannerAdActivity extends AppCompatActivity {

    private ABannerView bannerView;
    private TextView loadingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        loadingTv = findViewById(R.id.loading);

        createBanner();
    }

    private void createBanner() {
        bannerView = findViewById(R.id.bannerview);
        bannerView.setPlacementId("1207186642240487424");

        TaRequest tAdRequest = new TaRequest.TaRequestBuild()
                .setListener(new TadListener(this))
                .build();
        bannerView.setAdRequest(tAdRequest);
    }

    public void loadBannerAd(View view) {
        bannerView.loadAd();
        loadingVisible(true);
    }

    public void showAd() {
        bannerView.show();
    }

    private void loadingVisible(boolean load) {
        loadingTv.setVisibility(load ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
    }

    private static class TadListener extends AdListener {
        WeakReference<BannerAdActivity> weakReference;

        TadListener(BannerAdActivity activity) {
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
    }
}
