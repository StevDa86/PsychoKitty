package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.psychokitty.game.Utils.Constants;

/**
 * Created by Steven on 10.11.2016.
 */

public class Player {

    //cat animation
    final int FRAME_COLS = 2;         // #1
    final int FRAME_ROWS = 1;         // #2
    Animation<TextureRegion> CatAnimation;          // #3
    Texture catImage;              // #4
    TextureRegion[] CatFrames;             // #5
    TextureRegion CatFrame;           // #7
    float stateTime;

    Vector3 touchPos;
    private int direction = 0;
    private Rectangle cat;
    private float deltaTime;

    public void createPlayer() {
        catImage = new Texture(Constants.playerImage); //#9
        TextureRegion[][] tmp = TextureRegion.split(catImage, catImage.getWidth() / FRAME_COLS, catImage.getHeight() / FRAME_ROWS);//#10
        CatFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                CatFrames[index++] = tmp[i][j];
            }
        }
        CatAnimation = new Animation<TextureRegion>(0.300f, CatFrames);      // #11
        stateTime = 0f;                         // #13

        //catSprite = new Sprite(catImage);
        touchPos = new Vector3(Constants.NATIVE_WIDTH / 2 - com.psychokitty.game.Utils.Constants.catsize / 2, 32 ,0);

        cat = new Rectangle();
        cat.x = Constants.NATIVE_WIDTH / 2 - com.psychokitty.game.Utils.Constants.catsize / 2;
        cat.y = 22; //höhe über boden
        cat.width = com.psychokitty.game.Utils.Constants.catsize;
        cat.height = com.psychokitty.game.Utils.Constants.catsize;
    }

    public void renderPlayer(SpriteBatch batch, OrthographicCamera camera) {
        deltaTime = Gdx.graphics.getDeltaTime();

        //state Time für Katzenanimation
        stateTime += Gdx.graphics.getDeltaTime();

        CatFrame = CatAnimation.getKeyFrame(stateTime, true);  // #16
        batch.draw(CatFrame, cat.x, cat.y, com.psychokitty.game.Utils.Constants.catsize, com.psychokitty.game.Utils.Constants.catsize);

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
                for (TextureRegion textureRegion:CatAnimation.getKeyFrames()) {
                    if(textureRegion.isFlipX()) textureRegion.flip(true,false);
                }
            }
        }
        //move left
        else if (touchPos.x < cat.x) {
            cat.x -= com.psychokitty.game.Utils.Constants.catspeed * deltaTime;
            if (direction == 0) {
                direction = 1;
                //CatFrame.flip(true, false);
                for (TextureRegion textureRegion:CatAnimation.getKeyFrames()) {
                    if(!textureRegion.isFlipX()) textureRegion.flip(true,false);
                }
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