package com.psychokitty.game;

/**
 * Created by Steven on 02.08.2015.
 */
public class DummyAdsController implements AdsController{


    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }


    @Override
    public boolean isWifiConnected() {
        return false;
    }

}