package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.psychokitty.game.Utils.Assets;

public class PsychoKittyGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;

    private com.psychokitty.game.AdMob.AdsController adsController;

    public PsychoKittyGame(com.psychokitty.game.AdMob.AdsController adsController) {
        if (adsController != null) {
            this.adsController = adsController;
        } else {
            this.adsController = new com.psychokitty.game.AdMob.DummyAdsController();
        }
    }

    @Override
    public void create() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        batch = new SpriteBatch();
        font = new BitmapFont();
        //Werbung nur bei aktiven WIFI
        if(adsController.isWifiConnected()) {adsController.showBannerAd();}
        this.setScreen(new com.psychokitty.game.Screens.SplashScreen(this, adsController));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}