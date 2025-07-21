package com.psychokitty.game.android;

	import android.content.Context;
	import android.net.ConnectivityManager;
	import android.net.Network;
	import android.net.NetworkCapabilities;
	import android.net.NetworkInfo;
	import android.os.Build;
	import android.os.Bundle;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.RelativeLayout;

	import com.badlogic.gdx.backends.android.AndroidApplication;
	import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
	import com.google.android.gms.ads.AdRequest;
	import com.google.android.gms.ads.AdSize;
	import com.google.android.gms.ads.AdView;
	import com.psychokitty.game.AdMob.AdsController;
	import com.psychokitty.game.BuildConfig;
	import com.psychokitty.game.PsychoKittyGame;

	public class AndroidLauncher extends AndroidApplication implements AdsController {
	    private static final String BANNER_AD_UNIT_ID = BuildConfig.BANNER_AD_UNIT_ID;
	    private AdView bannerAd;
	    private AdRequest adRequest;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

	        View gameView = initializeForView(new PsychoKittyGame(this), config);
	        setupAds();
	        setupLayout(gameView);
	    }

	    private void setupLayout(View gameView) {
	        RelativeLayout layout = new RelativeLayout(this);
	        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
	                ViewGroup.LayoutParams.MATCH_PARENT);

	        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
	                ViewGroup.LayoutParams.MATCH_PARENT,
	                ViewGroup.LayoutParams.WRAP_CONTENT);
	        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	        layout.addView(bannerAd, adParams);

	        setContentView(layout);
	    }

	    private void setupAds() {
	        bannerAd = new AdView(this);
	        bannerAd.setVisibility(View.INVISIBLE);
	        bannerAd.setBackgroundColor(0xff000000);
	        bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
	        bannerAd.setAdSize(getAdaptiveBannerSize());

	        // Pre-build the ad request
	        adRequest = new AdRequest.Builder().build();
	    }

	    private AdSize getAdaptiveBannerSize() {
	        android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
	        int adWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
	        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	    }

	    @Override
	    public void showBannerAd() {
	        runOnUiThread(() -> {
	            bannerAd.setVisibility(View.VISIBLE);
	            bannerAd.loadAd(adRequest);
	        });
	    }

	    @Override
	    public void hideBannerAd() {
	        runOnUiThread(() -> bannerAd.setVisibility(View.INVISIBLE));
	    }

	    @Override
	    public boolean isWifiConnected() {
	        ConnectivityManager connectivityManager =
	            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivityManager == null) return false;

	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
	            return isConnectedModern(connectivityManager);
	        } else {
	            return isConnectedLegacy(connectivityManager);
	        }
	    }

	    private boolean isConnectedModern(ConnectivityManager connectivityManager) {
	        Network network = connectivityManager.getActiveNetwork();
	        if (network == null) return false;

	        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
	        return capabilities != null && (
	            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
	            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
	            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
	            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
	        );
	    }

	    @SuppressWarnings("deprecation")
	    private boolean isConnectedLegacy(ConnectivityManager connectivityManager) {
	        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	        return networkInfo != null &&
	               (networkInfo.isConnected() || networkInfo.getState() == NetworkInfo.State.CONNECTING);
	    }
	}