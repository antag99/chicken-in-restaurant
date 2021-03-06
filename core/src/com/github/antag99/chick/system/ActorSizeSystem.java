package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Size;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class ActorSizeSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Size> mSize;

    public ActorSizeSystem() {
        super(Family.with(Acting.class, Size.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Size size = mSize.get(entity);

        acting.actor.setSize(size.width, size.height);
        acting.actor.setOrigin(size.width * 0.5f, size.height * 0.5f);
    }
}
