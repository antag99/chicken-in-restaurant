package com.github.antag99.chick.component;

import com.github.antag99.retinazer.Component;

public final class Eater implements Component {
    /** amount of food this entity has eaten */
    public float food = 0f;
    /** time in seconds between bites */
    public float biteDelay = 0.25f;
    /** counter for the next bit */
    public float biteCounter = 0f;
    /** whether the entity has eaten a food during this frame */
    public boolean eating = false;
}
