package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.psychokitty.game.Utils.Constants;

/**
 * Created by Steven on 15.11.2016.
 */

public class Items {

    public Array<Rectangle> catfood;
    private Texture dropImage;
    private long lastDropTime;
    private Sprite Drops;
    private int itemSize = 30;

    public void createItems() {
        dropImage = new Texture(Constants.catnipImage);
        catfood = new Array<Rectangle>();
        Drops = new Sprite(dropImage);
        Drops.setSize(itemSize,itemSize);
    }

    public void renderItems(SpriteBatch batch) {
        for (Rectangle Items : catfood) {
            Drops.setRotation(Items.y);
            Drops.setX(Items.x);
            Drops.setY(Items.y);
            Drops.draw(batch);
        }
    }

    public void spawnItems() {
        Rectangle Items = new Rectangle();
        Items.x = MathUtils.random(0, Constants.NATIVE_WIDTH - itemSize);
        Items.y = Constants.NATIVE_HEIGHT;
        Items.width = itemSize;
        Items.height = itemSize;
        catfood.add(Items);
        lastDropTime = TimeUtils.nanoTime();
    }

    public long getLastDropTime() {
        return lastDropTime;
    }

    public Array<Rectangle> getArray() {
        return catfood;
    }

    public void disposeItems() {
        dropImage.dispose();

    }
}
