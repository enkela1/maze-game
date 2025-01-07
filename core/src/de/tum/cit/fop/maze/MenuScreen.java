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
    private final MazeRunnerGame game;
    private Table mainMenuTable;
    private Table mapSelectionTable;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;

        // Set up the camera and viewport
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        // Initialize UI components
        setupMainMenu(); // Set up the main menu layout
        setupMapSelection(); // Set up the map selection layout

        // Show the main menu by default
        showMainMenu();
    }

    /**
     * Sets up the main menu layout with buttons and title.
     */
    private void setupMainMenu() {
        mainMenuTable = new Table();
        mainMenuTable.setFillParent(true); // Make the table fill the stage

        // Add title to the main menu
        mainMenuTable.add(new Label("Hello World from the Menu!", game.getSkin(), "title"))
                .padBottom(80)
                .row();

        // Add "Continue Game" button
        TextButton goToGameButton = new TextButton("Continue Game", game.getSkin());
        mainMenuTable.add(goToGameButton).width(300).padBottom(20).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Navigate to the game screen
            }
        });

        // Add "Select New Map" button
        TextButton selectMapButton = new TextButton("Select New Map", game.getSkin());
        mainMenuTable.add(selectMapButton).width(300).padBottom(20).row();
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMapSelection(); // Show the map selection screen
            }
        });

        // Add "Quit Game" button
        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        mainMenuTable.add(quitButton).width(300).padBottom(20).row();
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the application
            }
        });
    }

    /**
     * Sets up the map selection layout with level buttons and a back button.
     */
    private void setupMapSelection() {
        mapSelectionTable = new Table();
        mapSelectionTable.setFillParent(true); // Make the table fill the stage

        // Add "Level 1" button
        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        mapSelectionTable.add(level1Button).width(300).padBottom(20).row();
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level1"); // Load level 1
            }
        });

        // Add "Level 2" button
        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        mapSelectionTable.add(level2Button).width(300).padBottom(20).row();
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("level2"); // Load level 2
            }
        });

        // Add "Back to Main Menu" button
        TextButton backButton = new TextButton("Back to Menu", game.getSkin());
        mapSelectionTable.add(backButton).width(300).padTop(40).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(); // Return to the main menu
            }
        });
    }

    /**
     * Displays the main menu by adding its table to the stage.
     */
    private void showMainMenu() {
        stage.clear(); // Clear the stage of any previous actors
        stage.addActor(mainMenuTable); // Add the main menu table to the stage
    }

    /**
     * Displays the map selection menu by adding its table to the stage.
     */
    private void showMapSelection() {
        stage.clear(); // Clear the stage of any previous actors
        stage.addActor(mapSelectionTable); // Add the map selection table to the stage
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage logic
        stage.draw(); // Render the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
