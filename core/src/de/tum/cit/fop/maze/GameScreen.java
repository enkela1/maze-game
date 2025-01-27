package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;


import static de.tum.cit.fop.maze.GameScreen.Direction.*;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private Sound fasterSound; // 加速的声音
    private boolean isFasterSoundPlaying = false; // 声音是否正在播放
    private float fasterSoundTimer = 0f; // 跟踪声音播放的计时器
    private final float FASTER_SOUND_DURATION = 6.0f; // faster.mp3 的播放时长，单位：秒

    private boolean isHurtSoundPlaying = false; // 声音是否正在播放
    private float hurtSoundTimer = 0f; // 计时器，用于追踪播放时间
    private final float HURT_SOUND_DURATION = 3.3f; // 声音播放的时长（根据实际声音时长设置，单位秒）

    // 定义文字数组
    private final String[] tutorialMessages = {
            "You wake up...",
            "Aurora spaceship is damaged...",
            "Find the repair manual...",
            "Escape through the portal...",
            "AWSD controls direction...",
            "Shift controls acceleration...",
            "J controls attack...",
            "The coins on the ground are rare metals...",
            " which determine your game score...",
            " Hearts on the ground ...",
            " can heal you to your maximum health...",
            " The strange boxes on the ground...",
            " can increase your speed...",
            "Don't be seen by monsters...",
            "Don't forget to avoid the flames...",
            "Black holes are attractive...",
            "They are dangerous...",
            "Good Luck..."

    };
    private boolean tutorialIsActive = false;
    private int tutorialWordState = 0; // 当前显示的状态：0=显示"1 word"，1=显示"2 word"，2=显示"3 word"，3=全部消失
    private float wordDisplayTimer = 0f; // 用于计时
    private final float WORD_DISPLAY_INTERVAL = 2f; // 每个状态持续时间(秒)
    private final MazeRunnerGame game;
    private MenuScreen menuScreen;
    private  OrthographicCamera camera;
    private final BitmapFont font;
    private Stage pauseStage;
    private Stage gameStage;
    private Stage winStage;
    private Stage loseStage;
    private Hud hud; // Our new HUD
    private Sound collectSound;
    private Sound heartSound;
    private Sound hurtSound;
    private Sound deathSound;
    private Sound victorySound;
    private Sound menuSound;

    private float sinusInput = 0f;

    // Character position and movement state
    private float characterX;
    private float characterY;
    private float characterSpeed = 80f; // Base speed (pixels/sec)
    private boolean isaccelarationActive = false;
    private long accelarationEndTime = 0;
    private static final float SPEED_MULTIPLIER = 2f;
    private int characterWidth = 32;
    private int characterHeight = 64;
    private int enemyWidth = 32;
    private int enemyHeight = 32; // Speed multiplier when Shift is pressed


    private static final float FOOT_BOX_OFFSET_X = 8f;    // offset from characterX's left edge
    private static final float FOOT_BOX_OFFSET_Y = 10.5f; // offset from characterY's bottom edge
    private static final float FOOT_BOX_WIDTH    = 16f;   // box width for collision
    private static final float FOOT_BOX_HEIGHT   = 9f;    // box height for collision

    // Enemies
    private List<Enemy> enemies; // store enemies
    private float detectionRange = 140f; // detecting range

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapBounds;
    private boolean isGameOver = false;
    private float gameOverTimer = 0;


    Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
    Texture backgroundTexture1 = new Texture(Gdx.files.internal("background1.png"));

    // Directions for animations
    public enum Direction {
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

    private boolean keyCollected = false;



    private Array<Rectangle> collisionRectangles;

    // Character health system
    private int characterHealth;
    private static final int MAX_HEALTH = 100;
    private static final int DAMAGE_AMOUNT = MAX_HEALTH / 20; // 每次碰撞减少1/20生命值
    private static final int HEAL_AMOUNT = MAX_HEALTH / 2;   // 每次拾取爱心恢复1/2生命值

    private boolean isCharacterRed = false;
    private float redTimer = 0f;
    private static final float RED_DURATION = 0.5f; // 角色变红持续时间（秒）





    private int coinCount = 0;







    // use a small class to unify hearts, coins, and fires:
    private static class Item {
        float x, y;
        ItemType type;
        boolean collected = false; // track if already taken
        public Item(float x, float y, ItemType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    // define the types of items:
    public enum ItemType {
        HEART, COIN, FIRE, KEY,ACCELARATION,BLACKHOLE
    }

    // Portal
    private boolean isPortalActive = false;
    private float portalX;
    private float portalY;
    private static final float PORTAL_WIDTH = 64;
    private static final float PORTAL_HEIGHT = 64;

    // For a simple texture (or you can store an Animation<TextureRegion> if you like).
    private Texture portalTexture;

    // Game win state
    private boolean isGameWon = false;
    private float gameWinTimer = 0f;



    private List<Item> items;


    public int getCoinCount() {
        return coinCount;
    }

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        menuScreen = new MenuScreen(game,coinCount);
        isGameOver = false;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.5f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        // Load the tiled map
        TmxMapLoader loader = new TmxMapLoader();
        tiledMap = loader.load("input.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tutorialIsActive = true;

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

        hud = new Hud();

        // Initialize health
        characterHealth = MAX_HEALTH;

        // Initialize our items list
        items = new ArrayList<>();


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

        initializeItems();

        camera.zoom = 0.5f; // 这里设置缩放程度，值越小视角越大
        camera.update();


    }

     public void loadMap(String mapName) {

        TmxMapLoader loader = new TmxMapLoader();

        tiledMap = loader.load(mapName + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapBounds = new Rectangle(
                0,
                0,
                layer.getWidth() * layer.getTileWidth(),
                layer.getHeight() * layer.getTileHeight()
        );

         resetCharacterPosition();
         currentDirection = Direction.IDLE_DOWN;

         resetLevelState();
         camera.zoom=0.5f;


         if (mapName.equals("input")) { // "input" 对应 Level 1 地图文件名
             tutorialIsActive = true;  // 启用教程
         } else {
             tutorialIsActive = false; // 禁用教程
         }



    }
    private void resetCharacterPosition() {
        RectangleMapObject characterObject =
                (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {

            // Rectangle rect = characterObject.getRectangle();
            // characterX = rect.x;
            // characterY = rect.y;

            characterX = 87;               // Start at the left corner (X-coordinate)
            characterY = mapBounds.height - 130;  // Start near the top corner
        } else {
            // Fallback if no character object is found
            characterX = camera.viewportWidth / 2;
            characterY = camera.viewportHeight / 2;
        }
    }

    void resetLevelState() {
        enemies.clear();
        items.clear();

        initializeEnemies();
        initializeItems();
        characterHealth= MAX_HEALTH;
        coinCount=0;
    }





    /**
     * Initializes character position from the "Objects" layer in the Tiled map.
     */
    private void initializeCharacterPosition() {
        RectangleMapObject characterObject =
                (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {

             Rectangle rect = characterObject.getRectangle();
             characterX = rect.x;
             characterY = rect.y;
//
//            characterX = 87;               // Start at the left corner (X-coordinate)
//            characterY = mapBounds.height - 130;  // Start near the top corner
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
                enemies.add(new Enemy(rect.x, rect.y, 95f, i));
            }
        }
    }

    private List<Vector2> gatherOpenTiles() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        // or whichever layer holds your maze
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();

        int mapWidth = layer.getWidth();
        int mapHeight = layer.getHeight();

        List<Vector2> openTiles = new ArrayList<>();

        // Loop over every tile in the layer
        for (int ty = 0; ty < mapHeight; ty++) {
            for (int tx = 0; tx < mapWidth; tx++) {
                float worldX = tx * tileWidth;
                float worldY = ty * tileHeight;

                // If NOT blocked => store in openTiles
                if (!isTileBlockedInMaze(worldX, worldY)) {
                    openTiles.add(new Vector2(worldX, worldY));
                }
            }
        }

        return openTiles;
    }

    private boolean isTileBlockedInMaze(float worldX, float worldY) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();

        int tileX = (int) (worldX / tileWidth);
        int tileY = (int) (worldY / tileHeight);

        // Check bounds
        if (tileX < 0 || tileY < 0 || tileX >= layer.getWidth() || tileY >= layer.getHeight()) {
            return true; // Out-of-bounds => treat as blocked
        }

        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) {
            return false; // no tile => not blocked
        }

        MapProperties props = cell.getTile().getProperties();
        // If "blocked" property is true => blocked
        return props.containsKey("blocked") && (boolean) props.get("blocked");
    }

    private void initializeItems() {
        items = new ArrayList<>();

        // 1) Gather all open tile positions (already implemented above)
        List<Vector2> openTiles = gatherOpenTiles();

        // 2) Shuffle them for randomization
        java.util.Collections.shuffle(openTiles);

        // We'll track how many items we still need.
        int heartsToPlace = 5;
        int coinsToPlace  = 5;
        int firesToPlace  = 3;
        int keysToPlace = 1;
        int accelarationToPlace =3;
        int blackHolesToPlace = 2;


        // 3) Iterate over shuffled openTiles. For each tile:
        //    - Compute center of the tile
        //    - Check collisions with walls (the item bounding box)
        //    - Check distance from other items
        //    - If safe, place the item
        for (Vector2 tilePos : openTiles) {
            // If we've placed them all, break out
            if (heartsToPlace <= 0 && coinsToPlace <= 0 && firesToPlace <= 0 && keysToPlace <= 0&& accelarationToPlace <=0 &&blackHolesToPlace <= 0 ) {
                break;
            }

            // Convert tile’s top-left to item center:
            // tilePos is the top-left corner of the tile, e.g. (x=16, y=32).
            // We want the item (32×32) to appear centered in a 16×16 tile.
            final float tileWidth  = 16;
            final float tileHeight = 16;

            // Center of this tile
            float centerX = tilePos.x + (tileWidth / 2f) - (32 / 2f);
            float centerY = tilePos.y + (tileHeight / 2f) - (32 / 2f);

            // Check collision with walls or if too close to other items
            if (isItemCollidingWithWalls(centerX, centerY)) continue;
            if (isTooCloseToOtherItems(centerX, centerY))    continue;

            // At this point, the tile is valid. Place whichever item we still need.
            if (heartsToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.HEART));
                heartsToPlace--;
                continue; // go to the next tile
            }
            if (coinsToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.COIN));
                coinsToPlace--;
                continue;
            }
            if (firesToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.FIRE));
                firesToPlace--;
                continue;
            }
            if (keysToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.KEY));
                keysToPlace--;
                continue;
            }
            if (accelarationToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.ACCELARATION));
                accelarationToPlace--;

            }

            if (blackHolesToPlace > 0) {
                items.add(new Item(centerX, centerY, ItemType.BLACKHOLE));
                blackHolesToPlace--;
            }

        }
    }


    @Override
    public void render(float delta) {



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        if (isGameWon) {


            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            winStage.getBatch().begin();
            winStage.getBatch().draw(
                    backgroundTexture,
                    0, 0,
                    winStage.getViewport().getWorldWidth(),
                    winStage.getViewport().getWorldHeight()
            );
            winStage.getBatch().end();

            winStage.act(delta);
            winStage.draw();

            return;
        }



        // Check if ESC is pressed => go to menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
            game.setScreen(new MenuScreen(game,0));
            camera.zoom=1f;
        }

