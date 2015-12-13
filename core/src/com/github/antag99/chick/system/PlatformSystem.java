package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Gravity;
import com.github.antag99.chick.component.Platform;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.chick.util.CollisionListener;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class PlatformSystem extends EntitySystem {
    private CollisionSystem collisionSystem;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Velocity> mVelocity;
    private Mapper<Gravity> mGravity;

    @Override
    protected void initialize() {
        collisionSystem.collisionListener(new CollisionListener() {
            @Override
            public void onCollison(int entity, int platformEntity) {
                Position position = mPosition.get(entity);
                Velocity velocity = mVelocity.get(entity);
                Gravity gravity = mGravity.get(entity);

                Position platformPosition = mPosition.get(platformEntity);
                Size platformSize = mSize.get(platformEntity);
                float platformTop = platformPosition.y + platformSize.height;

                if (position.prevY >= platformTop && position.y < platformTop) {
                    gravity.hitFloor = true;
                    position.y = platformTop;
                    velocity.y = 0f;
                }
            }
        }).a(Family.with(Gravity.class, Velocity.class)).b(Family.with(Platform.class)).add();
    }
}
