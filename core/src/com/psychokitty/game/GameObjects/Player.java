package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.psychokitty.game.Utils.Constants;

/**
 * Created by Steven on 10.11.2016.
 */

public class Player {
    Vector3 touchPos;
    private Sprite catSprite;
    private Texture catImage;
    private int direction = 0;
    private Rectangle cat;
    private float deltaTime;

    public void createPlayer() {
        catImage = new Texture(Constants.playerImage);
        catSprite = new Sprite(catImage);
        touchPos = new Vector3(Constants.NATIVE_WIDTH / 2 - com.psychokitty.game.Utils.Constants.catsize / 2, 32 ,0);

        cat = new Rectangle();
        cat.x = Constants.NATIVE_WIDTH / 2 - com.psychokitty.game.Utils.Constants.catsize / 2;
        cat.y = 22; //höhe über boden
        cat.width = com.psychokitty.game.Utils.Constants.catsize;
        cat.height = com.psychokitty.game.Utils.Constants.catsize;
    }

    public void renderPlayer(SpriteBatch batch, OrthographicCamera camera) {
        deltaTime = Gdx.graphics.getDeltaTime();
        batch.draw(catSprite, cat.x, cat.y, com.psychokitty.game.Utils.Constants.catsize, com.psychokitty.game.Utils.Constants.catsize);
        move(camera);
    }

    private void move(OrthographicCamera camera) {

        //setup user interaction
        if (Gdx.input.isTouched()) {
            camera.unproject(touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0));
            touchPos.x = touchPos.x - Constants.catsize/2;
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

        //Katze in lezte Richtung stehen lassen.
        if (Math.abs(touchPos.x - cat.x) < 5)
            cat.x = touchPos.x;
        //Katze am rand aufhalten
        if (cat.x > Constants.NATIVE_WIDTH - Constants.catsize)
            cat.x = Constants.NATIVE_WIDTH - Constants.catsize;
        if (cat.x < 0)
            cat.x = 0;
    }

    public Rectangle getRectangle() {
        return cat;
    }

    public void disposePlayer() {
        catImage.dispose();
    }
}