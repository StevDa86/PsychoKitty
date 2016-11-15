package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.psychokitty.game.Utils.Constants;

/**
 * Created by Steven on 10.11.2016.
 */

public class Player {

    Vector2 touchPos;
    private Sprite catSprite;
     private Texture catImage;
    private int direction = 0;
    private Rectangle cat;
    private float deltaTime;

    public void createPlayer(){
        catImage = new Texture(Constants.playerImage);
        catSprite = new Sprite(catImage);
        touchPos = new Vector2(Gdx.graphics.getWidth() / 2 - com.psychokitty.game.Utils.Constants.catsize / 2, 0);
        cat = new Rectangle();
        cat.x = Gdx.graphics.getWidth() / 2 - com.psychokitty.game.Utils.Constants.catsize / 2;
        cat.y = 20;
        cat.width = com.psychokitty.game.Utils.Constants.catsize;
        cat.height = com.psychokitty.game.Utils.Constants.catsize;
    }

    public void renderPlayer(SpriteBatch batch){
        deltaTime = Gdx.graphics.getDeltaTime();
        batch.draw(catSprite, catSprite.getX(), catSprite.getY(), com.psychokitty.game.Utils.Constants.catsize, com.psychokitty.game.Utils.Constants.catsize);
        move();
    }

    private void move( ){

        catSprite.setPosition(cat.x,cat.y);

        //setup user interaction
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX() - 32, Gdx.input.getY());
        }
        //Move right
        if (touchPos.x > cat.x) {
            cat.x += com.psychokitty.game.Utils.Constants.catspeed * deltaTime;
            if (direction == 1) {
                direction = 0;
                catSprite.flip(true, false);
            }
        }
        //move left
        else if (touchPos.x < cat.x) {
            cat.x -= com.psychokitty.game.Utils.Constants.catspeed * deltaTime;
            if (direction == 0) {
                direction = 1;
                catSprite.flip(true, false);
            }
        }

        if (Math.abs(touchPos.x - cat.x) < 5)
            cat.x = touchPos.x;
        //Katze am rand aufhalten
        if (cat.x > Gdx.graphics.getWidth() - 100)
            cat.x = Gdx.graphics.getWidth() - 100;
        if (cat.x < 0)
            cat.x = 0;

    }

    public void disposePlayer(){
        catImage.dispose();
    }

}
