package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Colored;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class ActorColorSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Colored> mColored;

    public ActorColorSystem() {
        super(Family.with(Acting.class, Colored.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Colored colored = mColored.get(entity);

        acting.actor.setColor(colored.color);
    }
}
