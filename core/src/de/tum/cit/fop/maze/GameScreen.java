package de.tum.cit.fop.maze;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;

    // Character position and movement state
    private float characterX;
    private float characterY;
    private float characterSpeed = 200f; // Base speed of character movement (pixels per second)
    private static final float SPEED_MULTIPLIER = 2.5f; // Speed multiplier when Shift is pressed

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapBounds;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT
    }

    private Direction currentDirection = Direction.IDLE_DOWN; // Default direction

    private boolean isGameStarted = false; // Flag to track if the game has started

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");



        // Load the tiled map
        TmxMapLoader loader = new TmxMapLoader();
        tiledMap = loader.load("input.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapBounds = new Rectangle(0, 0, layer.getWidth() * layer.getTileWidth(), layer.getHeight() * layer.getTileHeight());

    }

    private void initializeCharacterPosition() {
        RectangleMapObject characterObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {
            Rectangle rect = characterObject.getRectangle();
            characterX = rect.x;
            characterY = rect.y;
        } else {
            // Fallback if no character object is found
            characterX = camera.viewportWidth / 2;
            characterY = camera.viewportHeight / 2;
        }
    }

    @Override
    public void render(float delta) {
        // 检查是否按下 ESC 键返回菜单
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // 检查是否按下 ENTER 键开始游戏
        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        // 清屏
        ScreenUtils.clear(0, 0, 0, 1);

        // 如果游戏已开始，更新地图和角色
        if (isGameStarted) {
            // 更新角色移动
            updateCharacterMovement(delta);

            // 更新相机位置
            camera.position.set(characterX, characterY, 0);
            camera.update();

            // 渲染地图
            mapRenderer.setView(camera);
            mapRenderer.render();


        }

        // 更新 sinusInput 动画
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // 使用 SpriteBatch 绘制文字和角色动画
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();

        if (!isGameStarted) {
            // 游戏未开始时显示初始说明
            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);
            game.getSpriteBatch().draw(
                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                    textX - 96,
                    textY - 64,
                    64,
                    128
            );
        } else {
            // 游戏已开始时绘制角色
            switch (currentDirection) {
                case UP:
                    game.getSpriteBatch().draw(game.getCharacterUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case DOWN:
                    game.getSpriteBatch().draw(game.getCharacterDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case LEFT:
                    game.getSpriteBatch().draw(game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case RIGHT:
                    game.getSpriteBatch().draw(game.getCharacterRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case IDLE_UP:
                    game.getSpriteBatch().draw(game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case IDLE_DOWN:
                    game.getSpriteBatch().draw(game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case IDLE_LEFT:
                    game.getSpriteBatch().draw(game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
                case IDLE_RIGHT:
                    game.getSpriteBatch().draw(game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
                    break;
            }
        }

        game.getSpriteBatch().end();
    }

    private void startGame() {
        isGameStarted = true;
        // Reinitialize character position
        characterX = camera.viewportWidth / 2;
        characterY = camera.viewportHeight / 2;
        currentDirection = Direction.IDLE_DOWN;

        initializeCharacterPosition();
    }

    private void updateCharacterMovement(float delta) {
        // Handle input for WASD keys and Shift for speed boost
        float adjustedSpeed = characterSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            adjustedSpeed *= SPEED_MULTIPLIER;
        }

        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterY += adjustedSpeed * delta;
            currentDirection = Direction.UP;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterY -= adjustedSpeed * delta;
            currentDirection = Direction.DOWN;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterX -= adjustedSpeed * delta;
            currentDirection = Direction.LEFT;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterX += adjustedSpeed * delta;
            currentDirection = Direction.RIGHT;
            moved = true;
        }

        // If no key is pressed, set direction to idle based on last movement direction
        if (!moved) {
            switch (currentDirection) {
                case UP:
                    currentDirection = Direction.IDLE_UP;
                    break;
                case DOWN:
                    currentDirection = Direction.IDLE_DOWN;
                    break;
                case LEFT:
                    currentDirection = Direction.IDLE_LEFT;
                    break;
                case RIGHT:
                    currentDirection = Direction.IDLE_RIGHT;
                    break;
                default:
                    break;
            }
        }

        // Prevent character from going out of bounds
        characterX = Math.max(0, Math.min(characterX, camera.viewportWidth - 64));
        characterY = Math.max(0, Math.min(characterY, camera.viewportHeight - 128));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
    }
}