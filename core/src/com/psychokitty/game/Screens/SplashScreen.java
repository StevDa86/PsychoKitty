package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.psychokitty.game.PsychoKittyGame;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.AdMob.AdsController;

public class SplashScreen implements Screen {

    private final PsychoKittyGame game;
    private final AdsController adcont;
    private Stage stage;
    private Music splashSound;
    private Texture splashTexture;

    public SplashScreen(final PsychoKittyGame game, AdsController adsController) {
        this.game = game;
        this.adcont = adsController;
    }

    @Override
    public void show() {
        // Initialize resources when screen is shown
        stage = new Stage(new FitViewport(Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT));
        splashSound = Gdx.audio.newMusic(Gdx.files.internal(Constants.soundSplash));
        splashTexture = new Texture(Gdx.files.internal(Constants.splashImage));

        splashSound.setVolume(1f);
        splashSound.play();

        Image splashImage = new Image(splashTexture);

        // Center the image properly using the viewport
        float imageX = (Constants.NATIVE_WIDTH - splashImage.getWidth()) / 2f;
        float imageY = (Constants.NATIVE_HEIGHT - splashImage.getHeight()) / 2f;

        splashImage.setPosition(imageX, imageY);
        stage.addActor(splashImage);

        // Add fade in/out sequence with proper cleanup
        splashImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(2.0f),
                        Actions.delay(3f),
                        Actions.run(() -> {
                            // Stop sound before transitioning
                            if (splashSound != null && splashSound.isPlaying()) {
                                splashSound.stop();
                            }

                            // Transition to menu screen
                            Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game, adcont));

                            // Dispose of current screen after transition
                            if (currentScreen != null && currentScreen != this) {
                                currentScreen.dispose();
                            }
                        })
                )
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void hide() {
        // Don't dispose here - let LibGDX handle the lifecycle
        if (splashSound != null && splashSound.isPlaying()) {
            splashSound.stop();
        }
    }

    @Override
    public void pause() {
        if (splashSound != null && splashSound.isPlaying()) {
            splashSound.pause();
        }
    }

    @Override
    public void resume() {
        if (splashSound != null && !splashSound.isPlaying()) {
            splashSound.play();
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if (splashSound != null) {
            splashSound.dispose();
            splashSound = null;
        }
        if (splashTexture != null) {
            splashTexture.dispose();
            splashTexture = null;
        }
    }
}