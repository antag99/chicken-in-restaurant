package com.github.antag99.chick.component;

import com.github.antag99.retinazer.Component;

public final class Plate implements Component {
    /** whether this plate is currently open */
    public boolean open = false;
    /** entity for this plate - either lid or food */
    public int entity = -1;
    /** time until this plate will close/open */
    public float counter;

    public float amount;
}
