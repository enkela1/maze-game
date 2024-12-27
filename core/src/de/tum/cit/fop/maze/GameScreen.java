////package de.tum.cit.fop.maze;
////
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.Input;
////import com.badlogic.gdx.Screen;
////import com.badlogic.gdx.graphics.OrthographicCamera;
////import com.badlogic.gdx.graphics.g2d.BitmapFont;
////import com.badlogic.gdx.maps.objects.RectangleMapObject;
////import com.badlogic.gdx.maps.tiled.TiledMap;
////import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
////import com.badlogic.gdx.maps.tiled.TmxMapLoader;
////import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
////import com.badlogic.gdx.math.Rectangle;
////import com.badlogic.gdx.math.Vector2;
////import com.badlogic.gdx.utils.ScreenUtils;
////import java.util.ArrayList;
////import java.util.List;
////
/////**
//// * The GameScreen class is responsible for rendering the gameplay screen.
//// * It handles the game logic and rendering of the game elements.
//// */
////public class GameScreen implements Screen {
////
////    private final MazeRunnerGame game;
////    private final OrthographicCamera camera;
////    private final BitmapFont font;
////
////    private float sinusInput = 0f;
////
////    // Character position and movement state
////    private float characterX;
////    private float characterY;
////    private float characterSpeed = 200f; // Base speed of character movement (pixels per second)
////    private static final float SPEED_MULTIPLIER = 2.5f; // Speed multiplier when Shift is pressed
////
////    private List<Enemy> enemies; // 用于存储敌人的列表
////    private float detectionRange = 300f; // 检测范围
////
////    private TiledMap tiledMap;
////    private OrthogonalTiledMapRenderer mapRenderer;
////    private Rectangle mapBounds;
////
////    private enum Direction {
////        UP, DOWN, LEFT, RIGHT, IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT
////    }
////
////    private Direction currentDirection = Direction.IDLE_DOWN; // Default direction
////
////    private boolean isGameStarted = false; // Flag to track if the game has started
////
////    /**
////     * Constructor for GameScreen. Sets up the camera and font.
////     *
////     * @param game The main game class, used to access global resources and methods.
////     */
////    public GameScreen(MazeRunnerGame game) {
////        this.game = game;
////
////        // Create and configure the camera for the game view
////        camera = new OrthographicCamera();
////        camera.setToOrtho(false);
////        camera.zoom = 0.75f;
////
////        // Get the font from the game's skin
////        font = game.getSkin().getFont("font");
////
////        // Load the tiled map
////        TmxMapLoader loader = new TmxMapLoader();
////        tiledMap = loader.load("input.tmx");
////        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
////
////        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
////        mapBounds = new Rectangle(0, 0, layer.getWidth() * layer.getTileWidth(), layer.getHeight() * layer.getTileHeight());
////
////        // 初始化敌人列表
////        enemies = new ArrayList<>();
////    }
////
////    private void initializeCharacterPosition() {
////        RectangleMapObject characterObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
////        if (characterObject != null) {
////            Rectangle rect = characterObject.getRectangle();
////            characterX = rect.x;
////            characterY = rect.y;
////        } else {
////            // Fallback if no character object is found
////            characterX = camera.viewportWidth / 2;
////            characterY = camera.viewportHeight / 2;
////        }
////    }
////
////    private void initializeEnemies() {
////        // 遍历地图中的敌人对象
////        for (int i = 1; i <= 5; i++) {
////            String objectName = "object" + i;
////            RectangleMapObject enemyObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get(objectName);
////            if (enemyObject != null) {
////                Rectangle rect = enemyObject.getRectangle();
////                enemies.add(new Enemy(rect.x, rect.y, 100f,i));
////            }
////        }
////    }
////
////    @Override
////    public void render(float delta) {
////        // 检查是否按下 ESC 键返回菜单
////        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
////            game.goToMenu();
////        }
////
////        // 检查是否按下 ENTER 键开始游戏
////        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
////            startGame();
////        }
////
////        // 清屏
////        ScreenUtils.clear(0, 0, 0, 1);
////
////        // 如果游戏已开始，更新地图和角色
////        if (isGameStarted) {
////            // 更新角色移动
////            updateCharacterMovement(delta);
////
////            // 更新敌人逻辑
////            updateEnemies(delta);
////
////            // 更新相机位置
////            camera.position.set(characterX, characterY, 0);
////            camera.update();
////
////            // 渲染地图
////            mapRenderer.setView(camera);
////            mapRenderer.render();
////        }
////
////        // 更新 sinusInput 动画
////        sinusInput += delta;
////        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
////        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);
////
////        // 使用 SpriteBatch 绘制文字和角色动画
////        game.getSpriteBatch().setProjectionMatrix(camera.combined);
////        game.getSpriteBatch().begin();
////
////        if (!isGameStarted) {
////            // 游戏未开始时显示初始说明
////            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
////            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);
////            game.getSpriteBatch().draw(
////                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
////                    textX - 96,
////                    textY - 64,
////                    64,
////                    128
////            );
////        } else {
////            // 游戏已开始时绘制角色
////            switch (currentDirection) {
////                case UP:
////                    game.getSpriteBatch().draw(game.getCharacterUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case DOWN:
////                    game.getSpriteBatch().draw(game.getCharacterDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case LEFT:
////                    game.getSpriteBatch().draw(game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case RIGHT:
////                    game.getSpriteBatch().draw(game.getCharacterRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case IDLE_UP:
////                    game.getSpriteBatch().draw(game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case IDLE_DOWN:
////                    game.getSpriteBatch().draw(game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case IDLE_LEFT:
////                    game.getSpriteBatch().draw(game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////                case IDLE_RIGHT:
////                    game.getSpriteBatch().draw(game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
////                    break;
////            }
////
////            // 绘制敌人
////            for (Enemy enemy : enemies) {switch (enemy.getType()) {
////                case 1:  // 敌人类型 1
////                    switch (currentDirection) {
////                        case RIGHT:
////                            game.getSpriteBatch().draw(game.getEnemy1RightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case LEFT:
////                            game.getSpriteBatch().draw(game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case UP:
////                            game.getSpriteBatch().draw(game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case DOWN:
////                            game.getSpriteBatch().draw(game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_RIGHT:
////                            game.getSpriteBatch().draw(game.getEnemy1IdleRightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_LEFT:
////                            game.getSpriteBatch().draw(game.getEnemy1IdleLeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_UP:
////                            game.getSpriteBatch().draw(game.getEnemy1IdleUpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_DOWN:
////                            game.getSpriteBatch().draw(game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                    }
////                    break;
////                case 2:  // 敌人类型 2
////                    switch (currentDirection) {
////                        case RIGHT:
////                            game.getSpriteBatch().draw(game.getEnemy2RightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case LEFT:
////                            game.getSpriteBatch().draw(game.getEnemy2LeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case UP:
////                            game.getSpriteBatch().draw(game.getEnemy2UpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case DOWN:
////                            game.getSpriteBatch().draw(game.getEnemy2DownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_RIGHT:
////                            game.getSpriteBatch().draw(game.getEnemy2IdleRightAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_LEFT:
////                            game.getSpriteBatch().draw(game.getEnemy2IdleLeftAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_UP:
////                            game.getSpriteBatch().draw(game.getEnemy2IdleUpAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                        case IDLE_DOWN:
////                            game.getSpriteBatch().draw(game.getEnemy2IdleDownAnimation().getKeyFrame(sinusInput, true), enemy.getX(), enemy.getY(), 64, 128);
////                            break;
////                    }
////                    break;
////                // 对于敌人 3、4、5 也做类似的处理
////                case 3:  // 敌人类型 3
////                    // 处理敌人 3 的动画
////                    break;
////                case 4:  // 敌人类型 4
////                    // 处理敌人 4 的动画
////                    break;
////                case 5:  // 敌人类型 5
////                    // 处理敌人 5 的动画
////                    break;
////            }
////
////            }
////        }
////
////        game.getSpriteBatch().end();
////    }
////
////    private void startGame() {
////        isGameStarted = true;
////        // 初始化角色位置
////        initializeCharacterPosition();
////        // 初始化敌人位置
////        initializeEnemies();
////    }
////
////    private void updateCharacterMovement(float delta) {
////        // Handle input for WASD keys and Shift for speed boost
////        float adjustedSpeed = characterSpeed;
////        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
////            adjustedSpeed *= SPEED_MULTIPLIER;
////        }
////
////        boolean moved = false;
////        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
////            characterY += adjustedSpeed * delta;
////            currentDirection = Direction.UP;
////            moved = true;
////        }
////        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
////            characterY -= adjustedSpeed * delta;
////            currentDirection = Direction.DOWN;
////            moved = true;
////        }
////        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
////            characterX -= adjustedSpeed * delta;
////            currentDirection = Direction.LEFT;
////            moved = true;
////        }
////        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
////            characterX += adjustedSpeed * delta;
////            currentDirection = Direction.RIGHT;
////            moved = true;
////        }
////
////        // If no key is pressed, set direction to idle based on last movement direction
////        if (!moved) {
////            switch (currentDirection) {
////                case UP:
////                    currentDirection = Direction.IDLE_UP;
////                    break;
////                case DOWN:
////                    currentDirection = Direction.IDLE_DOWN;
////                    break;
////                case LEFT:
////                    currentDirection = Direction.IDLE_LEFT;
////                    break;
////                case RIGHT:
////                    currentDirection = Direction.IDLE_RIGHT;
////                    break;
////                default:
////                    break;
////            }
////        }
////
////        // Prevent character from going out of bounds
////        characterX = Math.max(0, Math.min(characterX, camera.viewportWidth - 64));
////        characterY = Math.max(0, Math.min(characterY, camera.viewportHeight - 128));
////    }
////
////    private void updateEnemies(float delta) {
////        for (Enemy enemy : enemies) {
////            float distance = Vector2.dst(characterX, characterY, enemy.getX(), enemy.getY());
////            if (distance <= detectionRange) {
////                // 朝角色移动
////                Vector2 direction = new Vector2(characterX - enemy.getX(), characterY - enemy.getY()).nor();
////                enemy.setX(enemy.getX() + direction.x * enemy.getSpeed() * delta);
////                enemy.setY(enemy.getY() + direction.y * enemy.getSpeed() * delta);
////            }
////        }
////    }
////
////    @Override
////    public void resize(int width, int height) {
////        camera.setToOrtho(false);
////    }
////
////    @Override
////    public void pause() {
////    }
////
////    @Override
////    public void resume() {
////    }
////
////    @Override
////    public void show() {
////
////    }
////
////    @Override
////    public void hide() {
////    }
////
////    @Override
////    public void dispose() {
////        mapRenderer.dispose();
////        tiledMap.dispose();
////    }
////
////    /**
////     * 敌人类，用于存储敌人的位置和速度。
////     */
////    private static class Enemy {
////        private float x;
////        private float y;
////        private final float speed;
////        private final int type;
////
////        public Enemy(float x, float y, float speed, int type) {
////            this.x = x;
////            this.y = y;
////            this.speed = speed;
////            this.type = type;
////        }
////
////        public float getX() {
////            return x;
////        }
////
////        public void setX(float x) {
////            this.x = x;
////        }
////
////        public float getY() {
////            return y;
////        }
////
////        public void setY(float y) {
////            this.y = y;
////        }
////
////        public float getSpeed() {
////            return speed;
////        }
////
////        public int getType() {
////            return type;
////        }
////    }
////}
//
//package de.tum.cit.fop.maze;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.maps.MapProperties;
//import com.badlogic.gdx.maps.objects.RectangleMapObject;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.ScreenUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * The GameScreen class is responsible for rendering the gameplay screen.
// * It handles the game logic and rendering of the game elements.
// */
//public class GameScreen implements Screen {
//
//    private final MazeRunnerGame game;
//    private final OrthographicCamera camera;
//    private final BitmapFont font;
//
//    private float sinusInput = 0f;
//
//    // Character position and movement state
//    private float characterX;
//    private float characterY;
//    private float characterSpeed = 200f; // Base speed of character movement (pixels per second)
//    private static final float SPEED_MULTIPLIER = 2.5f; // Speed multiplier when Shift is pressed
//
//    private List<Enemy> enemies; // Used to store the list of enemies
//    private float detectionRange = 300f; // Detection range for enemies
//
//    private TiledMap tiledMap;
//    private OrthogonalTiledMapRenderer mapRenderer;
//    private Rectangle mapBounds;
//
//    private enum Direction {
//        UP, DOWN, LEFT, RIGHT, IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT
//    }
//
//    private Direction currentDirection = Direction.IDLE_DOWN; // Default direction
//
//    private boolean isGameStarted = false; // Flag to track if the game has started
//
//    /**
//     * Constructor for GameScreen. Sets up the camera and font.
//     *
//     * @param game The main game class, used to access global resources and methods.
//     */
//    public GameScreen(MazeRunnerGame game) {
//        this.game = game;
//
//        // Create and configure the camera for the game view
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false);
//        camera.zoom = 0.75f;
//
//        // Get the font from the game's skin
//        font = game.getSkin().getFont("font");
//
//        // Load the tiled map
//        TmxMapLoader loader = new TmxMapLoader();
//        tiledMap = loader.load("input.tmx");
//        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
//
//        // Get the collision-layer bounds (assuming first layer is the main tile layer)
//        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
//        mapBounds = new Rectangle(
//                0,
//                0,
//                layer.getWidth() * layer.getTileWidth(),
//                layer.getHeight() * layer.getTileHeight()
//        );
//
//        // Initialize enemy list
//        enemies = new ArrayList<>();
//    }
//
//    private void initializeCharacterPosition() {
//        // We look for an object named "character" in the "Objects" layer
//        RectangleMapObject characterObject = (RectangleMapObject) tiledMap.getLayers()
//                .get("Objects").getObjects().get("character");
//        if (characterObject != null) {
//            Rectangle rect = characterObject.getRectangle();
//            characterX = rect.x;
//            characterY = rect.y;
//        } else {
//            // Fallback if no character object is found
//            characterX = camera.viewportWidth / 2;
//            characterY = camera.viewportHeight / 2;
//        }
//    }
//
//    private void initializeEnemies() {
//        // Loop through objects named "object1", "object2", ..., "object5"
//        for (int i = 1; i <= 5; i++) {
//            String objectName = "object" + i;
//            RectangleMapObject enemyObject = (RectangleMapObject) tiledMap.getLayers()
//                    .get("Objects").getObjects().get(objectName);
//            if (enemyObject != null) {
//                Rectangle rect = enemyObject.getRectangle();
//                enemies.add(new Enemy(rect.x, rect.y, 100f, i));
//            }
//        }
//    }
//
//    @Override
//    public void render(float delta) {
//        // Check if ESC is pressed to return to menu
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            game.goToMenu();
//        }
//
//        // Check if ENTER is pressed to start the game
//        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//            startGame();
//        }
//
//        // Clear screen
//        ScreenUtils.clear(0, 0, 0, 1);
//
//        // If the game has started, update
//        if (isGameStarted) {
//            // Update character movement
//            updateCharacterMovement(delta);
//
//            // Update enemies
//            updateEnemies(delta);
//
//            // Update camera position
//            camera.position.set(characterX, characterY, 0);
//            camera.update();
//
//            // Render the map
//            mapRenderer.setView(camera);
//            mapRenderer.render();
//        }
//
//        // Update sinusInput animation
//        sinusInput += delta;
//        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
//        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);
//
//        // Draw text / sprites
//        game.getSpriteBatch().setProjectionMatrix(camera.combined);
//        game.getSpriteBatch().begin();
//
//        if (!isGameStarted) {
//            // Show instructions before starting
//            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
//            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);
//            // Simple "dancing" idle animation for the character
//            game.getSpriteBatch().draw(
//                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
//                    textX - 96,
//                    textY - 64,
//                    64,
//                    128
//            );
//        } else {
//            // Draw the player based on direction
//            switch (currentDirection) {
//                case UP:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterUpAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case DOWN:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case LEFT:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case RIGHT:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterRightAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case IDLE_UP:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case IDLE_DOWN:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case IDLE_LEFT:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//                case IDLE_RIGHT:
//                    game.getSpriteBatch().draw(
//                            game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true),
//                            characterX, characterY, 64, 128
//                    );
//                    break;
//            }
//
//            // Draw enemies
//            for (Enemy enemy : enemies) {
//                switch (enemy.getType()) {
//                    case 1: // Enemy type 1
//                        switch (currentDirection) {
//                            case RIGHT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1RightAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case LEFT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case UP:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case DOWN:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_RIGHT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1IdleRightAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_LEFT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1IdleLeftAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_UP:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1IdleUpAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_DOWN:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                        }
//                        break;
//
//                    case 2: // Enemy type 2
//                        switch (currentDirection) {
//                            case RIGHT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2RightAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case LEFT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2LeftAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case UP:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2UpAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case DOWN:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2DownAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_RIGHT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2IdleRightAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_LEFT:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2IdleLeftAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_UP:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2IdleUpAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                            case IDLE_DOWN:
//                                game.getSpriteBatch().draw(
//                                        game.getEnemy2IdleDownAnimation().getKeyFrame(sinusInput, true),
//                                        enemy.getX(), enemy.getY(), 64, 128
//                                );
//                                break;
//                        }
//                        break;
//
//                    // Enemy types 3, 4, 5 (not fully animated in example, but you get the idea)
//                    case 3:
//                        // ... Add your animations for enemy 3 ...
//                        break;
//                    case 4:
//                        // ... Add your animations for enemy 4 ...
//                        break;
//                    case 5:
//                        // ... Add your animations for enemy 5 ...
//                        break;
//                }
//            }
//        }
//
//        game.getSpriteBatch().end();
//    }
//
//    /**
//     * Start the game: initialize character and enemy positions.
//     */
//    private void startGame() {
//        isGameStarted = true;
//        // Initialize character position
//        initializeCharacterPosition();
//        // Initialize enemies
//        initializeEnemies();
//    }
//
//    /**
//     * Updates character movement each frame (checks collisions).
//     */
//    private void updateCharacterMovement(float delta) {
//        float oldX = characterX;
//        float oldY = characterY;
//
//        float adjustedSpeed = characterSpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
//            adjustedSpeed *= SPEED_MULTIPLIER;
//        }
//
//        boolean moved = false;
//
//        // 1) Attempt to move vertically (W/S)
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            characterY += adjustedSpeed * delta;
//            currentDirection = Direction.UP;
//            moved = true;
//            // If new Y is blocked, revert
//            if (isCellBlocked(characterX, characterY)) {
//                characterY = oldY;
//            }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            characterY -= adjustedSpeed * delta;
//            currentDirection = Direction.DOWN;
//            moved = true;
//            // If new Y is blocked, revert
//            if (isCellBlocked(characterX, characterY)) {
//                characterY = oldY;
//            }
//        }
//
//        // 2) Attempt to move horizontally (A/D)
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            characterX -= adjustedSpeed * delta;
//            currentDirection = Direction.LEFT;
//            moved = true;
//            // If new X is blocked, revert
//            if (isCellBlocked(characterX, characterY)) {
//                characterX = oldX;
//            }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            characterX += adjustedSpeed * delta;
//            currentDirection = Direction.RIGHT;
//            moved = true;
//            // If new X is blocked, revert
//            if (isCellBlocked(characterX, characterY)) {
//                characterX = oldX;
//            }
//        }
//
//        // If no key is pressed, set direction to idle based on last movement direction
//        if (!moved) {
//            switch (currentDirection) {
//                case UP:
//                    currentDirection = Direction.IDLE_UP;
//                    break;
//                case DOWN:
//                    currentDirection = Direction.IDLE_DOWN;
//                    break;
//                case LEFT:
//                    currentDirection = Direction.IDLE_LEFT;
//                    break;
//                case RIGHT:
//                    currentDirection = Direction.IDLE_RIGHT;
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        // Prevent character from going out of map bounds (optional, if you want bounding checks)
//        characterX = Math.max(mapBounds.x, Math.min(characterX, mapBounds.x + mapBounds.width - 64));
//        characterY = Math.max(mapBounds.y, Math.min(characterY, mapBounds.y + mapBounds.height - 128));
//    }
//
//    /**
//     * Checks if the given (x, y) center of the character is on a blocked tile.
//     */
//    private boolean isCellBlocked(float x, float y) {
//        // Adjust if you want to check corners instead of center
//        // For demonstration, we'll check the center point of the character's feet:
//        // e.g. x + 32, y might be near the bottom, or x + 32, y + something
//        // Simplify to check "bottom center" of the sprite
//        float checkX = x + 32;  // half width of 64
//        float checkY = y;       // bottom of the sprite
//
//        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
//        float tileWidth = layer.getTileWidth();
//        float tileHeight = layer.getTileHeight();
//
//        // Convert world coords -> tile coords
//        int tileX = (int) (checkX / tileWidth);
//        int tileY = (int) (checkY / tileHeight);
//
//        // If out of range, assume blocked or out of map
//        if (tileX < 0 || tileY < 0 || tileX >= layer.getWidth() || tileY >= layer.getHeight()) {
//            return true;
//        }
//
//        // Get the cell
//        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);
//        if (cell == null || cell.getTile() == null) {
//            return false; // no tile means no collision
//        }
//
//        // Check tile property "blocked"
//        MapProperties props = cell.getTile().getProperties();
//        if (props.containsKey("blocked") && (boolean) props.get("blocked")) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private void updateEnemies(float delta) {
//        for (Enemy enemy : enemies) {
//            float distance = Vector2.dst(characterX, characterY, enemy.getX(), enemy.getY());
//            if (distance <= detectionRange) {
//                // Move enemy toward player
//                Vector2 direction = new Vector2(
//                        characterX - enemy.getX(),
//                        characterY - enemy.getY()
//                ).nor();
//                enemy.setX(enemy.getX() + direction.x * enemy.getSpeed() * delta);
//                enemy.setY(enemy.getY() + direction.y * enemy.getSpeed() * delta);
//            }
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        camera.setToOrtho(false);
//    }
//
//    @Override
//    public void pause() {
//    }
//
//    @Override
//    public void resume() {
//    }
//
//    @Override
//    public void show() {
//
//    }
//
//    @Override
//    public void hide() {
//    }
//
//    @Override
//    public void dispose() {
//        mapRenderer.dispose();
//        tiledMap.dispose();
//    }
//
//    /**
//     * Internal Enemy class.
//     */
//    private static class Enemy {
//        private float x;
//        private float y;
//        private final float speed;
//        private final int type;
//
//        public Enemy(float x, float y, float speed, int type) {
//            this.x = x;
//            this.y = y;
//            this.speed = speed;
//            this.type = type;
//        }
//
//        public float getX() {
//            return x;
//        }
//
//        public void setX(float x) {
//            this.x = x;
//        }
//
//        public float getY() {
//            return y;
//        }
//
//        public void setY(float y) {
//            this.y = y;
//        }
//
//        public float getSpeed() {
//            return speed;
//        }
//
//        public int getType() {
//            return type;
//        }
//    }
//}