//clear the screen
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //if character is dead=>SHOW GAMEOVER
        if (isGameOver) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Draw the pause background
            loseStage.getBatch().begin();
            loseStage.getBatch().draw(
                    backgroundTexture,
                    0, 0,
                    loseStage.getViewport().getWorldWidth(),
                    loseStage.getViewport().getWorldHeight()
            );
            loseStage.getBatch().end();

            // Draw pause UI
            Gdx.input.setInputProcessor(loseStage);
            loseStage.act(delta);
            loseStage.draw();


            return;
        }

            // Skip all normal game rendering



        // Check if ENTER is pressed => start game
        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        // Check if 'P' is pressed => pause toggle

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
            if (isPaused) {
                Gdx.input.setInputProcessor(pauseStage); // Set input processor to pause menu
            } else {
                Gdx.input.setInputProcessor(null); // Reset input processor to game logic
            }
        }

        if (isPaused) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Draw the pause background
            pauseStage.getBatch().begin();
            pauseStage.getBatch().draw(
                    backgroundTexture,
                    0, 0,
                    pauseStage.getViewport().getWorldWidth(),
                    pauseStage.getViewport().getWorldHeight()
            );
            pauseStage.getBatch().end();

            // Draw pause UI
            pauseStage.act(delta);
            pauseStage.draw();

            // Skip all normal game rendering
            return;
        }

        // Clear screen
