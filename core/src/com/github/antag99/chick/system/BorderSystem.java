package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Border;
import com.github.antag99.chick.component.Gravity;
import com.github.antag99.chick.component.Location;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Solid;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class BorderSystem extends EntityProcessorSystem {
    private Mapper<Border> mBorder;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Velocity> mVelocity;
    private Mapper<Gravity> mGravity;

    public BorderSystem() {
        super(Family.with(Location.class, Position.class,
                Size.class, Gravity.class, Velocity.class, Solid.class));
    }

    @Override
    protected void process(int entity) {
        Border border = mBorder.get(mLocation.get(entity).room);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        Velocity velocity = mVelocity.get(entity);
        Gravity gravity = mGravity.get(entity);

        if (position.x < border.minX) {
            position.x = border.minX;
            velocity.x = 0f;
        }
        if (position.x + size.width > border.maxX) {
            position.x = border.maxX - size.width;
            velocity.x = 0f;
        }
        if (position.y < border.minY) {
            position.y = border.minY;
            velocity.y = 0f;
            gravity.hitFloor = true;
        }
        if (position.y + size.height > border.maxY) {
            position.y = border.maxY - size.height;
            velocity.y = 0f;
        }
    }
}
