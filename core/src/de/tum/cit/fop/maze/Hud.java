package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
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

        arrowImage.setSize(70, 70);

        arrowImage.setOrigin(arrowImage.getWidth() / 2f, arrowImage.getHeight() / 2f);

        arrowImage.getColor().a = 0.5f;

        arrowImage.setPosition(-999, -999);

        stage.addActor(arrowImage);
    }


    public void update(float delta,
                       float playerX, float playerY,
                       float portalX, float portalY,
                       OrthographicCamera camera) {


        Vector3 playerOnScreen = camera.project(new Vector3(playerX, playerY, 0));
        Vector3 portalOnScreen = camera.project(new Vector3(portalX, portalY, 0));


        Vector2 dir = new Vector2(
                portalOnScreen.x - playerOnScreen.x,
                portalOnScreen.y - playerOnScreen.y
        );


        float angle = dir.angleDeg();
        arrowImage.setRotation(angle);


        float arrowX = playerOnScreen.x +  40 +  arrowImage.getWidth() / 2f;
        float arrowY = playerOnScreen.y +   arrowImage.getHeight() / 2f;
        arrowImage.setPosition(arrowX, arrowY);


        stage.act(delta);
    }


    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }
}

