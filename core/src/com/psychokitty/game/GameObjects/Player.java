package com.psychokitty.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.psychokitty.game.Utils.Constants;

public class Player {

    private static final int FRAME_COLS = 2;
    private static final int FRAME_ROWS = 1;
    private static final float ANIMATION_SPEED = 0.300f;

    private Animation<TextureRegion> catAnimation;
    private Texture catImage;
    private TextureRegion[] catFrames;
    private TextureRegion catFrame;
    private float stateTime;

    private Vector3 touchPos;
    private Rectangle cat;
    private float deltaTime;

    private int direction;

    public void createPlayer() {
        catImage = new Texture(Constants.playerImage);
        TextureRegion[][] tmp = TextureRegion.split(catImage, catImage.getWidth() / FRAME_COLS, catImage.getHeight() / FRAME_ROWS);
        catFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                catFrames[index++] = tmp[i][j];
            }
        }
        catAnimation = new Animation<TextureRegion>(ANIMATION_SPEED, catFrames);
        stateTime = 0f;

        touchPos = new Vector3(Constants.NATIVE_WIDTH / 2 - Constants.catsize / 2, 32, 0);

        cat = new Rectangle();
        cat.x = Constants.NATIVE_WIDTH / 2 - Constants.catsize / 2;
        cat.y = 22;
        cat.width = Constants.catsize;
        cat.height = Constants.catsize;
    }

    public void renderPlayer(SpriteBatch batch, OrthographicCamera camera) {
        deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += Gdx.graphics.getDeltaTime();
        catFrame = catAnimation.getKeyFrame(stateTime, true);
        batch.draw(catFrame, cat.x, cat.y, Constants.catsize, Constants.catsize);
        move(camera);
    }

    private void move(OrthographicCamera camera) {
        if (Gdx.input.isTouched()) {
            camera.unproject(touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            touchPos.x = touchPos.x - Constants.catsize / 2;
        }

        if (touchPos.x > cat.x) {
            cat.x += Constants.catspeed * deltaTime;
            if (direction == 1) {
                direction = 0;
                flipCatFrames(false);
            }
        } else if (touchPos.x < cat.x) {
            cat.x -= Constants.catspeed * deltaTime;
            if (direction == 0) {
                direction = 1;
                flipCatFrames(true);
            }
        }

        if (Math.abs(touchPos.x - cat.x) < 5)
            cat.x = touchPos.x;

        if (cat.x > Constants.NATIVE_WIDTH - Constants.catsize)
            cat.x = Constants.NATIVE_WIDTH - Constants.catsize;

        if (cat.x < 0)
            cat.x = 0;
    }

    private void flipCatFrames(boolean flipX) {
        for (TextureRegion textureRegion : catAnimation.getKeyFrames()) {
            if (textureRegion.isFlipX() != flipX) {
                textureRegion.flip(true, false);
            }
        }
    }

    public Rectangle getRectangle() {
        return cat;
    }

    public void disposePlayer() {
        catImage.dispose();
    }
}
