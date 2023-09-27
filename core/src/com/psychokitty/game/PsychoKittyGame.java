package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.psychokitty.game.AdMob.AdsController;
import com.psychokitty.game.AdMob.DummyAdsController;
import com.psychokitty.game.Screens.SplashScreen;

public class PsychoKittyGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;
    private AdsController adsController;

    public PsychoKittyGame(AdsController adsController) {
        this.adsController = (adsController != null) ? adsController : new DummyAdsController();
    }

    @Override
    public void create() {
        setupInputHandling();
        initializeGraphics();

        if (adsController.isWifiConnected()) {
            adsController.showBannerAd();
        }

        setScreen(new SplashScreen(this, adsController));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    private void setupInputHandling() {
        // Catch the BACK key input
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    private void initializeGraphics() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }
}
