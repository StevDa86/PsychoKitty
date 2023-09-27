package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.psychokitty.game.Utils.Constants;

public class Enemies {
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 1;
    private static final int ITEM_SIZE = 50;
    private static final float ANIMATION_SPEED = 0.300f;

    private Array<Rectangle> dog;
    private Animation<TextureRegion> dogwalkAnimation;
    private TextureRegion[] dogwalkFrames;
    private float stateTime;
    private long lastDogDropTime;

    public void createEnemies() {
        // Dog Animation
        Texture dogwalkSheet = new Texture(Constants.dogAnimationImage);
        TextureRegion[][] tmp = TextureRegion.split(dogwalkSheet, dogwalkSheet.getWidth() / FRAME_COLS, dogwalkSheet.getHeight() / FRAME_ROWS);
        dogwalkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                dogwalkFrames[index++] = tmp[i][j];
            }
        }
        dogwalkAnimation = new Animation<>(ANIMATION_SPEED, dogwalkFrames);
        stateTime = 0f;

        dog = new Array<>();
    }

    public void renderEnemies(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion dogCurrentFrame = dogwalkAnimation.getKeyFrame(stateTime, true);

        for (Rectangle enemy : dog) {
            batch.draw(dogCurrentFrame, enemy.x, enemy.y, ITEM_SIZE, ITEM_SIZE);
        }
    }

    public long getLastDropTime() {
        return lastDogDropTime;
    }

    public Array<Rectangle> getArray() {
        return dog;
    }

    public void spawnDog() {
        Rectangle enemy = new Rectangle();
        enemy.x = MathUtils.random(0, Constants.NATIVE_WIDTH - ITEM_SIZE);
        enemy.y = Constants.NATIVE_HEIGHT;
        enemy.width = ITEM_SIZE;
        enemy.height = ITEM_SIZE;
        dog.add(enemy);
        lastDogDropTime = TimeUtils.nanoTime();
    }

    public void disposeEnemies() {
        dogwalkAnimation = null; // Setting animation to null can help with freeing resources.
    }
}
