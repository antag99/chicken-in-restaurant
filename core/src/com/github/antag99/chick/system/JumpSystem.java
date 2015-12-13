package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Jump;
import com.github.antag99.chick.component.Jumpable;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.chick.util.CollisionListener;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class JumpSystem extends EntitySystem {
    private CollisionSystem collisionSystem;
    private Mapper<Jump> mJump;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Velocity> mVelocity;

    @Override
    protected void initialize() {
        collisionSystem.collisionListener(new CollisionListener() {
            @Override
            public void onCollison(int jumpableEntity, int jumpEntity) {
                Position position = mPosition.get(jumpableEntity);
                Size size = mSize.get(jumpableEntity);
                Velocity velocity = mVelocity.get(jumpableEntity);

                Position jumpPosition = mPosition.get(jumpEntity);
                Size jumpSize = mSize.get(jumpEntity);
                Jump jump = mJump.get(jumpEntity);

                if (position.x + size.width > jumpPosition.x &&
                        position.prevX + size.width <= jumpPosition.x &&
                        velocity.x > 0f && jump.fromLeftSpeed != 0f) {
                    if (position.y == position.prevY) {
                        velocity.y = jump.fromLeftSpeed;
                    }
                    position.x = jumpPosition.x - size.width;
                } else if (position.x < jumpPosition.x + jumpSize.width &&
                        position.prevX >= jumpPosition.x + jumpSize.width &&
                        velocity.x < 0f && jump.fromRightSpeed != 0f) {
                    if (position.y == position.prevY) {
                        velocity.y = jump.fromRightSpeed;
                    }
                    position.x = jumpPosition.x + jumpSize.width;
                }
            }
        }).a(Family.with(Jumpable.class, Velocity.class))
                .b(Family.with(Jump.class)).add();
    }
}
