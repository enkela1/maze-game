package de.tum.cit.fop.maze;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private final int coinCount;
    private Table mainMenuTable;
    private Table mapSelectionTable;
    private final Texture backgroundTexture;



    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game, int coinCount) {
        this.game = game;
        this.coinCount = coinCount;


        // Set up the camera and viewport
        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, game.getSpriteBatch());
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));

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
        mainMenuTable.add(new Label("Cosmic Survival:\nThe Aurora’s Crash", game.getSkin(), "title"))
                .padBottom(80)
                .row();


        // Add "Continue Game" button
        TextButton goToGameButton = new TextButton(
                "Continue Mission", game.getSkin());
        mainMenuTable.add(goToGameButton).width(400).padBottom(20).row();

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Navigate to the game screen
            }
        });

        // Add "Select New Map" button
        TextButton selectMapButton = new TextButton("Navigate the Aurora", game.getSkin());
        mainMenuTable.add(selectMapButton).width(400).padBottom(20).row();
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMapSelection(); // Show the map selection screen
            }
        });

        // Add "Quit Game" button
        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        mainMenuTable.add(quitButton).width(400).padBottom(20).row();
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
        // 创建 Level 1 按钮
        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        mapSelectionTable.add(level1Button).width(300).padBottom(20).row();
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("input"); // 加载 maps 下的 input 文件
            }
        });

// 创建 Level 2 按钮
        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        mapSelectionTable.add(level2Button).width(300).padBottom(20).row();
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("map_2"); // 加载 maps 下的 map_2 文件
            }
        });

        TextButton level3Button = new TextButton("Level 3", game.getSkin());
        mapSelectionTable.add(level3Button).width(300).padBottom(20).row();
        level3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("map_3");
            }
        });
        TextButton level4Button = new TextButton("Level 4", game.getSkin());
        mapSelectionTable.add(level4Button).width(300).padBottom(20).row();
        level4Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("map_4");
            }
        });
        TextButton level5Button = new TextButton("Level 5", game.getSkin());
        mapSelectionTable.add(level5Button).width(300).padBottom(20).row();
        level5Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("map_5");
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


    private void renderPauseMenu(Stage pauseStage, Texture backgroundTexture) {
        // Clear the screen if needed
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background texture
        pauseStage.getBatch().begin();
        pauseStage.getBatch().draw(
                backgroundTexture,
                0,
                0,
                pauseStage.getViewport().getWorldWidth(),
                pauseStage.getViewport().getWorldHeight()
        );
        pauseStage.getBatch().end();

        // Then draw any UI elements in the stage
        pauseStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        pauseStage.draw();
    }


    public Stage createPauseMenu() {
        // Create a stage for the pause menu
        Stage pauseStage = new Stage(new ScreenViewport());

        // Load background texture
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        // Create a table for layoutJ
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        // Add "Game Paused" label
        pauseTable.add(new Label("Game Paused", game.getSkin(), "title"))
                .padBottom(50)
                .row();

        // Add "Press 'P' to resume" label
        pauseTable.add(new Label("Press 'P' to resume", game.getSkin()))
                .padBottom(20)
                .row();

        // Add "Quit Game" button
        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        pauseTable.add(quitButton).width(200).padTop(20).row();

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the game when Quit is clicked
            }
        });

        return pauseStage; // Return the created pause menu stage
    }


    public Stage createWinMenu(int coinCount) {
        // Create a stage for the pause menu
        Stage winStage = new Stage(new ScreenViewport());

        // Load background texture
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        // Create a table for layoutJ
        Table winTable = new Table();
        winTable.setFillParent(true);
        winStage.addActor(winTable);

        // Add "Game Paused" label
        winTable.add(new Label("YOU WIN!", game.getSkin(), "title"))
                .padBottom(50)
                .row();

        winTable.add(new Label("Coins collected: " + coinCount, game.getSkin()))
                .padBottom(20)
                .row();
        winStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game, coinCount));
                    return true;
                }
                return false;
            }
        });


        Gdx.input.setInputProcessor(winStage);
        return winStage;
    }

    public Stage createGameoverMenu() {
        // Create a stage for the pause menu
        Stage loseStage = new Stage(new ScreenViewport());

        // Load background texture
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        Table loseTable = new Table();
        loseTable.setFillParent(true);
        loseStage.addActor(loseTable);


        loseTable.add(new Label("GAME OVER!", game.getSkin(), "title"))
                .padBottom(50)
                .row();

        TextButton restartButton = new TextButton("Back to Menu", game.getSkin());
        restartButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.goToMenu();
                dispose();

            }
        });
        loseTable.add(restartButton).width(300).height(50).padTop(20).row();

        return loseStage;
    }
    @Override
    public void render(float delta) {


        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background texture
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(
                backgroundTexture,
                0,
                0,
                stage.getViewport().getWorldWidth(), // Match viewport width
                stage.getViewport().getWorldHeight() // Match viewport height
        );
        game.getSpriteBatch().end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }




    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

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


