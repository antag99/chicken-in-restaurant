package com.github.antag99.chick.system;

import com.github.antag99.chick.component.Input;
import com.github.antag99.chick.component.Control;
import com.github.antag99.chick.component.Velocity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class ControlSystem extends EntityProcessorSystem {
    private Mapper<Velocity> mVelocity;
    private Mapper<Control> mControl;
    private Mapper<Input> mInput;

    public ControlSystem() {
        super(Family.with(Velocity.class, Control.class, Input.class));
    }

    @Override
    protected void process(int entity) {
        Velocity velocity = mVelocity.get(entity);
        Control control = mControl.get(entity);
        Input input = mInput.get(entity);

        if (input.moveLeft && !input.moveRight) {
            velocity.x = -control.moveSpeed;
        } else if (input.moveRight && !input.moveLeft) {
            velocity.x = control.moveSpeed;
        } else {
            velocity.x = 0f;
        }
    }
}
