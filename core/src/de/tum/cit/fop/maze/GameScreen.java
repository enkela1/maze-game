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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

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

    private List<Enemy> enemies; // 用于存储敌人的列表
    private float detectionRange = 300f; // 检测范围

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

        // 初始化敌人列表
        enemies = new ArrayList<>();
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

    private void initializeEnemies() {
        // 遍历地图中的敌人对象
        for (int i = 1; i <= 5; i++) {
            String objectName = "object" + i;
            RectangleMapObject enemyObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get(objectName);
            if (enemyObject != null) {
                Rectangle rect = enemyObject.getRectangle();
                enemies.add(new Enemy(rect.x, rect.y, 100f,i));
            }
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

            // 更新敌人逻辑
            updateEnemies(delta);

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

            // 绘制敌人
            for (Enemy enemy : enemies) {switch (enemy.getType()) {
                case 1:  // 敌人类型 1
                    switch (currentDirection) {
                        case RIGHT:
                            game.getSpriteBatch().draw(game.getEnemy1RightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case LEFT:
                            game.getSpriteBatch().draw(game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case UP:
                            game.getSpriteBatch().draw(game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case DOWN:
                            game.getSpriteBatch().draw(game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_RIGHT:
                            game.getSpriteBatch().draw(game.getEnemy1IdleRightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_LEFT:
                            game.getSpriteBatch().draw(game.getEnemy1IdleLeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_UP:
                            game.getSpriteBatch().draw(game.getEnemy1IdleUpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_DOWN:
                            game.getSpriteBatch().draw(game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                    }
                    break;
                case 2:  // 敌人类型 2
                    switch (currentDirection) {
                        case RIGHT:
                            game.getSpriteBatch().draw(game.getEnemy2RightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case LEFT:
                            game.getSpriteBatch().draw(game.getEnemy2LeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case UP:
                            game.getSpriteBatch().draw(game.getEnemy2UpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case DOWN:
                            game.getSpriteBatch().draw(game.getEnemy2DownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_RIGHT:
                            game.getSpriteBatch().draw(game.getEnemy2IdleRightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_LEFT:
                            game.getSpriteBatch().draw(game.getEnemy2IdleLeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_UP:
                            game.getSpriteBatch().draw(game.getEnemy2IdleUpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                        case IDLE_DOWN:
                            game.getSpriteBatch().draw(game.getEnemy2IdleDownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
                            break;
                    }
                    break;
                // 对于敌人 3、4、5 也做类似的处理
                case 3:  // 敌人类型 3
                    // 处理敌人 3 的动画
                    break;
                case 4:  // 敌人类型 4
                    // 处理敌人 4 的动画
                    break;
                case 5:  // 敌人类型 5
                    // 处理敌人 5 的动画
                    break;
            }

            }
        }

        game.getSpriteBatch().end();
    }

    private void startGame() {
        isGameStarted = true;
        // 初始化角色位置
        initializeCharacterPosition();
        // 初始化敌人位置
        initializeEnemies();
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

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            float distance = Vector2.dst(characterX, characterY, enemy.getX(), enemy.getY());
            if (distance <= detectionRange) {
                // 朝角色移动
                Vector2 direction = new Vector2(characterX - enemy.getX(), characterY - enemy.getY()).nor();
                enemy.setX(enemy.getX() + direction.x * enemy.getSpeed() * delta);
                enemy.setY(enemy.getY() + direction.y * enemy.getSpeed() * delta);
            }
        }
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

    /**
     * 敌人类，用于存储敌人的位置和速度。
     */
    private static class Enemy {
        private float x;
        private float y;
        private final float speed;
        private final int type;

        public Enemy(float x, float y, float speed, int type) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.type = type;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getSpeed() {
            return speed;
        }

        public int getType() {
            return type;
        }
    }
}