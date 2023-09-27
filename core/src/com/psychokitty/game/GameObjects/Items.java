package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.psychokitty.game.Utils.Constants;

public class Items {

    private static final int ITEM_SIZE = 30;

    private Array<Rectangle> catfood;
    private Texture dropImage;
    private long lastDropTime;
    private Sprite drops;

    public void createItems() {
        dropImage = new Texture(Constants.burgerImage);
        catfood = new Array<>();
        drops = new Sprite(dropImage);
        drops.setSize(ITEM_SIZE, ITEM_SIZE);
    }

    public void renderItems(SpriteBatch batch) {
        for (Rectangle item : catfood) {
            drops.setRotation(item.y);
            drops.setOrigin(0, 0);
            drops.setX(item.x);
            drops.setY(item.y);
            drops.draw(batch);
        }
    }

    public void spawnItems() {
        Rectangle item = new Rectangle();
        item.x = MathUtils.random(0, Constants.NATIVE_WIDTH - ITEM_SIZE);
        item.y = Constants.NATIVE_HEIGHT;
        item.width = ITEM_SIZE;
        item.height = ITEM_SIZE;
        catfood.add(item);
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
