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

/**
 * Created by Steven on 10.11.2016.
 */

public class Enemies {

    //Dog Animation
    final int FRAME_COLS = 2;         // #1
    final int FRAME_ROWS = 1;         // #2
    public Array<Rectangle> dog;
    Animation<TextureRegion> DogwalkAnimation;          // #3
    Texture DogwalkSheet;              // #4
    TextureRegion[] DogwalkFrames;             // #5
    TextureRegion DogcurrentFrame;           // #7
    float stateTime;                                        // #8
    private long lastDogDropTime;

    public void CreateEnemies() {
        //Dog Animation
        DogwalkSheet = new Texture(Constants.dogAnimationImage); // #9
        TextureRegion[][] tmp = TextureRegion.split(DogwalkSheet, DogwalkSheet.getWidth() / FRAME_COLS, DogwalkSheet.getHeight() / FRAME_ROWS);              // #10
        DogwalkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                DogwalkFrames[index++] = tmp[i][j];
            }
        }
        DogwalkAnimation = new Animation<TextureRegion>(0.225f, DogwalkFrames);      // #11
        stateTime = 0f;                         // #13
        //

        dog = new Array<Rectangle>();
    }

    public void RenderEnemies(SpriteBatch batch) {
        //Dog Animation
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        DogcurrentFrame = DogwalkAnimation.getKeyFrame(stateTime, true);  // #16

        for (Rectangle Items2 : dog) {
            batch.draw(DogcurrentFrame, Items2.x, Items2.y, 100, 100);
        }
    }

    public long getLastDropTime() {
        return lastDogDropTime;
    }

    public Array<Rectangle> getArray() {
        return dog;
    }

    public void spawnDog() {
        Rectangle Items2 = new Rectangle();
        Items2.x = MathUtils.random(0, Constants.NATIVE_WIDTH - 100);
        Items2.y = Constants.NATIVE_HEIGHT;
        Items2.width = 100;
        Items2.height = 100;
        dog.add(Items2);
        lastDogDropTime = TimeUtils.nanoTime();
    }

    public void DisposeEnemies() {

    }
}
