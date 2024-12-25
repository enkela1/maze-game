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

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterIdleUpAnimation;
    private Animation<TextureRegion> characterIdleDownAnimation;
    private Animation<TextureRegion> characterIdleLeftAnimation;
    private Animation<TextureRegion> characterIdleRightAnimation;


    //Enemy1 animation downloads
    private Animation<TextureRegion> enemy1DownAnimation;
    private Animation<TextureRegion> enemy1UpAnimation;
    private Animation<TextureRegion> enemy1RightAnimation;
    private Animation<TextureRegion> enemy1LeftAnimation;
    private Animation<TextureRegion> enemy1IdleUpAnimation;
    private Animation<TextureRegion> enemy1IdleDownAnimation;
    private Animation<TextureRegion> enemy1IdleLeftAnimation;
    private Animation<TextureRegion> enemy1IdleRightAnimation;

    //Enemy2 animation downloads
    private Animation<TextureRegion> enemy2DownAnimation;
    private Animation<TextureRegion> enemy2UpAnimation;
    private Animation<TextureRegion> enemy2RightAnimation;
    private Animation<TextureRegion> enemy2LeftAnimation;
    private Animation<TextureRegion> enemy2IdleUpAnimation;
    private Animation<TextureRegion> enemy2IdleDownAnimation;
    private Animation<TextureRegion> enemy2IdleLeftAnimation;
    private Animation<TextureRegion> enemy2IdleRightAnimation;

    //Enemy3 animation downloads
    private Animation<TextureRegion> enemy3DownAnimation;
    private Animation<TextureRegion> enemy3UpAnimation;
    private Animation<TextureRegion> enemy3RightAnimation;
    private Animation<TextureRegion> enemy3LeftAnimation;
    private Animation<TextureRegion> enemy3IdleUpAnimation;
    private Animation<TextureRegion> enemy3IdleDownAnimation;
    private Animation<TextureRegion> enemy3IdleLeftAnimation;
    private Animation<TextureRegion> enemy3IdleRightAnimation;

    //Enemy4 animation downloads
    private Animation<TextureRegion> enemy4DownAnimation;
    private Animation<TextureRegion> enemy4UpAnimation;
    private Animation<TextureRegion> enemy4RightAnimation;
    private Animation<TextureRegion> enemy4LeftAnimation;
    private Animation<TextureRegion> enemy4IdleUpAnimation;
    private Animation<TextureRegion> enemy4IdleDownAnimation;
    private Animation<TextureRegion> enemy4IdleLeftAnimation;
    private Animation<TextureRegion> enemy4IdleRightAnimation;

    //Enemy5 animation downloads
    private Animation<TextureRegion> enemy5DownAnimation;
    private Animation<TextureRegion> enemy5UpAnimation;
    private Animation<TextureRegion> enemy5RightAnimation;
    private Animation<TextureRegion> enemy5LeftAnimation;
    private Animation<TextureRegion> enemy5IdleUpAnimation;
    private Animation<TextureRegion> enemy5IdleDownAnimation;
    private Animation<TextureRegion> enemy5IdleLeftAnimation;
    private Animation<TextureRegion> enemy5IdleRightAnimation;


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
        this.loadCharacterAnimation(); // Load character animation
        this.loadEnemyAnimation();

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }


    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        Array<TextureRegion> walkRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleRightFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<>(0.1f, walkFrames);
        //walkright
        for (int col = 0; col < animationFrames; col++) {
            walkRightFrames.add(new TextureRegion(walkSheet, col * 1 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<>(0.1f, walkRightFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            walkLeftFrames.add(new TextureRegion(walkSheet, col * 1 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<>(0.1f, walkLeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            walkUpFrames.add(new TextureRegion(walkSheet, col * 1 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<>(0.1f, walkUpFrames);

        //idle_down

        walkIdleDownFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 0, frameWidth, frameHeight));

        characterIdleDownAnimation = new Animation<>(0.1f, walkIdleDownFrames);

        //idle_up

        walkIdleUpFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));


        characterIdleUpAnimation = new Animation<>(0.1f, walkIdleUpFrames);

        //idle_left

        walkIdleLeftFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));


        characterIdleLeftAnimation = new Animation<>(0.1f, walkIdleLeftFrames);

        //idle_right

        walkIdleRightFrames.add(new TextureRegion(walkSheet, 2 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));


        characterIdleRightAnimation = new Animation<>(0.1f, walkIdleRightFrames);


    }

    private void loadEnemyAnimation() {
        Texture enemySheet = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        //walk
        Array<TextureRegion> enemy1DownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1RightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1LeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1UpFrames = new Array<>(TextureRegion.class);
        //idle
        Array<TextureRegion> enemy1IdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1IdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1IdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy1IdleRightFrames = new Array<>(TextureRegion.class);

        //walk
        Array<TextureRegion> enemy2DownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2RightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2LeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2UpFrames = new Array<>(TextureRegion.class);
        //idle
        Array<TextureRegion> enemy2IdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2IdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2IdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy2IdleRightFrames = new Array<>(TextureRegion.class);

        //walk
        Array<TextureRegion> enemy3DownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3RightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3LeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3UpFrames = new Array<>(TextureRegion.class);
        //idle
        Array<TextureRegion> enemy3IdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3IdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3IdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy3IdleRightFrames = new Array<>(TextureRegion.class);

        //walk
        Array<TextureRegion> enemy4DownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4RightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4LeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4UpFrames = new Array<>(TextureRegion.class);
        //idle
        Array<TextureRegion> enemy4IdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4IdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4IdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy4IdleRightFrames = new Array<>(TextureRegion.class);

        //walk
        Array<TextureRegion> enemy5DownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5RightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5LeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5UpFrames = new Array<>(TextureRegion.class);
        //idle
        Array<TextureRegion> enemy5IdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5IdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5IdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemy5IdleRightFrames = new Array<>(TextureRegion.class);

        //1
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            enemy1DownFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 0, frameWidth, frameHeight));
        }

        enemy1DownAnimation = new Animation<>(0.1f, enemy1DownFrames);

        //walkright
        for (int col = 0; col < animationFrames; col++) {
            enemy1RightFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }

        enemy1RightAnimation = new Animation<>(0.1f, enemy1RightFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            enemy1LeftFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }

        enemy1LeftAnimation = new Animation<>(0.1f, enemy1LeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            enemy1UpFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }

        enemy1UpAnimation = new Animation<>(0.1f, enemy1UpFrames);

        //idle_down

        enemy1IdleDownFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 0, frameWidth, frameHeight));

        enemy1IdleDownAnimation = new Animation<>(0.1f, enemy1IdleDownFrames);

        //idle_up

        enemy1IdleUpFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));

        enemy1IdleUpAnimation = new Animation<>(0.1f, enemy1IdleUpFrames);

        //idle_left

        enemy1IdleLeftFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));


        enemy1IdleLeftAnimation = new Animation<>(0.1f, enemy1IdleLeftFrames);

        //idle_right

        enemy1IdleRightFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));


        enemy1IdleRightAnimation = new Animation<>(0.1f, enemy1IdleRightFrames);

        //2
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            enemy2DownFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }

        enemy2DownAnimation = new Animation<>(0.1f, enemy2DownFrames);
        //walkright
        for (int col = 0; col < animationFrames; col++) {
            enemy2RightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameHeight, frameWidth, frameHeight));
        }

        enemy2RightAnimation = new Animation<>(0.1f, enemy2RightFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            enemy2LeftFrames.add(new TextureRegion(enemySheet, col * 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }

        enemy2LeftAnimation = new Animation<>(0.1f, enemy2LeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            enemy2UpFrames.add(new TextureRegion(enemySheet, col * 1 * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }

        enemy2UpAnimation = new Animation<>(0.1f, enemy2UpFrames);

        //idle_down

        enemy2IdleDownFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 4 * frameHeight, frameWidth, frameHeight));

        enemy2IdleDownAnimation = new Animation<>(0.1f, enemy2IdleDownFrames);

        //idle_up

        enemy2IdleUpFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 7 * frameHeight, frameWidth, frameHeight));


        enemy2IdleUpAnimation = new Animation<>(0.1f, enemy2IdleUpFrames);

        //idle_left

        enemy2IdleLeftFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));


        enemy2IdleLeftAnimation = new Animation<>(0.1f, enemy2IdleLeftFrames);

        //idle_right

        enemy2IdleRightFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));


        enemy2IdleRightAnimation = new Animation<>(0.1f, enemy2IdleRightFrames);

        //3
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            enemy3DownFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }

        enemy3DownAnimation = new Animation<>(0.1f, enemy3DownFrames);
        //walkright
        for (int col = 0; col < animationFrames; col++) {
            enemy3RightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameHeight, frameWidth, frameHeight));
        }

        enemy3RightAnimation = new Animation<>(0.1f, enemy3RightFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            enemy3LeftFrames.add(new TextureRegion(enemySheet, col * 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
        }

        enemy3LeftAnimation = new Animation<>(0.1f, enemy3LeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            enemy3UpFrames.add(new TextureRegion(enemySheet, col * 1 * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
        }

        enemy3UpAnimation = new Animation<>(0.1f, enemy3UpFrames);

        //idle_down

        enemy3IdleDownFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 4 * frameHeight, frameWidth, frameHeight));

        enemy3IdleDownAnimation = new Animation<>(0.1f, enemy3IdleDownFrames);

        //idle_up

        enemy3IdleUpFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 7 * frameHeight, frameWidth, frameHeight));


        enemy3IdleUpAnimation = new Animation<>(0.1f, enemy3IdleUpFrames);

        //idle_left

        enemy3IdleLeftFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));


        enemy3IdleLeftAnimation = new Animation<>(0.1f, enemy3IdleLeftFrames);

        //idle_right

        enemy3IdleRightFrames.add(new TextureRegion(enemySheet, 1 * frameWidth, 5 * frameHeight, frameWidth, frameHeight));


        enemy3IdleRightAnimation = new Animation<>(0.1f, enemy3IdleRightFrames);

        //4
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            enemy4DownFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 0, frameWidth, frameHeight));
        }

        enemy4DownAnimation = new Animation<>(0.1f, enemy4DownFrames);
        //walkright
        for (int col = 0; col < animationFrames; col++) {
            enemy4RightFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }

        enemy4RightAnimation = new Animation<>(0.1f, enemy4LeftFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            enemy4LeftFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }

        enemy4LeftAnimation = new Animation<>(0.1f, enemy4LeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            enemy4UpFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }

        enemy4UpAnimation = new Animation<>(0.1f, enemy4UpFrames);

        //idle_down

        enemy4IdleDownFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 0, frameWidth, frameHeight));

        enemy4IdleDownAnimation = new Animation<>(0.1f, enemy4IdleDownFrames);

        //idle_up

        enemy4IdleUpFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));


        enemy4IdleUpAnimation = new Animation<>(0.1f, enemy4IdleUpFrames);

        //idle_left

        enemy4IdleLeftFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));


        enemy4IdleLeftAnimation = new Animation<>(0.1f, enemy4IdleLeftFrames);

        //idle_right

        enemy4IdleRightFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));


        enemy4IdleRightAnimation = new Animation<>(0.1f, enemy4IdleRightFrames);

        //5
        //walkdown
        for (int col = 0; col < animationFrames; col++) {
            enemy5DownFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 0, frameWidth, frameHeight));
        }

        enemy5DownAnimation = new Animation<>(0.1f, enemy5DownFrames);
        //walkright
        for (int col = 0; col < animationFrames; col++) {
            enemy5RightFrames.add(new TextureRegion(enemySheet, 144 + col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }

        enemy5RightAnimation = new Animation<>(0.1f, enemy5LeftFrames);

        //walkleft
        for (int col = 0; col < animationFrames; col++) {
            enemy5LeftFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }

        enemy5LeftAnimation = new Animation<>(0.1f, enemy5LeftFrames);

        //walkup
        for (int col = 0; col < animationFrames; col++) {
            enemy5UpFrames.add(new TextureRegion(enemySheet, 144 + col * 1 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }

        enemy5UpAnimation = new Animation<>(0.1f, enemy5UpFrames);

        //idle_down

        enemy5IdleDownFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 0, frameWidth, frameHeight));

        enemy5IdleDownAnimation = new Animation<>(0.1f, enemy5IdleDownFrames);

        //idle_up

        enemy5IdleUpFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 3 * frameHeight, frameWidth, frameHeight));


        enemy5IdleUpAnimation = new Animation<>(0.1f, enemy5IdleUpFrames);

        //idle_left

        enemy5IdleLeftFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 1 * frameHeight, frameWidth, frameHeight));


        enemy5IdleLeftAnimation = new Animation<>(0.1f, enemy5IdleLeftFrames);

        //idle_right

        enemy5IdleRightFrames.add(new TextureRegion(enemySheet, 2 * frameWidth, 2 * frameHeight, frameWidth, frameHeight));


        enemy5IdleRightAnimation = new Animation<>(0.1f, enemy5IdleRightFrames);

    }


    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

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

    public Animation<TextureRegion> getEnemy3DownAnimation() {
        return enemy3DownAnimation;
    }

    public Animation<TextureRegion> getEney3UpAnimation() {
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

    public Animation<TextureRegion> getEnemy4DownAnimation() {
        return enemy4DownAnimation;
    }

    public Animation<TextureRegion> getEney4UpAnimation() {
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

    public Animation<TextureRegion> getEnemy5DownAnimation() {
        return enemy5DownAnimation;
    }

    public Animation<TextureRegion> getEney5UpAnimation() {
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

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}

