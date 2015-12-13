package com.github.antag99.chick.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.antag99.chick.component.Input;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class InputSystem extends EntityProcessorSystem {
    private Mapper<Input> mInput;

    public InputSystem() {
        super(Family.with(Input.class));
    }

    @Override
    protected void process(int entity) {
        Input input = mInput.get(entity);
        input.moveLeft = Gdx.input.isKeyPressed(Keys.A);
        input.moveRight = Gdx.input.isKeyPressed(Keys.D);
    }
}
