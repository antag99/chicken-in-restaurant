package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Direction;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class DirectionSystem extends EntityProcessorSystem {
    private Mapper<Velocity> mVelocity;
    private Mapper<Direction> mDirection;

    public DirectionSystem() {
        super(Family.with(Velocity.class, Direction.class));
    }

    @Override
    protected void process(int entity) {
        Velocity velocity = mVelocity.get(entity);
        Direction direction = mDirection.get(entity);
        direction.directionX = velocity.x < 0f ? -1 : velocity.x > 0f ? 1 : direction.directionX;
        direction.directionY = velocity.y < 0f ? -1 : velocity.y > 0f ? 1 : direction.directionY;
    }
}
