package com.github.antag99.chick.system;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.github.antag99.chick.GameScreen;
import com.github.antag99.chick.Tuning;
import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Border;
import com.github.antag99.chick.component.CameraShake;
import com.github.antag99.chick.component.Control;
import com.github.antag99.chick.component.Direction;
import com.github.antag99.chick.component.Eater;
import com.github.antag99.chick.component.Food;
import com.github.antag99.chick.component.Gravity;
import com.github.antag99.chick.component.Hear;
import com.github.antag99.chick.component.Input;
import com.github.antag99.chick.component.Jump;
import com.github.antag99.chick.component.Jumpable;
import com.github.antag99.chick.component.Location;
import com.github.antag99.chick.component.MapRender;
import com.github.antag99.chick.component.Nausea;
import com.github.antag99.chick.component.Plate;
import com.github.antag99.chick.component.Platform;
import com.github.antag99.chick.component.Player;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Room;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Solid;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.chick.component.View;
import com.github.antag99.chick.component.Wine;
import com.github.antag99.chick.util.SpineActor;
import com.github.antag99.chick.util.WineActor;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;

public final class GameSystem extends EntitySystem {
    private AssetSystem assetSystem;
    private Mapper<Room> mRoom;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Velocity> mVelocity;
    private Mapper<Size> mSize;
    private Mapper<Control> mControl;
    private Mapper<Gravity> mGravity;
    private Mapper<Input> mInput;
    private Mapper<Acting> mActing;
    private Mapper<View> mView;
    private Mapper<Border> mBorder;
    private Mapper<Jumpable> mJumpable;
    private Mapper<Eater> mEater;
    private Mapper<Platform> mPlatform;
    private Mapper<Jump> mJump;
    private Mapper<MapRender> mMapRender;
    private Mapper<Plate> mPlate;
    private Mapper<Player> mPlayer;
    private Mapper<Direction> mDirection;
    private Mapper<Hear> mHear;
    private Mapper<Wine> mWine;
    private Mapper<Food> mFood;
    private Mapper<Nausea> mNausea;
    private Mapper<CameraShake> mCameraShake;
    private Mapper<Solid> mSolid;

    private @SkipWire GameScreen gameScreen;

    public GameSystem(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Starts the game.
     */
    public int startGame() {
        // Create the room
        int room = engine.createEntity();
        Room cRoom = mRoom.create(room);

        // Load tiled map
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.convertObjectToTileSpace = true;
        TiledMap tiledMap = new TmxMapLoader().load("map.tmx", params);
        MapProperties mapProperties = tiledMap.getProperties();

        MapRender mapRender = mMapRender.create(room);
        mapRender.renderer = new OrthogonalTiledMapRenderer(
                tiledMap, 1f / 16f, gameScreen.game.batch);
        mapRender.viewport = gameScreen.viewport;
        mapRender.renderer.setView(gameScreen.camera);
        // Round coordinates to pixels
        float tileWidth = mapProperties.get("tilewidth", Number.class).floatValue();
        float tileHeight = mapProperties.get("tileheight", Number.class).floatValue();
        for (MapLayer layer : tiledMap.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer)) {
                for (MapObject object : layer.getObjects()) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                        float x0 = rectangle.x;
                        float x1 = rectangle.x + rectangle.width;
                        float y0 = rectangle.y;
                        float y1 = rectangle.y + rectangle.height;
                        x0 = MathUtils.roundPositive(x0 * tileWidth) / tileWidth;
                        x1 = MathUtils.roundPositive(x1 * tileWidth) / tileWidth;
                        y0 = MathUtils.roundPositive(y0 * tileHeight) / tileHeight;
                        y1 = MathUtils.roundPositive(y1 * tileHeight) / tileHeight;
                        rectangle.x = x0;
                        rectangle.y = y0;
                        rectangle.width = x1 - x0;
                        rectangle.height = y1 - y0;
                    }
                }
            }
        }

        float mapWidth = mapProperties.get("width", Number.class).floatValue();
        float mapHeight = mapProperties.get("height", Number.class).floatValue();

        // Create objects
        for (MapLayer layer : tiledMap.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer)) {
                for (MapObject object : layer.getObjects()) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                        MapProperties objectProperties = object.getProperties();
                        String type = objectProperties.get("type", String.class);

                        int entity = engine.createEntity();
                        mLocation.create(entity).room = room;
                        mPosition.create(entity).xy(rectangle.x, rectangle.y);
                        mSize.create(entity).set(rectangle.width, rectangle.height);

                        if ("platform".equals(type) || "table".equals(type)) {
                            mPlatform.create(entity);
                        } else if ("jump".equals(type)) {
                            Jump jump = mJump.create(entity);
                            float[] speed = Tuning.JUMP_SPEEDS.get(object.getName());
                            if (speed == null)
                                throw new RuntimeException("no speed for " + object.getName());
                            jump.fromLeftSpeed = speed[0];
                            jump.fromRightSpeed = speed[1];
                        } else if ("plate".equals(type)) {
                            mPlate.create(entity).amount = Tuning.CHICKEN_AMOUNT;
                        } else if ("wine".equals(type)) {
                            mWine.create(entity);
                            Food food = mFood.create(entity);
                            food.amount = Tuning.WINE_AMOUNT;
                            food.consumePerBite = Tuning.WINE_CONSUME_PER_SIP;
                            mActing.create(entity).actor = new WineActor(gameScreen.game.skin.getAtlas());
                        } else {
                            throw new RuntimeException("invalid type: " + type);
                        }
                    }
                }
            }
        }

        Border border = mBorder.create(room);
        border.minX = 0f;
        border.maxX = mapWidth;
        border.minY = 2f;
        border.maxY = mapHeight;

        int player = engine.createEntity();
        cRoom.player = player;
        mPosition.create(player).xy(border.maxX - Tuning.PLAYER_WIDTH - 2.5f, 2f);
        mVelocity.create(player);
        mSize.create(player).set(Tuning.PLAYER_WIDTH, Tuning.PLAYER_HEIGHT);
        Control control = mControl.create(player);
        control.moveSpeed = Tuning.PLAYER_MOVE_SPEED;
        mGravity.create(player);
        Acting acting = mActing.create(player);
        acting.actor = new SpineActor(
                new Skeleton(assetSystem.playerSkeletonData),
                new AnimationState(assetSystem.playerAnimationStateData));
        acting.z = 1f;
        View view = mView.create(player);
        view.camera = gameScreen.camera;
        view.minX = 0f;
        view.maxX = mapWidth;
        view.minY = 0f;
        view.maxY = mapHeight;

        mInput.create(player);
        mLocation.create(player).room = room;
        mJumpable.create(player);
        Eater eater = mEater.create(player);
        eater.biteDelay = Tuning.PLAYER_BITE_DELAY;
        eater.food = 0f;
        mPlayer.create(player);
        mDirection.create(player).directionX = -1;
        mHear.create(player).range = Tuning.PLAYER_HEAR_RANGE;
        Nausea nausea = mNausea.create(player);
        nausea.threshold = Tuning.NAUSEA_THRESHOLD;
        nausea.recoverySpeed = Tuning.NAUSEA_RECOVERY_SPEED;
        nausea.rotationSpeed = Tuning.NAUSEA_ROTATION_SPEED;
        mCameraShake.create(player);
        mSolid.create(player);
        return room;
    }
}