package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
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
    private float characterSpeed = 200f; // Base speed (pixels/sec)
    private static final float SPEED_MULTIPLIER = 2.5f; // Speed multiplier when Shift is pressed

    // Define the bounding box for the character's feet
    private static final float FOOT_BOX_OFFSET_X = 16f; // offset from characterX's left edge
    private static final float FOOT_BOX_OFFSET_Y = 16f;  // offset from characterY's bottom edge
    private static final float FOOT_BOX_WIDTH    = 32f; // box width for collision
    private static final float FOOT_BOX_HEIGHT   = 18f; // box height for collision

    private List<Enemy> enemies;
    private float detectionRange = 300f; // detection range for enemies

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapBounds;

    // Directions for animations
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

        // Get the collision-layer bounds (assuming layer 0 is the main tile layer)
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapBounds = new Rectangle(
                0,
                0,
                layer.getWidth()  * layer.getTileWidth(),
                layer.getHeight() * layer.getTileHeight()
        );

        // Initialize enemy list
        enemies = new ArrayList<>();
    }

    /**
     * Initializes the character position from the Tiled map "character" object (if any).
     */
    private void initializeCharacterPosition() {
        RectangleMapObject characterObject = (RectangleMapObject)
                tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {
            Rectangle rect = characterObject.getRectangle();
            characterX = 0;  // Start at the left corner (X-coordinate)
            characterY = mapBounds.height - 128;  // Start at the top corner (Y-coordinate)
//            characterX = rect.x;
//            characterY = rect.y;
        } else {
            // Fallback if no character object is found
            characterX = camera.viewportWidth  / 2;
            characterY = camera.viewportHeight / 2;
        }
    }

    /**
     * Initializes enemies based on "object1", "object2", ... in the Tiled map.
     */
    private void initializeEnemies() {
        for (int i = 1; i <= 5; i++) {
            String objectName = "object" + i;
            RectangleMapObject enemyObject = (RectangleMapObject)
                    tiledMap.getLayers().get("Objects").getObjects().get(objectName);
            if (enemyObject != null) {
                Rectangle rect = enemyObject.getRectangle();
                enemies.add(new Enemy(rect.x, rect.y, 100f, i));
            }
        }
    }

    @Override
    public void render(float delta) {
        // Check if ESC is pressed to return to menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Check if ENTER is pressed to start the game
        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        // Clear screen
        ScreenUtils.clear(0, 0, 0, 1);

        // If the game has started, update logic
        if (isGameStarted) {
            updateCharacterMovement(delta);
            updateEnemies(delta);

            // Update camera position
            camera.position.set(characterX, characterY, 0);
            camera.update();

            // Render the map
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        // Update sinusInput for animations
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Draw text / sprites
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();

        if (!isGameStarted) {
            // Show instructions before starting
            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);

            // Simple "dancing" idle animation for the character
            game.getSpriteBatch().draw(
                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                    textX - 96, textY - 64,
                    64, 128
            );
        } else {
            // Draw the character sprite based on direction
            switch (currentDirection) {
                case UP:
                    game.getSpriteBatch().draw(
                            game.getCharacterUpAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case DOWN:
                    game.getSpriteBatch().draw(
                            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case LEFT:
                    game.getSpriteBatch().draw(
                            game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case RIGHT:
                    game.getSpriteBatch().draw(
                            game.getCharacterRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case IDLE_UP:
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case IDLE_DOWN:
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case IDLE_LEFT:
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
                case IDLE_RIGHT:
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY, 64, 128
                    );
                    break;
            }

            // Draw enemies (this example just uses the player's direction for animations)
            for (Enemy enemy : enemies) {
                switch (enemy.getType()) {
                    case 1:
                        switch (currentDirection) {
                            case RIGHT:
                                game.getSpriteBatch().draw(
                                        game.getEnemy1RightAnimation().getKeyFrame(sinusInput, true),
                                        enemy.getX(), enemy.getY(), 64, 128
                                );
                                break;
                            case LEFT:
                                game.getSpriteBatch().draw(
                                        game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true),
                                        enemy.getX(), enemy.getY(), 64, 128
                                );
                                break;
                            case UP:
                                game.getSpriteBatch().draw(
                                        game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true),
                                        enemy.getX(), enemy.getY(), 64, 128
                                );
                                break;
                            case DOWN:
                                game.getSpriteBatch().draw(
                                        game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true),
                                        enemy.getX(), enemy.getY(), 64, 128
                                );
                                break;
                            // IDLE cases...
                            default:
                                game.getSpriteBatch().draw(
                                        game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true),
                                        enemy.getX(), enemy.getY(), 64, 128
                                );
                                break;
                        }
                        break;

                    case 2:
                        // ... Add enemy2's animations ...
                        break;
                    case 3:
                        // ... Add enemy3's animations ...
                        break;
                    case 4:
                        // ... Add enemy4's animations ...
                        break;
                    case 5:
                        // ... Add enemy5's animations ...
                        break;
                }
            }
        }

        game.getSpriteBatch().end();
    }

    /**
     * Starts the game by initializing positions.
     */
    private void startGame() {
        isGameStarted = true;
        initializeCharacterPosition();
        initializeEnemies();
    }

    /**
     * Updates the character movement with bounding-box collision checks.
     */
    private void updateCharacterMovement(float delta) {
        float oldX = characterX;
        float oldY = characterY;

        float adjustedSpeed = characterSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            adjustedSpeed *= SPEED_MULTIPLIER;
        }

        boolean moved = false;

        // 1) Move vertically first (W/S)
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterY += adjustedSpeed * delta;
            currentDirection = Direction.UP;
            moved = true;
            if (isBoxBlocked(characterX, characterY)) {
                characterY = oldY; // revert
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterY -= adjustedSpeed * delta;
            currentDirection = Direction.DOWN;
            moved = true;
            if (isBoxBlocked(characterX, characterY)) {
                characterY = oldY; // revert
            }
        }

        // 2) Move horizontally next (A/D)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterX -= adjustedSpeed * delta;
            currentDirection = Direction.LEFT;
            moved = true;
            if (isBoxBlocked(characterX, characterY)) {
                characterX = oldX; // revert
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterX += adjustedSpeed * delta;
            currentDirection = Direction.RIGHT;
            moved = true;
            if (isBoxBlocked(characterX, characterY)) {
                characterX = oldX; // revert
            }
        }

        // If no key is pressed, switch to an idle direction
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

        // Optional: Prevent character from going out of map bounds
