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


public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private final int coinCount;
    private Table mainMenuTable;
    private Table mapSelectionTable;
    private final Texture backgroundTexture;




    public MenuScreen(MazeRunnerGame game, int coinCount) {
        this.game = game;
        this.coinCount = coinCount;

        var camera = new OrthographicCamera();
        camera.zoom = 1f;
        Viewport viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, game.getSpriteBatch());
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));


        setupMainMenu();
        setupMapSelection();


        showMainMenu();
    }


    private void setupMainMenu() {
        mainMenuTable = new Table();
        mainMenuTable.setFillParent(true);
        mainMenuTable.add(new Label("Cosmic Survival:\nThe Aurora’s Crash", game.getSkin(), "title"))
                .padBottom(80)
                .row();


        TextButton goToGameButton = new TextButton(
                "Continue Mission", game.getSkin());
        mainMenuTable.add(goToGameButton).width(400).padBottom(20).row();

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame();
            }
        });

        TextButton selectMapButton = new TextButton("Navigate the Aurora", game.getSkin());
        mainMenuTable.add(selectMapButton).width(400).padBottom(20).row();
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMapSelection();
            }
        });

        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        mainMenuTable.add(quitButton).width(400).padBottom(20).row();
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }


    private void setupMapSelection() {
        mapSelectionTable = new Table();
        mapSelectionTable.setFillParent(true);


        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        mapSelectionTable.add(level1Button).width(300).padBottom(20).row();
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("input");
            }
        });


        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        mapSelectionTable.add(level2Button).width(300).padBottom(20).row();
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap("map_2");
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

        TextButton backButton = new TextButton("Back to Menu", game.getSkin());
        mapSelectionTable.add(backButton).width(300).padTop(40).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu();
            }
        });
    }

    private void showMainMenu() {
        stage.clear();
        stage.addActor(mainMenuTable);
    }


    private void showMapSelection() {
        stage.clear();
        stage.addActor(mapSelectionTable);
    }


    private void renderPauseMenu(Stage pauseStage, Texture backgroundTexture) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        pauseStage.getBatch().begin();
        pauseStage.getBatch().draw(
                backgroundTexture,
                0,
                0,
                pauseStage.getViewport().getWorldWidth(),
                pauseStage.getViewport().getWorldHeight()
        );
        pauseStage.getBatch().end();

        pauseStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        pauseStage.draw();
    }


    public Stage createPauseMenu() {
        Stage pauseStage = new Stage(new ScreenViewport());

        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        pauseTable.add(new Label("Game Paused", game.getSkin(), "title"))
                .padBottom(50)
                .row();

        pauseTable.add(new Label("Press 'P' to resume", game.getSkin()))
                .padBottom(20)
                .row();

        TextButton quitButton = new TextButton("Quit Game", game.getSkin());
        pauseTable.add(quitButton).width(200).padTop(20).row();

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return pauseStage;
    }


    public Stage createWinMenu(int coinCount,String currentMapName) {

        Stage winStage = new Stage(new ScreenViewport());


        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));


        Table winTable = new Table();
        winTable.setFillParent(true);
        winStage.addActor(winTable);


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

    public Stage createGameoverMenu(String currentMapName) {
        Stage loseStage = new Stage(new ScreenViewport());

        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        Table loseTable = new Table();
        loseTable.setFillParent(true);
        loseStage.addActor(loseTable);

        loseTable.add(new Label("GAME OVER!", game.getSkin(), "title"))
                .padBottom(50)
                .row();

        TextButton restartButton = new TextButton("Restart Game", game.getSkin());
        restartButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen=new GameScreen(game);
                game.gameScreen.loadMap(currentMapName);
                game.setScreen(game.gameScreen);
                dispose();

            }
        });
        loseTable.add(restartButton).width(300).height(50).padTop(20).row();

        return loseStage;
    }
    @Override
    public void render(float delta) {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(
                backgroundTexture,
                0,
                0,
                stage.getViewport().getWorldWidth(),
                stage.getViewport().getWorldHeight()
        );
        game.getSpriteBatch().end();


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


