package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Position;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class ActorPositionSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Position> mPosition;

    public ActorPositionSystem() {
        super(Family.with(Acting.class, Position.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Position position = mPosition.get(entity);

        acting.actor.setPosition(position.x, position.y);
    }
}
