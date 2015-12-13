package com.github.antag99.chick.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.antag99.retinazer.Component;

public final class Acting implements Component {
    public Actor actor;
    public float z;

    public Acting actor(Actor actor) {
        this.actor = actor;
        return this;
    }
}
