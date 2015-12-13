package com.github.antag99.chick.system;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.Event;
import com.github.antag99.chick.Tuning;
import com.github.antag99.chick.command.Commands;
import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Control;
import com.github.antag99.chick.component.Dead;
import com.github.antag99.chick.component.Direction;
import com.github.antag99.chick.component.Eater;
import com.github.antag99.chick.component.Fade;
import com.github.antag99.chick.component.Gravity;
import com.github.antag99.chick.component.Input;
import com.github.antag99.chick.component.Location;
import com.github.antag99.chick.component.Player;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Script;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Solid;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.chick.util.EntityAdapter;
import com.github.antag99.chick.util.SpineActor;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class PlayerSystem extends EntityProcessorSystem {
    private AssetSystem assetSystem;
    private SoundSystem soundSystem;
    private Mapper<Eater> mEater;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Velocity> mVelocity;
    private Mapper<Direction> mDirection;
    private Mapper<Acting> mActing;
    private Mapper<Size> mSize;
    private Mapper<Script> mScript;
    private Mapper<Gravity> mGravity;
    private Mapper<Input> mInput;
    private Mapper<Control> mControl;
    private Mapper<Dead> mDead;
    private Mapper<Solid> mSolid;

    private static final int TRACK_WALK = 0;
    private static final int TRACK_EAT = 1;
    private static final int TRACK_FAT = 2;

    public PlayerSystem() {
        super(Family.with(Player.class).exclude(Dead.class));
    }

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Player.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(final int entity) {
                SpineActor actor = (SpineActor) mActing.get(entity).actor;
                actor.animationState.setAnimation(TRACK_EAT, "eat", true).setTimeScale(0f);
                actor.animationState.setAnimation(TRACK_FAT, "fat", true).setTimeScale(0f);
                actor.animationState.addListener(new AnimationStateListener() {
                    @Override
                    public void start(int trackIndex) {
                    }

                    @Override
                    public void event(int trackIndex, Event event) {
                        String name = event.getData().getName();
                        Position position = mPosition.get(entity);
                        Velocity velocity = mVelocity.get(entity);
                        Size size = mSize.get(entity);
                        if (velocity.y == 0f && ("tip".equals(name) || "tap".equals(name)))
                            soundSystem.play(mLocation.get(entity).room,
                                    position.x + size.width * 0.5f,
                                    position.y + size.height * 0.5f,
                                    assetSystem.tapSound);
                    }

                    @Override
                    public void end(int trackIndex) {
                    }

                    @Override
                    public void complete(int trackIndex, int loopCount) {
                    }
                });
            }
        });
    }

    @Override
    protected void process(int entity) {
        Velocity velocity = mVelocity.get(entity);
        Direction direction = mDirection.get(entity);
        Eater eater = mEater.get(entity);
        SpineActor actor = (SpineActor) mActing.get(entity).actor;

        actor.skeleton.getRootBone().setFlipX(direction.directionX == 1);
        actor.skeleton.getRootBone().setX(actor.getWidth() * 0.5f);
        if (velocity.x != 0f && actor.animationState.getCurrent(TRACK_WALK) == null) {
            actor.animationState.addAnimation(TRACK_WALK, "walk", false, 0f);
        }

        actor.animationState.getCurrent(TRACK_EAT).setTime(0.5f + eater.biteCounter / eater.biteDelay);
        actor.animationState.getCurrent(TRACK_FAT).setTime(eater.food);

        if (eater.food >= Tuning.PLAYER_FOOD_VICTORY) {
            for (String gore : new String[] {
                    "gore/body_0",
                    "gore/body_1",
                    "gore/body_2",
                    "gore/body_3",
                    "player/images/head_eat",
                    "player/images/foot",
                    "player/images/foot"
            }) {
                TextureRegion goreTexture = assetSystem.skin.getRegion(gore);
                int goreEntity = engine.createEntity();
                mLocation.create(goreEntity).room = mLocation.get(entity).room;
                mPosition.create(goreEntity).xy(mPosition.get(entity).v());
                mSize.create(goreEntity).set(goreTexture.getRegionWidth() / 16f, goreTexture.getRegionHeight() / 16f);
                mVelocity.create(goreEntity).xy(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
                Acting acting = mActing.create(goreEntity);
                acting.actor = new Image(goreTexture);
                acting.z = 1f;
                mScript.create(goreEntity).command(Commands.seq(Commands.delay(1f),
                        Commands.add(new Fade().color(1f, 1f, 1f, 0f).duration(1f)),
                        Commands.destroy()));
                mGravity.create(goreEntity);
                mSolid.create(goreEntity);
            }

            mEater.remove(entity);
            mDead.create(entity);
            mActing.get(entity).actor.setScale(0f);
            mVelocity.get(entity).xy(0f, 0f);
            mInput.remove(entity);
            mControl.remove(entity);
            Position position = mPosition.get(entity);
            soundSystem.play(mLocation.get(entity).room, position.x, position.y, assetSystem.explodeSound);
        }
    }
}
