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

    // Character move
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    //idle
    private Animation<TextureRegion> characterIdleUpAnimation;
    private Animation<TextureRegion> characterIdleDownAnimation;
    private Animation<TextureRegion> characterIdleLeftAnimation;
    private Animation<TextureRegion> characterIdleRightAnimation;
    //pick up
    private Animation<TextureRegion> characterPickupDownAnimation;
    private Animation<TextureRegion> characterPickupUpAnimation;
    private Animation<TextureRegion> characterPickupRightAnimation;
    private Animation<TextureRegion> characterPickupLeftAnimation;
    //hold
    private Animation<TextureRegion> characterHoldDownAnimation;
    private Animation<TextureRegion> characterHoldUpAnimation;
    private Animation<TextureRegion> characterHoldRightAnimation;
    private Animation<TextureRegion> characterHoldLeftAnimation;
    private Animation<TextureRegion> characterHoldIdleUpAnimation;
    private Animation<TextureRegion> characterHoldIdleDownAnimation;
    private Animation<TextureRegion> characterHoldIdleLeftAnimation;
    private Animation<TextureRegion> characterHoldIdleRightAnimation;
    //attack
    private Animation<TextureRegion> characterAttackDownAnimation;
    private Animation<TextureRegion> characterAttackUpAnimation;
    private Animation<TextureRegion> characterAttackRightAnimation;
    private Animation<TextureRegion> characterAttackLeftAnimation;
    //Enemy1 move
    private Animation<TextureRegion> e1DownAnimation;
    private Animation<TextureRegion> e1LeftAnimation;
    private Animation<TextureRegion> e1RightAnimation;
    private Animation<TextureRegion> e1UpAnimation;





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
        this.loadCharacterAnimation();// Load character animation
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

        int attackframeWidth = 32;
        int attackframeHeight = 32;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkIdleRightFrames = new Array<>(TextureRegion.class);
        //pickup frames
        Array<TextureRegion> pickupRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> pickupDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> pickupUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> pickupLeftFrames = new Array<>(TextureRegion.class);
        //hold frames
        Array<TextureRegion> holdDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdIdleDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdIdleUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdIdleLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> holdIdleRightFrames = new Array<>(TextureRegion.class);
        //attack
        Array<TextureRegion> attackDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> attackUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> attackRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> attackLeftFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
//walk
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
//idle
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

//pick up

        //pickup_down
        for (int col = 0; col < 3; col++) {
            pickupDownFrames.add(new TextureRegion(walkSheet, 80 + col *  frameWidth, 0* frameHeight, frameWidth, frameHeight));
        }

        characterPickupDownAnimation= new Animation<>(0.1f, pickupDownFrames);


        //pickup_right
        for (int col = 0; col < 3; col++) {
            pickupRightFrames.add(new TextureRegion(walkSheet, 80 + col *  frameWidth, 1 * frameHeight, frameWidth, frameHeight));
        }

        characterPickupRightAnimation= new Animation<>(0.1f, pickupRightFrames);


        //pickup_up
        for (int col = 0; col < 3; col++) {
            pickupUpFrames.add(new TextureRegion(walkSheet, 80 + col * frameWidth, 2* frameHeight, frameWidth, frameHeight));
        }

        characterPickupUpAnimation= new Animation<>(0.1f, pickupUpFrames);

        //pickup_left
        for (int col = 0; col < 3; col++) {
            pickupLeftFrames.add(new TextureRegion(walkSheet, 80 + col* frameWidth, 3* frameHeight, frameWidth, frameHeight));
        }

        characterPickupLeftAnimation= new Animation<>(0.1f, pickupLeftFrames);