//        ScreenUtils.clear(0, 0, 0, 1);

        if (!isGameOver && isGameStarted && !isPaused) {
            updateGameState(delta);
            checkPortalCollision();
        }

        // Use SpriteBatch for drawing text, character, enemies, etc.
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();

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



        if (!isGameStarted) {
            game.getSpriteBatch().draw(
                    backgroundTexture1,
                    0, 0,
                    camera.viewportWidth/2,
                    camera.viewportHeight/2
            );
            // If game not started, show instructions
            font.draw(game.getSpriteBatch(), "Press ENTER to start", textX, textY);
            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY - 100);
            // Draw a fun animation for the character (e.g. idleDown)
            game.getSpriteBatch().draw(
                    game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                    textX - 4, textY - 120,
                    64, 128
            );
        } else {
            // If game is started, render character and enemies
            if (isPaused) {
                // Show "Game Paused" message if paused
                if (isPaused) {
                    pauseStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                    pauseStage.draw();
                }


            }


            if (isCharacterRed) {
                if (!isHurtSoundPlaying) {
                    hurtSound.play(); // 播放声音
                    isHurtSoundPlaying = true; // 标记声音正在播放
                    hurtSoundTimer = 0f; // 重置计时器
                }
                game.getSpriteBatch().setColor(Color.RED);
                hurtSoundTimer += Gdx.graphics.getDeltaTime();
                if (hurtSoundTimer >= HURT_SOUND_DURATION) {
                    isHurtSoundPlaying = false; // 声音播放完成，允许再次播放
                }
            } else {
                game.getSpriteBatch().setColor(Color.WHITE);
            }

            // 1) Render character (with pickup/attack logic)
            renderCharacter();

            game.getSpriteBatch().setColor(Color.WHITE);


            // 2) Render enemies
            for (Enemy enemy : enemies) {
                renderEnemy(enemy);
            }


            // Draw items (heart, coin, fire) if they are not yet collected
            for (Item item : items) {
                if (!item.collected) {
                    switch (item.type) {
                        case HEART -> game.getSpriteBatch().draw(
                                game.getHeartAnimation().getKeyFrame(sinusInput, true),
                                item.x, item.y,
                                32, 32 // scale as you like
                        );
                        case COIN -> game.getSpriteBatch().draw(
                                game.getCoinAnimation().getKeyFrame(sinusInput, true),
                                item.x, item.y,
                                32, 32
                        );
                        case FIRE -> game.getSpriteBatch().draw(
                                game.getFireAnimation().getKeyFrame(sinusInput, true),
                                item.x, item.y,
                                32, 32
                        );

                        case KEY -> {
                            // Draw the key on the map
                            game.getSpriteBatch().draw(
                                    game.getKeyAnimation().getKeyFrame(sinusInput, true),
                                    item.x, item.y,
                                    32, 32 //size
                            );

                        }
                        case ACCELARATION -> game.getSpriteBatch().draw(
                                game.getAccelarationAnimation().getKeyFrame(sinusInput, true),
                                item.x, item.y,
                                32, 32
                        );

                        case BLACKHOLE -> game.getSpriteBatch().draw(
                                game.getBlackholeAnimation().getKeyFrame(sinusInput, true),
                                item.x, item.y,
                                50, 50
                        );


                    }
                }
            }

            // Display health and coinCount




            if (isPortalActive && !isGameOver && !isGameWon) {

                float stateTime = sinusInput;
                TextureRegion portalFrame = game.getPortalAnimation().getKeyFrame(stateTime, true);

                game.getSpriteBatch().draw(
                        portalFrame,
                        portalX, portalY,
                        PORTAL_WIDTH, PORTAL_HEIGHT
                );
            }

            float zoom = camera.zoom;


            float fixedVerticalSpacing = 50;


            font.draw(game.getSpriteBatch(),
                    "GameScores: " + coinCount,
                    camera.position.x - 930 * zoom,
                    camera.position.y + 530 * zoom
            );

            font.draw(game.getSpriteBatch(),
                    "Health: " + characterHealth,
                    camera.position.x - 930 * zoom,
                    camera.position.y + 530 * zoom - fixedVerticalSpacing
            );

        }


        if (keyCollected) {
            font.draw(
                    game.getSpriteBatch(),
                    "KEY COLLECTED!",
                    camera.position.x + 220,
                    camera.position.y + 300
            );
        }

        game.getSpriteBatch().end();

        // draws the arrow



        if(keyCollected) {
            hud.update(delta, characterX, characterY, portalX, portalY, camera);
            hud.render(); // arrow is drawn pinned to the corner
        }



        if (isCharacterRed) {
            redTimer += delta;
            if (redTimer >= RED_DURATION) {
                isCharacterRed = false;
                redTimer = 0f;
            }
        }

        checkItemCollisions();
