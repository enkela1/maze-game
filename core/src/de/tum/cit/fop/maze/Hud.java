package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Hud {

    private final Stage stage;
    private final Image arrowImage;

    public Hud() {
        stage = new Stage(new ScreenViewport());
        Texture arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrowImage = new Image(new TextureRegionDrawable(arrowTexture));

        // Resize the arrow if it's huge (example: 32×32)
        arrowImage.setSize(70, 70);

        // Rotate around center
        arrowImage.setOrigin(arrowImage.getWidth() / 2f, arrowImage.getHeight() / 2f);

        // Make it half-transparent
        arrowImage.getColor().a = 0.5f;

        // Initially, place it off-screen (we'll update the position each frame)
        arrowImage.setPosition(-999, -999);

        stage.addActor(arrowImage);
    }

    /**
     * @param delta    Time delta
     * @param playerX  Character's world X
     * @param playerY  Character's world Y
     * @param portalX  Portal's world X
     * @param portalY  Portal's world Y
     * @param camera   The game world camera (OrthographicCamera)
     */
    public void update(float delta,
                       float playerX, float playerY,
                       float portalX, float portalY,
                       OrthographicCamera camera) {

        // 1) Project player and portal positions from world => screen
        Vector3 playerOnScreen = camera.project(new Vector3(playerX, playerY, 0));
        Vector3 portalOnScreen = camera.project(new Vector3(portalX, portalY, 0));

        // 2) Compute direction on screen
        Vector2 dir = new Vector2(
                portalOnScreen.x - playerOnScreen.x,
                portalOnScreen.y - playerOnScreen.y
        );

        // 3) Rotation in degrees.
        //    If arrow.png is drawn pointing RIGHT by default, do arrowImage.setRotation(angle).
        //    If it's drawn pointing UP, do arrowImage.setRotation(angle - 90f).
        float angle = dir.angleDeg();
        arrowImage.setRotation(angle);

        // 4) Place the arrow slightly in front of the character on screen
        //    40 pixels in direction of 'angle'


        float arrowX = playerOnScreen.x +  40 +  arrowImage.getWidth() / 2f;
        float arrowY = playerOnScreen.y +   arrowImage.getHeight() / 2f;
        arrowImage.setPosition(arrowX, arrowY);

        // 5) Update stage
        stage.act(delta);
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    // No corner pinning logic in resize(), because we want it to track the character, not a corner
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }
}

