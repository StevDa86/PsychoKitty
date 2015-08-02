package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PsychoKittyGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;

    private AdsController adsController;

    public PsychoKittyGame(AdsController adsController) {
        this.adsController = adsController;
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();
        font = new BitmapFont();
        adsController.showBannerAd();
        this.setScreen(new SplashScreen(this, adsController));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}