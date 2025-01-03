package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Hello World from the Menu!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Continue Game", game.getSkin());
        table.add(goToGameButton).width(300).padBottom(20).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });

        TextButton selectMapButton = new TextButton("Select New Map", game.getSkin());
        table.add(selectMapButton).width(300).padBottom(20).row();
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level1");
            }
        });
        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        table.add(quitButton).width(300).padBottom(20).row();
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the application
            }
        });
    }
    public void selectMap(MazeRunnerGame game) {
        // Create a new table to display level selection buttons
        Table mapSelectionTable = new Table();
        mapSelectionTable.setFillParent(true); // Make the table fill the screen
        stage.addActor(mapSelectionTable); // Add the table to the stage

        // Create buttons for level selection
        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        TextButton level3Button = new TextButton("Level 3", game.getSkin());

        // Add the buttons to the table
        mapSelectionTable.add(level1Button).width(300).padBottom(20).row();
        mapSelectionTable.add(level2Button).width(300).padBottom(20).row();
        mapSelectionTable.add(level3Button).width(300).padBottom(20).row();

        // Add listeners for each button to load the corresponding map
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level1"); // Load level 1
            }
        });
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level2"); // Load level 2
            }
        });
        level3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level3"); // Load level 3
            }
        });
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
