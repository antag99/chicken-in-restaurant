package com.github.antag99.chick.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.antag99.chick.component.MapRender;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class MapRenderSystem extends EntityProcessorSystem {
    private Mapper<MapRender> mMapRender;

    public MapRenderSystem() {
        super(Family.with(MapRender.class));
    }

    @Override
    protected void process(int entity) {
        MapRender mapRender = mMapRender.get(entity);
        mapRender.renderer.setView((OrthographicCamera) mapRender.viewport.getCamera());
        mapRender.renderer.getBatch().setColor(Color.WHITE);
        mapRender.renderer.render();
    }
}