//        checkHeartCollision();

        //level1 && game starts -> tutorial starts
        //tutorial starts -> no health reduce
        game.getSpriteBatch().begin();

            if (tutorialIsActive && isGameStarted) {
                wordDisplayTimer += delta;

                // 切换到下一个文字
                if (wordDisplayTimer >= WORD_DISPLAY_INTERVAL) {
                    wordDisplayTimer = 0f; // 重置计时器
                    tutorialWordState++; // 进入下一个状态
                }



                // 动态显示当前文字
                if (tutorialWordState < tutorialMessages.length) {
                    font.getData().setScale(0.5f); // 将字体缩放到原来的 50%
                    font.draw(game.getSpriteBatch(), tutorialMessages[tutorialWordState], characterX - 5, characterY + 80);
                    font.getData().setScale(1.0f); // 恢复默认字体大小，避免影响其他文字
                } else {
                    tutorialIsActive = false; // 结束教程
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
            } else if ((isaccelarationActive && System.currentTimeMillis() > accelarationEndTime))
                characterSpeed = 150f;
            isaccelarationActive = false;{

            }
        } else if (!isPaused) {
            // Otherwise, handle normal movement input or toggles
            handleInput(delta);


            // Update enemies logic
            updateEnemies(delta);
//        checkHeartCollision();
            checkItemCollisions();
        }
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
        // Key J => start attackingr
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            isAttacking = true;
            attackTimer = 0f;
            handleAttack(new Vector2(characterX, characterY));
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            zoomIn();
        }

        // Handle zoom out (DOWN key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            zoomOut();
        }


        float adjustedSpeed = characterSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            adjustedSpeed *= SPEED_MULTIPLIER;
            // 播放加速声音
            if (!isFasterSoundPlaying) {
                fasterSound.play(); // 播放声音
                isFasterSoundPlaying = true; // 标记声音正在播放
                fasterSoundTimer = 0f; // 重置计时器
            }
        }
        if (isFasterSoundPlaying) {
            fasterSoundTimer += delta;
            if (fasterSoundTimer >= FASTER_SOUND_DURATION) {
                isFasterSoundPlaying = false; // 声音播放完成
            }
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
    private void zoomIn() {
        camera.zoom -= 0.1f;
        if (camera.zoom < 0.2f) camera.zoom = 0.2f;  // Prevent too much zoom out
        camera.update();

    }

    private void zoomOut() {
        camera.zoom += 0.1f;
        if (camera.zoom > 2.0f) camera.zoom = 2.0f;  // Prevent too much zoom in
        camera.update();

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
                                enemyWidth, enemyHeight
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy1UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy1DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy1IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
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
                                enemyWidth, enemyHeight
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy2UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy2DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy2IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                }
                break;

            case 3:switch (currentDirection) {
                case RIGHT:
                    game.getSpriteBatch().draw(
                            game.getEnemy3RightAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case LEFT:
                    game.getSpriteBatch().draw(
                            game.getEnemy3LeftAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case UP:
                    game.getSpriteBatch().draw(
                            game.getEnemy3UpAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case DOWN:
                    game.getSpriteBatch().draw(
                            game.getEnemy3DownAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case IDLE_RIGHT:
                    game.getSpriteBatch().draw(
                            game.getEnemy3IdleRightAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case IDLE_LEFT:
                    game.getSpriteBatch().draw(
                            game.getEnemy3IdleLeftAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case IDLE_UP:
                    game.getSpriteBatch().draw(
                            game.getEnemy3IdleUpAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
                case IDLE_DOWN:
                    game.getSpriteBatch().draw(
                            game.getEnemy3IdleDownAnimation().getKeyFrame(sinusInput, true),
                            enemy.getX(), enemy.getY(),
                            enemyWidth, enemyHeight
                    );
                    break;
            }

                break;
            case 4:
                switch (currentDirection) {
                    case RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy4RightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy4LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy4UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy4DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy4IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy4IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy4IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy4IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                }
                break;
            case 5:
                switch (currentDirection) {
                    case RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy5RightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy5LeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy5UpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy5DownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_RIGHT:
                        game.getSpriteBatch().draw(
                                game.getEnemy5IdleRightAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_LEFT:
                        game.getSpriteBatch().draw(
                                game.getEnemy5IdleLeftAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_UP:
                        game.getSpriteBatch().draw(
                                game.getEnemy5IdleUpAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                    case IDLE_DOWN:
                        game.getSpriteBatch().draw(
                                game.getEnemy5IdleDownAnimation().getKeyFrame(sinusInput, true),
                                enemy.getX(), enemy.getY(),
                                enemyWidth, enemyHeight
                        );
                        break;
                }
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
                    characterWidth, characterHeight
            );
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterPickupDownAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterPickupLeftAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterPickupRightAnimation().getKeyFrame(pickUpTimer, false),
                    characterX, characterY,
                    characterWidth, characterHeight
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
                    characterWidth, characterHeight
            );
            case DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterHoldDownAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldLeftAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldRightAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case IDLE_UP -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleUpAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleDownAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleLeftAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
            );
            case IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterHoldIdleRightAnimation().getKeyFrame(sinusInput, false),
                    characterX, characterY,
                    characterWidth, characterHeight
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
                    characterX - 16, characterY,
                    characterHeight, characterHeight
            );
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(
                    game.getCharacterAttackDownAnimation().getKeyFrame(attackTimer, true),
                    characterX - 16 , characterY,
                    characterHeight, characterHeight
            );
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(
                    game.getCharacterAttackLeftAnimation().getKeyFrame(attackTimer, true),
                    characterX - 16, characterY,
                    characterHeight, characterHeight
            );
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(
                    game.getCharacterAttackRightAnimation().getKeyFrame(attackTimer, true),
                    characterX - 16, characterY,
                    characterHeight, characterHeight
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
                            characterWidth, characterHeight
                    );
            case DOWN ->
                    game.getSpriteBatch().draw(
                            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case LEFT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case RIGHT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case IDLE_UP ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case IDLE_DOWN ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case IDLE_LEFT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
            case IDLE_RIGHT ->
                    game.getSpriteBatch().draw(
                            game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true),
                            characterX, characterY,
                            characterWidth, characterHeight
                    );
        }
    }
    private boolean isCollidingWithEnemy(Vector2 characterPos, Vector2 enemyPos) {
        float attackRange = 50f;
        return characterPos.dst(enemyPos) <= attackRange;
    }

    private void handleAttack(Vector2 characterPos) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.isDead() && isCollidingWithEnemy(characterPos, new Vector2(enemy.getX(), enemy.getY()))) {
                enemy.reduceHealth();
                if (enemy.isDead()) {
                    iterator.remove();
                }
            }
        }
    }
    private void renderEnemies() {
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                renderEnemy(enemy);
            }
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

            Rectangle enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), 64, 64);
            Rectangle characterBounds = new Rectangle(characterX, characterY, 64, 128);
            if (Intersector.overlaps(enemyBounds, characterBounds)) {
                reduceHealth(DAMAGE_AMOUNT);
            }
        }
    }

    void checkItemCollisions() {
        Rectangle characterBounds = new Rectangle(characterX, characterY, 64, 128);
        for (Item item : items) {
            if (!item.collected && !tutorialIsActive) {
                Rectangle itemBounds = new Rectangle(item.x, item.y, 32, 32);
                if (Intersector.overlaps(itemBounds, characterBounds)) {
                    collectSound.play();
                    // Handle the collision
                    switch (item.type) {
                        case HEART -> {
                            restoreHealth(HEAL_AMOUNT);
                            item.collected = true;
                        }
                        case COIN -> {
                            coinCount++;
                            item.collected = true;
                        }
                        case FIRE -> {

                            reduceHealth(DAMAGE_AMOUNT * 2);
                            // if u don't want fire to go away after the burn remove this line:
//                            item.collected = true;
                        }
                        case ACCELARATION -> {
                            characterSpeed = 200f;
                            isaccelarationActive = true;
                            accelarationEndTime = System.currentTimeMillis() + 500;
                            item.collected = true;
                        }
                        case KEY -> {
                            // Actually collect the key here
                            keyCollected = true;
                            item.collected = true;


                            if (!isPortalActive) {
                                spawnPortal();
                            }
                        }

                        case BLACKHOLE -> {
                            // 吸引角色到黑洞中心
                            float blackholeCenterX = item.x ;
                            float blackholeCenterY = item.y ;
                            Vector2 direction = new Vector2(blackholeCenterX - characterX, blackholeCenterY - characterY).nor();
                            characterX += direction.x * 152 * Gdx.graphics.getDeltaTime(); // 吸引速度
                            characterY += direction.y * 152 * Gdx.graphics.getDeltaTime();

                            // 每次被吸引减少5点生命值
                            reduceHealth(2);

                            // 黑洞持续时间
                            if (item.collected) {
                                item.collected = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkHeartCollision() {
        RectangleMapObject heartObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("heart");
        if (heartObject != null) {
            Rectangle heartBounds = heartObject.getRectangle();
            Rectangle characterBounds = new Rectangle(characterX, characterY, 64, 128);
            if (Intersector.overlaps(heartBounds, characterBounds)) {
                restoreHealth(HEAL_AMOUNT);
                tiledMap.getLayers().get("Objects").getObjects().remove(heartObject);
            }
        }
    }

    private void reduceHealth(int amount) {
        if (!isCharacterRed && !tutorialIsActive) {
            characterHealth -= amount;
            if (characterHealth <= 0) {
                characterHealth = 0;
                onCharacterDeath();
            }
            isCharacterRed = true;
        }
    }

    private void spawnPortal() {
        // Put portal near bottom-right corner
        portalX = mapBounds.width  - PORTAL_WIDTH  - 40f;
        portalY =  40f;  // a bit above the bottom edge
        isPortalActive = true;
    }

    private void checkPortalCollision() {
        if (!isPortalActive) return;       // nothing to check if no portal
        if (isGameWon || isGameOver) return; // skip if game is over or won

        Rectangle portalBounds    = new Rectangle(portalX, portalY, PORTAL_WIDTH, PORTAL_HEIGHT);
        Rectangle characterBounds = new Rectangle(characterX, characterY, 64, 128);

        if (Intersector.overlaps(portalBounds, characterBounds)) {
            // The player steps onto the portal => YOU WIN!
            isGameWon = true;
            victorySound.play();
            winStage = menuScreen.createWinMenu(coinCount);

        }
    }



    private void restoreHealth(int amount) {
        characterHealth += amount;
        if (characterHealth > MAX_HEALTH) {
            characterHealth = MAX_HEALTH;
        }
    }

    private void onCharacterDeath() {
        deathSound.play();
        System.out.println("Game Over");
        isGameOver = true;
        gameOverTimer = 0;
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
        hud.resize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() {
        gameStage = new Stage(new ScreenViewport());
        pauseStage = menuScreen.createPauseMenu();
        loseStage=menuScreen.createGameoverMenu();
        Gdx.input.setInputProcessor(gameStage);
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
        hurtSound.play(1.0f); // 播放声音并设置音量
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("death.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("victory.mp3"));
        fasterSound = Gdx.audio.newSound(Gdx.files.internal("faster.mp3"));

    }
    public void hide() {

    }
    @Override
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
        if (pauseStage != null) {
            pauseStage.dispose();}
        hud.dispose();
        if (collectSound != null) collectSound.dispose();
        if (hurtSound != null) hurtSound.dispose();
        if (deathSound != null) deathSound.dispose();
        if (victorySound != null) victorySound.dispose();
        if (loseStage != null) {
            loseStage.dispose();
        }
        if (fasterSound != null) fasterSound.dispose();
    }
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * 敌人类，用于存储敌人的位置和速度。
     */
    private static class Enemy {
        private float x;
        private float y;
        private final float speed;
        private final int type;
        private int health;
        private boolean isDead;



        public Enemy(float x, float y, float speed, int type) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.type = type;
            this.health = 5;
            this.isDead = false;
        }
        public void reduceHealth() {
            if (!isDead) {
                health--;
                if (health <= 0) {
                    isDead = true;
                }
            }
        }

        public boolean isDead() {
            return isDead;
        }

        public void updatePosition(float delta) {
            this.x -= speed * delta;  // 向左移动

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



    // 1) Check if placing an item at (itemX, itemY) would collide with walls,
//   given the item is ITEM_SIZE x ITEM_SIZE in pixel dimensions.
    private boolean isItemCollidingWithWalls(float itemX, float itemY) {
        final float ITEM_SIZE = 32; // same as you draw items (32×32)

        // Check the four corners of the item’s bounding box
        if (isTileBlockedInMaze(itemX,        itemY))         return true;
        if (isTileBlockedInMaze(itemX + ITEM_SIZE - 1, itemY))         return true;
        if (isTileBlockedInMaze(itemX,        itemY + ITEM_SIZE - 1)) return true;
        if (isTileBlockedInMaze(itemX + ITEM_SIZE - 1, itemY + ITEM_SIZE - 1)) return true;

        return false;
    }

    // 2) Check if (itemX, itemY) is too close to already placed items.
    private boolean isTooCloseToOtherItems(float itemX, float itemY) {
        final float MIN_DISTANCE = 128f; // e.g., 64px = 4 tiles if tile is 16px

        for (Item existing : items) {
            if (!existing.collected) {
                float dx = existing.x - itemX;
                float dy = existing.y - itemY;
                float distSq = dx * dx + dy * dy;
                if (distSq < MIN_DISTANCE * MIN_DISTANCE) {
                    return true; // too close to another item
                }
            }
        }
        return false;
    }

}

