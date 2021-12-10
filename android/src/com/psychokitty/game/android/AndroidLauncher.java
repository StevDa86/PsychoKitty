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
		bannerAd.setAdSize(AdSize.SMART_BANNER);
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);

			}
		});
	}

    @Override
    public boolean isWifiConnected() {
		//old Style
        /*ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return (ni != null && ni.isConnected());
        */

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Network nw = connectivityManager.getActiveNetwork();
			if (nw == null) return false;
			NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
			return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
		} else {
			NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
			return nwInfo != null && nwInfo.isConnected();
		}
    }

    @Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
			}
		});
	}
}
