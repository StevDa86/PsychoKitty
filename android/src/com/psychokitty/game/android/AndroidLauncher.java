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

public class AndroidLauncher extends AndroidApplication implements AdsController{
	private static final String BANNER_AD_UNIT_ID = BuildConfig.BANNER_AD_UNIT_ID;
	AdView bannerAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new PsychoKittyGame(), config);
		//hier neu wegen adview
		View gameView = initializeForView(new PsychoKittyGame(this), config);
		setupAds();

		// Define the layout
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layout.addView(bannerAd, params);

		setContentView(layout);

	}

	public void setupAds() {
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(0xff000000); // black
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		AdSize adSize = getAdaptiveBannerSize();
		bannerAd.setAdSize(adSize);
	}

	private AdSize getAdaptiveBannerSize() {
		// Get the display metrics to determine screen width
		android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int adWidthPixels = displayMetrics.widthPixels;

		// Convert pixels to dp
		float density = displayMetrics.density;
		int adWidth = (int) (adWidthPixels / density);

		// Return the optimal size for the screen
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new ShowBannerAdRunnable());
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) return false;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Network nw = connectivityManager.getActiveNetwork();
			if (nw == null) return false;
			NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
			return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
					actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
					actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
					actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
		} else {
			// For older versions, suppress all deprecation warnings
			@SuppressWarnings("deprecation")
			NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
			@SuppressWarnings("deprecation")
			boolean isConnected = nwInfo != null && (nwInfo.isConnected() || nwInfo.getState() == NetworkInfo.State.CONNECTING);
			return isConnected;
		}
	}

    @Override
	public void hideBannerAd() {
		runOnUiThread(new HideBannerAdRunnable());
	}

	private class ShowBannerAdRunnable implements Runnable {
		@Override
		public void run() {
			bannerAd.setVisibility(View.VISIBLE);
			AdRequest.Builder builder = new AdRequest.Builder();
			AdRequest ad = builder.build();
			bannerAd.loadAd(ad);
		}
	}

	private class HideBannerAdRunnable implements Runnable {
		@Override
		public void run() {
			bannerAd.setVisibility(View.INVISIBLE);
			new AdRequest.Builder();
		}
	}
}

