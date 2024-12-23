package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private float characterX;
    private float characterY;
    private float characterSpeed = 200f;
    private static final float SPEED_MULTIPLIER = 2.5f;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapBounds;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT
    }

    private Direction currentDirection = Direction.IDLE_DOWN;
    private boolean isGameStarted = false;
    private Array<Rectangle> collisionRectangles;

    private boolean isAttacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.5f;

    private boolean isPickingUp = false;
    private boolean isHolding = false;
    private float pickUpTimer = 0f;
    private static final float PICKUP_DURATION = 1f;


    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        font = game.getSkin().getFont("font");

        TmxMapLoader loader = new TmxMapLoader();
        tiledMap = loader.load("input.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapBounds = new Rectangle(0, 0, layer.getWidth() * layer.getTileWidth(), layer.getHeight() * layer.getTileHeight());

        collisionRectangles = new Array<>();
        MapObjects objects = tiledMap.getLayers().get("layer").getObjects();
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            collisionRectangles.add(rectangleObject.getRectangle());
        }
    }

    private void initializeCharacterPosition() {
        RectangleMapObject characterObject = (RectangleMapObject) tiledMap.getLayers().get("Objects").getObjects().get("character");
        if (characterObject != null) {
            Rectangle rect = characterObject.getRectangle();
            characterX = rect.x;
            characterY = rect.y;
        } else {
            characterX = camera.viewportWidth / 2;
            characterY = camera.viewportHeight / 2;
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if (!isGameStarted && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        ScreenUtils.clear(0, 0, 0, 1);

        if (isGameStarted) {
            updateGameState(delta);
            camera.position.set(characterX, characterY, 0);
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();

        if (!isGameStarted) {
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
            renderCharacter();
        }

        game.getSpriteBatch().end();
    }

    private void startGame() {
        isGameStarted = true;
        characterX = camera.viewportWidth / 2;
        characterY = camera.viewportHeight / 2;
        currentDirection = Direction.IDLE_DOWN;

        initializeCharacterPosition();
    }

    private void updateGameState(float delta) {
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
            handleInput(delta);
        }
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            isPickingUp = true;
            pickUpTimer = 0f;
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            isHolding = true;
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            isAttacking = true;
            attackTimer = 0f;
            return;
        }

        float adjustedSpeed = characterSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            adjustedSpeed *= SPEED_MULTIPLIER;
        }

        float nextX = characterX;
        float nextY = characterY;

        boolean moved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            nextY += adjustedSpeed * delta;
            currentDirection = Direction.UP;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            nextY -= adjustedSpeed * delta;
            currentDirection = Direction.DOWN;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            nextX -= adjustedSpeed * delta;
            currentDirection = Direction.LEFT;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            nextX += adjustedSpeed * delta;
            currentDirection = Direction.RIGHT;
            moved = true;
        }

        if (!moved) {
            if (isHolding) {
                switch (currentDirection) {
                    case UP: currentDirection = Direction.IDLE_UP; break;
                    case DOWN: currentDirection = Direction.IDLE_DOWN; break;
                    case LEFT: currentDirection = Direction.IDLE_LEFT; break;
                    case RIGHT: currentDirection = Direction.IDLE_RIGHT; break;
                }
            } else {
                switch (currentDirection) {
                    case UP: currentDirection = Direction.IDLE_UP; break;
                    case DOWN: currentDirection = Direction.IDLE_DOWN; break;
                    case LEFT: currentDirection = Direction.IDLE_LEFT; break;
                    case RIGHT: currentDirection = Direction.IDLE_RIGHT; break;
                }
            }
        }

        nextX = Math.max(0, Math.min(nextX, mapBounds.width - 64));
        nextY = Math.max(0, Math.min(nextY, mapBounds.height - 48));

        Rectangle characterRect = new Rectangle(nextX, nextY, 16, 32);
        boolean collision = false;
        for (Rectangle rect : collisionRectangles) {
            if (Intersector.overlaps(characterRect, rect)) {
                collision = true;
                break;
            }
        }

        if (!collision) {
            characterX = nextX;
            characterY = nextY;
        }

        if (isHolding) {
            switch (currentDirection) {
                case IDLE_UP: currentDirection = Direction.UP; break;
                case IDLE_DOWN: currentDirection = Direction.DOWN; break;
                case IDLE_LEFT: currentDirection = Direction.LEFT; break;
                case IDLE_RIGHT: currentDirection = Direction.RIGHT; break;
            }
        }
    }

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

    private void renderPickUpAnimation() {
        switch (currentDirection) {
            case UP, IDLE_UP -> game.getSpriteBatch().draw(game.getCharacterPickupUpAnimation().getKeyFrame(pickUpTimer, false), characterX , characterY, 64, 128);
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(game.getCharacterPickupDownAnimation().getKeyFrame(pickUpTimer, false), characterX , characterY, 64, 128);
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(game.getCharacterPickupLeftAnimation().getKeyFrame(pickUpTimer, false), characterX , characterY, 64, 128);
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(game.getCharacterPickupRightAnimation().getKeyFrame(pickUpTimer, false), characterX , characterY, 64, 128);
        }
    }

    private void renderHoldAnimation() {
        switch (currentDirection) {
            case UP -> game.getSpriteBatch().draw(game.getCharacterHoldUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case DOWN -> game.getSpriteBatch().draw(game.getCharacterHoldDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case LEFT -> game.getSpriteBatch().draw(game.getCharacterHoldLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case RIGHT -> game.getSpriteBatch().draw(game.getCharacterHoldRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_UP -> game.getSpriteBatch().draw(game.getCharacterHoldIdleUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_DOWN -> game.getSpriteBatch().draw(game.getCharacterHoldIdleDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_LEFT -> game.getSpriteBatch().draw(game.getCharacterHoldIdleLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_RIGHT -> game.getSpriteBatch().draw(game.getCharacterHoldIdleRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
        }
    }

    private void renderAttackAnimation() {
        switch (currentDirection) {
            case UP, IDLE_UP -> game.getSpriteBatch().draw(game.getCharacterAttackUpAnimation().getKeyFrame(attackTimer, true), characterX - 32, characterY, 128, 128);
            case DOWN, IDLE_DOWN -> game.getSpriteBatch().draw(game.getCharacterAttackDownAnimation().getKeyFrame(attackTimer, true), characterX - 32, characterY, 128, 128);
            case LEFT, IDLE_LEFT -> game.getSpriteBatch().draw(game.getCharacterAttackLeftAnimation().getKeyFrame(attackTimer, true), characterX - 32, characterY, 128, 128);
            case RIGHT, IDLE_RIGHT -> game.getSpriteBatch().draw(game.getCharacterAttackRightAnimation().getKeyFrame(attackTimer, true), characterX - 32, characterY, 128, 128);
        }
    }

    private void renderMovementOrIdle() {
        switch (currentDirection) {
            case UP -> game.getSpriteBatch().draw(game.getCharacterUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case DOWN -> game.getSpriteBatch().draw(game.getCharacterDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case LEFT -> game.getSpriteBatch().draw(game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case RIGHT -> game.getSpriteBatch().draw(game.getCharacterRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_UP -> game.getSpriteBatch().draw(game.getCharacterIdleUpAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_DOWN -> game.getSpriteBatch().draw(game.getCharacterIdleDownAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_LEFT -> game.getSpriteBatch().draw(game.getCharacterIdleLeftAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
            case IDLE_RIGHT -> game.getSpriteBatch().draw(game.getCharacterIdleRightAnimation().getKeyFrame(sinusInput, true), characterX, characterY, 64, 128);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
    }
}