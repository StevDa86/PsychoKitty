package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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


    public void createItems(){
        dropImage = new Texture(Constants.catnipImage);
        catfood = new Array<Rectangle>();
    }

    public void renderItems(SpriteBatch batch){
        for (Rectangle Items : catfood) {
            batch.draw(dropImage, Items.x, Items.y, 80, 80);
        }
    }

    public void spawnItems() {
        Rectangle Items = new Rectangle();
        Items.x = MathUtils.random(0, Gdx.graphics.getWidth() - 64);
        Items.y = Gdx.graphics.getHeight();
        Items.width = 64;
        Items.height = 64;
        catfood.add(Items);
        lastDropTime = TimeUtils.nanoTime();
    }

    public long getLastDropTime(){return lastDropTime;}

    public Array<Rectangle> getArray() {
        return catfood;
    }

    public void disposeItems(){
        dropImage.dispose();

    }
}