//        characterX = Math.max(mapBounds.x, Math.min(
//                characterX, mapBounds.x + mapBounds.width  - 64
//        ));
//        characterY = Math.max(mapBounds.y, Math.min(
//                characterY, mapBounds.y + mapBounds.height - 128
//        ));
    }

    /**
     * Returns true if the character's foot bounding box at (x, y) is blocked by any tile.
     */
    private boolean isBoxBlocked(float x, float y) {
        // Calculate the foot box's corners
        float footBoxX = x + FOOT_BOX_OFFSET_X;
        float footBoxY = y + FOOT_BOX_OFFSET_Y;

        // Check each corner:
        // bottom-left
        if (isTileBlocked(footBoxX, footBoxY)) {
            return true;
        }
        // bottom-right
        if (isTileBlocked(footBoxX + FOOT_BOX_WIDTH, footBoxY)) {
            return true;
        }
        // top-left
        if (isTileBlocked(footBoxX, footBoxY + FOOT_BOX_HEIGHT)) {
            return true;
        }
        // top-right
        if (isTileBlocked(footBoxX + FOOT_BOX_WIDTH, footBoxY + FOOT_BOX_HEIGHT)) {
            return true;
        }

        return false;
    }

    /**
     * Checks a single point (world coordinates) to see if it's on a 'blocked' tile.
     */
    private boolean isTileBlocked(float worldX, float worldY) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float tileWidth  = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();

        // Convert world coords -> tile coords
        int tileX = (int) (worldX / tileWidth);
        int tileY = (int) (worldY / tileHeight);

        // If out of range, consider blocked or out of map
        if (tileX < 0 || tileY < 0 || tileX >= layer.getWidth() || tileY >= layer.getHeight()) {
            return true;
        }

        // Get the cell
        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) {
            return false; // no tile => not blocked
        }

        // Check tile property "blocked"
        MapProperties props = cell.getTile().getProperties();
        if (props.containsKey("blocked") && (boolean) props.get("blocked")) {
            return true;
        }

        return false;
    }

    /**
     * Updates enemies to move toward the player if within detection range.
     */
    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            float distance = Vector2.dst(characterX, characterY, enemy.getX(), enemy.getY());
            if (distance <= detectionRange) {
                // Move enemy toward player
                Vector2 direction = new Vector2(
                        characterX - enemy.getX(),
                        characterY - enemy.getY()
                ).nor();
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
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
    }

    /**
     * Internal Enemy class to store position and speed.
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
