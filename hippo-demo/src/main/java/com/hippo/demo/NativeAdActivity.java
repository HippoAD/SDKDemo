package com.hippo.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.transsion.core.utils.ToastUtil;
import com.zero.ta.api.adx.ANative;
import com.zero.ta.api.info.AdInfo;
import com.zero.ta.api.listener.AdListener;
import com.zero.ta.api.listener.TaRequest;
import com.zero.ta.api.view.HippoNativeView;
import com.zero.ta.api.view.MediaView;
import com.zero.ta.common.constant.TaErrorCode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of native ad
 */
public class NativeAdActivity extends AppCompatActivity {

    private ANative nativeAd;
    private HippoNativeView hippoNativeView;
    private TextView loadingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        hippoNativeView = findViewById(R.id.native_view);
        loadingTv = findViewById(R.id.loading);

        createNativeAd();
    }

    private void createNativeAd() {
        nativeAd = new ANative("1207186651035942912");
        TaRequest tAdRequest = new TaRequest.TaRequestBuild()
                .setListener(new TadListener(this))
                .build();
        nativeAd.setAdRequest(tAdRequest);
    }

    /**
     * Suitable for within the current scene
     */
    public void loadNativeAd(View view) {
        nativeAd.loadAd();
        loadingVisible(true);
    }

    private void loadingVisible(boolean load) {
        loadingTv.setVisibility(load ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
        super.onDestroy();
    }

    private void inflateView(AdInfo adInfo) {
        if (nativeAd == null || nativeAd.isLoading()) {
            return;
        }

        if(!adInfo.isMediaCached()) {
            nativeAd.downloadMedia(adInfo);
        } else {
            LinearLayout adView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.native_ad_unit, null);
            ImageView icon = adView.findViewById(R.id.native_ad_icon);
            MediaView mediaView = adView.findViewById(R.id.coverview);
            TextView title = adView.findViewById(R.id.native_ad_title);
            TextView des = adView.findViewById(R.id.native_ad_body);
            Button calltoaction = adView.findViewById(R.id.call_to_action);
            TextView sponsored = adView.findViewById(R.id.sponsored);

            hippoNativeView.addView(adView);
            hippoNativeView.setIconView(icon);
            hippoNativeView.setMediaView(mediaView);

            sponsored.setVisibility(TextUtils.isEmpty(adInfo.getSponsor()) ? View.GONE : View.VISIBLE);

            title.setText(adInfo.getTitle());
            des.setText(adInfo.getDescription());

            calltoaction.setText(adInfo.getCtatext());
            List<View> adContains = new ArrayList<>();
            adContains.add(title);
            adContains.add(icon);
            adContains.add(mediaView);
            adContains.add(calltoaction);
            adContains.add(des);
            nativeAd.registerViews(hippoNativeView, mediaView, adContains, adInfo);
        }
    }

    private static class TadListener extends AdListener {
        WeakReference<NativeAdActivity> weakReference;

        TadListener(NativeAdActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onMediaDownloaded(AdInfo adInfo) {
            if (weakReference.get() != null) {
                weakReference.get().inflateView(adInfo);
            }
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
        public void onAdLoaded(AdInfo adInfo) {
            if (weakReference.get() != null) {
                weakReference.get().loadingVisible(false);

                weakReference.get().inflateView(adInfo);
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
            ToastUtil.showToast("ad click");
        }
    }
}
