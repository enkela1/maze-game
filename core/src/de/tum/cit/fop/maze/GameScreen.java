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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    private static final float FOOT_BOX_OFFSET_Y = 21f; // offset from characterY's bottom edge
    private static final float FOOT_BOX_WIDTH    = 32f; // box width for collision
    private static final float FOOT_BOX_HEIGHT   = 18f; // box height for collision

    // Enemies
    private List<Enemy> enemies; // 用于存储敌人的列表
    private float detectionRange = 300f; // 检测范围

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapBounds;

    // Directions for animations
    private enum Direction {
        UP, DOWN, LEFT, RIGHT,
        IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT
    }

    private Direction currentDirection = Direction.IDLE_DOWN; // Default direction

    // Flag to track if the game has started
    private boolean isGameStarted = false;


    private boolean isPaused = false;
    private boolean isAttacking = false;
    private boolean isPickingUp = false;
    private boolean isHolding = false;

    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.5f;

    private float pickUpTimer = 0f;
    private static final float PICKUP_DURATION = 1f;



    private Array<Rectangle> collisionRectangles;

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

        // Get boundaries from first layer
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapBounds = new Rectangle(
                0,
                0,
                layer.getWidth() * layer.getTileWidth(),
                layer.getHeight() * layer.getTileHeight()
        );

        // Initialize the enemies list
        enemies = new ArrayList<>();

        // rectangle based collisions if needed:

        // collisionRectangles = new Array<>();
        // MapObjects objects = tiledMap.getLayers().get("layer").getObjects();
        // for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
        //     collisionRectangles.add(rectangleObject.getRectangle());
        // }

        collisionRectangles = new Array<>();
    }

    /**
     * Called when the player presses ENTER to start the game.
     */
    private void startGame() {
        isGameStarted = true;
        // Initialize character position
        initializeCharacterPosition();
        // Initialize enemy positions
        initializeEnemies();
    }

    /**
     * Initializes character position from the "Objects" layer in the Tiled map.
     */
    private void initializeCharacterPosition() {
        RectangleMapObject characterObject =
                (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {

            // Rectangle rect = characterObject.getRectangle();
            // characterX = rect.x;
            // characterY = rect.y;

            characterX = 0;               // Start at the left corner (X-coordinate)
            characterY = mapBounds.height - 128;  // Start near the top corner
        } else {
            // Fallback if no character object is found
            characterX = camera.viewportWidth / 2;
            characterY = camera.viewportHeight / 2;
        }
    }

    /**
     * Initializes enemies from the "Objects" layer in the Tiled map.
     * For each "objectX" name, we create an Enemy with type X.
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
        // Check if ESC is pressed => go to menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Check if ENTER is pressed => start game
        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        // Check if 'P' is pressed => pause toggle
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
        }

        // Clear screen
        ScreenUtils.clear(0, 0, 0, 1);

        // If the game has started and is not paused, update logic
        if (isGameStarted && !isPaused) {
            updateGameState(delta);
            // Update camera position
            camera.position.set(characterX, characterY, 0);
            camera.update();
            // Render map
            mapRenderer.setView(camera);
            mapRenderer.render();
        } else if (isGameStarted) {
            // Game is started but paused, so we skip updates but still might want to show something
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        // Update sinusInput for the wiggly text
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Use SpriteBatch for drawing text, character, enemies, etc.
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();

        if (!isGameStarted) {
            // If game not started, show instructions
            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);
            // Draw a fun animation for the character (e.g. idleDown)
            game.getSpriteBatch().draw(
                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                    textX - 96, textY - 64,
                    64, 128
            );
        } else {
            // If game is started, render character and enemies
            if (isPaused) {
                // Show "Game Paused" message if paused
                font.draw(
                        game.getSpriteBatch(),
                        "Game Paused. Press 'P' to resume.",
                        camera.position.x - 100,
                        camera.position.y
                );
            }

            // 1) Render character (with pickup/attack logic)
            renderCharacter();

            // 2) Render enemies
            for (Enemy enemy : enemies) {
                renderEnemy(enemy);
            }
        }

        game.getSpriteBatch().end();
    }

    /**
     * Update the overall game logic (movement, attacking, pickups, enemies, etc.).
     */
    private void updateGameState(float delta) {
        // If currently picking up or attacking, advance timers
        if (isPickingUp) {
            pickUpTimer += delta;
            if (pickUpTimer >= PICKUP_DURATION) {
                isPickingUp = false;
                isHolding = true;
                pickUpTimer = 0f;
            }
        } else if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0f;
            }
        } else {
            // Otherwise, handle normal movement input or toggles
            handleInput(delta);
        }

        // Update enemies logic
        updateEnemies(delta);
    }

    /**
     * Handle player input for movement, attacking (J), picking up (F), holding (G), dropping (H).
     */
    private void handleInput(float delta) {
        if (isPaused) return;

        // Key F => start picking up
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            isPickingUp = true;
            pickUpTimer = 0f;
            return;
        }
        // Key G => forcibly go to 'holding' state
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            isHolding = true;
            return;
        }
        // Key J => start attacking
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            isAttacking = true;
            attackTimer = 0f;
            return;
        }
        // Key H => put down (stop holding)
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            isHolding = false;
            // Force direction to idle
            switch (currentDirection) {
                case UP:
                    currentDirection = Direction.IDLE_UP; break;
                case DOWN:
                    currentDirection = Direction.IDLE_DOWN; break;
                case LEFT:
                    currentDirection = Direction.IDLE_LEFT; break;
                case RIGHT:
                    currentDirection = Direction.IDLE_RIGHT; break;
                default:
                    break;
            }
            return;
        }


        float adjustedSpeed = characterSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            adjustedSpeed *= SPEED_MULTIPLIER;
        }

        // We do partial movement checks + collisions
        boolean moved = false;
        float oldX = characterX;
        float oldY = characterY;

        // Move vertically (W/S)
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

        // Move horizontally (A/D)
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

        // If no movement, switch to idle direction
        if (!moved) {
            // If holding something, we might keep the movement direction or revert to idle
            if (isHolding) {

                // switch to idle if no movement
                switch (currentDirection) {
                    case UP:
                        currentDirection = Direction.IDLE_UP;   break;
                    case DOWN:
                        currentDirection = Direction.IDLE_DOWN; break;
                    case LEFT:
                        currentDirection = Direction.IDLE_LEFT; break;
                    case RIGHT:
                        currentDirection = Direction.IDLE_RIGHT;break;
                    default:
                        break;
                }
            } else {
                // Not holding => go idle
                switch (currentDirection) {
                    case UP:
                        currentDirection = Direction.IDLE_UP;   break;
                    case DOWN:
                        currentDirection = Direction.IDLE_DOWN; break;
                    case LEFT:
                        currentDirection = Direction.IDLE_LEFT; break;
                    case RIGHT:
                        currentDirection = Direction.IDLE_RIGHT;break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Renders the character, taking into account picking up, holding, and attacking animations.
     */
    private void renderCharacter() {
        if (isPickingUp) {
            renderPickUpAnimation();
        } else if (isHolding) {
            renderHoldAnimation();
        } else if (isAttacking) {
            renderAttackAnimation();
        } else {
            renderMovementOrIdle();
        }
    }

    /**
     * Renders a single enemy, using your existing approach (enemy1, enemy2, etc.).
     */
    private void renderEnemy(Enemy enemy) {
        // Switch on the enemy type and draw accordingly.
        switch (enemy.getType()) {
            case 1:
                // Animations for enemy1
                switch (currentDirection) {
                    case RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1RightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                }
                break;

            case 2:
                // Animations for enemy2
                switch (currentDirection) {
                    case RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2RightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy2UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy2DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                64, 64
                        );
                        break;
                }
                break;

            case 3:

                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    /**
     * Renders the "picking up" animation.
     */
    private void renderPickUpAnimation() {
        switch (currentDirection) {
            case UP, IDLE_UP -> game.getSpriteBatch().draw(
                    game.getCharacterPickupUpAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    64, 128
            );
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterPickupDownAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    64, 128
            );
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterPickupLeftAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    64, 128
            );
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterPickupRightAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    64, 128
            );
        }
    }

    /**
     * Renders the "holding" animation.
     */
    private void renderHoldAnimation() {
        switch (currentDirection) {
            case UP -> game.getSpriteBatch().draw(
                    game.getCharacterHoldUpAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterHoldDownAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldLeftAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldRightAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case IDLE_UP -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleUpAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleDownAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleLeftAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
            case IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleRightAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    64, 128
            );
        }
    }

    /**
     * Renders the "attacking" animation.
     */
    private void renderAttackAnimation() {
        switch (currentDirection) {
            case UP, IDLE_UP -> game.getSpriteBatch().draw(
                    game.getCharacterAttackUpAnimation().getKeyFrame(attackTimer, true),
                    characterX - 32, characterY,
                    128, 128
            );
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterAttackDownAnimation().getKeyFrame(attackTimer, true),
                    characterX - 32, characterY,
                    128, 128
            );
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterAttackLeftAnimation().getKeyFrame(attackTimer, true),
                    characterX - 32, characterY,
                    128, 128
            );
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterAttackRightAnimation().getKeyFrame(attackTimer, true),
                    characterX - 32, characterY,
                    128, 128
            );
        }
    }

    /**
     * Renders the normal walking or idle animation (no pickup/attack/hold).
     */
    private void renderMovementOrIdle() {
        switch (currentDirection) {
            case UP ->
                    game.getSpriteBatch().draw(
                            game.getCharacterUpAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case DOWN ->
                    game.getSpriteBatch().draw(
                            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case LEFT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case RIGHT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case IDLE_UP ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case IDLE_DOWN ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case IDLE_LEFT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
            case IDLE_RIGHT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            64, 128
                    );
        }
    }

    /**
     * Updates enemies so that if within detectionRange, they move toward the character.
     */
    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            float distance = Vector2.dst(characterX, characterY, enemy.getX(), enemy.getY());
            if (distance <= detectionRange) {
                // Move toward the character
                Vector2 direction = new Vector2(characterX - enemy.getX(), characterY - enemy.getY()).nor();
                enemy.setX(enemy.getX() + direction.x * enemy.getSpeed() * delta);
                enemy.setY(enemy.getY() + direction.y * enemy.getSpeed() * delta);
            }
        }
    }

    /**
     * Returns true if the character's foot bounding box at (x, y) is blocked by any tile with "blocked" property.
     */
    private boolean isBoxBlocked(float x, float y) {
        float footBoxX = x + FOOT_BOX_OFFSET_X;
        float footBoxY = y + FOOT_BOX_OFFSET_Y;

        // Check each corner
        if (isTileBlocked(footBoxX, footBoxY)) return true;
        if (isTileBlocked(footBoxX + FOOT_BOX_WIDTH, footBoxY)) return true;
        if (isTileBlocked(footBoxX, footBoxY + FOOT_BOX_HEIGHT)) return true;
        if (isTileBlocked(footBoxX + FOOT_BOX_WIDTH, footBoxY + FOOT_BOX_HEIGHT)) return true;

        return false;
    }

    /**
     * Checks a single point (in world coordinates) to see if it's on a 'blocked' tile.
     */
    private boolean isTileBlocked(float worldX, float worldY) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float tileWidth = layer.getTileWidth();
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
