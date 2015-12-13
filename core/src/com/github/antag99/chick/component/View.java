package com.github.antag99.chick.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.antag99.retinazer.Component;

public final class View implements Component {
    public OrthographicCamera camera;
    public float minX, maxX;
    public float minY, maxY;
}
