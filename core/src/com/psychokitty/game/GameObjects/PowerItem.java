package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.psychokitty.game.Utils.Constants;

public class PowerItem {

    public Array<Rectangle> poweritem;
    private Texture powerImage;
    private long lastDropTime;
    private Sprite powerItems;
    private int itemSize = 32;

    public void createItems() {
        powerImage = new Texture(Constants.powerItemImage);
        poweritem = new Array<Rectangle>();
        powerItems = new Sprite(powerImage);
        powerItems.setSize(itemSize,itemSize);
    }

    public void renderItems(SpriteBatch batch) {
        for (Rectangle Items : poweritem) {
            powerItems.setRotation(Items.y);
            powerItems.setX(Items.x);
            powerItems.setY(Items.y);
            powerItems.draw(batch);
        }
    }

    public void spawnItems() {
        Rectangle Items3 = new Rectangle();
        Items3.x = MathUtils.random(0, Constants.NATIVE_WIDTH - itemSize);
        Items3.y = Constants.NATIVE_HEIGHT;
        Items3.width = itemSize;
        Items3.height = itemSize;
        poweritem.add(Items3);
        lastDropTime = TimeUtils.nanoTime();
    }

    public long getLastDropTime() {
        return lastDropTime;
    }

    public Array<Rectangle> getArray() {
        return poweritem;
    }

    public void disposeItems() {
        powerImage.dispose();
    }
}