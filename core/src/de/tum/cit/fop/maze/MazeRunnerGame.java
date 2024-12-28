package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;


    // Movement
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    // Idle
    private Animation<TextureRegion> characterIdleUpAnimation;
    private Animation<TextureRegion> characterIdleDownAnimation;
    private Animation<TextureRegion> characterIdleLeftAnimation;
    private Animation<TextureRegion> characterIdleRightAnimation;
    // Pick Up
    private Animation<TextureRegion> characterPickupDownAnimation;
    private Animation<TextureRegion> characterPickupUpAnimation;
    private Animation<TextureRegion> characterPickupRightAnimation;
    private Animation<TextureRegion> characterPickupLeftAnimation;
    // Hold
    private Animation<TextureRegion> characterHoldDownAnimation;
    private Animation<TextureRegion> characterHoldUpAnimation;
    private Animation<TextureRegion> characterHoldRightAnimation;
    private Animation<TextureRegion> characterHoldLeftAnimation;
    private Animation<TextureRegion> characterHoldIdleUpAnimation;
    private Animation<TextureRegion> characterHoldIdleDownAnimation;
    private Animation<TextureRegion> characterHoldIdleLeftAnimation;
    private Animation<TextureRegion> characterHoldIdleRightAnimation;
    // Attack
    private Animation<TextureRegion> characterAttackDownAnimation;
    private Animation<TextureRegion> characterAttackUpAnimation;
    private Animation<TextureRegion> characterAttackRightAnimation;
    private Animation<TextureRegion> characterAttackLeftAnimation;


    // Enemy Animations

    private Animation<TextureRegion> enemy1DownAnimation;
    private Animation<TextureRegion> enemy1UpAnimation;
    private Animation<TextureRegion> enemy1RightAnimation;
    private Animation<TextureRegion> enemy1LeftAnimation;
    private Animation<TextureRegion> enemy1IdleUpAnimation;
    private Animation<TextureRegion> enemy1IdleDownAnimation;
    private Animation<TextureRegion> enemy1IdleLeftAnimation;
    private Animation<TextureRegion> enemy1IdleRightAnimation;

    private Animation<TextureRegion> enemy2DownAnimation;
    private Animation<TextureRegion> enemy2UpAnimation;
    private Animation<TextureRegion> enemy2RightAnimation;
    private Animation<TextureRegion> enemy2LeftAnimation;
    private Animation<TextureRegion> enemy2IdleUpAnimation;
    private Animation<TextureRegion> enemy2IdleDownAnimation;
    private Animation<TextureRegion> enemy2IdleLeftAnimation;
    private Animation<TextureRegion> enemy2IdleRightAnimation;

    private Animation<TextureRegion> enemy3DownAnimation;
    private Animation<TextureRegion> enemy3UpAnimation;
    private Animation<TextureRegion> enemy3RightAnimation;
    private Animation<TextureRegion> enemy3LeftAnimation;
    private Animation<TextureRegion> enemy3IdleUpAnimation;
    private Animation<TextureRegion> enemy3IdleDownAnimation;
    private Animation<TextureRegion> enemy3IdleLeftAnimation;
    private Animation<TextureRegion> enemy3IdleRightAnimation;

    private Animation<TextureRegion> enemy4DownAnimation;
    private Animation<TextureRegion> enemy4UpAnimation;
    private Animation<TextureRegion> enemy4RightAnimation;
    private Animation<TextureRegion> enemy4LeftAnimation;
    private Animation<TextureRegion> enemy4IdleUpAnimation;
    private Animation<TextureRegion> enemy4IdleDownAnimation;
    private Animation<TextureRegion> enemy4IdleLeftAnimation;
    private Animation<TextureRegion> enemy4IdleRightAnimation;

    private Animation<TextureRegion> enemy5DownAnimation;
    private Animation<TextureRegion> enemy5UpAnimation;
    private Animation<TextureRegion> enemy5RightAnimation;
    private Animation<TextureRegion> enemy5LeftAnimation;
    private Animation<TextureRegion> enemy5IdleUpAnimation;
    private Animation<TextureRegion> enemy5IdleDownAnimation;
    private Animation<TextureRegion> enemy5IdleLeftAnimation;
    private Animation<TextureRegion> enemy5IdleRightAnimation;

    private Animation<TextureRegion> heartAnimation;
    private Animation<TextureRegion> coinAnimation;
    private Animation<TextureRegion> fireAnimation;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        // Load all animations
        this.loadCharacterAnimation();
        this.loadEnemyAnimation();

        // Play some background music
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this));
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this));
        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file,
     * including move/idle/pickup/hold/attack frames.
     */
    private void loadObjectsAnimation() {
        Texture oSheet = new Texture(Gdx.files.internal("Objects.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int col = 4;


        Array<TextureRegion> heartFrames = new Array<>();
        Array<TextureRegion> coinFrames = new Array<>();
        Array<TextureRegion> fireFrames = new Array<>();


//heart
        heartFrames.add(new TextureRegion(oSheet, col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));

        heartAnimation = new Animation<>(0.1f, heartFrames);

//coin
        coinFrames.add(new TextureRegion(oSheet, col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));

        coinAnimation = new Animation<>(0.1f, coinFrames);

//fire
        fireFrames.add(new TextureRegion(oSheet, 48 + 7 * frameWidth, 4 * frameHeight, frameWidth, frameHeight));

        fireAnimation = new Animation<>(0.1f, fireFrames);


    }





        private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        int attackFrameWidth = 32;
        int attackFrameHeight = 32;

        // Movement
        Array<TextureRegion> walkDownFrames  = new Array<>();
        Array<TextureRegion> walkRightFrames = new Array<>();
        Array<TextureRegion> walkLeftFrames  = new Array<>();
        Array<TextureRegion> walkUpFrames    = new Array<>();

        // Idle
        Array<TextureRegion> idleDownFrames  = new Array<>();
        Array<TextureRegion> idleUpFrames    = new Array<>();
        Array<TextureRegion> idleLeftFrames  = new Array<>();
        Array<TextureRegion> idleRightFrames = new Array<>();

        // Pickup
        Array<TextureRegion> pickupDownFrames  = new Array<>();
        Array<TextureRegion> pickupUpFrames    = new Array<>();
        Array<TextureRegion> pickupRightFrames = new Array<>();
        Array<TextureRegion> pickupLeftFrames  = new Array<>();

        // Hold
        Array<TextureRegion> holdDownFrames     = new Array<>();
        Array<TextureRegion> holdRightFrames    = new Array<>();
        Array<TextureRegion> holdLeftFrames     = new Array<>();
        Array<TextureRegion> holdUpFrames       = new Array<>();
        Array<TextureRegion> holdIdleDownFrames = new Array<>();
        Array<TextureRegion> holdIdleUpFrames   = new Array<>();
        Array<TextureRegion> holdIdleLeftFrames = new Array<>();
        Array<TextureRegion> holdIdleRightFrames= new Array<>();

        // Attack
        Array<TextureRegion> attackDownFrames  = new Array<>();
        Array<TextureRegion> attackUpFrames    = new Array<>();
        Array<TextureRegion> attackRightFrames = new Array<>();
        Array<TextureRegion> attackLeftFrames  = new Array<>();


        // Movement Animations

        // walk down
        for (int col = 0; col < animationFrames; col++) {
            walkDownFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }
        characterDownAnimation = new Animation<>(0.1f, walkDownFrames);

        // walk right
        for (int col = 0; col < animationFrames; col++) {
            walkRightFrames.add(new TextureRegion(walkSheet, col * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }
        characterRightAnimation = new Animation<>(0.1f, walkRightFrames);

        // walk left
        for (int col = 0; col < animationFrames; col++) {
            walkLeftFrames.add(new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        characterLeftAnimation = new Animation<>(0.1f, walkLeftFrames);

        // walk up
        for (int col = 0; col < animationFrames; col++) {
            walkUpFrames.add(new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        characterUpAnimation = new Animation<>(0.1f, walkUpFrames);

        // -------------------------
        // Idle Animations
        // -------------------------
        // idle down
        idleDownFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 0, frameWidth, frameHeight));
        characterIdleDownAnimation = new Animation<>(0.1f, idleDownFrames);

        // idle up
        idleUpFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        characterIdleUpAnimation = new Animation<>(0.1f, idleUpFrames);

        // idle left
        idleLeftFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        characterIdleLeftAnimation = new Animation<>(0.1f, idleLeftFrames);

        // idle right
        idleRightFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        characterIdleRightAnimation = new Animation<>(0.1f, idleRightFrames);


        // Pickup Animations

        // pickup down
        for (int col = 0; col < 3; col++) {
            pickupDownFrames.add(new TextureRegion(walkSheet, 80 + col * frameWidth, 0, frameWidth, frameHeight));
        }
        characterPickupDownAnimation = new Animation<>(0.1f, pickupDownFrames);

        // pickup right
        for (int col = 0; col < 3; col++) {
            pickupRightFrames.add(new TextureRegion(walkSheet, 80 + col * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }
        characterPickupRightAnimation = new Animation<>(0.1f, pickupRightFrames);

        // pickup up
        for (int col = 0; col < 3; col++) {
            pickupUpFrames.add(new TextureRegion(walkSheet, 80 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        characterPickupUpAnimation = new Animation<>(0.1f, pickupUpFrames);

        // pickup left
        for (int col = 0; col < 3; col++) {
            pickupLeftFrames.add(new TextureRegion(walkSheet, 80 + col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        characterPickupLeftAnimation = new Animation<>(0.1f, pickupLeftFrames);


        // Hold Animations

        // hold down
        for (int col = 0; col < animationFrames; col++) {
            holdDownFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 0, frameWidth, frameHeight));
        }
        characterHoldDownAnimation = new Animation<>(0.1f, holdDownFrames);

        // hold right
        for (int col = 0; col < animationFrames; col++) {
            holdRightFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }
        characterHoldRightAnimation = new Animation<>(0.1f, holdRightFrames);

        // hold up
        for (int col = 0; col < animationFrames; col++) {
            holdUpFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        characterHoldUpAnimation = new Animation<>(0.1f, holdUpFrames);

        // hold left
        for (int col = 0; col < animationFrames; col++) {
            holdLeftFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        characterHoldLeftAnimation = new Animation<>(0.1f, holdLeftFrames);

        // hold idle down
        holdIdleDownFrames.add(new TextureRegion(walkSheet, 144, 0, frameWidth, frameHeight));
        characterHoldIdleDownAnimation = new Animation<>(0.1f, holdIdleDownFrames);

        // hold idle right
        holdIdleRightFrames.add(new TextureRegion(walkSheet, 144, 1 * frameHeight, frameWidth, frameHeight));
        characterHoldIdleRightAnimation = new Animation<>(0.1f, holdIdleRightFrames);

        // hold idle up
        holdIdleUpFrames.add(new TextureRegion(walkSheet, 144, 2 * frameHeight, frameWidth, frameHeight));
        characterHoldIdleUpAnimation = new Animation<>(0.1f, holdIdleUpFrames);

        // hold idle left
        holdIdleLeftFrames.add(new TextureRegion(walkSheet, 144, 3 * frameHeight, frameWidth, frameHeight));
        characterHoldIdleLeftAnimation = new Animation<>(0.1f, holdIdleLeftFrames);


        // Attack Animations

        // attack down
        for (int col = 0; col < animationFrames; col++) {
            attackDownFrames.add(new TextureRegion(
                    walkSheet,
                    col * attackFrameWidth,
                    4 * attackFrameHeight,
                    attackFrameWidth,
                    attackFrameHeight
            ));
        }
        characterAttackDownAnimation = new Animation<>(0.1f, attackDownFrames);

        // attack up
        for (int col = 0; col < animationFrames; col++) {
            attackUpFrames.add(new TextureRegion(
                    walkSheet,
                    col * attackFrameWidth,
                    5 * attackFrameHeight,
                    attackFrameWidth,
                    attackFrameHeight
            ));
        }
        characterAttackUpAnimation = new Animation<>(0.1f, attackUpFrames);

        // attack right
        for (int col = 0; col < animationFrames; col++) {
            attackRightFrames.add(new TextureRegion(
                    walkSheet,
                    col * attackFrameWidth,
                    6 * attackFrameHeight,
                    attackFrameWidth,
                    attackFrameHeight
            ));
        }
        characterAttackRightAnimation = new Animation<>(0.1f, attackRightFrames);

        // attack left
        for (int col = 0; col < animationFrames; col++) {
            attackLeftFrames.add(new TextureRegion(
                    walkSheet,
                    col * attackFrameWidth,
                    7 * attackFrameHeight,
                    attackFrameWidth,
                    attackFrameHeight
            ));
        }
        characterAttackLeftAnimation = new Animation<>(0.1f, attackLeftFrames);
    }

    /**
     * Loads enemy animations (enemy1... enemy5) from mobs.png.
     */
    private void loadEnemyAnimation() {
        Texture enemySheet = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        // -------------------------
        // Enemy1
        // -------------------------
        Array<TextureRegion> e1DownFrames  = new Array<>();
        Array<TextureRegion> e1RightFrames = new Array<>();
        Array<TextureRegion> e1LeftFrames  = new Array<>();
        Array<TextureRegion> e1UpFrames    = new Array<>();
        Array<TextureRegion> e1IdleDownFrames  = new Array<>();
        Array<TextureRegion> e1IdleUpFrames    = new Array<>();
        Array<TextureRegion> e1IdleLeftFrames  = new Array<>();
        Array<TextureRegion> e1IdleRightFrames = new Array<>();

        // walk down
        for (int col = 0; col < animationFrames; col++) {
            e1DownFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 0, frameWidth, frameHeight));
        }
        enemy1DownAnimation = new Animation<>(0.1f, e1DownFrames);

        // walk right
        for (int col = 0; col < animationFrames; col++) {
            e1RightFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        enemy1RightAnimation = new Animation<>(0.1f, e1RightFrames);

        // walk left
        for (int col = 0; col < animationFrames; col++) {
            e1LeftFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }
        enemy1LeftAnimation = new Animation<>(0.1f, e1LeftFrames);

        // walk up
        for (int col = 0; col < animationFrames; col++) {
            e1UpFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        enemy1UpAnimation = new Animation<>(0.1f, e1UpFrames);

        // idle_down
        e1IdleDownFrames.add(new TextureRegion(enemySheet, 144, 0, frameWidth, frameHeight));
        enemy1IdleDownAnimation = new Animation<>(0.1f, e1IdleDownFrames);

        // idle_up
        e1IdleUpFrames.add(new TextureRegion(enemySheet, 144, 3 * frameHeight, frameWidth, frameHeight));
        enemy1IdleUpAnimation = new Animation<>(0.1f, e1IdleUpFrames);

        // idle_left
        e1IdleLeftFrames.add(new TextureRegion(enemySheet, 144, 1 * frameHeight, frameWidth, frameHeight));
        enemy1IdleLeftAnimation = new Animation<>(0.1f, e1IdleLeftFrames);

        // idle_right
        e1IdleRightFrames.add(new TextureRegion(enemySheet, 144, 2 * frameHeight, frameWidth, frameHeight));
        enemy1IdleRightAnimation = new Animation<>(0.1f, e1IdleRightFrames);


        // Enemy2

        Array<TextureRegion> e2DownFrames  = new Array<>();
        Array<TextureRegion> e2RightFrames = new Array<>();
        Array<TextureRegion> e2LeftFrames  = new Array<>();
        Array<TextureRegion> e2UpFrames    = new Array<>();
        Array<TextureRegion> e2IdleDownFrames  = new Array<>();
        Array<TextureRegion> e2IdleUpFrames    = new Array<>();
        Array<TextureRegion> e2IdleLeftFrames  = new Array<>();
        Array<TextureRegion> e2IdleRightFrames = new Array<>();

        // walk down
        for (int col = 0; col < animationFrames; col++) {
            e2DownFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }
        enemy2DownAnimation = new Animation<>(0.1f, e2DownFrames);

        // walk right
        for (int col = 0; col < animationFrames; col++) {
            e2RightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameHeight, frameWidth, frameHeight));
        }
        enemy2RightAnimation = new Animation<>(0.1f, e2RightFrames);

        // walk left
        for (int col = 0; col < animationFrames; col++) {
            e2LeftFrames.add(new TextureRegion(enemySheet, col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }
        enemy2LeftAnimation = new Animation<>(0.1f, e2LeftFrames);

        // walk up
        for (int col = 0; col < animationFrames; col++) {
            e2UpFrames.add(new TextureRegion(enemySheet, col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }
        enemy2UpAnimation = new Animation<>(0.1f, e2UpFrames);

        // idle_down
        e2IdleDownFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        enemy2IdleDownAnimation = new Animation<>(0.1f, e2IdleDownFrames);

        // idle_up
        e2IdleUpFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        enemy2IdleUpAnimation = new Animation<>(0.1f, e2IdleUpFrames);

        // idle_left
        e2IdleLeftFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        enemy2IdleLeftAnimation = new Animation<>(0.1f, e2IdleLeftFrames);

        // idle_right
        e2IdleRightFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        enemy2IdleRightAnimation = new Animation<>(0.1f, e2IdleRightFrames);

        // -------------------------
        // Enemy3
        // -------------------------
        Array<TextureRegion> e3DownFrames  = new Array<>();
        Array<TextureRegion> e3RightFrames = new Array<>();
        Array<TextureRegion> e3LeftFrames  = new Array<>();
        Array<TextureRegion> e3UpFrames    = new Array<>();
        Array<TextureRegion> e3IdleDownFrames  = new Array<>();
        Array<TextureRegion> e3IdleUpFrames    = new Array<>();
        Array<TextureRegion> e3IdleLeftFrames  = new Array<>();
        Array<TextureRegion> e3IdleRightFrames = new Array<>();


        for (int col = 0; col < animationFrames; col++) {
            e3DownFrames.add(new TextureRegion(enemySheet, 48+col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }
        enemy3DownAnimation = new Animation<>(0.1f, e3DownFrames);

        for (int col = 0; col < animationFrames; col++) {
            e3RightFrames.add(new TextureRegion(enemySheet, 48+col * frameWidth, 6 * frameHeight, frameWidth, frameHeight));
        }
        enemy3RightAnimation = new Animation<>(0.1f, e3RightFrames);

        for (int col = 0; col < animationFrames; col++) {
            e3LeftFrames.add(new TextureRegion(enemySheet, 48+col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }
        enemy3LeftAnimation = new Animation<>(0.1f, e3LeftFrames);

        for (int col = 0; col < animationFrames; col++) {
            e3UpFrames.add(new TextureRegion(enemySheet, 48+col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }
        enemy3UpAnimation = new Animation<>(0.1f, e3UpFrames);

        e3IdleDownFrames.add(new TextureRegion(enemySheet, 48, 4 * frameHeight, frameWidth, frameHeight));
        enemy3IdleDownAnimation = new Animation<>(0.1f, e3IdleDownFrames);

        e3IdleUpFrames.add(new TextureRegion(enemySheet, 48, 7 * frameHeight, frameWidth, frameHeight));
        enemy3IdleUpAnimation = new Animation<>(0.1f, e3IdleUpFrames);

        e3IdleLeftFrames.add(new TextureRegion(enemySheet, 48, 5 * frameHeight, frameWidth, frameHeight));
        enemy3IdleLeftAnimation = new Animation<>(0.1f, e3IdleLeftFrames);

        e3IdleRightFrames.add(new TextureRegion(enemySheet, 48, 6 * frameHeight, frameWidth, frameHeight));
        enemy3IdleRightAnimation = new Animation<>(0.1f, e3IdleRightFrames);


        // Enemy4

        Array<TextureRegion> e4DownFrames  = new Array<>();
        Array<TextureRegion> e4RightFrames = new Array<>();
        Array<TextureRegion> e4LeftFrames  = new Array<>();
        Array<TextureRegion> e4UpFrames    = new Array<>();
        Array<TextureRegion> e4IdleDownFrames  = new Array<>();
        Array<TextureRegion> e4IdleUpFrames    = new Array<>();
        Array<TextureRegion> e4IdleLeftFrames  = new Array<>();
        Array<TextureRegion> e4IdleRightFrames = new Array<>();

        for (int col = 0; col < animationFrames; col++) {
            e4DownFrames.add(new TextureRegion(enemySheet, 96 + col * frameWidth, 4*frameHeight, frameWidth, frameHeight));
        }
        enemy4DownAnimation = new Animation<>(0.1f, e4DownFrames);

        for (int col = 0; col < animationFrames; col++) {
            e4RightFrames.add(new TextureRegion(enemySheet, 96 + col * frameWidth, 6* frameHeight, frameWidth, frameHeight));
        }
        enemy4RightAnimation = new Animation<>(0.1f, e4RightFrames);

        for (int col = 0; col < animationFrames; col++) {
            e4LeftFrames.add(new TextureRegion(enemySheet, 96 + col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }
        enemy4LeftAnimation = new Animation<>(0.1f, e4LeftFrames);

        for (int col = 0; col < animationFrames; col++) {
            e4UpFrames.add(new TextureRegion(enemySheet, 96 + col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }
        enemy4UpAnimation = new Animation<>(0.1f, e4UpFrames);

        e4IdleDownFrames.add(new TextureRegion(enemySheet, 96, 4*frameHeight, frameWidth, frameHeight));
        enemy4IdleDownAnimation = new Animation<>(0.1f, e4IdleDownFrames);

        e4IdleUpFrames.add(new TextureRegion(enemySheet, 96, 7* frameHeight, frameWidth, frameHeight));
        enemy4IdleUpAnimation = new Animation<>(0.1f, e4IdleUpFrames);

        e4IdleLeftFrames.add(new TextureRegion(enemySheet, 96, 5 * frameHeight, frameWidth, frameHeight));
        enemy4IdleLeftAnimation = new Animation<>(0.1f, e4IdleLeftFrames);

        e4IdleRightFrames.add(new TextureRegion(enemySheet, 96, 6* frameHeight, frameWidth, frameHeight));
        enemy4IdleRightAnimation = new Animation<>(0.1f, e4IdleRightFrames);

        // -------------------------
        // Enemy5
        // -------------------------
        Array<TextureRegion> e5DownFrames  = new Array<>();
        Array<TextureRegion> e5RightFrames = new Array<>();
        Array<TextureRegion> e5LeftFrames  = new Array<>();
        Array<TextureRegion> e5UpFrames    = new Array<>();
        Array<TextureRegion> e5IdleDownFrames  = new Array<>();
        Array<TextureRegion> e5IdleUpFrames    = new Array<>();
        Array<TextureRegion> e5IdleLeftFrames  = new Array<>();
        Array<TextureRegion> e5IdleRightFrames = new Array<>();

        for (int col = 0; col < animationFrames; col++) {
            e5DownFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 4*frameHeight, frameWidth, frameHeight));
        }
        enemy5DownAnimation = new Animation<>(0.1f, e5DownFrames);

        for (int col = 0; col < animationFrames; col++) {
            e5RightFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 6* frameHeight, frameWidth, frameHeight));
        }
        enemy5RightAnimation = new Animation<>(0.1f, e5RightFrames);

        for (int col = 0; col < animationFrames; col++) {
            e5LeftFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }
        enemy5LeftAnimation = new Animation<>(0.1f, e5LeftFrames);

        for (int col = 0; col < animationFrames; col++) {
            e5UpFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }
        enemy5UpAnimation = new Animation<>(0.1f, e5UpFrames);

        e5IdleDownFrames.add(new TextureRegion(enemySheet, 144, 4*frameHeight, frameWidth, frameHeight));
        enemy5IdleDownAnimation = new Animation<>(0.1f, e5IdleDownFrames);

        e5IdleUpFrames.add(new TextureRegion(enemySheet, 144, 7 * frameHeight, frameWidth, frameHeight));
        enemy5IdleUpAnimation = new Animation<>(0.1f, e5IdleUpFrames);

        e5IdleLeftFrames.add(new TextureRegion(enemySheet, 144, 5 * frameHeight, frameWidth, frameHeight));
        enemy5IdleLeftAnimation = new Animation<>(0.1f, e5IdleLeftFrames);

        e5IdleRightFrames.add(new TextureRegion(enemySheet, 144, 6* frameHeight, frameWidth, frameHeight));
        enemy5IdleRightAnimation = new Animation<>(0.1f, e5IdleRightFrames);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide();
        getScreen().dispose();
        spriteBatch.dispose();
        skin.dispose();
    }

    // ------------------------------------------------
    // Getter methods
    // ------------------------------------------------
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    // Movement
    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }
    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }
    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }
    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    // Idle
    public Animation<TextureRegion> getCharacterIdleUpAnimation() {
        return characterIdleUpAnimation;
    }
    public Animation<TextureRegion> getCharacterIdleDownAnimation() {
        return characterIdleDownAnimation;
    }
    public Animation<TextureRegion> getCharacterIdleLeftAnimation() {
        return characterIdleLeftAnimation;
    }
    public Animation<TextureRegion> getCharacterIdleRightAnimation() {
        return characterIdleRightAnimation;
    }

    // Pickup
    public Animation<TextureRegion> getCharacterPickupDownAnimation() {
        return characterPickupDownAnimation;
    }
    public Animation<TextureRegion> getCharacterPickupUpAnimation() {
        return characterPickupUpAnimation;
    }
    public Animation<TextureRegion> getCharacterPickupRightAnimation() {
        return characterPickupRightAnimation;
    }
    public Animation<TextureRegion> getCharacterPickupLeftAnimation() {
        return characterPickupLeftAnimation;
    }

    // Hold
    public Animation<TextureRegion> getCharacterHoldDownAnimation() {
        return characterHoldDownAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldUpAnimation() {
        return characterHoldUpAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldRightAnimation() {
        return characterHoldRightAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldLeftAnimation() {
        return characterHoldLeftAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldIdleUpAnimation() {
        return characterHoldIdleUpAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldIdleDownAnimation() {
        return characterHoldIdleDownAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldIdleLeftAnimation() {
        return characterHoldIdleLeftAnimation;
    }
    public Animation<TextureRegion> getCharacterHoldIdleRightAnimation() {
        return characterHoldIdleRightAnimation;
    }

    // Attack
    public Animation<TextureRegion> getCharacterAttackDownAnimation() {
        return characterAttackDownAnimation;
    }
    public Animation<TextureRegion> getCharacterAttackUpAnimation() {
        return characterAttackUpAnimation;
    }
    public Animation<TextureRegion> getCharacterAttackRightAnimation() {
        return characterAttackRightAnimation;
    }
    public Animation<TextureRegion> getCharacterAttackLeftAnimation() {
        return characterAttackLeftAnimation;
    }

    // Enemy1
    public Animation<TextureRegion> getEnemy1DownAnimation() {
        return enemy1DownAnimation;
    }
    public Animation<TextureRegion> getEnemy1UpAnimation() {
        return enemy1UpAnimation;
    }
    public Animation<TextureRegion> getEnemy1RightAnimation() {
        return enemy1RightAnimation;
    }
    public Animation<TextureRegion> getEnemy1LeftAnimation() {
        return enemy1LeftAnimation;
    }
    public Animation<TextureRegion> getEnemy1IdleUpAnimation() {
        return enemy1IdleUpAnimation;
    }
    public Animation<TextureRegion> getEnemy1IdleDownAnimation() {
        return enemy1IdleDownAnimation;
    }
    public Animation<TextureRegion> getEnemy1IdleLeftAnimation() {
        return enemy1IdleLeftAnimation;
    }
    public Animation<TextureRegion> getEnemy1IdleRightAnimation() {
        return enemy1IdleRightAnimation;
    }

    // Enemy2
    public Animation<TextureRegion> getEnemy2DownAnimation() {
        return enemy2DownAnimation;
    }
    public Animation<TextureRegion> getEnemy2UpAnimation() {
        return enemy2UpAnimation;
    }
    public Animation<TextureRegion> getEnemy2RightAnimation() {
        return enemy2RightAnimation;
    }
    public Animation<TextureRegion> getEnemy2LeftAnimation() {
        return enemy2LeftAnimation;
    }
    public Animation<TextureRegion> getEnemy2IdleUpAnimation() {
        return enemy2IdleUpAnimation;
    }
    public Animation<TextureRegion> getEnemy2IdleDownAnimation() {
        return enemy2IdleDownAnimation;
    }
    public Animation<TextureRegion> getEnemy2IdleLeftAnimation() {
        return enemy2IdleLeftAnimation;
    }
    public Animation<TextureRegion> getEnemy2IdleRightAnimation() {
        return enemy2IdleRightAnimation;
    }

    // Enemy3
    public Animation<TextureRegion> getEnemy3DownAnimation() {
        return enemy3DownAnimation;
    }

    public Animation<TextureRegion> getEnemy3UpAnimation() {
        return enemy3UpAnimation;
    }
    public Animation<TextureRegion> getEnemy3RightAnimation() {
        return enemy3RightAnimation;
    }
    public Animation<TextureRegion> getEnemy3LeftAnimation() {
        return enemy3LeftAnimation;
    }
    public Animation<TextureRegion> getEnemy3IdleUpAnimation() {
        return enemy3IdleUpAnimation;
    }
    public Animation<TextureRegion> getEnemy3IdleDownAnimation() {
        return enemy3IdleDownAnimation;
    }
    public Animation<TextureRegion> getEnemy3IdleLeftAnimation() {
        return enemy3IdleLeftAnimation;
    }
    public Animation<TextureRegion> getEnemy3IdleRightAnimation() {
        return enemy3IdleRightAnimation;
    }

    // Enemy4

    public Animation<TextureRegion> getEnemy4DownAnimation() {
        return enemy4DownAnimation;
    }
    public Animation<TextureRegion> getEnemy4UpAnimation() {
        return enemy4UpAnimation;
    }
    public Animation<TextureRegion> getEnemy4RightAnimation() {
        return enemy4RightAnimation;
    }
    public Animation<TextureRegion> getEnemy4LeftAnimation() {
        return enemy4LeftAnimation;
    }
    public Animation<TextureRegion> getEnemy4IdleUpAnimation() {
        return enemy4IdleUpAnimation;
    }
    public Animation<TextureRegion> getEnemy4IdleDownAnimation() {
        return enemy4IdleDownAnimation;
    }
    public Animation<TextureRegion> getEnemy4IdleLeftAnimation() {
        return enemy4IdleLeftAnimation;
    }
    public Animation<TextureRegion> getEnemy4IdleRightAnimation() {
        return enemy4IdleRightAnimation;
    }

    // Enemy5
  
    public Animation<TextureRegion> getEnemy5DownAnimation() {
        return enemy5DownAnimation;
    }
    public Animation<TextureRegion> getEnemy5UpAnimation() {
        return enemy5UpAnimation;
    }
    public Animation<TextureRegion> getEnemy5RightAnimation() {
        return enemy5RightAnimation;
    }
    public Animation<TextureRegion> getEnemy5LeftAnimation() {
        return enemy5LeftAnimation;
    }
    public Animation<TextureRegion> getEnemy5IdleUpAnimation() {
        return enemy5IdleUpAnimation;
    }
    public Animation<TextureRegion> getEnemy5IdleDownAnimation() {
        return enemy5IdleDownAnimation;
    }
    public Animation<TextureRegion> getEnemy5IdleLeftAnimation() {
        return enemy5IdleLeftAnimation;
    }
    public Animation<TextureRegion> getEnemy5IdleRightAnimation() {
        return enemy5IdleRightAnimation;
    }

    public Animation<TextureRegion> getHeartAnimation() {
        return heartAnimation;
    }

    public Animation<TextureRegion> getCoinAnimation() {
        return coinAnimation;
    }

    public Animation<TextureRegion> getFireAnimation() {
        return fireAnimation;
    }
}
