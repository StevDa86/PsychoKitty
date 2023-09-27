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

    private static final int ITEM_SIZE = 32;

    private Array<Rectangle> poweritem;
    private Texture powerImage;
    private long lastDropTime;
    private Sprite powerItems;

    public void createItems() {
        powerImage = new Texture(Constants.powerItemImage);
        poweritem = new Array<>();
        powerItems = new Sprite(powerImage);
        powerItems.setSize(ITEM_SIZE, ITEM_SIZE);
    }

    public void renderItems(SpriteBatch batch) {
        for (Rectangle item : poweritem) {
            powerItems.setRotation(item.y);
            powerItems.setX(item.x);
            powerItems.setY(item.y);
            powerItems.draw(batch);
        }
    }

    public void spawnItems() {
        Rectangle item = new Rectangle();
        item.x = MathUtils.random(0, Constants.NATIVE_WIDTH - ITEM_SIZE);
        item.y = Constants.NATIVE_HEIGHT;
        item.width = ITEM_SIZE;
        item.height = ITEM_SIZE;
        poweritem.add(item);
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
