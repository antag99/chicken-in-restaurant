package com.github.antag99.chick.component;

import com.github.antag99.retinazer.Component;

public final class Food implements Component {
    /** amount of the remaining food */
    public float amount = 1f;
    /** amount of food to consume per bite */
    public float consumePerBite = 0.2f;
}