//hold
        //hold_down
        for (int col = 0; col < animationFrames; col++) {
            holdDownFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 0* frameHeight, frameWidth, frameHeight));
        }

        characterHoldDownAnimation= new Animation<>(0.1f, holdDownFrames);
        //hold_right
        for (int col = 0; col < animationFrames; col++) {
            holdRightFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 1* frameHeight, frameWidth, frameHeight));
        }

        characterHoldRightAnimation= new Animation<>(0.1f, holdRightFrames);
        //hold_up
        for (int col = 0; col < animationFrames; col++) {
            holdUpFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 2* frameHeight, frameWidth, frameHeight));
        }

        characterHoldUpAnimation= new Animation<>(0.1f, holdUpFrames);
        //hold_left
        for (int col = 0; col < animationFrames; col++) {
            holdLeftFrames.add(new TextureRegion(walkSheet, 144 + col * frameWidth, 3* frameHeight, frameWidth, frameHeight));
        }

        characterHoldLeftAnimation= new Animation<>(0.1f, holdLeftFrames);
        //hold_idle_down
        holdIdleDownFrames.add(new TextureRegion(walkSheet, 136 + 0 * frameWidth, 0* frameHeight, frameWidth, frameHeight));

        characterHoldIdleDownAnimation= new Animation<>(0.1f, holdIdleDownFrames);
        //hold_idle_right
        holdIdleRightFrames.add(new TextureRegion(walkSheet, 136 + 0 * frameWidth, 1* frameHeight, frameWidth, frameHeight));

        characterHoldIdleRightAnimation= new Animation<>(0.1f, holdIdleRightFrames);
        //hold_idle_up
        holdIdleUpFrames.add(new TextureRegion(walkSheet, 136 + 0 * frameWidth, 2* frameHeight, frameWidth, frameHeight));

        characterHoldIdleUpAnimation= new Animation<>(0.1f, holdIdleUpFrames);
        //hold_idle_left
        holdIdleLeftFrames.add(new TextureRegion(walkSheet, 136 + 0 * frameWidth, 3* frameHeight, frameWidth, frameHeight));

        characterHoldIdleLeftAnimation= new Animation<>(0.1f, holdIdleLeftFrames);

        //attackdown
        for (int col = 0; col < animationFrames; col++) {
            attackDownFrames.add(new TextureRegion(walkSheet, col * 1*attackframeWidth, 4*attackframeHeight,attackframeWidth, attackframeHeight));
        }

        characterAttackDownAnimation = new Animation<>(0.1f,attackDownFrames);

//attackup
        for (int col = 0; col < animationFrames; col++) {
            attackUpFrames.add(new TextureRegion(walkSheet, col * 1*attackframeWidth, 5*attackframeHeight,attackframeWidth, attackframeHeight));
        }

        characterAttackUpAnimation = new Animation<>(0.1f,attackUpFrames);

//attackright
        for (int col = 0; col < animationFrames; col++) {
            attackRightFrames.add(new TextureRegion(walkSheet, col * 1*attackframeWidth, 6*attackframeHeight,attackframeWidth, attackframeHeight));
        }

        characterAttackRightAnimation = new Animation<>(0.1f,attackRightFrames);

//attackleft
        for (int col = 0; col < animationFrames; col++) {
            attackLeftFrames.add(new TextureRegion(walkSheet, col * 1*attackframeWidth, 7*attackframeHeight,attackframeWidth, attackframeHeight));
        }

        characterAttackLeftAnimation = new Animation<>(0.1f,attackLeftFrames);

    }

    void loadEnemyAnimation() {

        Texture eSheet = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;
//frames
        //e1
        Array<TextureRegion> e1downFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> e1leftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> e1rightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> e1upFrames = new Array<>(TextureRegion.class);

        //e1down
        for (int col = 0; col < animationFrames; col++) {
            e1downFrames.add(new TextureRegion(eSheet, 144+ col * frameWidth, 0*frameHeight,frameWidth, frameHeight));
        }

        e1DownAnimation = new Animation<>(0.1f, e1downFrames);

        //e1left
        for (int col = 0; col < animationFrames; col++) {
            e1leftFrames.add(new TextureRegion(eSheet, 144 + col * frameWidth, 1 *frameHeight, frameWidth, frameHeight));
        }

        e1LeftAnimation = new Animation<>(0.1f, e1leftFrames);

        //e1right
        for (int col = 0; col < animationFrames; col++) {
            e1rightFrames.add(new TextureRegion(eSheet, 144 + col * frameWidth, 2 *frameHeight, frameWidth, frameHeight));
        }

        e1RightAnimation = new Animation<>(0.1f, e1rightFrames);

        //e1up
        for (int col = 0; col < animationFrames; col++) {
            e1upFrames.add(new TextureRegion(eSheet, 144 + col * frameWidth, 3 *frameHeight, frameWidth, frameHeight));
        }

        e1UpAnimation = new Animation<>(0.1f, e1upFrames);


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

    public Animation<TextureRegion> getE1DownAnimation() {
        return e1DownAnimation;
    }

    public Animation<TextureRegion> getE1LeftAnimation() {
        return e1LeftAnimation;
    }

    public Animation<TextureRegion> getE1RightAnimation() {
        return e1RightAnimation;
    }

    public Animation<TextureRegion> getE1UpAnimation() {
        return e1UpAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}


